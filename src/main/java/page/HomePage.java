package page;

import base.PageBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;

import static org.junit.Assert.*;


public class HomePage extends PageBase {

    @FindBy(xpath = "//div[1]/div/a/div/div/h5")
    private WebElement profilePageCardWE;

    @FindBy(xpath = "//div[2]/div/a/div/div/h5")
    private WebElement useCasesCardWE;

    @FindBy(xpath = "//div[3]/div/a/div/div/h5")
    private WebElement playgroundCardWE;

    @FindBy(xpath = "//div[4]/div/a/div/div/h5")
    private WebElement reportingIssuesCardWE;


    public HomePage(WebDriver driver) throws IOException {
        super(driver);
    }

    public void verifyHomePage() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.elementToBeClickable(profilePageCardWE));
        assertEquals("4 cards should be displayed on the SendBox dashboard!", 4,
                driver.findElements(By.className("card-body")).size());
        assertNotEquals("Profile page card title should display a name!", "", profilePageCardWE.getText());
        assertEquals("Use cases card title is not correct!", "Use Cases", useCasesCardWE.getText());
        assertEquals("Playground card title is not correct!", "Playground", playgroundCardWE.getText());
        assertEquals("Reporting issues card title is not correct!", "Reporting issues",
                reportingIssuesCardWE.getText());
    }

    public UseCasesPage openUseCasesPage() throws IOException {
        useCasesCardWE.click();
        return new UseCasesPage(driver);
    }


}
