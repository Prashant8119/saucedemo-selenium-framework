package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import base.BasePage;
import java.util.List;

/**
 * CartPage class - Page Object for Shopping Cart Page
 * Contains all elements and actions for cart functionality
 */
public class CartPage extends BasePage {

    // Page Elements
    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    @FindBy(className = "cart_button")
    private List<WebElement> removeButtons;

    @FindBy(className = "inventory_item_name")
    private List<WebElement> cartItemNames;

    @FindBy(className = "inventory_item_price")
    private List<WebElement> cartItemPrices;

    @FindBy(className = "cart_quantity")
    private List<WebElement> cartItemQuantities;

    // Constructor
    public CartPage(WebDriver driver) {
        super(driver);
    }

    // Page Actions

    /**
     * Get page title
     */
    public String getPageTitle() {
        return getText(pageTitle);
    }

    /**
     * Check if cart page is displayed
     */
    public boolean isCartPageDisplayed() {
        return isDisplayed(pageTitle) && getPageTitle().equals("Your Cart");
    }

    /**
     * Get count of items in cart
     */
    public int getCartItemCount() {
        int count = cartItems.size();
        System.out.println("  → Items in cart: " + count);
        return count;
    }

    /**
     * Get list of all cart item names
     */
    public List<String> getAllCartItemNames() {
        return cartItemNames.stream()
            .map(this::getText)
            .toList();
    }

    /**
     * Click checkout button to proceed to checkout
     */
    /*
    public CheckoutPage clickCheckout() {
        click(checkoutButton);
        System.out.println("  → Clicked checkout button");
        return new CheckoutPage(driver);
    }

*/
    /**
     * Click continue shopping button to go back to products
     */
    /*
    public ProductsPage clickContinueShopping() {
        click(continueShoppingButton);
        System.out.println("  → Clicked continue shopping");
        return new ProductsPage(driver);
    }
    */
    
    /**
     * Click checkout button with wait
     */
    public CheckoutPage clickCheckout() {
        try {
            // Wait for cart page to fully load
            Thread.sleep(1000);
            
            // Scroll to checkout button
            js.executeScript("arguments[0].scrollIntoView(true);", checkoutButton);
            Thread.sleep(500);
            
            click(checkoutButton);
            System.out.println("  → Clicked checkout button");
            
            // Wait for checkout page to load
            Thread.sleep(1000);
            
        } catch (Exception e) {
            System.err.println("  ✗ Failed to click checkout: " + e.getMessage());
        }
        return new CheckoutPage(driver);
    }

    /**
     * Click continue shopping button with wait
     */
    public ProductsPage clickContinueShopping() {
        try {
            // Wait for cart page to fully load
            Thread.sleep(1000);
            
            // Scroll to button
            js.executeScript("arguments[0].scrollIntoView(true);", continueShoppingButton);
            Thread.sleep(500);
            
            click(continueShoppingButton);
            System.out.println("  → Clicked continue shopping");
            
            // Wait for products page to load
            Thread.sleep(1000);
            
        } catch (Exception e) {
            System.err.println("  ✗ Failed to click continue shopping: " + e.getMessage());
        }
        return new ProductsPage(driver);
    }

    /**
     * Remove first item from cart
     */
    public CartPage removeFirstItem() {
        if (!removeButtons.isEmpty()) {
            String itemName = getText(cartItemNames.get(0));
            click(removeButtons.get(0));
            System.out.println("  → Removed item from cart: " + itemName);
        } else {
            System.out.println("  ✗ Cart is empty, no items to remove");
        }
        return this;
    }

    /**
     * Remove item from cart by index
     */
    public CartPage removeItemByIndex(int index) {
        if (index >= 0 && index < removeButtons.size()) {
            String itemName = getText(cartItemNames.get(index));
            click(removeButtons.get(index));
            System.out.println("  → Removed item from cart: " + itemName);
        } else {
            System.out.println("  ✗ Invalid item index: " + index);
        }
        return this;
    }

    /**
     * Remove item from cart by name
     */
    public CartPage removeItemByName(String itemName) {
        for (int i = 0; i < cartItemNames.size(); i++) {
            if (getText(cartItemNames.get(i)).equalsIgnoreCase(itemName)) {
                click(removeButtons.get(i));
                System.out.println("  → Removed item from cart: " + itemName);
                return this;
            }
        }
        System.out.println("  ✗ Item not found in cart: " + itemName);
        return this;
    }

    /**
     * Remove all items from cart
     */
    public CartPage removeAllItems() {
        int itemCount = removeButtons.size();
        for (int i = itemCount - 1; i >= 0; i--) {
            click(removeButtons.get(i));
        }
        System.out.println("  → Removed all items from cart (" + itemCount + " items)");
        return this;
    }

    /**
     * Check if cart is empty
     */
    public boolean isCartEmpty() {
        return cartItems.isEmpty();
    }

    /**
     * Verify specific item is in cart
     */
    public boolean isItemInCart(String itemName) {
        return cartItemNames.stream()
            .anyMatch(item -> getText(item).equalsIgnoreCase(itemName));
    }

    /**
     * Get total price of all items (calculation)
     */
    public double getTotalPrice() {
        double total = 0.0;
        for (WebElement priceElement : cartItemPrices) {
            String priceText = getText(priceElement).replace("$", "");
            total += Double.parseDouble(priceText);
        }
        System.out.println("  → Total cart price: $" + total);
        return total;
    }
}