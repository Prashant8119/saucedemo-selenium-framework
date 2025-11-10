package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotUtil class to capture screenshots during test execution
 * Screenshots are saved with timestamp for uniqueness
 */
public class ScreenshotUtil {
    
    /**
     * Capture screenshot and save to specified path
     * @param driver WebDriver instance
     * @param testName Name of the test case
     * @return Path of saved screenshot
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        // Create timestamp
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        
        // Create filename
        String fileName = testName + "_" + timestamp + ".png";
        
        // Create directory if not exists
        File screenshotDir = new File("./test-output/screenshots/");
        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs();
        }
        
        // Full path
        String screenshotPath = "./test-output/screenshots/" + fileName;

        try {
            // Take screenshot
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File(screenshotPath);
            
            // Copy file to destination
            FileUtils.copyFile(source, destination);
            
            System.out.println("✓ Screenshot captured: " + screenshotPath);
            return screenshotPath;
        } catch (IOException e) {
            System.err.println("✗ Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }

    /**
     * Capture screenshot with base64 encoding (for reports)
     * @param driver WebDriver instance
     * @return Base64 encoded screenshot
     */
    public static String captureBase64Screenshot(WebDriver driver) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            return ts.getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            System.err.println("✗ Failed to capture base64 screenshot: " + e.getMessage());
            return null;
        }
    }
}