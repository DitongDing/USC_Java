package ddt.test;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class TestA implements TestCase{
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
    // Test search
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("Test");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // ===Bookdetail.jsp===
    // Click one book for unlogin check
    driver.findElement(By.xpath("//td[2]/a/font/img")).click();
    driver.findElement(By.xpath("//tr[7]/td/a/font/img")).click();
    // Login as admin
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("admin");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("admin");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Use illegal URL to access an unexisting book with FormName Detail/Rating/Order
    driver.get(baseUrl + "/bookstore/BookDetail.jsp");
    driver.get(baseUrl + "/bookstore/BookDetail.jsp?FormAction=insert&item_id=24&FormName=Rating&");
    driver.get(baseUrl + "/bookstore/BookDetail.jsp?FormAction=update&item_id=24&FormName=Rating&");
    driver.get(baseUrl + "/bookstore/BookDetail.jsp?FormAction=delete&item_id=24&FormName=Rating&PK_item_id=24&");
    driver.get(baseUrl + "/bookstore/BookDetail.jsp?FormAction=&item_id=24&FormName=Rating&");
    driver.get(baseUrl + "/bookstore/BookDetail.jsp?FormAction=insert&item_id=24&FormName=Order&");
    driver.get(baseUrl + "/bookstore/BookDetail.jsp?FormAction=update&item_id=24&FormName=Order&");
    driver.get(baseUrl + "/bookstore/BookDetail.jsp?FormAction=delete&item_id=24&FormName=Order&PK_item_id=24&");
    driver.get(baseUrl + "/bookstore/BookDetail.jsp?FormAction=&item_id=24&FormName=Order&");
    driver.get(baseUrl + "/bookstore/BookDetail.jsp?FormAction=insert&item_id=24&FormName=Detail&");
    driver.get(baseUrl + "/bookstore/BookDetail.jsp?FormAction=update&item_id=24&FormName=Detail&");
    driver.get(baseUrl + "/bookstore/BookDetail.jsp?FormAction=delete&item_id=24&FormName=Detail&PK_item_id=24&");
    driver.get(baseUrl + "/bookstore/BookDetail.jsp?FormAction=&item_id=24&FormName=Detail&");
    driver.get(baseUrl + "/bookstore/BookDetail.jsp?FormAction=&item_id=24&FormName=");
    // Use illegal URL to rate an existing book [rating/rating_count]
    driver.get(baseUrl + "/bookstore/BookDetail.jsp?FormName=Rating&FormAction=update&item_id=1&Trn_item_id=1&PK_item_id=1&rating=x&rating_count=x");
    // Use illegal URL to make an order [item_id/PK_order_id][FormAction/FormName]
    driver.get(baseUrl + "/bookstore/BookDetail.jsp?quantity=1&FormName=Order&FormAction=insert&order_id=1&item_id=<&PK_order_id=<&");
    driver.get(baseUrl + "/bookstore/BookDetail.jsp?quantity=1&FormName=Order&FormAction=&order_id=1&item_id=<&PK_order_id=<&");
    driver.get(baseUrl + "/bookstore/BookDetail.jsp?quantity=1&FormName=Order&FormAction=x&order_id=1&item_id=<&PK_order_id=<&");
    driver.get(baseUrl + "/bookstore/BookDetail.jsp?quantity=1&FormName=Detail&FormAction=x&order_id=1&item_id=<&PK_order_id=<&");
    // Go back to Default.jsp and choose another book
    driver.findElement(By.xpath("//td[2]/a/font/img")).click();
    driver.findElement(By.xpath("//td[3]/table/tbody/tr[7]/td/a/font/img")).click();
    // Test rating function
    new Select(driver.findElement(By.name("rating"))).selectByVisibleText("Regular");
    driver.findElement(By.xpath("//input[@value='Vote']")).click();
    // Test add to cart with empty quantity
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Test add to cart with quantity 'abc'
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("abc");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Test add to cart with quantity '-1'
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("-1");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Update quantity to 'abc'
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("abc");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Update quantity to '1.5
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("1.5");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Update quantity to '10000000000000000'
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("10000000000000000");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Update quantity to '2'
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("2");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Check 'cancel' function
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Cancel']")).click();
    // Check 'delete' function
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    acceptNextAlert = false;
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    // ===ShoppingCart.jsp===
    // Use illegal URL to access Shopping Cart with FormName Member
    driver.get(baseUrl + "/bookstore/ShoppingCart.jsp?FormName=Member&FormAction=insert");
    driver.get(baseUrl + "/bookstore/ShoppingCart.jsp?FormName=Member&FormAction=delete");
    driver.get(baseUrl + "/bookstore/ShoppingCart.jsp?FormName=Member&FormAction=update");
    driver.get(baseUrl + "/bookstore/ShoppingCart.jsp?FormName=Detail&FormAction=update");
    driver.get(baseUrl + "/bookstore/ShoppingCart.jsp?FormName=Member&FormAction=");
    driver.get(baseUrl + "/bookstore/ShoppingCart.jsp?FormName=&FormAction=");
    driver.get(baseUrl + "/bookstore/ShoppingCart.jsp?FormName=Member&FormAction=insert&member_id=&");
    // Use legal URL to access ShoppingCartRecord.jsp
    driver.get(baseUrl + "/bookstore/ShoppingCartRecord.jsp?FormName=ShoppingCartRecord&FormAction=update&member_id=x&PK_order_id=-1");
    driver.get(baseUrl + "/bookstore/ShoppingCartRecord.jsp?FormName=ShoppingCartRecord&FormAction=update&member_id=-1&PK_order_id=-1&quantity=-1");
    driver.get(baseUrl + "/bookstore/ShoppingCartRecord.jsp?FormName=ShoppingCartRecord&FormAction=insert&member_id=-1&PK_order_id=-1&quantity=-1");
    driver.get(baseUrl + "/bookstore/ShoppingCartRecord.jsp?FormName=ShoppingCartRecord&FormAction=insert");
    driver.get(baseUrl + "/bookstore/ShoppingCartRecord.jsp?FormName=ShoppingCartRecord&FormAction=delete");
    driver.get(baseUrl + "/bookstore/ShoppingCartRecord.jsp?FormName=ShoppingCartRecord&FormAction=");
    driver.get(baseUrl + "/bookstore/ShoppingCartRecord.jsp?FormName=x&FormAction=");
    driver.get(baseUrl + "/bookstore/ShoppingCartRecord.jsp?FormName=x&FormAction=x&order_id=x");
    // Logout and go back to Default.jsp
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//td[2]/a/font/img")).click();
    // ===All Pages Login Test===
    // [IMPORTANT] there will be one branch that cannot be check unless we are going to use another session. As the logout function just set "userID" and "userRights" in session to "" rather than an null value.
    // BookDetail.jsp
    driver.get(baseUrl + "/bookstore/BookDetail.jsp");
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("guest");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("guest");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // ShoppingCartRecord.jsp
    driver.get(baseUrl + "/bookstore/ShoppingCartRecord.jsp");
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("guest");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("guest");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // ShoppingCart.jsp
    driver.get(baseUrl + "/bookstore/ShoppingCart.jsp");
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("guest");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("guest");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // AdminBooks.jsp
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("guest");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("guest");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("admin");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("admin");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // AdminMenu.jsp
    driver.get(baseUrl + "/bookstore/AdminMenu.jsp");
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("guest");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("guest");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("admin");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("admin");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // CardTypesGrid.jsp
    driver.get(baseUrl + "/bookstore/CardTypesGrid.jsp");
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("guest");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("guest");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.get(baseUrl + "/bookstore/CardTypesGrid.jsp");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("admin");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("admin");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // CardTypesRecord.jsp
    driver.get(baseUrl + "/bookstore/CardTypesRecord.jsp");
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("guest");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("guest");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("admin");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("admin");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // CategoriesGrid.jsp
    driver.get(baseUrl + "/bookstore/CategoriesGrid.jsp");
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("guest");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("guest");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("admin");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("admin");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // CategoriesRecord.jsp
    driver.get(baseUrl + "/bookstore/CategoriesRecord.jsp");
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("guest");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("guest");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("admin");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("admin");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // EditorialCatGrid.jsp
    driver.get(baseUrl + "/bookstore/EditorialCatGrid.jsp");
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("guest");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("guest");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("admin");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("admin");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // EditorialCatRecord.jsp
    driver.get(baseUrl + "/bookstore/EditorialCatRecord.jsp");
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("guest");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("guest");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("admin");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("admin");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // EditorialsGrid.jsp
    driver.get(baseUrl + "/bookstore/EditorialsGrid.jsp");
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("guest");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("guest");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("admin");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("admin");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // EditorialsRecord.jsp
    driver.get(baseUrl + "/bookstore/EditorialsRecord.jsp");
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("guest");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("guest");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("admin");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("admin");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // MembersGrid.jsp
    driver.get(baseUrl + "/bookstore/MembersGrid.jsp");
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("guest");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("guest");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("admin");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("admin");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // MembersInfo.jsp
    driver.get(baseUrl + "/bookstore/MembersInfo.jsp");
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("guest");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("guest");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("admin");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("admin");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // MembersRecord.jsp
    driver.get(baseUrl + "/bookstore/MembersRecord.jsp");
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("guest");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("guest");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("admin");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("admin");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // OrdersGrid.jsp
    driver.get(baseUrl + "/bookstore/OrdersGrid.jsp");
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("guest");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("guest");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("admin");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("admin");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // OrdersRecord.jsp
    driver.get(baseUrl + "/bookstore/OrdersRecord.jsp");
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("guest");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("guest");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("admin");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("admin");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // ===Registration.jsp===
    // Open Registration.jsp
    driver.findElement(By.xpath("//td[3]/a/font/img")).click();
    driver.findElement(By.xpath("//input[@value='Cancel']")).click();
    driver.findElement(By.xpath("//td[3]/a/font/img")).click();
    // Test empty input in registration
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Test register as an existing user [with error confirm password, error email, error phone, error card number]
    driver.findElement(By.name("member_login")).clear();
    driver.findElement(By.name("member_login")).sendKeys("admin");
    driver.findElement(By.name("member_password")).clear();
    driver.findElement(By.name("member_password")).sendKeys("admin");
    driver.findElement(By.name("member_password2")).clear();
    driver.findElement(By.name("member_password2")).sendKeys("admin1");
    driver.findElement(By.name("first_name")).clear();
    driver.findElement(By.name("first_name")).sendKeys("D");
    driver.findElement(By.name("last_name")).clear();
    driver.findElement(By.name("last_name")).sendKeys("D");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("ddtxxx");
    driver.findElement(By.name("address")).clear();
    driver.findElement(By.name("address")).sendKeys("1000000000 West 30th Street, Los Angeles, CA 90007");
    driver.findElement(By.name("phone")).clear();
    driver.findElement(By.name("phone")).sendKeys("abc-def-ghijk");
    new Select(driver.findElement(By.name("card_type_id"))).selectByVisibleText("American Express");
    driver.findElement(By.name("card_number")).clear();
    driver.findElement(By.name("card_number")).sendKeys("lmn-opq-rst");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Test correctly register an account
    driver.findElement(By.name("member_login")).clear();
    driver.findElement(By.name("member_login")).sendKeys("DDT");
    driver.findElement(By.name("member_password")).clear();
    driver.findElement(By.name("member_password")).sendKeys("DDT");
    driver.findElement(By.name("member_password2")).clear();
    driver.findElement(By.name("member_password2")).sendKeys("DDT");
    driver.findElement(By.name("first_name")).clear();
    driver.findElement(By.name("first_name")).sendKeys("DDT");
    driver.findElement(By.name("last_name")).clear();
    driver.findElement(By.name("last_name")).sendKeys("DDT");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("ditongdi@usc.edu");
    driver.findElement(By.name("address")).clear();
    driver.findElement(By.name("address")).sendKeys("Los Angeles, CA 90007");
    driver.findElement(By.name("phone")).clear();
    driver.findElement(By.name("phone")).sendKeys("1234567890");
    new Select(driver.findElement(By.name("card_type_id"))).selectByVisibleText("American Express");
    driver.findElement(By.name("card_number")).clear();
    driver.findElement(By.name("card_number")).sendKeys("0000000000000000");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Use illegal URL to register [PK_member_id]
    driver.get(baseUrl + "/bookstore/Registration.jsp?FormName=Reg&FormAction=delete");
    driver.get(baseUrl + "/bookstore/Registration.jsp?FormName=Reg&FormAction=update&card_type_id=x&member_login=x");
    driver.get(baseUrl + "/bookstore/Registration.jsp?FormName=Reg&FormAction=");
    driver.get(baseUrl + "/bookstore/Registration.jsp?member_login=&member_password=&member_password2=&first_name=&last_name=&email=&address=&phone=&card_type_id=&card_number=&FormName=Reg&FormAction=update&member_id=&PK_member_id=1");
    driver.get(baseUrl + "/bookstore/Registration.jsp?member_login=&member_password=&member_password2=&first_name=&last_name=&email=&address=&phone=&card_type_id=&card_number=&FormName=Regi&FormAction=update&member_id=1&PK_member_id=1");
    driver.get(baseUrl + "/bookstore/Registration.jsp?member_login=&member_password=&member_password2=&first_name=&last_name=&email=&address=&phone=&card_type_id=&card_number=&FormName=Reg&FormAction=&member_id=1&PK_member_id=1");
    // ===AdminBooks.jsp===
    // Login as admin and go toAdminBooks.jsp
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("admin");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("admin");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Use illegal URL to search
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp?category_id=x&is_recommended=x");
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp?category_id=x&is_recommended=-1");
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp?category_id=-1&is_recommended=x");
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp?category_id=-1&is_recommended=-1");
    // Sort by title(x2)/author/price/category/recommended
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.xpath("//tr[2]/td[2]/a/font")).click();
    driver.findElement(By.xpath("//tr[2]/td[2]/a/font")).click();
    driver.findElement(By.xpath("//tr[2]/td[3]/a/font")).click();
    driver.findElement(By.xpath("//tr[2]/td[4]/a/font")).click();
    driver.findElement(By.xpath("//tr[2]/td[5]/a/font")).click();
    driver.findElement(By.xpath("//tr[2]/td[6]/a/font")).click();
    // Change page
    driver.findElement(By.xpath("//a[3]/font")).click();
    driver.findElement(By.xpath("//a[2]/font")).click();
    // Test search
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    new Select(driver.findElement(By.name("category_id"))).selectByVisibleText("Databases");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    new Select(driver.findElement(By.name("is_recommended"))).selectByVisibleText("No");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    // ===BookMaint.jsp===
    // Insert empty new book
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Insert error new book [with error price]
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("Hello");
    driver.findElement(By.name("author")).clear();
    driver.findElement(By.name("author")).sendKeys("Hello");
    driver.findElement(By.name("price")).clear();
    driver.findElement(By.name("price")).sendKeys("abc");
    driver.findElement(By.name("product_url")).clear();
    driver.findElement(By.name("product_url")).sendKeys("def");
    driver.findElement(By.name("image_url")).clear();
    driver.findElement(By.name("image_url")).sendKeys("ghi");
    driver.findElement(By.name("notes")).clear();
    driver.findElement(By.name("notes")).sendKeys("xcxxxxxx");
    driver.findElement(By.name("is_recommended")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Insert error new book [price=-1]
    driver.findElement(By.name("price")).clear();
    driver.findElement(By.name("price")).sendKeys("-1");
    driver.findElement(By.name("is_recommended")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Update new book price [abc/9999999999999/1.123456789/120.63]
    driver.findElement(By.xpath("//tr[8]/td/a/font")).click();
    driver.findElement(By.name("price")).clear();
    driver.findElement(By.name("price")).sendKeys("abc");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.name("price")).clear();
    driver.findElement(By.name("price")).sendKeys("9999999999");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//tr[8]/td/a/font")).click();
    driver.findElement(By.name("price")).clear();
    driver.findElement(By.name("price")).sendKeys("1.12345678");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//tr[8]/td/a/font")).click();
    driver.findElement(By.name("price")).clear();
    driver.findElement(By.name("price")).sendKeys("120.63");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Edit new book then cancel
    driver.findElement(By.xpath("//tr[8]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Cancel']")).click();
    // Delete new book [no/yes]
    driver.findElement(By.xpath("//tr[8]/td/a/font")).click();
    acceptNextAlert = false;
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    // Logout for testing AdminMenu.jsp
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // ===AdminMenu.jsp===
    // Open Adminstration.jsp with admin
    driver.findElement(By.xpath("//td[6]/a/font/img")).click();
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("admin");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("admin");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Try to open all links
    driver.findElement(By.xpath("//tr[2]/td/a/font")).click();
    driver.findElement(By.xpath("//td[6]/a/font/img")).click();
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//td[6]/a/font/img")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//td[6]/a/font/img")).click();
    driver.findElement(By.xpath("//tr[5]/td/a/font")).click();
    driver.findElement(By.xpath("//td[6]/a/font/img")).click();
    driver.findElement(By.xpath("//tr[6]/td/a/font")).click();
    driver.findElement(By.xpath("//td[6]/a/font/img")).click();
    driver.findElement(By.xpath("//tr[7]/td/a/font")).click();
    driver.findElement(By.xpath("//td[6]/a/font/img")).click();
    driver.findElement(By.xpath("//tr[8]/td/a/font")).click();
    driver.findElement(By.xpath("//td[6]/a/font/img")).click();
    // Use illegal URL to access AdminMenu.jsp
    driver.get(baseUrl + "/bookstore/AdminMenu.jsp?FormAction=x&FormName=");
    // Logout and go back to Default.jsp for testing AdvSearch.jsp
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // ===AdvSearch.jsp===
    // Open AdvSearch.jsp
    driver.findElement(By.xpath("//td[2]/a/font/img")).click();
    driver.findElement(By.xpath("//tr[2]/td/a/font")).click();
    // Test empty input
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Test illegal price
    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/a/font")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("Test");
    driver.findElement(By.name("author")).clear();
    driver.findElement(By.name("author")).sendKeys("Test");
    driver.findElement(By.name("pricemin")).clear();
    driver.findElement(By.name("pricemin")).sendKeys("Test");
    driver.findElement(By.name("pricemax")).clear();
    driver.findElement(By.name("pricemax")).sendKeys("Test");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Test swaped min/max search
    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/a/font")).click();
    driver.findElement(By.name("pricemin")).clear();
    driver.findElement(By.name("pricemin")).sendKeys("100");
    driver.findElement(By.name("pricemax")).clear();
    driver.findElement(By.name("pricemax")).sendKeys("1");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Use illegal URL to access AdvSearch.jsp
    driver.get(baseUrl + "/bookstore/AdvSearch.jsp?name=");
    driver.get(baseUrl + "/bookstore/AdvSearch.jsp?name=x");
    // ===Books.jsp===
    // Go to Books.jsp and test change page
    driver.get(baseUrl + "/bookstore/Books.jsp");
    driver.findElement(By.xpath("//a[2]/font")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    // Use illegal URL to access Books.jsp
    driver.get(baseUrl + "/bookstore/Books.jsp?category_id=x");
    // Use legal URL to access Book.jsp
    driver.get(baseUrl + "/bookstore/Books.jsp?category_id=1");
    driver.get(baseUrl + "/bookstore/Books.jsp?category_id=1&author=test");
    driver.get(baseUrl + "/bookstore/Books.jsp?name=test");
    driver.get(baseUrl + "/bookstore/Books.jsp?name=test&pricemax=1");
    driver.get(baseUrl + "/bookstore/Books.jsp?pricemin=1");
    driver.get(baseUrl + "/bookstore/Books.jsp?FormResults_Sorting=1");
    driver.get(baseUrl + "/bookstore/Books.jsp?FormResults_Sorting=1&FormResults_Sorted=1");
    driver.get(baseUrl + "/bookstore/Books.jsp?FormResults_Sorting=2");
    driver.get(baseUrl + "/bookstore/Books.jsp?FormResults_Sorting=3");
    driver.get(baseUrl + "/bookstore/Books.jsp?FormResults_Sorting=4");
    driver.get(baseUrl + "/bookstore/Books.jsp?FormResults_Sorting=5");
    driver.get(baseUrl + "/bookstore/Books.jsp?FormResults_Sorting=6");
    // ===CardTypesGrid.jsp===
    // Open CardTypesGrid.jsp as admin
    driver.get(baseUrl + "/bookstore/CardTypesGrid.jsp");
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("admin");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("admin");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Sort by name(x2)
    driver.findElement(By.xpath("//tr[2]/td/a/font")).click();
    driver.findElement(By.xpath("//tr[2]/td/a/font")).click();
    // Use illegal URL to access CardTypesGrid.jsp
    driver.get(baseUrl + "/bookstore/CardTypesGrid.jsp?FormCardTypes_Sorting=2&FormCardTypes_Sorted=0&");
    // ===CardTypesRecord.jsp===
    // Insert error new card type [empty]
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Insert correct new card type
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("JCB");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("JCB");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Edit/Cancel Edit card type
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("JCBJCBJCBJCBJCBJCBJCBJCBJCBJCBJCBJCBJCBJCBJCBJCBJC");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Cancel']")).click();
    // Delete card type
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    // Use illegal URL to access CardTypeRecord.jsp
    driver.get(baseUrl + "/bookstore/CardTypesRecord.jsp?card_type_id=x");
    driver.get(baseUrl + "/bookstore/CardTypesRecord.jsp?FormName=CardTypes&FormAction=");
    driver.get(baseUrl + "/bookstore/CardTypesRecord.jsp?FormName=CardTypes&FormAction=update&PK_card_type_id=");
    driver.get(baseUrl + "/bookstore/CardTypesRecord.jsp?FormName=CardTypes&FormAction=x&PK_card_type_id=1");
    driver.get(baseUrl + "/bookstore/CardTypesRecord.jsp?FormName=x&FormAction=x&card_type_id=1");
    // ===CategoriesGrid.jsp===
    // Open CategoriesGrid.jsp
    driver.findElement(By.xpath("//td[6]/a/font/img")).click();
    driver.findElement(By.xpath("//tr[5]/td/a/font")).click();
    // Sort by name(x2)
    driver.findElement(By.xpath("//tr[2]/td/a/font")).click();
    driver.findElement(By.xpath("//tr[2]/td/a/font")).click();
    // Use illegal URL to access CategoriesGrid.jsp
    driver.get(baseUrl + "/bookstore/CategoriesGrid.jsp?FormCategories_Sorting=2&FormCategories_Sorted=0&");
    driver.get(baseUrl + "/bookstore/CategoriesGrid.jsp?FormCategories_Page=2");
    // Insert multiple record for page change [x19]
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Change page
    driver.findElement(By.xpath("//a[3]/font")).click();
    driver.findElement(By.xpath("//a[2]/font")).click();
    // ===CategoriesRecord.jsp===
    // Insert error new category [empty]
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Insert correct new category
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("Anime");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("Anime");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Edit/Cancel Edit card type
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("AnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnime");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Cancel']")).click();
    // Delete card type
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    // Use illegal URL to access CategoriesRecord.jsp
    driver.get(baseUrl + "/bookstore/CategoriesRecord.jsp?FormName=Categories&FormAction=");
    driver.get(baseUrl + "/bookstore/CategoriesRecord.jsp?FormName=&FormAction=x&category_id=x");
    driver.get(baseUrl + "/bookstore/CategoriesRecord.jsp?FormName=Categories&FormAction=update&PK_category_id=");
    driver.get(baseUrl + "/bookstore/CategoriesRecord.jsp?FormName=Categories&FormAction=x&PK_category_id=1");
    driver.get(baseUrl + "/bookstore/CategoriesRecord.jsp?FormName=x&FormAction=x&PK_category_id=1");
    // ===EditorialCatGrid.jsp===
    // Open EditorialCatGrid.jsp
    driver.findElement(By.xpath("//td[6]/a/font/img")).click();
    driver.findElement(By.xpath("//tr[7]/td/a/font")).click();
    // Sort by name(x2)
    driver.findElement(By.xpath("//tr[2]/td/a/font")).click();
    driver.findElement(By.xpath("//tr[2]/td/a/font")).click();
    // Use illegal URL to access EditorialCatGrid.jsp
    driver.get(baseUrl + "/bookstore/EditorialCatGrid.jsp?Formeditorial_categories_Sorting=2&Formeditorial_categories_Sorted=0&");
    driver.get(baseUrl + "/bookstore/EditorialCatGrid.jsp?Formeditorial_categories_Page=2");
    // Insert multiple record for page change [x17]
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("editorial_cat_name")).clear();
    driver.findElement(By.name("editorial_cat_name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("editorial_cat_name")).clear();
    driver.findElement(By.name("editorial_cat_name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("editorial_cat_name")).clear();
    driver.findElement(By.name("editorial_cat_name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("editorial_cat_name")).clear();
    driver.findElement(By.name("editorial_cat_name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("editorial_cat_name")).clear();
    driver.findElement(By.name("editorial_cat_name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("editorial_cat_name")).clear();
    driver.findElement(By.name("editorial_cat_name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("editorial_cat_name")).clear();
    driver.findElement(By.name("editorial_cat_name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("editorial_cat_name")).clear();
    driver.findElement(By.name("editorial_cat_name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("editorial_cat_name")).clear();
    driver.findElement(By.name("editorial_cat_name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("editorial_cat_name")).clear();
    driver.findElement(By.name("editorial_cat_name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("editorial_cat_name")).clear();
    driver.findElement(By.name("editorial_cat_name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("editorial_cat_name")).clear();
    driver.findElement(By.name("editorial_cat_name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("editorial_cat_name")).clear();
    driver.findElement(By.name("editorial_cat_name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("editorial_cat_name")).clear();
    driver.findElement(By.name("editorial_cat_name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("editorial_cat_name")).clear();
    driver.findElement(By.name("editorial_cat_name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("editorial_cat_name")).clear();
    driver.findElement(By.name("editorial_cat_name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("editorial_cat_name")).clear();
    driver.findElement(By.name("editorial_cat_name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Change page
    driver.findElement(By.xpath("//a[3]/font")).click();
    driver.findElement(By.xpath("//a[2]/font")).click();
    // ===EditorialCatRecord.jsp===
    // Insert error new EditorialCat [empty]
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Insert correct new EditorialCatRecord
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("editorial_cat_name")).clear();
    driver.findElement(By.name("editorial_cat_name")).sendKeys("Anime");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("editorial_cat_name")).clear();
    driver.findElement(By.name("editorial_cat_name")).sendKeys("Anime");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Edit/Cancel Edit EditorialCatRecord
    driver.findElement(By.xpath("//tr[5]/td/a/font")).click();
    driver.findElement(By.name("editorial_cat_name")).clear();
    driver.findElement(By.name("editorial_cat_name")).sendKeys("AnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnime");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//tr[5]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Cancel']")).click();
    // Delete EditorialCatRecord
    driver.findElement(By.xpath("//tr[5]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    // Use illegal URL to access EditorialCatRecord.jsp
    driver.get(baseUrl + "/bookstore/EditorialCatRecord.jsp?FormName=editorial_categories&FormAction=");
    driver.get(baseUrl + "/bookstore/EditorialCatRecord.jsp?FormName=&FormAction=x&editorial_cat_id=x");
    driver.get(baseUrl + "/bookstore/EditorialCatRecord.jsp?FormName=editorial_categories&FormAction=update&PK_editorial_cat_id=");
    driver.get(baseUrl + "/bookstore/EditorialCatRecord.jsp?FormName=editorial_categories&FormAction=x&PK_editorial_cat_id=1");
    driver.get(baseUrl + "/bookstore/EditorialCatRecord.jsp?FormName=x&FormAction=x&PK_editorial_cat_id=1");
    // ===EditorialsGrid.jsp===
    // Open EditorialsGrid.jsp
    driver.findElement(By.xpath("//td[6]/a/font/img")).click();
    driver.findElement(By.xpath("//tr[6]/td/a/font")).click();
    // Sort by title(x2)/EditorialCat/Item
    driver.findElement(By.xpath("//tr[2]/td/a/font")).click();
    driver.findElement(By.xpath("//tr[2]/td/a/font")).click();
    driver.findElement(By.xpath("//tr[2]/td[2]/a/font")).click();
    driver.findElement(By.xpath("//tr[2]/td[3]/a/font")).click();
    // Use illegal URL to access EditorialGrid.jsp
    driver.get(baseUrl + "/bookstore/EditorialsGrid.jsp?Formeditorials_Sorting=4&Formeditorials_Sorted=0&");
    driver.get(baseUrl + "/bookstore/EditorialsGrid.jsp?Formeditorials_Page=2");
    // Insert multiple record for page change [x18]
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("article_desc")).clear();
    driver.findElement(By.name("article_desc")).sendKeys("a");
    driver.findElement(By.name("article_title")).clear();
    driver.findElement(By.name("article_title")).sendKeys("a");
    new Select(driver.findElement(By.name("editorial_cat_id"))).selectByVisibleText("New");
    new Select(driver.findElement(By.name("item_id"))).selectByVisibleText("C# - Programming with the Public Beta");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("article_desc")).clear();
    driver.findElement(By.name("article_desc")).sendKeys("z");
    driver.findElement(By.name("article_title")).clear();
    driver.findElement(By.name("article_title")).sendKeys("z");
    new Select(driver.findElement(By.name("editorial_cat_id"))).selectByVisibleText("New");
    new Select(driver.findElement(By.name("item_id"))).selectByVisibleText("C# - Programming with the Public Beta");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("article_desc")).clear();
    driver.findElement(By.name("article_desc")).sendKeys("z");
    driver.findElement(By.name("article_title")).clear();
    driver.findElement(By.name("article_title")).sendKeys("z");
    new Select(driver.findElement(By.name("editorial_cat_id"))).selectByVisibleText("New");
    new Select(driver.findElement(By.name("item_id"))).selectByVisibleText("C# - Programming with the Public Beta");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("article_desc")).clear();
    driver.findElement(By.name("article_desc")).sendKeys("z");
    driver.findElement(By.name("article_title")).clear();
    driver.findElement(By.name("article_title")).sendKeys("z");
    new Select(driver.findElement(By.name("editorial_cat_id"))).selectByVisibleText("New");
    new Select(driver.findElement(By.name("item_id"))).selectByVisibleText("C# - Programming with the Public Beta");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("article_desc")).clear();
    driver.findElement(By.name("article_desc")).sendKeys("z");
    driver.findElement(By.name("article_title")).clear();
    driver.findElement(By.name("article_title")).sendKeys("z");
    new Select(driver.findElement(By.name("editorial_cat_id"))).selectByVisibleText("New");
    new Select(driver.findElement(By.name("item_id"))).selectByVisibleText("C# - Programming with the Public Beta");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("article_desc")).clear();
    driver.findElement(By.name("article_desc")).sendKeys("z");
    driver.findElement(By.name("article_title")).clear();
    driver.findElement(By.name("article_title")).sendKeys("z");
    new Select(driver.findElement(By.name("editorial_cat_id"))).selectByVisibleText("New");
    new Select(driver.findElement(By.name("item_id"))).selectByVisibleText("C# - Programming with the Public Beta");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("article_desc")).clear();
    driver.findElement(By.name("article_desc")).sendKeys("z");
    driver.findElement(By.name("article_title")).clear();
    driver.findElement(By.name("article_title")).sendKeys("z");
    new Select(driver.findElement(By.name("editorial_cat_id"))).selectByVisibleText("New");
    new Select(driver.findElement(By.name("item_id"))).selectByVisibleText("C# - Programming with the Public Beta");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("article_desc")).clear();
    driver.findElement(By.name("article_desc")).sendKeys("z");
    driver.findElement(By.name("article_title")).clear();
    driver.findElement(By.name("article_title")).sendKeys("z");
    new Select(driver.findElement(By.name("editorial_cat_id"))).selectByVisibleText("New");
    new Select(driver.findElement(By.name("item_id"))).selectByVisibleText("C# - Programming with the Public Beta");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("article_desc")).clear();
    driver.findElement(By.name("article_desc")).sendKeys("z");
    driver.findElement(By.name("article_title")).clear();
    driver.findElement(By.name("article_title")).sendKeys("z");
    new Select(driver.findElement(By.name("editorial_cat_id"))).selectByVisibleText("New");
    new Select(driver.findElement(By.name("item_id"))).selectByVisibleText("C# - Programming with the Public Beta");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("article_desc")).clear();
    driver.findElement(By.name("article_desc")).sendKeys("z");
    driver.findElement(By.name("article_title")).clear();
    driver.findElement(By.name("article_title")).sendKeys("z");
    new Select(driver.findElement(By.name("editorial_cat_id"))).selectByVisibleText("New");
    new Select(driver.findElement(By.name("item_id"))).selectByVisibleText("C# - Programming with the Public Beta");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("article_desc")).clear();
    driver.findElement(By.name("article_desc")).sendKeys("z");
    driver.findElement(By.name("article_title")).clear();
    driver.findElement(By.name("article_title")).sendKeys("z");
    new Select(driver.findElement(By.name("editorial_cat_id"))).selectByVisibleText("New");
    new Select(driver.findElement(By.name("item_id"))).selectByVisibleText("C# - Programming with the Public Beta");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("article_desc")).clear();
    driver.findElement(By.name("article_desc")).sendKeys("z");
    driver.findElement(By.name("article_title")).clear();
    driver.findElement(By.name("article_title")).sendKeys("z");
    new Select(driver.findElement(By.name("editorial_cat_id"))).selectByVisibleText("New");
    new Select(driver.findElement(By.name("item_id"))).selectByVisibleText("C# - Programming with the Public Beta");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("article_desc")).clear();
    driver.findElement(By.name("article_desc")).sendKeys("z");
    driver.findElement(By.name("article_title")).clear();
    driver.findElement(By.name("article_title")).sendKeys("z");
    new Select(driver.findElement(By.name("editorial_cat_id"))).selectByVisibleText("New");
    new Select(driver.findElement(By.name("item_id"))).selectByVisibleText("C# - Programming with the Public Beta");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("article_desc")).clear();
    driver.findElement(By.name("article_desc")).sendKeys("z");
    driver.findElement(By.name("article_title")).clear();
    driver.findElement(By.name("article_title")).sendKeys("z");
    new Select(driver.findElement(By.name("editorial_cat_id"))).selectByVisibleText("New");
    new Select(driver.findElement(By.name("item_id"))).selectByVisibleText("C# - Programming with the Public Beta");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("article_desc")).clear();
    driver.findElement(By.name("article_desc")).sendKeys("z");
    driver.findElement(By.name("article_title")).clear();
    driver.findElement(By.name("article_title")).sendKeys("z");
    new Select(driver.findElement(By.name("editorial_cat_id"))).selectByVisibleText("New");
    new Select(driver.findElement(By.name("item_id"))).selectByVisibleText("C# - Programming with the Public Beta");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("article_desc")).clear();
    driver.findElement(By.name("article_desc")).sendKeys("z");
    driver.findElement(By.name("article_title")).clear();
    driver.findElement(By.name("article_title")).sendKeys("z");
    new Select(driver.findElement(By.name("editorial_cat_id"))).selectByVisibleText("New");
    new Select(driver.findElement(By.name("item_id"))).selectByVisibleText("C# - Programming with the Public Beta");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("article_desc")).clear();
    driver.findElement(By.name("article_desc")).sendKeys("z");
    driver.findElement(By.name("article_title")).clear();
    driver.findElement(By.name("article_title")).sendKeys("z");
    new Select(driver.findElement(By.name("editorial_cat_id"))).selectByVisibleText("New");
    new Select(driver.findElement(By.name("item_id"))).selectByVisibleText("C# - Programming with the Public Beta");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("article_desc")).clear();
    driver.findElement(By.name("article_desc")).sendKeys("z");
    driver.findElement(By.name("article_title")).clear();
    driver.findElement(By.name("article_title")).sendKeys("z");
    new Select(driver.findElement(By.name("editorial_cat_id"))).selectByVisibleText("New");
    new Select(driver.findElement(By.name("item_id"))).selectByVisibleText("C# - Programming with the Public Beta");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Change page
    driver.findElement(By.xpath("//a[3]/font")).click();
    driver.findElement(By.xpath("//a[2]/font")).click();
    // ===EditorialsRecord.jsp===
    // Insert error new EditorialsRecord [empty]
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Insert correct new EditorialsRecord
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("article_desc")).clear();
    driver.findElement(By.name("article_desc")).sendKeys("Anime");
    driver.findElement(By.name("article_title")).clear();
    driver.findElement(By.name("article_title")).sendKeys("Anime");
    new Select(driver.findElement(By.name("editorial_cat_id"))).selectByVisibleText("New");
    new Select(driver.findElement(By.name("item_id"))).selectByVisibleText("C# - Programming with the Public Beta");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Edit/Cancel Edit EditorialsRecord
    driver.findElement(By.xpath("//tr[6]/td/a/font")).click();
    driver.findElement(By.name("article_desc")).clear();
    driver.findElement(By.name("article_desc")).sendKeys("AnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnime");
    driver.findElement(By.name("article_title")).clear();
    driver.findElement(By.name("article_title")).sendKeys("AnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnimeAnime");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//tr[6]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Cancel']")).click();
    // Delete card EditorialsRecord
    driver.findElement(By.xpath("//tr[6]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    // Use illegal URL to access EditorialsRecord.jsp
    driver.get(baseUrl + "/bookstore/EditorialsRecord.jsp?FormName=editorials&FormAction=update");
    driver.get(baseUrl + "/bookstore/EditorialsRecord.jsp?FormName=editorials&FormAction=insert&editorial_cat_id=x&item_id=x");
    driver.get(baseUrl + "/bookstore/EditorialsRecord.jsp?FormName=editorials&FormAction=delete");
    driver.get(baseUrl + "/bookstore/EditorialsRecord.jsp?FormName=editorials&FormAction=");
    driver.get(baseUrl + "/bookstore/EditorialsRecord.jsp?FormName=&FormAction=x&article_id=x");
    driver.get(baseUrl + "/bookstore/EditorialsRecord.jsp?FormName=editorials&FormAction=update&PK_article_id=");
    driver.get(baseUrl + "/bookstore/EditorialsRecord.jsp?FormName=editorials&FormAction=x&PK_article_id=1");
    driver.get(baseUrl + "/bookstore/EditorialsRecord.jsp?FormName=x&FormAction=x&PK_article_id=1");
    // ===MembersGrid.jsp===
    // Open MembersGrid.jsp
    driver.findElement(By.xpath("//td[6]/a/font/img")).click();
    driver.findElement(By.xpath("//tr[2]/td/a/font")).click();
    // Sort by login(x2)/FirstName/LastName/Level
    driver.findElement(By.xpath("//tr[2]/td/a/font")).click();
    driver.findElement(By.xpath("//tr[2]/td/a/font")).click();
    driver.findElement(By.xpath("//tr[2]/td[2]/a/font")).click();
    driver.findElement(By.xpath("//tr[2]/td[3]/a/font")).click();
    driver.findElement(By.xpath("//tr[2]/td[4]/a/font")).click();
    // Use illegal URL to access MembersGrid.jsp
    driver.get(baseUrl + "/bookstore/MembersGrid.jsp?FormMembers_Sorting=5&FormMembers_Sorted=0&");
    driver.get(baseUrl + "/bookstore/MembersGrid.jsp?FormMembers_Page=2");
    // Insert multiple record for page change [x19]
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("member_login")).clear();
    driver.findElement(By.name("member_login")).sendKeys("1");
    driver.findElement(By.name("member_password")).clear();
    driver.findElement(By.name("member_password")).sendKeys("a");
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.name("last_name")).clear();
    driver.findElement(By.name("last_name")).sendKeys("a");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("member_login")).clear();
    driver.findElement(By.name("member_login")).sendKeys("2");
    driver.findElement(By.name("member_password")).clear();
    driver.findElement(By.name("member_password")).sendKeys("a");
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.name("last_name")).clear();
    driver.findElement(By.name("last_name")).sendKeys("a");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("member_login")).clear();
    driver.findElement(By.name("member_login")).sendKeys("3");
    driver.findElement(By.name("member_password")).clear();
    driver.findElement(By.name("member_password")).sendKeys("a");
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.name("last_name")).clear();
    driver.findElement(By.name("last_name")).sendKeys("a");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("member_login")).clear();
    driver.findElement(By.name("member_login")).sendKeys("4");
    driver.findElement(By.name("member_password")).clear();
    driver.findElement(By.name("member_password")).sendKeys("a");
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.name("last_name")).clear();
    driver.findElement(By.name("last_name")).sendKeys("a");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("member_login")).clear();
    driver.findElement(By.name("member_login")).sendKeys("5");
    driver.findElement(By.name("member_password")).clear();
    driver.findElement(By.name("member_password")).sendKeys("a");
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.name("last_name")).clear();
    driver.findElement(By.name("last_name")).sendKeys("a");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("member_login")).clear();
    driver.findElement(By.name("member_login")).sendKeys("6");
    driver.findElement(By.name("member_password")).clear();
    driver.findElement(By.name("member_password")).sendKeys("a");
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.name("last_name")).clear();
    driver.findElement(By.name("last_name")).sendKeys("a");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("member_login")).clear();
    driver.findElement(By.name("member_login")).sendKeys("7");
    driver.findElement(By.name("member_password")).clear();
    driver.findElement(By.name("member_password")).sendKeys("a");
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.name("last_name")).clear();
    driver.findElement(By.name("last_name")).sendKeys("a");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("member_login")).clear();
    driver.findElement(By.name("member_login")).sendKeys("8");
    driver.findElement(By.name("member_password")).clear();
    driver.findElement(By.name("member_password")).sendKeys("a");
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.name("last_name")).clear();
    driver.findElement(By.name("last_name")).sendKeys("a");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("member_login")).clear();
    driver.findElement(By.name("member_login")).sendKeys("9");
    driver.findElement(By.name("member_password")).clear();
    driver.findElement(By.name("member_password")).sendKeys("a");
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.name("last_name")).clear();
    driver.findElement(By.name("last_name")).sendKeys("a");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("member_login")).clear();
    driver.findElement(By.name("member_login")).sendKeys("10");
    driver.findElement(By.name("member_password")).clear();
    driver.findElement(By.name("member_password")).sendKeys("a");
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.name("last_name")).clear();
    driver.findElement(By.name("last_name")).sendKeys("a");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("member_login")).clear();
    driver.findElement(By.name("member_login")).sendKeys("11");
    driver.findElement(By.name("member_password")).clear();
    driver.findElement(By.name("member_password")).sendKeys("a");
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.name("last_name")).clear();
    driver.findElement(By.name("last_name")).sendKeys("a");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("member_login")).clear();
    driver.findElement(By.name("member_login")).sendKeys("12");
    driver.findElement(By.name("member_password")).clear();
    driver.findElement(By.name("member_password")).sendKeys("a");
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.name("last_name")).clear();
    driver.findElement(By.name("last_name")).sendKeys("a");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("member_login")).clear();
    driver.findElement(By.name("member_login")).sendKeys("13");
    driver.findElement(By.name("member_password")).clear();
    driver.findElement(By.name("member_password")).sendKeys("a");
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.name("last_name")).clear();
    driver.findElement(By.name("last_name")).sendKeys("a");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("member_login")).clear();
    driver.findElement(By.name("member_login")).sendKeys("14");
    driver.findElement(By.name("member_password")).clear();
    driver.findElement(By.name("member_password")).sendKeys("a");
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.name("last_name")).clear();
    driver.findElement(By.name("last_name")).sendKeys("a");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("member_login")).clear();
    driver.findElement(By.name("member_login")).sendKeys("15");
    driver.findElement(By.name("member_password")).clear();
    driver.findElement(By.name("member_password")).sendKeys("a");
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.name("last_name")).clear();
    driver.findElement(By.name("last_name")).sendKeys("a");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("member_login")).clear();
    driver.findElement(By.name("member_login")).sendKeys("16");
    driver.findElement(By.name("member_password")).clear();
    driver.findElement(By.name("member_password")).sendKeys("a");
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.name("last_name")).clear();
    driver.findElement(By.name("last_name")).sendKeys("a");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("member_login")).clear();
    driver.findElement(By.name("member_login")).sendKeys("17");
    driver.findElement(By.name("member_password")).clear();
    driver.findElement(By.name("member_password")).sendKeys("a");
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.name("last_name")).clear();
    driver.findElement(By.name("last_name")).sendKeys("a");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("member_login")).clear();
    driver.findElement(By.name("member_login")).sendKeys("18");
    driver.findElement(By.name("member_password")).clear();
    driver.findElement(By.name("member_password")).sendKeys("a");
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.name("last_name")).clear();
    driver.findElement(By.name("last_name")).sendKeys("a");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("member_login")).clear();
    driver.findElement(By.name("member_login")).sendKeys("19");
    driver.findElement(By.name("member_password")).clear();
    driver.findElement(By.name("member_password")).sendKeys("a");
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.name("last_name")).clear();
    driver.findElement(By.name("last_name")).sendKeys("a");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Change page
    driver.findElement(By.xpath("//a[3]/font")).click();
    driver.findElement(By.xpath("//a[2]/font")).click();
    // Search function
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // ===MembersInfo.jsp===
    // Open a's member info
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    // Logout, then Login as 1/a and make an order, then logout and login as admin
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("1");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("a");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.get(baseUrl + "/bookstore/BookDetail.jsp?quantity=1&FormName=Order&FormAction=insert&order_id=&item_id=1&PK_order_id=");
    driver.get(baseUrl + "/bookstore/MembersGrid.jsp");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("admin");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("admin");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Open a's member info
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    // Sort with order(x2)/item/quantity
    driver.findElement(By.xpath("//table[2]/tbody/tr/td/table/tbody/tr[2]/td/a/font")).click();
    driver.findElement(By.xpath("//table[2]/tbody/tr/td/table/tbody/tr[2]/td/a/font")).click();
    driver.findElement(By.xpath("//table[2]/tbody/tr/td/table/tbody/tr[2]/td[2]/a/font")).click();
    driver.findElement(By.xpath("//tr[2]/td[3]/a/font")).click();
    // Use illegal URL to access MembersInfo.jsp
    driver.get(baseUrl + "/bookstore/MembersInfo.jsp?FormOrders_Sorting=4&FormOrders_Sorted=0&member_id=x");
    driver.get(baseUrl + "/bookstore/MembersInfo.jsp?FormName=Record&FormAction=update");
    driver.get(baseUrl + "/bookstore/MembersInfo.jsp?FormName=Record&FormAction=insert");
    driver.get(baseUrl + "/bookstore/MembersInfo.jsp?FormName=Record&FormAction=delete");
    driver.get(baseUrl + "/bookstore/MembersInfo.jsp?FormName=Record&FormAction=");
    driver.get(baseUrl + "/bookstore/MembersInfo.jsp?FormName=&FormAction=x");
    driver.get(baseUrl + "/bookstore/MembersInfo.jsp?FormName=Record&FormAction=update&PK_member_id=x");
    driver.get(baseUrl + "/bookstore/MembersInfo.jsp?FormName=x&FormAction=update&member_id=x");
    // ===MembersRecord.jsp===
    // Open MembersRecord.jsp
    driver.get(baseUrl + "/bookstore/MembersRecord.jsp");
    driver.findElement(By.xpath("//input[@value='Cancel']")).click();
    driver.get(baseUrl + "/bookstore/MembersRecord.jsp");
    // Test empty input in registration
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Test register as an existing user [with error confirm password, error email, error phone, error card number]
    driver.findElement(By.name("member_login")).clear();
    driver.findElement(By.name("member_login")).sendKeys("admin");
    driver.findElement(By.name("member_password")).clear();
    driver.findElement(By.name("member_password")).sendKeys("admin");
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("D");
    driver.findElement(By.name("last_name")).clear();
    driver.findElement(By.name("last_name")).sendKeys("D");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("ddtxxx");
    driver.findElement(By.name("address")).clear();
    driver.findElement(By.name("address")).sendKeys("1000000000 West 30th Street, Los Angeles, CA 90007");
    driver.findElement(By.name("phone")).clear();
    driver.findElement(By.name("phone")).sendKeys("abc-def-ghijk");
    driver.findElement(By.name("notes")).clear();
    driver.findElement(By.name("notes")).sendKeys("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    new Select(driver.findElement(By.name("card_type_id"))).selectByVisibleText("American Express");
    driver.findElement(By.name("card_number")).clear();
    driver.findElement(By.name("card_number")).sendKeys("lmn-opq-rst");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Use illegal URL to add member
    driver.get(baseUrl + "/bookstore/MembersRecord.jsp?FormName=Members&FormAction=delete");
    driver.get(baseUrl + "/bookstore/MembersRecord.jsp?FormName=Members&FormAction=delete&PK_member_id=-1");
    driver.get(baseUrl + "/bookstore/MembersRecord.jsp?FormName=Members&FormAction=update&PK_member_id=x&member_level=x&card_type_id=x&member_login=x");
    driver.get(baseUrl + "/bookstore/MembersRecord.jsp?FormName=Members&FormAction=update&PK_member_id=x&member_level=-1&card_type_id=-1&member_login=x&member_password=x&name=x&last_name=x&email=x");
    driver.get(baseUrl + "/bookstore/MembersRecord.jsp?FormName=Members&FormAction=x");
    driver.get(baseUrl + "/bookstore/MembersRecord.jsp?FormName=Members&FormAction=");
    driver.get(baseUrl + "/bookstore/MembersRecord.jsp?FormName=x&FormAction=x&member_id=x");
    driver.get(baseUrl + "/bookstore/MembersRecord.jsp?FormName=x&FormAction=&member_id=x");
    // ===MyInfo.jsp===
    // Open page with unlogin/1/admin
    driver.findElement(By.xpath("//td[5]/a/font/img")).click();
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.get(baseUrl + "/bookstore/MyInfo.jsp");
    driver.findElement(By.name("Login")).clear();
    driver.findElement(By.name("Login")).sendKeys("admin");
    driver.findElement(By.name("Password")).clear();
    driver.findElement(By.name("Password")).sendKeys("admin");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//input[@value='Cancel']")).click();
    driver.findElement(By.xpath("//tr[2]/td[2]/a/font")).click();
    // Edit error member information [empty input]
    driver.findElement(By.name("member_password")).clear();
    driver.findElement(By.name("member_password")).sendKeys("");
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("");
    driver.findElement(By.name("last_name")).clear();
    driver.findElement(By.name("last_name")).sendKeys("");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("");
    driver.findElement(By.name("address")).clear();
    driver.findElement(By.name("address")).sendKeys("");
    driver.findElement(By.name("phone")).clear();
    driver.findElement(By.name("phone")).sendKeys("");
    driver.findElement(By.name("notes")).clear();
    driver.findElement(By.name("notes")).sendKeys("");
    new Select(driver.findElement(By.name("card_type_id"))).selectByVisibleText("American Express");
    driver.findElement(By.name("card_number")).clear();
    driver.findElement(By.name("card_number")).sendKeys("");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Edit member information
    driver.findElement(By.name("member_password")).clear();
    driver.findElement(By.name("member_password")).sendKeys("admin");
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("D");
    driver.findElement(By.name("last_name")).clear();
    driver.findElement(By.name("last_name")).sendKeys("D");
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("ddtxxx");
    driver.findElement(By.name("address")).clear();
    driver.findElement(By.name("address")).sendKeys("1000000000 West 30th Street, Los Angeles, CA 90007");
    driver.findElement(By.name("phone")).clear();
    driver.findElement(By.name("phone")).sendKeys("abc-def-ghijk");
    driver.findElement(By.name("notes")).clear();
    driver.findElement(By.name("notes")).sendKeys("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    new Select(driver.findElement(By.name("card_type_id"))).selectByVisibleText("American Express");
    driver.findElement(By.name("card_number")).clear();
    driver.findElement(By.name("card_number")).sendKeys("lmn-opq-rst");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Use illegal URL to access MyInfo.jsp
    driver.get(baseUrl + "/bookstore/MyInfo.jsp?FormName=Form&FormAction=delete");
    driver.get(baseUrl + "/bookstore/MyInfo.jsp?FormName=Form&FormAction=delete&PK_member_id=-1");
    driver.get(baseUrl + "/bookstore/MyInfo.jsp?FormName=Form&FormAction=insert");
    driver.get(baseUrl + "/bookstore/MyInfo.jsp?FormName=Form&FormAction=update&PK_member_id=x&member_level=x&card_type_id=x&member_login=x");
    driver.get(baseUrl + "/bookstore/MyInfo.jsp?FormName=Form&FormAction=update&PK_member_id=x&member_level=-1&card_type_id=-1&member_login=x&member_password=x&name=x&last_name=x&email=x");
    driver.get(baseUrl + "/bookstore/MyInfo.jsp?FormName=Form&FormAction=x");
    driver.get(baseUrl + "/bookstore/MyInfo.jsp?FormName=Form&FormAction=");
    driver.get(baseUrl + "/bookstore/MyInfo.jsp?FormName=x&FormAction=x&member_id=x");
    driver.get(baseUrl + "/bookstore/MyInfo.jsp?FormName=x&FormAction=&member_id=x");
    // ===OrdersGrid.jsp===[TODO]
    // Open OrdersGrid.jsp
    driver.findElement(By.xpath("//td[6]/a/font/img")).click();
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    // Sort by item(x2)/member/quantity
    driver.findElement(By.xpath("//tr[2]/td[2]/a/font")).click();
    driver.findElement(By.xpath("//tr[2]/td[2]/a/font")).click();
    driver.findElement(By.xpath("//tr[2]/td[3]/a/font")).click();
    driver.findElement(By.xpath("//tr[2]/td[4]/a/font")).click();
    // Use illegal URL to access OrdersGrid.jsp
    driver.get(baseUrl + "/bookstore/OrdersGrid.jsp?FormOrders_Sorting=5&FormOrders_Sorted=0&");
    driver.get(baseUrl + "/bookstore/OrdersGrid.jsp?FormOrders_Page=2");
    // Insert multiple record for page change [x20]
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("1");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("1");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("1");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("1");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("1");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("1");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("1");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("1");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("1");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("1");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("1");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("1");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("1");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("1");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("1");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("1");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("1");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("1");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("1");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("font > a > font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("1");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Change page
    driver.findElement(By.xpath("//a[3]/font")).click();
    driver.findElement(By.xpath("//a[2]/font")).click();
    // Search
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    new Select(driver.findElement(By.name("item_id"))).selectByVisibleText("1001 Web Site Construction Tips and Tricks");
    new Select(driver.findElement(By.name("member_id"))).selectByVisibleText("1");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Use illegal URL to access OrdersGrid.jsp
    driver.get("http://localhost:8080/bookstore/OrdersGrid.jsp?item_id=x&member_id=x");
    driver.get("http://localhost:8080/bookstore/OrdersGrid.jsp?item_id=&member_id=-1");
    // ===OrdersRecord.jsp===
    // Open OrdersRecord.jsp
    driver.get(baseUrl + "/bookstore/OrdersRecord.jsp");
    driver.findElement(By.xpath("//input[@value='Cancel']")).click();
    driver.get(baseUrl + "/bookstore/OrdersRecord.jsp");
    // Test empty input in registration
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Test error input [quantity]
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("abc");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("-1");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Edit order
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("1000000000");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.name("quantity")).clear();
    driver.findElement(By.name("quantity")).sendKeys("2.5");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    // Delete order
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    // Use illegal URL to access OrdersRecord.jsp
    driver.get(baseUrl + "/bookstore/OrdersRecord.jsp?FormName=Orders&FormAction=delete");
    driver.get(baseUrl + "/bookstore/OrdersRecord.jsp?FormName=Orders&FormAction=update&PK_order_id=-1&member_id=x&item_id=x");
    driver.get(baseUrl + "/bookstore/OrdersRecord.jsp?FormName=Orders&FormAction=x");
    driver.get(baseUrl + "/bookstore/OrdersRecord.jsp?FormName=Orders&FormAction=");
    driver.get(baseUrl + "/bookstore/OrdersRecord.jsp?FormName=x&FormAction=delete&order_id=x");
    driver.get(baseUrl + "/bookstore/OrdersRecord.jsp?FormName=x&FormAction=x");
    driver.get(baseUrl + "/bookstore/OrdersRecord.jsp?FormName=x&FormAction=");
    driver.get(baseUrl + "/bookstore/OrdersRecord.jsp?FormName=&FormAction=");
    // ===For iCounter===
    // Delete all books then Books.jsp
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    driver.get(baseUrl + "/bookstore/AdminBooks.jsp");
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Delete record[\\s\\S]$"));
    driver.get(baseUrl + "/bookstore/Books.jsp");
    // Delete all card type then CardTypesGrid.jsp
    driver.get(baseUrl + "/bookstore/CardTypesGrid.jsp");
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    // Delete all 'EdiCat', 'Special' and 'categories' then Default.jsp
    driver.get(baseUrl + "/bookstore/EditorialCatGrid.jsp");
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.get(baseUrl + "/bookstore/CategoriesGrid.jsp");
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[4]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//tr[3]/td/a/font")).click();
    driver.findElement(By.xpath("//input[@value='Delete']")).click();
    driver.findElement(By.xpath("//td[2]/a/font/img")).click();
    // ===Logout and finish test===
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
