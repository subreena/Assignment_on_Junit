import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class dseTableScrap {
    WebDriver driver;
    @BeforeAll
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @DisplayName("Scrap Table data and store in text file")
    @Test
    public void scrapWebTable() throws IOException {
        driver.get("https://dsebd.org/latest_share_price_scroll_by_value.php");

        WebElement Div = driver.findElement(By.cssSelector(".table-responsive.inner-scroll"));
        WebElement table = Div.findElement(By.tagName("table"));
        List<WebElement> Alltbody = table.findElements(By.tagName("tbody"));

        FileWriter writer = new FileWriter("./src/test/resources/tableData.txt");
        try {

            for (WebElement tbody : Alltbody) {
                List<WebElement> rows = tbody.findElements(By.tagName("tr"));

                for (WebElement row : rows) {
                    List<WebElement> cells = row.findElements(By.tagName("td"));
                    for (WebElement cell : cells) {
                        System.out.print(cell.getText() + " ");
                        writer.write(cell.getText() + " | ");
                    }
                    System.out.println();

                    writer.write("\n");
                }
            }
        } finally {
            writer.close();
        }       }

    @AfterAll
    public void killDriver(){
        driver.quit();
    }
}
