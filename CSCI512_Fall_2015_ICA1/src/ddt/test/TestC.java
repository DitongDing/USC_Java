package ddt.test;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class TestC implements TestCase{
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://localhost:8080";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void test() throws Exception {
    driver.get(baseUrl + "/bookstore/Default.jsp");
    // Test illegal URL
    driver.get(baseUrl + "/bookstore/Login.jsp?FormAction=x&FormName=Login");
    // Open Login Page and test empty filed
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Test empty password
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("D");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Test empty username
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("D");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Test error username/password
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("D");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("D");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Test long input
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("DDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDT");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("DDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDTDDT");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Test login as guest
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("guest");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("guest");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Test login as admin
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("admin");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("admin");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Open Login Page and test logout
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Test login as ADMIN/ADMIN
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("ADMIN");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("ADMIN");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Logout and go back to Default.jsp
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//td[2]/a/font/img")).click();
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
