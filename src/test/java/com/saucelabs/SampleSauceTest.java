package com.saucelabs;

import com.saucelabs.common.SauceOnDemandAuthentication;
 
import org.junit.After; 
import org.junit.Before;
import org.junit.Rule; 
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.saucelabs.junit.ConcurrentParameterized;
import com.saucelabs.junit.SauceOnDemandTestWatcher;

import java.net.URL; 
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

import com.saucelabs.common.SauceOnDemandSessionIdProvider;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
  
/** 
 * Demonstrates how to write a JUnit test that runs tests against Sauce Labs using multiple browsers in parallel.
 * <p/>
 * The test also includes the {@link SauceOnDemandTestWatcher} which will invoke the Sauce REST API to mark
 * the test as passed or failed.
 *
 * @author Kristian Meier
 */
@RunWith(ConcurrentParameterized.class)
public class SampleSauceTest implements SauceOnDemandSessionIdProvider {

    /**
     * Constructs a {@link SauceOnDemandAuthentication} instance using the supplied user name/access key.  To use the authentication
     * supplied by environment variables or from an external file, use the no-arg {@link SauceOnDemandAuthentication} constructor.
     */
 
    public String username = System.getenv("SAUCE_USERNAME");
    public String accesskey = System.getenv("SAUCE_ACCESS_KEY");

 public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(username, accesskey);

   // public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(username, accesskey);

    /**
     * JUnit Rule which will mark the Sauce Job as passed/failed when the test succeeds or fails.
     */
    @Rule
    public SauceOnDemandTestWatcher resultReportingTestWatcher = new SauceOnDemandTestWatcher(this, authentication);

    @Rule public TestName name = new TestName() {
        public String getMethodName() {
                return String.format("%s : (%s %s %s)", super.getMethodName(), os, browser, version);
        };
    };
    /**
     * Represents the browser to be used as part of the test run.
     */
    private String browser;
    /**
     * Represents the operating system to be used as part of the test run.
     */
    private String os;
    /**
     * Represents the version of the browser to be used as part of the test run.
     */
    private String version;
    /**
     * Represents the deviceName of mobile device
     */
    private String deviceName;
    /**
     * Represents the device-orientation of mobile device
     */

    //private String name;
    /**
     * Instance variable which contains the Sauce Job Id.
     */
    private String sessionId;

    /**
     * The {@link WebDriver} instance which is used to perform browser interactions with.
     */
    private WebDriver driver;
    private String tunnelIdentifier; 
    //private String extendedDebugging;

    /**
     * Constructs a new instance of the test.  The constructor requires three string parameters, which represent the operating
     * system, version and browser to be used when launching a Sauce VM.  The order of the parameters should be the same
     * as that of the elements within the {@link #browsersStrings()} method.
     * @param os
     * @param version
     * @param browser
     * @param deviceName
     * @param deviceOrientation
     */

    //public SampleSauceTest(String os, String version, String browser, String tunnelIdentifier) {
    public SampleSauceTest(String os, String version, String browser) {

        super();
        this.os = os;
        this.version = version;
        this.browser = browser;
        this.deviceName = deviceName;

    } 

    /**
     * @return a LinkedList containing String arrays representing the browser combinations the test should be run against. The values
     * in the String array are used as part of the invocation of the test constructor
     */
    @ConcurrentParameterized.Parameters
    public static LinkedList browsersStrings() {
        LinkedList browsers = new LinkedList();
        browsers.add(new String[]{"Windows 10", "latest", "internet explorer"});
        browsers.add(new String[]{"Windows 8.1", "latest-1", "internet explorer"});
        browsers.add(new String[]{"Windows 7", "10", "internet explorer", });   
        browsers.add(new String[]{"Windows XP", "latest-1", "chrome"});   
        browsers.add(new String[]{"OSX 10.12", "11", "Safari"});      
        browsers.add(new String[]{"OSX 10.9", "32", "firefox"});
        browsers.add(new String[]{"OSX 10.11", "43", "Chrome"});
        browsers.add(new String[]{"Linux", "5.1", "Android"}); 
        browsers.add(new String[]{"OSX 10.10", "9.3", "iPhone"}); 
        browsers.add(new String[]{"Windows 8", "10", "internet explorer"}); 
        browsers.add(new String[]{"Windows 7", "9", "internet explorer"});   
        browsers.add(new String[]{"Windows 8", "39", "chrome"});   
        browsers.add(new String[]{"Windows 8", "46", "firefox"});
        browsers.add(new String[]{"Windows 10", "55", "firefox"});
        browsers.add(new String[]{"Windows 10", "50", "Chrome"}); 
        browsers.add(new String[]{"Windows 10", "13.10586", "MicrosoftEdge"});
       //browsers.add(new String[]{"Linux", "47", "firefox"});
   
        return browsers;
    } 
      
     
    /**  
     * Constructs a new {@link RemoteWebDriver} instance which is configured to use the capabilities defined by the {@link #browser},
     * {@link #version} and {@link #os} instance variables, and which is configured to run against ondemand.saucelabs.com, using
     * the username and access key populated by the {@link #authentication} instance.
     *
     * @throws Exception if an error occurs during the creation of the {@link RemoteWebDriver} instance.
     */
    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        if (browser != null) capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
        if (version != null) capabilities.setCapability(CapabilityType.VERSION, version);
        if (deviceName != null) capabilities.setCapability("deviceName", deviceName);

