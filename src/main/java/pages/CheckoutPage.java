package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import base.BasePage;

/**
 * CheckoutPage class - Page Object for Checkout Pages
 * Handles checkout information, overview, and completion
 */
public class CheckoutPage extends BasePage {

    // Checkout Step 1: Your Information
    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement postalCodeField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(id = "cancel")
    private WebElement cancelButton;

    // Checkout Step 2: Overview
    @FindBy(className = "summary_subtotal_label")
    private WebElement subtotalLabel;

    @FindBy(className = "summary_tax_label")
    private WebElement taxLabel;

    @FindBy(className = "summary_total_label")
    private WebElement totalLabel;

    @FindBy(id = "finish")
    private WebElement finishButton;

    // Checkout Complete
    @FindBy(className = "complete-header")
    private WebElement confirmationHeader;

    @FindBy(className = "complete-text")
    private WebElement confirmationText;

    @FindBy(id = "back-to-products")
    private WebElement backToProductsButton;

    // Error message
    @FindBy(css = "h3[data-test='error']")
    private WebElement errorMessage;

    // Constructor
    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    // Checkout Step 1 Actions

    /**
     * Enter first name
     */
    public CheckoutPage enterFirstName(String firstName) {
        type(firstNameField, firstName);
        System.out.println("  → Entered first name: " + firstName);
        return this;
    }

    /**
     * Enter last name
     */
    public CheckoutPage enterLastName(String lastName) {
        type(lastNameField, lastName);
        System.out.println("  → Entered last name: " + lastName);
        return this;
    }

    /**
     * Enter postal code
     */
    public CheckoutPage enterPostalCode(String postalCode) {
        type(postalCodeField, postalCode);
        System.out.println("  → Entered postal code: " + postalCode);
        return this;
    }

    /**
     * Click continue button
     */
    public CheckoutPage clickContinue() {
        click(continueButton);
        System.out.println("  → Clicked continue button");
        return this;
    }

    /**
     * Fill complete checkout information and proceed
     */
    public CheckoutPage fillCheckoutInformation(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
        clickContinue();
        System.out.println("  → Filled checkout information");
        return this;
    }

    /**
     * Click cancel button
     */
    public CartPage clickCancel() {
        click(cancelButton);
        System.out.println("  → Clicked cancel button");
        return new CartPage(driver);
    }

    // Checkout Step 2 Actions

    /**
     * Get subtotal amount
     */
    public String getSubtotal() {
        return getText(subtotalLabel);
    }

    /**
     * Get tax amount
     */
    public String getTax() {
        return getText(taxLabel);
    }

    /**
     * Get total amount
     */
    public String getTotal() {
        return getText(totalLabel);
    }

    /**
     * Click finish button to complete order
     */
    public CheckoutPage clickFinish() {
        click(finishButton);
        System.out.println("  → Clicked finish button");
        return this;
    }

    // Checkout Complete Actions

    /**
     * Get confirmation message header
     */
    public String getConfirmationHeader() {
        return getText(confirmationHeader);
    }

    /**
     * Get confirmation message text
     */
    public String getConfirmationText() {
        return getText(confirmationText);
    }

    /**
     * Check if order is complete
     */
    public boolean isOrderComplete() {
        boolean isComplete = isDisplayed(confirmationHeader);
        if (isComplete) {
            System.out.println("  ✓ Order completed successfully!");
        }
        return isComplete;
    }

    /**
     * Click back to products button
     */
    public ProductsPage clickBackToProducts() {
        click(backToProductsButton);
        System.out.println("  → Navigated back to products");
        return new ProductsPage(driver);
    }

    /**
     * Get error message
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
     * Verify checkout page is loaded
     */
    public boolean isCheckoutPageLoaded() {
        return isDisplayed(firstNameField) || isDisplayed(finishButton);
    }
}