package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.math.BigDecimal;

public abstract class BasePage {
  protected final Page page;

  protected BasePage(Page page) {
    this.page = page;
  }

  protected Locator testId(String id) {
    return page.getByTestId(id);
  }

  protected Locator exactText(String text) {
    return page.getByText(text, new Page.GetByTextOptions().setExact(true));
  }

  protected void navigateTo(String url) {
    page.navigate(url);
  }

  protected BigDecimal priceFrom(String text) {
    return new BigDecimal(text.replace("$", "").trim());
  }
}
