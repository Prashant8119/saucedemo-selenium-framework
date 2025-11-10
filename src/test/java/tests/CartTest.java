package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;
import pages.CartPage;
import pages.LoginPage;
import pages.ProductsPage;
import utils.ExtentManager;

/**
 * CartTest class - Contains all test cases related to Cart functionality
 * Tests cover cart operations like add, remove, and checkout
 */
public class CartTest extends BaseTest {

    private ProductsPage productsPage;
    private CartPage cartPage;

    @BeforeMethod
    public void setupCart() {
        // Login
        LoginPage loginPage = new LoginPage(driver);
        productsPage = loginPage.login(
            config.getProperty("username"),
            config.getProperty("password")
        );
        
        // Add products to cart
        productsPage.addProductToCartByIndex(0);
        productsPage.addProductToCartByIndex(1);
        
        // Navigate to cart
        cartPage = productsPage.clickCartIcon();
        
        ExtentManager.getTest().log(Status.INFO, "✓ Setup: Logged in and added 2 products to cart");
    }

    @Test(priority = 1, description = "Verify cart page displays correct items")
    public void testCartPageDisplay() {
        ExtentManager.getTest().log(Status.INFO, "Step 1: Verify cart page is displayed");
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cart page is not displayed");
        
        ExtentManager.getTest().log(Status.INFO, "Step 2: Verify cart has 2 items");
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should have 2 items");
        
        ExtentManager.getTest().log(Status.PASS, "Cart page displays correct items");
    }

    @Test(priority = 2, description = "Verify removing single item from cart")
    public void testRemoveSingleItem() {
        ExtentManager.getTest().log(Status.INFO, "Step 1: Get initial cart count");
        int initialCount = cartPage.getCartItemCount();
        
        ExtentManager.getTest().log(Status.INFO, "Step 2: Remove first item");
        cartPage.removeFirstItem();
        
        ExtentManager.getTest().log(Status.INFO, "Step 3: Verify cart count decreased by 1");
        Assert.assertEquals(cartPage.getCartItemCount(), initialCount - 1, "Cart count should decrease by 1");
        
        ExtentManager.getTest().log(Status.PASS, "Item removed from cart successfully");
    }

    @Test(priority = 3, description = "Verify removing all items from cart")
    public void testRemoveAllItems() {
        ExtentManager.getTest().log(Status.INFO, "Step 1: Remove all items from cart");
        cartPage.removeAllItems();
        
        ExtentManager.getTest().log(Status.INFO, "Step 2: Verify cart is empty");
        Assert.assertTrue(cartPage.isCartEmpty(), "Cart should be empty after removing all items");
        
        ExtentManager.getTest().log(Status.PASS, "All items removed from cart successfully");
    }

    @Test(priority = 4, description = "Verify continue shopping functionality")
    public void testContinueShopping() {
        ExtentManager.getTest().log(Status.INFO, "Step 1: Click continue shopping");
        ProductsPage returnedProductsPage = cartPage.clickContinueShopping();
        
        ExtentManager.getTest().log(Status.INFO, "Step 2: Verify user is back on products page");
        Assert.assertTrue(returnedProductsPage.isProductsPageLoaded(), "User should be on products page");
        
        ExtentManager.getTest().log(Status.PASS, "Continue shopping works correctly");
    }

    @Test(priority = 5, description = "Verify cart item names are displayed")
    public void testCartItemNames() {
        ExtentManager.getTest().log(Status.INFO, "Step 1: Get all cart item names");
        var itemNames = cartPage.getAllCartItemNames();
        
        ExtentManager.getTest().log(Status.INFO, "Step 2: Verify item names list is not empty");
        Assert.assertFalse(itemNames.isEmpty(), "Cart items should have names");
        
        ExtentManager.getTest().log(Status.INFO, "Step 3: Verify each item has a valid name");
        for (String name : itemNames) {
            Assert.assertFalse(name.isEmpty(), "Item name should not be empty");
        }
        
        ExtentManager.getTest().log(Status.PASS, "Cart items: " + itemNames);
    }

    @Test(priority = 6, description = "Verify specific item exists in cart")
    public void testVerifyItemInCart() {
        ExtentManager.getTest().log(Status.INFO, "Step 1: Check if specific item is in cart");
        boolean itemExists = cartPage.isItemInCart("Sauce Labs Backpack");
        
        ExtentManager.getTest().log(Status.INFO, "Step 2: Verify item existence");
        Assert.assertTrue(itemExists, "Item 'Sauce Labs Backpack' should be in cart");
        
        ExtentManager.getTest().log(Status.PASS, "Item found in cart successfully");
    }

    @Test(priority = 7, description = "Verify proceed to checkout")
    public void testProceedToCheckout() {
        ExtentManager.getTest().log(Status.INFO, "Step 1: Click checkout button");
        var checkoutPage = cartPage.clickCheckout();
        
        ExtentManager.getTest().log(Status.INFO, "Step 2: Verify checkout page is loaded");
        Assert.assertTrue(checkoutPage.isCheckoutPageLoaded(), "Checkout page should be loaded");
        
        ExtentManager.getTest().log(Status.PASS, "Successfully navigated to checkout page");
    }

    @Test(priority = 8, description = "Verify empty cart behavior")
    public void testEmptyCart() {
        ExtentManager.getTest().log(Status.INFO, "Step 1: Remove all items");
        cartPage.removeAllItems();
        
        ExtentManager.getTest().log(Status.INFO, "Step 2: Verify cart is empty");
        Assert.assertTrue(cartPage.isCartEmpty(), "Cart should be empty");
        Assert.assertEquals(cartPage.getCartItemCount(), 0, "Cart count should be 0");
        
        ExtentManager.getTest().log(Status.PASS, "Empty cart verified successfully");
    }

    @Test(priority = 9, description = "Verify removing item by name")
    public void testRemoveItemByName() {
        ExtentManager.getTest().log(Status.INFO, "Step 1: Remove item by name");
        String itemToRemove = "Sauce Labs Backpack";
        cartPage.removeItemByName(itemToRemove);
        
        ExtentManager.getTest().log(Status.INFO, "Step 2: Verify item is removed");
        Assert.assertFalse(cartPage.isItemInCart(itemToRemove), "Item should be removed from cart");
        
        ExtentManager.getTest().log(Status.PASS, "Item removed by name successfully");
    }
}