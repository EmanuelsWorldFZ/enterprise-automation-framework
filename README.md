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

Tests run headlessly in Chromium by default. To run them in Firefox or WebKit,
install those browsers and select one with the `browser` property:

```sh
mvn exec:java \
  -Dexec.classpathScope=test \
  -Dexec.mainClass=com.microsoft.playwright.CLI \
  -Dexec.args="install firefox webkit"

mvn test -Dbrowser=firefox
mvn test -Dbrowser=webkit
```

## Test coverage

- Successful login, invalid credentials, and logout
- Product listing and cart badge count
- Adding and removing products from the cart
- Required checkout details and successful order confirmation

The tests use Sauce Demo's public practice credentials: `standard_user` /
`secret_sauce`.

## Project structure

```text
src/test/java/com/saucedemo/
├── base/       Shared browser setup and teardown
├── pages/      Login, inventory, cart, and checkout page objects
├── tests/      Login, cart, and checkout tests
└── utils/      Screenshot capture on test failures
```

JUnit reports are saved under `target/surefire-reports`. Screenshots from failed
tests are saved under `target/test-artifacts/screenshots`.

GitHub Actions runs the test suite on Chromium, Firefox, and WebKit for pushes
and pull requests.
