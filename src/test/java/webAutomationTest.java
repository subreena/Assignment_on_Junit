import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class webAutomationTest {
    WebDriver driver;
    @BeforeAll
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Verify Visiting URL correctly")
    public void visitURL() {
        driver.get("https://www.digitalunite.com/practice-webform-learners");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            WebElement acceptCookies = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("onetrust-accept-btn-handler"))
            );
            acceptCookies.click();
            System.out.println("Cookies accepted.");
        } catch (Exception e) {
            System.out.println("No cookie popup found or already accepted.");
        }

        String titleActual = driver.getTitle();
        String titleExpected = "Practice webform for learners | Digital Unite";
        Assertions.assertEquals(titleExpected, titleActual);
    }

    @Test
    @DisplayName("Verify if registration is successful")
    public void createUser() throws InterruptedException {
        driver.get("https://www.digitalunite.com/practice-webform-learners");

        // Accept cookies again
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            WebElement acceptCookies = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("onetrust-accept-btn-handler"))
            );
            acceptCookies.click();
        } catch (Exception ignored) {}

        driver.findElement(By.id("edit-name")).sendKeys("Subreena");
        driver.findElement(By.id("edit-number")).sendKeys("0123456789");

        //enter Today's date
        LocalDate today = LocalDate.now();
        String month = today.format(DateTimeFormatter.ofPattern("MM"));
        String day   = today.format(DateTimeFormatter.ofPattern("dd"));
        String year  = today.format(DateTimeFormatter.ofPattern("yyyy"));
        driver.findElement(By.id("edit-date")).sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
        driver.findElement(By.id("edit-date")).sendKeys(month);
        driver.findElement(By.id("edit-date")).sendKeys(day);
        driver.findElement(By.id("edit-date")).sendKeys(year);
        driver.findElement(By.id("edit-date")).sendKeys(Keys.ENTER);

        //email
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0,500)");

        driver.findElement(By.id("edit-email")).sendKeys("test@test.com");

        //tell us about yourself
        driver.findElement(By.id("edit-tell-us-a-bit-about-yourself-"))
                .sendKeys("I am an ICE graduate exploring Web Automation of SQA.");

        //file upload
        js.executeScript("window.scrollTo(0,500)");
        File image = new File("C:/Users/USER/IdeaProjects/junit_assignment/src/test/resources/image.jpg");
        driver.findElement(By.id("edit-uploadocument-upload")).sendKeys(image.getAbsolutePath());


        // Scroll and click checkbox
        WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edit-age")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", checkbox);
        Thread.sleep(500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
        Assertions.assertTrue(checkbox.isSelected(), "Checkbox was not selected!");

        //submit
       driver.findElement(By.id("edit-submit")).click();

        // Wait for the confirmation message to appear
        WebDriverWait confirmWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement confirmationMessage = confirmWait.until(
                ExpectedConditions.visibilityOfElementLocated(By.tagName("h1"))
        );

       // Assert the thank-you message
        String actualMessage = confirmationMessage.getText();
        String expectedMessage = "Thank you for your submission!";
        Assertions.assertEquals(expectedMessage, actualMessage, "Submission confirmation message mismatch!");
        System.out.println("Asserted message after submission: "+actualMessage);
    }

    @AfterAll
    public void killDriver(){
        driver.quit();
    }
}


