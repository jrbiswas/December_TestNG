package variousConcepts;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class LoginTest {

	WebDriver driver;
	String browser = "chrome"; // usually I need to write null; but I am habving only one browser, not the
								// firefox, So keep "chrome"

	// WebElement List
	By USER_NAME_FIELD = By.xpath("//*[@id=\"username\"]");
	By PASSWORD_FIELD = By.xpath("//*[@id=\"password\"]");
	By SIGNIN_BUTTON_FIELD = By.xpath("/html/body/div/div/div/form/div[3]/button");
	By DASH_BOARD_FIELD = By.xpath("//*[@id=\"page-wrapper\"]/div[2]/div/h2");
	By CUSTOMER_MENU_LOCATOR = By.xpath("//*[@id=\"side-menu\"]/li[3]/a/span[1]");
	By ADD_CUSTOMER_MENU_LOCATOR = By.xpath("//*[@id=\"side-menu\"]/li[3]/ul/li[1]/a");
	By ADD_CONTACT_HEADER_LOCATOR = By.xpath("//*[@id=\"page-wrapper\"]/div[3]/div[1]/div/div/div/div[1]/h5");
	By FULL_NAME_LOCATOR = By.xpath("//*[@id=\"rform\"]/div[1]/div[1]/div[1]/label");
	By COMPANY_DROPDOWN_LOCATOR = By.xpath("//*[@id=\"cid\"]");
	By EMAIL_LOCATOR = By.xpath("//*[@id=\"email\"]");
	By COUNTRY_LOCATOR = By.xpath("//*[@id=\"select2-country-container\"]");

	// Test Data
	String username = "demo@techfios.com";
	String password = "abc123";
	// String url;

	@BeforeClass
	// Reading the file
	public void readConfig() {

		// there are three ways we can read a file: 1) FileReader, 2) InputStream,
		// 3)BufferReader
		try {
			InputStream input = new FileInputStream("src\\main\\java\\config\\config.properties");
			Properties prop = new Properties();
			prop.load(input);
			prop.getProperty("browser");
			String browser = prop.getProperty("browser");
			System.out.println("Browser used: " + browser);
			// url = prop.getProperty("url")

		} catch (IOException e) {
			e.getStackTrace();
		}

	}

	@BeforeMethod
	public void init() {

		if (browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver",
					"C:\\Users\\Owner\\DecemberQA2021_Selenium\\crm\\driver\\chromedriver.exe");
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver", "drivers\\geckodriver.exe");
			driver = new FirefoxDriver();
		}

		/*
		 * System.setProperty("webdriver.chrome.driver",
		 * "C:\\Users\\Owner\\DecemberQA2021_Selenium\\crm\\driver\\chromedriver.exe");
		 * driver = new ChromeDriver();
		 */

		driver.manage().deleteAllCookies();
		driver.get("https://www.techfios.com/billing/?ng=admin/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}

	@Test(priority = 1)
	public void loginTest() {

		driver.findElement(USER_NAME_FIELD).sendKeys(username);
		driver.findElement(PASSWORD_FIELD).sendKeys(password);
		driver.findElement(SIGNIN_BUTTON_FIELD).click();

		Assert.assertEquals(driver.findElement(DASH_BOARD_FIELD).getText(), "Dashboard", "Wrong page!");
	}

	/*
	 * @Test(priority = 1) public void negLoginTest() {
	 * 
	 * driver.findElement(USER_NAME_FIELD).sendKeys(username);
	 * driver.findElement(PASSWORD_FIELD).sendKeys(password);
	 * driver.findElement(SIGNIN_BUTTON_FIELD).click();
	 * 
	 * // Assert.assertEquals(driver.findElement(DASH_BOARD_FIELD).getText(), //
	 * "Dashboard", "Wrong page!");
	 * 
	 * }
	 */

	@Test(priority=2)
	public void addCustomerTest() {
		loginTest();

		driver.findElement(CUSTOMER_MENU_LOCATOR).click();

		waitForElement(driver, 10, ADD_CUSTOMER_MENU_LOCATOR);

		driver.findElement(ADD_CUSTOMER_MENU_LOCATOR).click();
		Assert.assertEquals(driver.findElement(ADD_CONTACT_HEADER_LOCATOR).getText(), "Add Contact", "Wrong page!!!");

		//driver.findElement(FULL_NAME_LOCATOR).sendKeys("December Selenium" + generateRandomNo(999));
		selectFromDropDown(COMPANY_DROPDOWN_LOCATOR, "Techfios");

		selectFromDropDown(COUNTRY_LOCATOR, "Canada");

		// Select sel = new Select(driver.findElement(COMPANY_DROPDOWN_LOCATOR));
		// sel.selectByVisibleText("Techfios"); we can create a method for other drop
		// down sections, not only for company

	}

	private int generateRandomNo(int boundaryNo) {
		Random rnd = new Random();
		int generateNo = rnd.nextInt(boundaryNo);
		return generateNo;

	}

	private void selectFromDropDown(By locator, String VisibleText) {
		Select sel = new Select(driver.findElement(locator));
		sel.selectByVisibleText(VisibleText);
	}

	// anything is repitative, we create a method and call that method whenever we
	// need it.

	private void waitForElement(WebDriver driver, int timeInSeconds, By locator) {

		WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	@AfterMethod
	public void tearDown() {

		driver.close();
		driver.quit();

	}

}