        capabilities.setCapability(CapabilityType.PLATFORM, os);
        String methodName = name.getMethodName();
        capabilities.setCapability("name", methodName);
        //capabilities.setCapability("extendedDebugging", "true");
        //capabilities.setCapability("prerun", "https://s3-us-west-1.amazonaws.com/kristianmeier/intuit_test.bat");
        //capabilities.setCapability("prerun", "https://s3-us-west-1.amazonaws.com/kristianmeier/netflix.bat");
        //capabilities.setCapability("tunnelIdentifier", "kristian-tunnel");
        //capabilities.setCapability("seleniumVersion", "3.5.0");
        //capabilities.setCapability("parentTunnel", "kristianmeiersl")
        //capabilities.setCapability("app", "sauce-storage:myapp.apk")
        //capabilities.setCapability("app", "https://internal.ebay.com/apprepo/myapp.apprepok")

        this.driver = new RemoteWebDriver(
                        new URL("https://" + authentication.getUsername() + ":" + authentication.getAccessKey() +
                          "@ondemand.saucelabs.com:443/wd/hub"),
                    //new URL("https://" + authentication.getUsername() + ":" + authentication.getAccessKey() +
                      //"@localhost:4445/wd/hub"),
                capabilities);
        this.sessionId = (((RemoteWebDriver) driver).getSessionId()).toString();

        String message = String.format("SauceOnDemandSessionID=%1$s job-name=%2$s", this.sessionId, methodName);
        System.out.println(message);    
    } 
      /**
     * Runs a simple test verifying the title of the home page.
     * @throws Exception
     */ 
      
    
    @Test
    public void verifyTitleTest() throws Exception {
    driver.get("http://www.saucelabs.com/");
    assertEquals("Cross Browser Testing, Selenium Testing, and Mobile Testing | Sauce Labs", driver.getTitle());
    } 
         
    /**
     * Runs a simple Authentication test 
     * @throws Exception
     */
    
    @Test
    public void loginTest() throws Exception {
            driver.get("https://saucelabs.com/beta/login");
            WebDriverWait wait = new WebDriverWait(driver, 60); // wait for a maximum of 60 seconds
            wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
            driver.findElement(By.name("username")).sendKeys("kmeier2");
            driver.findElement(By.name("password")).sendKeys("saucelabs");
            driver.findElement(By.id("submit")).click();
    }
      /**
     * Runs a simple test to sign up for a free trial.
     * @throws Exception
     */ 
    
    @Test
    public void freeTrialTest() throws Exception {
        driver.get("http://saucelabs.com/");
        WebDriverWait wait = new WebDriverWait(driver, 30); // wait for a maximum of 30 seconds
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='site-header']/div[3]/div/div/div[3]/div/a[3]")));
        driver.findElement(By.xpath("//*[@id='site-header']/div[3]/div/div/div[3]/div/a[3]")).click();
        Thread.sleep(5000);
        driver.findElement(By.name("first_name")).sendKeys("FirstName");
        driver.findElement(By.name("last_name")).sendKeys("LastName");
        driver.findElement(By.name("email")).sendKeys("MyEmail@company.com");
        driver.findElement(By.name("company")).sendKeys("My Company");
        driver.findElement(By.name("username")).sendKeys("myUsername");
        driver.findElement(By.name("password")).sendKeys("thatsright");
        } 

    /**
     * Closes the {@link WebDriver} session.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

    /**
     *
     * @return the value of the Sauce Job id.
     */
    @Override
    public String getSessionId() {
        return sessionId;
    }
}