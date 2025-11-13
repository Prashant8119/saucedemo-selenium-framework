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

public class BaseTest {
    protected WebDriver driver;
    protected ConfigReader config;
    protected ExtentTest test;

    @BeforeSuite
    public void setupSuite() {
        ExtentManager.createInstance();
        System.out.println("========================================");
        System.out.println("    TEST SUITE EXECUTION STARTED");
        System.out.println("========================================");
    }

    /*
    @BeforeMethod
    public void setUp(ITestResult result) {
        config = ConfigReader.getInstance();

        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        test = ExtentManager.getInstance().createTest(testName, description);
        ExtentManager.setTest(test);

        test.log(Status.INFO, "Test execution started: " + testName);
        System.out.println("\n▶ Starting Test: " + testName);

        String browser = config.getBrowser().toLowerCase();
        test.log(Status.INFO, "Opening browser: " + browser);

        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                
                // Headless mode for Jenkins
                chromeOptions.addArguments("--headless=new");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--window-size=1920,1080");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                chromeOptions.addArguments("--disable-extensions");
                chromeOptions.addArguments("--disable-popup-blocking");
                
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

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(
            Duration.ofSeconds(Long.parseLong(config.getProperty("implicit.wait", "15")))
        );
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        String url = config.getUrl();
        driver.get(url);
        test.log(Status.INFO, "Navigated to URL: " + url);
        System.out.println("✓ Browser opened: " + browser);
        System.out.println("✓ Navigated to: " + url);
    }

    */
    @BeforeMethod
    public void setUp(ITestResult result) {
        config = ConfigReader.getInstance();

        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        test = ExtentManager.getInstance().createTest(testName, description);
        ExtentManager.setTest(test);

        test.log(Status.INFO, "Test execution started: " + testName);
        System.out.println("\n▶ Starting Test: " + testName);

        String browser = config.getBrowser().toLowerCase();
        test.log(Status.INFO, "Opening browser: " + browser);

        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                
                // Optimized options for Jenkins headless mode
                chromeOptions.addArguments("--headless=new");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--window-size=1920,1080");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-popup-blocking");
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                chromeOptions.addArguments("--disable-extensions");
                chromeOptions.addArguments("--disable-infobars");
                chromeOptions.addArguments("--disable-web-security");
                chromeOptions.addArguments("--allow-running-insecure-content");
                chromeOptions.addArguments("--ignore-certificate-errors");
                
                // CRITICAL: Disable automation flags
                chromeOptions.setExperimentalOption("excludeSwitches", 
                    java.util.Arrays.asList("enable-automation"));
                chromeOptions.setExperimentalOption("useAutomationExtension", false);
                
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

        driver.manage().window().maximize();
        
        // Increased timeouts for Jenkins
        driver.manage().timeouts().implicitlyWait(
            Duration.ofSeconds(20)  // Increased from 15
        );
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));

        String url = config.getUrl();
        driver.get(url);
        
        // Wait for page to fully load
        try {
            Thread.sleep(2000);  // Initial page load wait
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        test.log(Status.INFO, "Navigated to URL: " + url);
        System.out.println("✓ Browser opened: " + browser);
        System.out.println("✓ Navigated to: " + url);
    }
    
    
    @AfterMethod
    public void tearDown(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        if (result.getStatus() == ITestResult.SUCCESS) {
            test.log(Status.PASS, "✓ Test PASSED: " + testName);
            System.out.println("✓ Test PASSED: " + testName);
        } 
        else if (result.getStatus() == ITestResult.FAILURE) {
            test.log(Status.FAIL, "✗ Test FAILED: " + testName);
            test.log(Status.FAIL, "Failure Reason: " + result.getThrowable());
            System.out.println("✗ Test FAILED: " + testName);
            System.out.println("Reason: " + result.getThrowable().getMessage());

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

        if (driver != null) {
            driver.quit();
            test.log(Status.INFO, "Browser closed");
            System.out.println("✓ Browser closed");
        }

        System.out.println("----------------------------------------");
    }

    @AfterSuite
    public void tearDownSuite() {
        ExtentManager.flushReports();
        System.out.println("========================================");
        System.out.println("    TEST SUITE EXECUTION COMPLETED");
        System.out.println("========================================");
        System.out.println("✓ Reports generated successfully!");
    }
}