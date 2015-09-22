package ddt.test;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class TestB implements TestCase {
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
    // ===Default.jsp===
    // Open Default.jsp
    driver.findElement(By.xpath("//td[2]/a/font/img")).click();
    // Change page
    driver.findElement(By.xpath("//a[2]/font")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    // Test recommend
    driver.get(baseUrl + "/bookstore/Default.jsp?FormRecommended_Sorting=1&FormRecommended_Sorted=1");
    driver.get(baseUrl + "/bookstore/Default.jsp?FormRecommended_Sorting=2&FormRecommended_Sorted=1");
    driver.get(baseUrl + "/bookstore/Default.jsp?FormRecommended_Sorting=3&FormRecommended_Sorted=1");
    driver.get(baseUrl + "/bookstore/Default.jsp?FormRecommended_Sorting=4&FormRecommended_Sorted=1");
    driver.get(baseUrl + "/bookstore/Default.jsp?FormRecommended_Sorting=5&FormRecommended_Sorted=1");
    // ===BookDetail.jsp===
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("admin");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("admin");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//td[2]/a/font/img")).click();
    driver.findElement(By.xpath("//tr[7]/td/a/font/img")).click();
    // ===Logout for next test===
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
