package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import base.BasePage;

/**
 * LoginPage class - Page Object for Login Page
 * Contains all elements and actions for login functionality
 */
public class LoginPage extends BasePage {

    // Page Elements using @FindBy annotation
    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "h3[data-test='error']")
    private WebElement errorMessage;

    @FindBy(className = "login_logo")
    private WebElement loginLogo;

    // Constructor
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // Page Actions

    /**
     * Enter username in username field
     */
    public LoginPage enterUsername(String username) {
        type(usernameField, username);
        System.out.println("  → Entered username: " + username);
        return this;
    }

    /**
     * Enter password in password field
     */
    public LoginPage enterPassword(String password) {
        type(passwordField, password);
        System.out.println("  → Entered password: " + password);
        return this;
    }

    /**
     * Click on login button
     */
    public void clickLoginButton() {
        click(loginButton);
        System.out.println("  → Clicked login button");
    }

    /**
     * Complete login action with username and password
     */
    public ProductsPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        return new ProductsPage(driver);
    }

    /**
     * Get error message text
     */
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }

    /**
     * Check if login page is displayed
     */
    public boolean isLoginPageDisplayed() {
        return isDisplayed(loginLogo);
    }

    /**
     * Verify login page elements
     */
    public boolean verifyLoginPageElements() {
        return isDisplayed(usernameField) && 
               isDisplayed(passwordField) && 
               isDisplayed(loginButton);
    }
}