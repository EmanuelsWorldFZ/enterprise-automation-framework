package com.saucedemo.tests;

import static io.restassured.RestAssured.given;

import com.saucedemo.utils.ConfigManager;
import org.junit.jupiter.api.Test;

class ApiTests {
  @Test
  void canGetUsersFromReqres() {
    given()
        .header("x-api-key", ConfigManager.get("apiKey"))
        .when()
        .get(ConfigManager.get("apiBaseUrl") + "/users?page=2")
        .then()
        .statusCode(200);
  }

  @Test
  void canCreateAReqresUser() {
    given()
        .header("x-api-key", ConfigManager.get("apiKey"))
        .contentType("application/json")
        .body("{\"name\":\"Automation User\",\"job\":\"QA Engineer\"}")
        .when()
        .post(ConfigManager.get("apiBaseUrl") + "/users")
        .then()
        .statusCode(201);
  }
}
