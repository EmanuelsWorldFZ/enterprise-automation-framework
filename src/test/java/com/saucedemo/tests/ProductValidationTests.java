package com.saucedemo.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.utils.TestData;
import com.saucedemo.utils.TestData.ProductExpectation;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ProductValidationTests extends BaseTest {
  @ParameterizedTest(name = "{0}")
  @MethodSource("expectedProducts")
  void inventoryProductMatchesExpectedData(ProductExpectation expected) {
    loginAsStandardUser();

    InventoryPage inventoryPage = new InventoryPage(page);
    assertEquals(expected.name(), inventoryPage.getProductTitle(expected.name()));
    assertEquals("$" + expected.price(), inventoryPage.getProductPrice(expected.name()));
  }

  private static Stream<ProductExpectation> expectedProducts() {
    return TestData.products().stream();
  }
}
