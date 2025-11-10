package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import base.BasePage;
import java.util.List;

/**
 * ProductsPage class - Page Object for Products/Inventory Page
 * Contains all elements and actions for products functionality
 */
public class ProductsPage extends BasePage {

    // Page Elements
    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "inventory_item")
    private List<WebElement> productItems;

    @FindBy(className = "shopping_cart_link")
    private WebElement cartIcon;

    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    @FindBy(css = ".inventory_item button")
    private List<WebElement> addToCartButtons;

    @FindBy(className = "inventory_item_name")
    private List<WebElement> productNames;

    @FindBy(className = "inventory_item_price")
    private List<WebElement> productPrices;

    @FindBy(className = "product_sort_container")
    private WebElement sortDropdown;

    // Constructor
    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    // Page Actions

    /**
     * Check if page title is displayed
     */
    public boolean isPageTitleDisplayed() {
        return isDisplayed(pageTitle);
    }

    /**
     * Get page title text
     */
    public String getPageTitle() {
        return getText(pageTitle);
    }

    /**
     * Get total count of products displayed
     */
    public int getProductCount() {
        System.out.println("  → Total products found: " + productItems.size());
        return productItems.size();
    }

    /**
     * Get list of all product names
     */
    public List<String> getAllProductNames() {
        return productNames.stream()
            .map(this::getText)
            .toList();
    }

    /**
     * Add first product to cart
     */
    public ProductsPage addFirstProductToCart() {
        click(addToCartButtons.get(0));
        System.out.println("  → Added first product to cart");
        return this;
    }

    /**
     * Add product to cart by index
     */
    public ProductsPage addProductToCartByIndex(int index) {
        if (index >= 0 && index < addToCartButtons.size()) {
            String productName = getText(productNames.get(index));
            click(addToCartButtons.get(index));
            System.out.println("  → Added product to cart: " + productName);
        } else {
            System.out.println("  ✗ Invalid product index: " + index);
        }
        return this;
    }

    /**
     * Add product to cart by name
     */
    public ProductsPage addProductToCartByName(String productName) {
        for (int i = 0; i < productNames.size(); i++) {
            if (getText(productNames.get(i)).equalsIgnoreCase(productName)) {
                click(addToCartButtons.get(i));
                System.out.println("  → Added product to cart: " + productName);
                return this;
            }
        }
        System.out.println("  ✗ Product not found: " + productName);
        return this;
    }

    /**
     * Click on cart icon to navigate to cart page
     */
    public CartPage clickCartIcon() {
        click(cartIcon);
        System.out.println("  → Clicked cart icon");
        return new CartPage(driver);
    }

    /**
     * Get cart item count from badge
     */
    public String getCartItemCount() {
        if (isDisplayed(cartBadge)) {
            String count = getText(cartBadge);
            System.out.println("  → Cart item count: " + count);
            return count;
        }
        System.out.println("  → Cart is empty");
        return "0";
    }

    /**
     * Check if cart badge is displayed
     */
    public boolean isCartBadgeDisplayed() {
        return isDisplayed(cartBadge);
    }

    /**
     * Open menu
     */
    public ProductsPage openMenu() {
        click(menuButton);
        System.out.println("  → Opened menu");
        return this;
    }

    /**
     * Logout from application
     */
    public LoginPage logout() {
        openMenu();
        click(logoutLink);
        System.out.println("  → Logged out successfully");
        return new LoginPage(driver);
    }

    /**
     * Verify products page is loaded
     */
    public boolean isProductsPageLoaded() {
        return isPageTitleDisplayed() && getPageTitle().equals("Products");
    }

    /**
     * Get product price by index
     */
    public String getProductPriceByIndex(int index) {
        if (index >= 0 && index < productPrices.size()) {
            return getText(productPrices.get(index));
        }
        return null;
    }
}