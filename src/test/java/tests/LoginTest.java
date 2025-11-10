package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;
import pages.LoginPage;
import pages.ProductsPage;
import utils.ExtentManager;

/**
 * LoginTest class - Contains all test cases related to Login functionality
 * Tests cover positive, negative, and boundary scenarios
 */
public class LoginTest extends BaseTest {

    @Test(priority = 1, description = "Verify successful login with valid credentials")
    public void testValidLogin() {
        ExtentManager.getTest().log(Status.INFO, "Step 1: Enter valid username and password");
        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = loginPage.login(
            config.getProperty("username"), 
            config.getProperty("password")
        );

        ExtentManager.getTest().log(Status.INFO, "Step 2: Verify user is redirected to Products page");
        Assert.assertTrue(productsPage.isPageTitleDisplayed(), "Products page title is not displayed");
        
        ExtentManager.getTest().log(Status.INFO, "Step 3: Verify page title is 'Products'");
        Assert.assertEquals(productsPage.getPageTitle(), "Products", "Page title does not match");
        
        ExtentManager.getTest().log(Status.PASS, "Login successful with valid credentials");
    }

    @Test(priority = 2, description = "Verify login failure with locked out user")
    public void testLockedOutUser() {
        ExtentManager.getTest().log(Status.INFO, "Step 1: Enter locked out user credentials");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(
            config.getProperty("locked_user"), 
            config.getProperty("password")
        );

        ExtentManager.getTest().log(Status.INFO, "Step 2: Verify error message is displayed");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message is not displayed");
        
        ExtentManager.getTest().log(Status.INFO, "Step 3: Verify error message contains 'locked out'");
        String errorMsg = loginPage.getErrorMessage();
        Assert.assertTrue(errorMsg.contains("locked out"), 
            "Error message does not contain 'locked out'. Actual: " + errorMsg);
        
        ExtentManager.getTest().log(Status.PASS, "Locked out user cannot login - Expected behavior");
    }

    @Test(priority = 3, description = "Verify login failure with invalid username")
    public void testInvalidUsername() {
        ExtentManager.getTest().log(Status.INFO, "Step 1: Enter invalid username");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(
            config.getProperty("invalid_username"), 
            config.getProperty("password")
        );

        ExtentManager.getTest().log(Status.INFO, "Step 2: Verify error message is displayed");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message is not displayed");
        
        ExtentManager.getTest().log(Status.INFO, "Step 3: Verify appropriate error message");
        String errorMsg = loginPage.getErrorMessage();
        Assert.assertTrue(errorMsg.contains("do not match"), 
            "Error message mismatch. Actual: " + errorMsg);
        
        ExtentManager.getTest().log(Status.PASS, "Login failed with invalid username - Expected behavior");
    }

    @Test(priority = 4, description = "Verify login failure with invalid password")
    public void testInvalidPassword() {
        ExtentManager.getTest().log(Status.INFO, "Step 1: Enter valid username but invalid password");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(
            config.getProperty("username"), 
            config.getProperty("invalid_password")
        );

        ExtentManager.getTest().log(Status.INFO, "Step 2: Verify error message is displayed");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message is not displayed");
        
        ExtentManager.getTest().log(Status.PASS, "Login failed with invalid password - Expected behavior");
    }

    @Test(priority = 5, description = "Verify login failure with empty credentials")
    public void testEmptyCredentials() {
        ExtentManager.getTest().log(Status.INFO, "Step 1: Click login without entering any credentials");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickLoginButton();

        ExtentManager.getTest().log(Status.INFO, "Step 2: Verify error message is displayed");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message is not displayed");
        
        ExtentManager.getTest().log(Status.INFO, "Step 3: Verify error message for empty username");
        String errorMsg = loginPage.getErrorMessage();
        Assert.assertTrue(errorMsg.contains("Username is required"), 
            "Error message mismatch. Actual: " + errorMsg);
        
        ExtentManager.getTest().log(Status.PASS, "Cannot login with empty credentials - Expected behavior");
    }

    @Test(priority = 6, description = "Verify login failure with empty password")
    public void testEmptyPassword() {
        ExtentManager.getTest().log(Status.INFO, "Step 1: Enter username but leave password empty");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername(config.getProperty("username"));
        loginPage.clickLoginButton();

        ExtentManager.getTest().log(Status.INFO, "Step 2: Verify error message is displayed");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message is not displayed");
        
        ExtentManager.getTest().log(Status.INFO, "Step 3: Verify error message for empty password");
        String errorMsg = loginPage.getErrorMessage();
        Assert.assertTrue(errorMsg.contains("Password is required"), 
            "Error message mismatch. Actual: " + errorMsg);
        
        ExtentManager.getTest().log(Status.PASS, "Cannot login with empty password - Expected behavior");
    }

    @Test(priority = 7, description = "Verify login page UI elements are present")
    public void testLoginPageElements() {
        ExtentManager.getTest().log(Status.INFO, "Step 1: Verify all login page elements are displayed");
        LoginPage loginPage = new LoginPage(driver);
        
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page is not displayed");
        Assert.assertTrue(loginPage.verifyLoginPageElements(), "Login page elements are not displayed");
        
        ExtentManager.getTest().log(Status.PASS, "All login page elements are present and visible");
    }
}