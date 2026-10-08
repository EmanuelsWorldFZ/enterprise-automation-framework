package com.saucedemo.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public final class TestData {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private TestData() {}

  public static List<UserCredentials> users() {
    return readJson("data/users.json", new TypeReference<>() {});
  }

  public static List<ProductExpectation> products() {
    return readJson("data/products.json", new TypeReference<>() {});
  }

  public static List<CheckoutDetails> checkoutDetails() {
    try (InputStream input = resource("data/checkout.csv");
         BufferedReader reader = new BufferedReader(
             new InputStreamReader(input, StandardCharsets.UTF_8))) {
      String header = reader.readLine();
      if (!"firstname,lastname,postcode".equals(header)) {
        throw new IllegalStateException("Unexpected checkout CSV header: " + header);
      }

      return reader.lines().filter(line -> !line.isBlank()).map(TestData::parseCheckoutRow).toList();
    } catch (IOException e) {
      throw new IllegalStateException("Unable to read checkout CSV", e);
    }
  }

  private static CheckoutDetails parseCheckoutRow(String line) {
    String[] values = line.split(",", -1);
    if (values.length != 3 || Arrays.stream(values).anyMatch(String::isBlank)) {
      throw new IllegalStateException("Invalid checkout CSV row: " + line);
    }
    return new CheckoutDetails(values[0], values[1], values[2]);
  }

  private static <T> T readJson(String resourcePath, TypeReference<T> type) {
    try (InputStream input = resource(resourcePath)) {
      return OBJECT_MAPPER.readValue(input, type);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to read test data: " + resourcePath, e);
    }
  }

  private static InputStream resource(String resourcePath) {
    InputStream input = TestData.class.getClassLoader().getResourceAsStream(resourcePath);
    if (input == null) {
      throw new IllegalStateException("Missing test resource: " + resourcePath);
    }
    return input;
  }

  public record UserCredentials(
      String username, String password, boolean canLogin, String expectedError) {}

  public record ProductExpectation(String name, String price) {}

  public record CheckoutDetails(String firstname, String lastname, String postcode) {}
}
