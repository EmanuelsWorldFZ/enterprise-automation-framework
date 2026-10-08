package com.saucedemo.pages;

import com.microsoft.playwright.Page;

public class CheckoutPage {
  private final Page page;

  public CheckoutPage(Page page) {
    this.page = page;
  }

  public void enterDetails(String firstName, String lastName, String postalCode) {
    page.getByTestId("firstName").fill(firstName);
    page.getByTestId("lastName").fill(lastName);
    page.getByTestId("postalCode").fill(postalCode);
  }

  public void continueToOverview() {
    page.getByTestId("continue").click();
  }

  public String getErrorMessage() {
    return page.getByTestId("error").innerText();
  }

  public void finishOrder() {
    page.getByTestId("finish").click();
  }

  public String getConfirmationMessage() {
    return page.getByText("Thank you for your order!").innerText();
  }
}
