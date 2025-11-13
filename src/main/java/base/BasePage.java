/*

package base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.js = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    protected void click(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            
            // Scroll into view first
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            Thread.sleep(300);
            
            // Try normal click
            element.click();
            Thread.sleep(500); // Wait for action to complete
            
        } catch (Exception e) {
            try {
                // Fallback to JavaScript click
                js.executeScript("arguments[0].click();", element);
                Thread.sleep(500);
            } catch (Exception jsEx) {
                throw new RuntimeException("Failed to click element", jsEx);
            }
        }
    }

    protected void type(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        return element.getText();
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isEnabled(WebElement element) {
        try {
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}

*/

package base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Enhanced click with multiple retry strategies for headless mode
     */
    protected void click(WebElement element) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                // Wait for element to be clickable
                wait.until(ExpectedConditions.elementToBeClickable(element));
                
                // Scroll into view with JavaScript
                js.executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", element);
                
                // Small wait after scroll
                Thread.sleep(200);
                
                // Highlight element (helps in debugging)
                js.executeScript("arguments[0].style.border='2px solid red'", element);
                Thread.sleep(100);
                
                // Try standard click
                element.click();
                
                // Wait for any JavaScript to complete
                Thread.sleep(500);
                
                System.out.println("✓ Successfully clicked element");
                return;
                
            } catch (Exception e) {
                attempts++;
                System.out.println("⚠️ Click attempt " + attempts + " failed, trying JavaScript click...");
                
                try {
                    // JavaScript click as fallback
                    js.executeScript("arguments[0].click();", element);
                    Thread.sleep(500);
                    System.out.println("✓ JavaScript click successful");
                    return;
                    
                } catch (Exception jsEx) {
                    if (attempts >= 3) {
                        System.err.println("✗ All click attempts failed");
                        throw new RuntimeException("Failed to click element after " + attempts + " attempts", e);
                    }
                    
                    try {
                        Thread.sleep(1000); // Wait before retry
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    protected void type(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return element.getText();
        } catch (Exception e) {
            return "";
        }
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isEnabled(WebElement element) {
        try {
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Wait for page to be fully loaded
     */
    protected void waitForPageLoad() {
        wait.until(driver -> 
            ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete")
        );
    }
}