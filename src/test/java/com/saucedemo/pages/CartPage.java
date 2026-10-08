package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import java.math.BigDecimal;
import java.util.List;

public class CartPage extends BasePage {
  public CartPage(Page page) {
    super(page);
  }

  public int getItemCount() {
    exactText("Your Cart").waitFor();
    return page.locator(".cart_item").count();
  }

  public int getCartCount() {
    var badge = testId("shopping-cart-badge");
    return badge.count() == 0 ? 0 : Integer.parseInt(badge.innerText());
  }

  public List<BigDecimal> getItemPrices() {
    return page.locator(".cart_item .inventory_item_price").allInnerTexts().stream()
        .map(this::priceFrom)
        .toList();
  }

  public void removeItem(String productName) {
    page.locator(".cart_item")
        .filter(new Locator.FilterOptions().setHasText(productName))
        .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Remove"))
        .click();
  }

  public void checkout() {
    testId("checkout").click();
  }
}
