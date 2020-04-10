package page;

import base.PageBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;


public class LoginPage extends PageBase {

    @FindBy(name = "email")
    private WebElement inputEmailWE;

    @FindBy(name = "password")
    private WebElement inputPasswordWE;


    public LoginPage(WebDriver driver) throws IOException {
        super(driver);
        driver.get(properties.getValue("URL"));
    }

    public HomePage login(String email, String password) throws IOException {
        WebDriverWait wait = new WebDriverWait(driver, 40);
        wait.until(ExpectedConditions.and(ExpectedConditions.elementToBeClickable(inputEmailWE),
                ExpectedConditions.elementToBeClickable(inputPasswordWE)));
        inputEmailWE.sendKeys(email);
        inputPasswordWE.sendKeys(password);
        submitButtonWE.click();
        return new HomePage(driver);
    }

}
