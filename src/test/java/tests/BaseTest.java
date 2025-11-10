package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ConfigReader;
import utils.ExtentManager;
import utils.ScreenshotUtil;

import java.time.Duration;

/**
 * BaseTest class - Parent class for all Test classes
 * Contains setup, teardown, and reporting logic
 */
public class BaseTest {
    protected WebDriver driver;
    protected ConfigReader config;
    protected ExtentTest test;

    /**
     * Suite level setup - Initialize Extent Reports
     */
    @BeforeSuite
    public void setupSuite() {
        ExtentManager.createInstance();
        System.out.println("========================================");
        System.out.println("    TEST SUITE EXECUTION STARTED");
        System.out.println("========================================");
    }

    /**
     * Method level setup - Initialize WebDriver and open browser
     */
    @BeforeMethod
    public void setUp(ITestResult result) {
        // Load configuration
        config = ConfigReader.getInstance();

        // Create test in Extent Report
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        test = ExtentManager.getInstance().createTest(testName, description);
        ExtentManager.setTest(test);

        // Log test start
        test.log(Status.INFO, "Test execution started: " + testName);
        System.out.println("\n▶ Starting Test: " + testName);

        // Initialize WebDriver
        String browser = config.getBrowser().toLowerCase();
        test.log(Status.INFO, "Opening browser: " + browser);

        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (Boolean.parseBoolean(config.getProperty("headless", "false"))) {
                    chromeOptions.addArguments("--headless");
                }
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--start-maximized");
                driver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;

            default:
                test.log(Status.FAIL, "Unsupported browser: " + browser);
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }

        // Configure driver
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(
            Duration.ofSeconds(Long.parseLong(config.getProperty("implicit.wait", "10")))
        );

        // Navigate to URL
        String url = config.getUrl();
        driver.get(url);
        test.log(Status.INFO, "Navigated to URL: " + url);
        System.out.println("✓ Browser opened: " + browser);
        System.out.println("✓ Navigated to: " + url);
    }

    /**
     * Method level teardown - Handle test results and close browser
     */
    @AfterMethod
    public void tearDown(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        // Check test status and log accordingly
        if (result.getStatus() == ITestResult.SUCCESS) {
            test.log(Status.PASS, "✓ Test PASSED: " + testName);
            System.out.println("✓ Test PASSED: " + testName);
        } 
        else if (result.getStatus() == ITestResult.FAILURE) {
            test.log(Status.FAIL, "✗ Test FAILED: " + testName);
            test.log(Status.FAIL, "Failure Reason: " + result.getThrowable());
            System.out.println("✗ Test FAILED: " + testName);
            System.out.println("Reason: " + result.getThrowable().getMessage());

            // Capture screenshot on failure
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, testName);
            if (screenshotPath != null) {
                try {
                    test.addScreenCaptureFromPath(screenshotPath);
                    test.log(Status.INFO, "Screenshot attached");
                } catch (Exception e) {
                    test.log(Status.WARNING, "Failed to attach screenshot");
                }
            }
        } 
        else if (result.getStatus() == ITestResult.SKIP) {
            test.log(Status.SKIP, "⊘ Test SKIPPED: " + testName);
            test.log(Status.SKIP, "Skip Reason: " + result.getThrowable());
            System.out.println("⊘ Test SKIPPED: " + testName);
        }

        // Close browser
        if (driver != null) {
            driver.quit();
            test.log(Status.INFO, "Browser closed");
            System.out.println("✓ Browser closed");
        }

        System.out.println("----------------------------------------");
    }

    /**
     * Suite level teardown - Generate final report
     */
    @AfterSuite
    public void tearDownSuite() {
        ExtentManager.flushReports();
        System.out.println("========================================");
        System.out.println("    TEST SUITE EXECUTION COMPLETED");
        System.out.println("========================================");
        System.out.println("✓ Reports generated successfully!");
    }
}