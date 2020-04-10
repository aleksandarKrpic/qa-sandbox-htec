package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import util.PropertiesUtil;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public class PageBase {

    @FindBy(xpath = "//button[@type='submit']")
    protected WebElement submitButtonWE;

    protected WebDriver driver;
    protected PropertiesUtil properties;

    public PageBase(WebDriver driver) throws IOException {
        this.driver = driver;
        PageFactory.initElements(this.driver, this);
        properties = new PropertiesUtil(PropertiesUtil.PROPERTIES);
    }

    public static String getRandomString() {
        Random r = new Random();
        return UUID.randomUUID().toString().
                substring(0, r.ints(1, 6, 11).findFirst().getAsInt());
    }

    public boolean isTextPresent(String element) {
        return driver.getPageSource().contains(element);
    }

}
