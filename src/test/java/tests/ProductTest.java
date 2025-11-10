package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;
import pages.LoginPage;
import pages.ProductsPage;
import utils.ExtentManager;

/**
 * ProductTest class - Contains all test cases related to Products functionality
 * Tests cover product display, cart operations, and product interactions
 */
public class ProductTest extends BaseTest {

    private ProductsPage productsPage;

    @BeforeMethod
    public void loginBeforeTest() {
        LoginPage loginPage = new LoginPage(driver);
        productsPage = loginPage.login(
            config.getProperty("username"),
            config.getProperty("password")
        );
        ExtentManager.getTest().log(Status.INFO, "✓ Logged in successfully");
    }

    @Test(priority = 1, description = "Verify products are displayed on products page")
    public void testProductsDisplayed() {
        ExtentManager.getTest().log(Status.INFO, "Step 1: Verify products page is loaded");
        Assert.assertTrue(productsPage.isProductsPageLoaded(), "Products page is not loaded");
        
        ExtentManager.getTest().log(Status.INFO, "Step 2: Verify products are displayed");
        Assert.assertTrue(productsPage.getProductCount() > 0, "No products are displayed");
        
        ExtentManager.getTest().log(Status.INFO, "Step 3: Verify correct number of products");
        Assert.assertEquals(productsPage.getProductCount(), 6, "Expected 6 products but found: " + productsPage.getProductCount());
        
        ExtentManager.getTest().log(Status.PASS, "All 6 products are displayed correctly");
    }

    @Test(priority = 2, description = "Verify adding single product to cart")
    public void testAddSingleProductToCart() {
        ExtentManager.getTest().log(Status.INFO, "Step 1: Add first product to cart");
        productsPage.addFirstProductToCart();
        
        ExtentManager.getTest().log(Status.INFO, "Step 2: Verify cart badge is displayed");
        Assert.assertTrue(productsPage.isCartBadgeDisplayed(), "Cart badge is not displayed after adding product");
        
        ExtentManager.getTest().log(Status.INFO, "Step 3: Verify cart count is 1");
        Assert.assertEquals(productsPage.getCartItemCount(), "1", "Cart count should be 1 after adding one product");
        
        ExtentManager.getTest().log(Status.PASS, "Product added to cart successfully");
    }

    @Test(priority = 3, description = "Verify adding multiple products to cart")
    public void testAddMultipleProductsToCart() {
        ExtentManager.getTest().log(Status.INFO, "Step 1: Add first product to cart");
        productsPage.addProductToCartByIndex(0);
        
        ExtentManager.getTest().log(Status.INFO, "Step 2: Add second product to cart");
        productsPage.addProductToCartByIndex(1);
        
        ExtentManager.getTest().log(Status.INFO, "Step 3: Add third product to cart");
        productsPage.addProductToCartByIndex(2);
        
        ExtentManager.getTest().log(Status.INFO, "Step 4: Verify cart count is 3");
        Assert.assertEquals(productsPage.getCartItemCount(), "3", "Cart count should be 3 after adding three products");
        
        ExtentManager.getTest().log(Status.PASS, "Multiple products added to cart successfully");
    }

    @Test(priority = 4, description = "Verify adding all products to cart")
    public void testAddAllProductsToCart() {
        ExtentManager.getTest().log(Status.INFO, "Step 1: Add all 6 products to cart");
        for (int i = 0; i < 6; i++) {
            productsPage.addProductToCartByIndex(i);
        }
        
        ExtentManager.getTest().log(Status.INFO, "Step 2: Verify cart count is 6");
        Assert.assertEquals(productsPage.getCartItemCount(), "6", "Cart count should be 6 after adding all products");
        
        ExtentManager.getTest().log(Status.PASS, "All products added to cart successfully");
    }

    @Test(priority = 5, description = "Verify product names are displayed")
    public void testProductNamesDisplayed() {
        ExtentManager.getTest().log(Status.INFO, "Step 1: Get all product names");
        var productNames = productsPage.getAllProductNames();
        
        ExtentManager.getTest().log(Status.INFO, "Step 2: Verify product names list is not empty");
        Assert.assertFalse(productNames.isEmpty(), "Product names list is empty");
        
        ExtentManager.getTest().log(Status.INFO, "Step 3: Verify each product has a name");
        for (String name : productNames) {
            Assert.assertFalse(name.isEmpty(), "Product name is empty");
        }
        
        ExtentManager.getTest().log(Status.PASS, "All products have valid names: " + productNames);
    }

    @Test(priority = 6, description = "Verify adding product by name")
    public void testAddProductByName() {
        ExtentManager.getTest().log(Status.INFO, "Step 1: Add product by name 'Sauce Labs Backpack'");
        productsPage.addProductToCartByName("Sauce Labs Backpack");
        
        ExtentManager.getTest().log(Status.INFO, "Step 2: Verify cart count is 1");
        Assert.assertEquals(productsPage.getCartItemCount(), "1", "Cart count should be 1");
        
        ExtentManager.getTest().log(Status.PASS, "Product added by name successfully");
    }

    @Test(priority = 7, description = "Verify navigation to cart page")
    public void testNavigateToCart() {
        ExtentManager.getTest().log(Status.INFO, "Step 1: Add a product to cart");
        productsPage.addFirstProductToCart();
        
        ExtentManager.getTest().log(Status.INFO, "Step 2: Click cart icon");
        var cartPage = productsPage.clickCartIcon();
        
        ExtentManager.getTest().log(Status.INFO, "Step 3: Verify cart page is displayed");
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cart page is not displayed");
        
        ExtentManager.getTest().log(Status.PASS, "Successfully navigated to cart page");
    }

    @Test(priority = 8, description = "Verify logout functionality")
    public void testLogout() {
        ExtentManager.getTest().log(Status.INFO, "Step 1: Click logout from menu");
        var loginPage = productsPage.logout();
        
        ExtentManager.getTest().log(Status.INFO, "Step 2: Verify user is redirected to login page");
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "User is not redirected to login page");
        
        ExtentManager.getTest().log(Status.PASS, "Logout successful");
    }
}