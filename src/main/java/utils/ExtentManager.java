package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;

/**
 * ExtentManager class to manage Extent Reports
 * Provides methods to create and manage test reports
 */
public class ExtentManager {
    private static ExtentReports extent;
    private static ExtentSparkReporter sparkReporter;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    /**
     * Initialize Extent Reports
     */
    public static ExtentReports createInstance() {
        // Create reports directory if not exists
        File reportDir = new File("./test-output/reports/");
        if (!reportDir.exists()) {
            reportDir.mkdirs();
        }

        String reportPath = "./test-output/reports/ExtentReport.html";
        
        // Create Spark Reporter
        sparkReporter = new ExtentSparkReporter(reportPath);
        
        // Configure Report
        sparkReporter.config().setDocumentTitle("SauceDemo Automation Report");
        sparkReporter.config().setReportName("Test Execution Report");
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setTimeStampFormat("dd-MM-yyyy HH:mm:ss");
        
        // Create Extent Reports instance
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        
        // Set system information
        extent.setSystemInfo("Application", "SauceDemo");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("User", System.getProperty("user.name"));
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        
        System.out.println("✓ Extent Report initialized: " + reportPath);
        return extent;
    }

    /**
     * Get Extent Reports instance
     */
    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }

    /**
     * Set ExtentTest for current thread
     */
    public static void setTest(ExtentTest test) {
        extentTest.set(test);
    }

    /**
     * Get ExtentTest for current thread
     */
    public static ExtentTest getTest() {
        return extentTest.get();
    }

    /**
     * Flush reports
     */
    public static void flushReports() {
        if (extent != null) {
            extent.flush();
            System.out.println("✓ Extent Report saved successfully");
        }
    }
}