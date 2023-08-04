import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest {

	private WebDriver driver;
	

    @BeforeClass
    public void setup() {
        // Configure ChromeDriver path
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

        // Set Chrome options to disable browser notifications
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");

        // Create a WebDriver instance
        driver = new ChromeDriver(options);
    }

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        // Specify the path to the Excel file
        String excelFilePath = "credentials.xlsx";
        Object[][] data = null;

        try {
            // Load the Excel file
            FileInputStream fis = new FileInputStream(new File(excelFilePath));
            Workbook workbook = new XSSFWorkbook(fis);
         Sheet sheet = workbook.getSheet("Sheet1");

            // Get the total number of rows
            int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();

            // Initialize the data array
            data = new Object[rowCount][2];

            // Iterate through each row in the Excel sheet
            for (int i = 0; i < rowCount; i++) {
                Row row = sheet.getRow(i+1); // Skip the header row

                // Read username and password from the current row
                Cell usernameCell = row.getCell(0);
                Cell passwordCell = row.getCell(1);

                String username = usernameCell.getStringCellValue();
                String password = passwordCell.getStringCellValue();

                // Add the data to the array
                data[i][0] = username;
                data[i][1] = password;
            }

            // Close the workbook and input stream
            workbook.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    @Test(dataProvider = "loginData")
    public void loginTest(String username, String password) throws InterruptedException {
        // Open the web application login page
        driver.get("https://www.facebook.com/");
        driver.manage().window().maximize();

        // Find the username and password input fields and enter the values
        WebElement usernameField = driver.findElement(By.id("email"));
        WebElement passwordField = driver.findElement(By.id("pass"));
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);

        // Submit the form
        WebElement loginButton = driver.findElement(By.name("login"));
        loginButton.click();

        Thread.sleep(5000);
    }
        // Wait for the login process to complete (you can add custom wait logic here)
    
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
//        wait.until(ExpectedConditions.titleContains("Log in to Facebook"));

        // Perform any further actions on the logged-in page (e.g., assertions, data extraction, etc.)

        // Logout from the application (optional)
       // driver.get("https://www.facebook.com");
//        WebElement friend = driver.findElement(By.xpath("//*[text()='Friends']"));
//   	if (friend.isDisplayed()) {
//   		System.out.println("Test Passed");
//   	}
//        System.out.println("Test Failed");
//    }
   
    @AfterClass
    public void tearDown() {
//    	 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
//         wait.until(ExpectedConditions.titleContains("Log in to Facebook"));
    	WebElement friend = driver.findElement(By.xpath("//*[text()='Friends']"));
    	 Assert.assertTrue(friend.isDisplayed());
        // Close the browser
        driver.quit();
    }
}
