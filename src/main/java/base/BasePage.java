package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage class - Parent class for all Page Objects
 * Contains common methods used across all pages
 */
public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    /**
     * Constructor to initialize WebDriver and WebDriverWait
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    /**
     * Click on element after waiting for it to be clickable
     
    protected void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }
    */
    protected void click(WebElement element) {
        try {
            WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // Ensure element is visible and clickable
            localWait.until(ExpectedConditions.visibilityOf(element));
            localWait.until(ExpectedConditions.elementToBeClickable(element));

            element.click();
            System.out.println("✅ Clicked element: " + element);
        } catch (Exception e) {
            System.out.println("⚠️ Element click failed, retrying with JS Click...");

            // Fallback to JavaScript click if normal click fails
            try {
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                System.out.println("✅ JS Click successful on: " + element);
            } catch (Exception jsEx) {
                System.out.println("❌ JS Click also failed for element: " + element);
                throw jsEx;
            }
        }
    }


    /**
     * Type text into element after waiting for visibility
     */
    protected void type(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Get text from element after waiting for visibility
     */
    protected String getText(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        return element.getText();
    }

    /**
     * Check if element is displayed
     */
    protected boolean isDisplayed(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if element is enabled
     */
    protected boolean isEnabled(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get current page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Get current page URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}