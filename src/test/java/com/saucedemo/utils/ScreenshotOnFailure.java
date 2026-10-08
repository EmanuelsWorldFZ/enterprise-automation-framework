package com.saucedemo.utils;

import com.microsoft.playwright.Page;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

public class ScreenshotOnFailure implements TestExecutionExceptionHandler {
  private final Supplier<Page> pageSupplier;
  private final Path outputDirectory;

  public ScreenshotOnFailure(Supplier<Page> pageSupplier, Path outputDirectory) {
    this.pageSupplier = pageSupplier;
    this.outputDirectory = outputDirectory;
  }

  @Override
  public void handleTestExecutionException(ExtensionContext context, Throwable cause)
      throws Throwable {
    Page page = pageSupplier.get();
    if (page == null || page.isClosed()) {
      throw cause;
    }

    try {
      Files.createDirectories(outputDirectory);
      String fileName = context.getDisplayName().replaceAll("[^a-zA-Z0-9.-]", "_");
      page.screenshot(new Page.ScreenshotOptions()
          .setPath(outputDirectory.resolve(fileName + ".png"))
          .setFullPage(true));
    } catch (IOException | RuntimeException screenshotFailure) {
      cause.addSuppressed(new RuntimeException(
          "Unable to capture screenshot for " + context.getDisplayName(), screenshotFailure));
    }
    throw cause;
  }
}
