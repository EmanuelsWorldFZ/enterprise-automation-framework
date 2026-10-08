# Saucedemo Testing Automation

End-to-end browser tests for the [Sauce Demo](https://www.saucedemo.com) practice
shop, built with Java 21, Playwright, JUnit 5, and the Page Object Model.

## Prerequisites

- Java 21
- Maven 3.9 or newer
- Git

Check that Java and Maven are installed:

```sh
java -version
mvn -version
```

## Get started

Clone the repository and move into the project directory:

```sh
git clone https://github.com/EmanuelsWorldFZ/enterprise-automation-framework.git
cd enterprise-automation-framework
```

Install the Playwright browser binaries:

```sh
mvn exec:java \
  -Dexec.classpathScope=test \
  -Dexec.mainClass=com.microsoft.playwright.CLI \
  -Dexec.args="install chromium"
```

Run the tests:

```sh
mvn test
```

Tests run headlessly in Chromium by default. The base URL, browser, headless
mode, and timeout are read from `src/test/resources/config.properties`.
Override settings without changing files using system properties or environment
variables. For example, to run in Firefox:

```sh
mvn exec:java \
  -Dexec.classpathScope=test \
  -Dexec.mainClass=com.microsoft.playwright.CLI \
  -Dexec.args="install firefox webkit"

mvn test -Dbrowser=firefox
mvn test -Dbrowser=webkit
```

Equivalent environment overrides include `PLAYWRIGHT_BROWSER=firefox` and
`SAUCEDEMO_BASE_URL=https://www.saucedemo.com`. The workflow already runs each
browser in a separate GitHub Actions matrix job; test classes also run in
parallel locally (four workers by default).

## Test coverage

- Data-driven login for standard, problem, performance-glitch, and locked-out
  users; invalid credentials and logout
- Product listing and cart badge count
- Inventory values checked against JSON product expectations
- Adding/removing products, and checkout subtotal calculated from cart prices
- Checkout performed with each row in the CSV customer dataset
- ReqRes GET users and POST create-user API checks
- Failure screenshots saved under `test-results/screenshots` and attached to
  Allure results

The browser tests use Sauce Demo's public practice credentials from the JSON
dataset. API settings can be overridden with `REQRES_BASE_URL` and
`REQRES_API_KEY`.

## Reports

Surefire XML/text reports are saved under `target/surefire-reports`. Generate
the Allure HTML report after a test run with:

```sh
mvn io.qameta.allure:allure-maven:2.15.0:report
```

Open `target/site/allure-maven-plugin/index.html`. GitHub Actions uploads
Surefire reports, Allure results/reports, and screenshots as workflow artifacts
for every browser.

## Git workflow

Use `main` for stable work, `develop` for integration, and short-lived
`feature/<topic>` branches for enhancements. Open a pull request from each
feature branch into `develop`, then promote reviewed releases from `develop` to
`main`. Keep commits focused and descriptive; this local enhancement is on
`feature/advanced-framework` and is intentionally left uncommitted so it can be
committed with your configured Git identity.

## Project structure

```text
src/test/java/com/saucedemo/
├── base/       Shared browser setup and teardown
├── pages/      Shared base page plus page objects
├── tests/      UI, API, product, and checkout tests
└── utils/      Configuration, test data, and failure screenshots
```

Test resources (configuration and JSON/CSV datasets) live under
`src/test/resources`. GitHub Actions runs the suite on Chromium, Firefox, and
WebKit for pushes and pull requests, then saves the reports and screenshots as
artifacts.
