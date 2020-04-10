package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import util.PropertiesUtil;

import java.io.IOException;

public class TestBase {

    protected WebDriver driver;
    protected PropertiesUtil properties;

    public TestBase() throws IOException {
        properties = new PropertiesUtil(PropertiesUtil.PROPERTIES);
    }

    @BeforeMethod(alwaysRun = true)
    public void initializeClass() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("incognito");
        options.addArguments("chrome.switches", "--disable-extensions");
        driver = new ChromeDriver(options);
    }

    @AfterMethod(alwaysRun = true)
    public void turnOff() {
        driver.quit();
    }


}
