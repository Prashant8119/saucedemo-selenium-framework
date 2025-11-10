package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;
import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ProductsPage;
import utils.ExtentManager;

/**
 * EndToEndTest class - Contains complete end-to-end test scenarios
 * Tests cover full user journeys from login to order completion
 */
public class EndToEndTest extends BaseTest {

    @Test(priority = 1, description = "Complete purchase flow: Login → Add to Cart → Checkout → Complete Order")
    public void testCompletePurchaseFlow() {
        // Step 1: Login
        ExtentManager.getTest().log(Status.INFO, "Step 1: Login with valid credentials");
        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = loginPage.login(
            config.getProperty("username"),
            config.getProperty("password")
        );
        Assert.assertTrue(productsPage.isProductsPageLoaded(), "Login failed - Products page not loaded");
        ExtentManager.getTest().log(Status.PASS, "✓ Login successful");

        // Step 2: Add products to cart
        ExtentManager.getTest().log(Status.INFO, "Step 2: Add products to cart");
        productsPage.addProductToCartByIndex(0);
        productsPage.addProductToCartByIndex(1);
        Assert.assertEquals(productsPage.getCartItemCount(), "2", "Failed to add products to cart");
        ExtentManager.getTest().log(Status.PASS, "✓ Added 2 products to cart");

        // Step 3: Go to cart
        ExtentManager.getTest().log(Status.INFO, "Step 3: Navigate to cart");
        CartPage cartPage = productsPage.clickCartIcon();
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cart page not loaded");
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart items mismatch");
        ExtentManager.getTest().log(Status.PASS, "✓ Cart page loaded with 2 items");

        // Step 4: Proceed to checkout
        ExtentManager.getTest().log(Status.INFO, "Step 4: Proceed to checkout");
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        Assert.assertTrue(checkoutPage.isCheckoutPageLoaded(), "Checkout page not loaded");
        ExtentManager.getTest().log(Status.PASS, "✓ Checkout page loaded");

        // Step 5: Fill checkout information
        ExtentManager.getTest().log(Status.INFO, "Step 5: Fill checkout information");
        checkoutPage.fillCheckoutInformation(
            config.getProperty("first_name"),
            config.getProperty("last_name"),
            config.getProperty("postal_code")
        );
        ExtentManager.getTest().log(Status.PASS, "✓ Checkout information filled");

        // Step 6: Complete order
        ExtentManager.getTest().log(Status.INFO, "Step 6: Complete the order");
        checkoutPage.clickFinish();
        ExtentManager.getTest().log(Status.PASS, "✓ Order completion initiated");

        // Step 7: Verify order completion
        ExtentManager.getTest().log(Status.INFO, "Step 7: Verify order completion");
        Assert.assertTrue(checkoutPage.isOrderComplete(), "Order not completed");
        Assert.assertEquals(
            checkoutPage.getConfirmationHeader(),
            "Thank you for your order!",
            "Confirmation message mismatch"
        );
        ExtentManager.getTest().log(Status.PASS, "✓ Order completed successfully!");
    }

    @Test(priority = 2, description = "Test complete flow with single product")
    public void testSingleProductPurchase() {
        ExtentManager.getTest().log(Status.INFO, "Testing single product purchase flow");

        // Login
        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = loginPage.login(
            config.getProperty("username"),
            config.getProperty("password")
        );

        // Add single product
        productsPage.addProductToCartByName("Sauce Labs Backpack");
        Assert.assertEquals(productsPage.getCartItemCount(), "1");

        // Checkout
        CartPage cartPage = productsPage.clickCartIcon();
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        checkoutPage.fillCheckoutInformation("Jane", "Smith", "54321");
        checkoutPage.clickFinish();

        // Verify
        Assert.assertTrue(checkoutPage.isOrderComplete());
        ExtentManager.getTest().log(Status.PASS, "Single product purchase completed");
    }

    @Test(priority = 3, description = "Test shopping cart modification during checkout")
    public void testCartModificationFlow() {
        ExtentManager.getTest().log(Status.INFO, "Testing cart modification flow");

        // Login and add products
        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = loginPage.login(
            config.getProperty("username"),
            config.getProperty("password")
        );
        productsPage.addProductToCartByIndex(0);
        productsPage.addProductToCartByIndex(1);
        productsPage.addProductToCartByIndex(2);

        // Go to cart and remove one item
        CartPage cartPage = productsPage.clickCartIcon();
        int initialCount = cartPage.getCartItemCount();
        cartPage.removeFirstItem();
        Assert.assertEquals(cartPage.getCartItemCount(), initialCount - 1);

        // Continue shopping and add more
        productsPage = cartPage.clickContinueShopping();
        productsPage.addProductToCartByIndex(3);

        // Proceed to checkout
        cartPage = productsPage.clickCartIcon();
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        checkoutPage.fillCheckoutInformation("Test", "User", "11111");
        checkoutPage.clickFinish();

        Assert.assertTrue(checkoutPage.isOrderComplete());
        ExtentManager.getTest().log(Status.PASS, "Cart modification flow completed");
    }

    @Test(priority = 4, description = "Test purchase all products")
    public void testPurchaseAllProducts() {
        ExtentManager.getTest().log(Status.INFO, "Testing purchase of all products");

        // Login
        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = loginPage.login(
            config.getProperty("username"),
            config.getProperty("password")
        );

        // Add all products
        for (int i = 0; i < 6; i++) {
            productsPage.addProductToCartByIndex(i);
        }
        Assert.assertEquals(productsPage.getCartItemCount(), "6");

        // Complete purchase
        CartPage cartPage = productsPage.clickCartIcon();
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        checkoutPage.fillCheckoutInformation(
            config.getProperty("first_name"),
            config.getProperty("last_name"),
            config.getProperty("postal_code")
        );
        checkoutPage.clickFinish();

        // Verify
        Assert.assertTrue(checkoutPage.isOrderComplete());
        ExtentManager.getTest().log(Status.PASS, "All products purchased successfully");
    }

    @Test(priority = 5, description = "Test checkout with missing information")
    public void testCheckoutWithMissingInfo() {
        ExtentManager.getTest().log(Status.INFO, "Testing checkout with incomplete information");

        // Login and add product
        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = loginPage.login(
            config.getProperty("username"),
            config.getProperty("password")
        );
        productsPage.addFirstProductToCart();

        // Go to checkout without filling information
        CartPage cartPage = productsPage.clickCartIcon();
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        checkoutPage.clickContinue(); // Try to continue without filling info

        // Verify error message
        Assert.assertTrue(checkoutPage.isErrorMessageDisplayed(), "Error message should be displayed");
        ExtentManager.getTest().log(Status.PASS, "Validation works - Cannot proceed without information");
    }

    @Test(priority = 6, description = "Test complete flow with logout")
    public void testPurchaseAndLogout() {
        ExtentManager.getTest().log(Status.INFO, "Testing complete flow with logout");

        // Complete purchase
        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = loginPage.login(
            config.getProperty("username"),
            config.getProperty("password")
        );
        productsPage.addFirstProductToCart();
        CartPage cartPage = productsPage.clickCartIcon();
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        checkoutPage.fillCheckoutInformation("Final", "Test", "99999");
        checkoutPage.clickFinish();
        Assert.assertTrue(checkoutPage.isOrderComplete());

        // Navigate back and logout
        productsPage = checkoutPage.clickBackToProducts();
        loginPage = productsPage.logout();
        Assert.assertTrue(loginPage.isLoginPageDisplayed());

        ExtentManager.getTest().log(Status.PASS, "Complete flow with logout successful");
    }
}