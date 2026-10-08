package com.saucedemo.pages;

import com.microsoft.playwright.Page;
import java.math.BigDecimal;

public class CheckoutPage extends BasePage {
  public CheckoutPage(Page page) {
    super(page);
  }

  public void enterDetails(String firstName, String lastName, String postalCode) {
    testId("firstName").fill(firstName);
    testId("lastName").fill(lastName);
    testId("postalCode").fill(postalCode);
  }

  public void continueToOverview() {
    testId("continue").click();
  }

  public String getErrorMessage() {
    return testId("error").innerText();
  }

  public void finishOrder() {
    testId("finish").click();
  }

  public String getConfirmationMessage() {
    return page.getByText("Thank you for your order!").innerText();
  }

  public BigDecimal getSubtotal() {
    return priceFrom(page.locator(".summary_subtotal_label").innerText()
        .replaceFirst(".*\\$", ""));
  }
}
