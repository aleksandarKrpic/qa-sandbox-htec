package page;

import base.PageBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UseCasesPage extends PageBase {

    @FindBy(xpath = "//div[2]/div/a[2]")
    private WebElement createUseCaseButtonWE;

    @FindBy(xpath = "//div[2]/div/span")
    private WebElement pageTitleWE;

    @FindBy(className = "muted-text")
    private WebElement noCreatedDataMessageWE;

    @FindBy(xpath = "//input[@name='title']")
    private WebElement titleInputWE;

    @FindBy(xpath = "//textarea[@name='description']")
    private WebElement descriptionWE;

    @FindBy(name = "expected_result")
    private WebElement expectedResultWE;

    @FindBy(xpath = "//button[@data-testid='add_step_btn']")
    private WebElement addStepButtonWE;

    @FindBy(xpath = "//input[@name='testStepId-0']")
    private WebElement stepOneInputWE;

    @FindBy(xpath = "//input[@name='testStepId-1']")
    private WebElement stepTwoInputWE;

    private String useCasesXpath = "//div[2]/div/div/a";

    public UseCasesPage(WebDriver driver) throws IOException {
        super(driver);
    }

    public List<WebElement> getUseCases() {
        return driver.findElements(By.xpath(useCasesXpath));
    }

    public void verifyUseCasesPage() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(createUseCaseButtonWE));
        assertEquals("Create use case button text is not correct!", "CREATE USE CASE", createUseCaseButtonWE.getText());
        assertEquals("Page title is not correct!", "Use Cases", pageTitleWE.getText());
        wait.until(ExpectedConditions.or(ExpectedConditions.elementToBeClickable(By.xpath(useCasesXpath)),
                ExpectedConditions.visibilityOf(noCreatedDataMessageWE)));
        if (getUseCases().isEmpty()) {
            assertTrue("If there are no use cases, the message should be displayed!",
                    noCreatedDataMessageWE.getText().contains(properties.getValue("NO.USE.CASES.MESSAGE")));
        }
    }

    public void verifyUseCase(String title, String description, String expectedResult, String stepOne, String stepTwo) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        assertEquals("The latest use case is not displayed in the list of use cases!", title,
                getUseCases().get(0).getText().trim());
        getUseCases().get(0).click();
        wait.until(ExpectedConditions.elementToBeClickable(titleInputWE));
        assertEquals("Title is not correct!", title, titleInputWE.getAttribute("value"));
        assertEquals("Description is not correct!", description, descriptionWE.getAttribute("value"));
        assertEquals("Expected result is not correct!", expectedResult, expectedResultWE.getAttribute("value"));
        assertEquals("Step one is not correct!", stepOne, stepOneInputWE.getAttribute("value"));
        assertEquals("Step two not correct!", stepTwo, stepTwoInputWE.getAttribute("value"));
    }

    public void addUseCase(String title, String description, String expectedResult, String stepOne, String stepTwo) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(createUseCaseButtonWE));
        createUseCaseButtonWE.click();
        wait.until(ExpectedConditions.elementToBeClickable(titleInputWE));
        this.enterDefaultInputFields(title, description, expectedResult, stepOne);
        addStepButtonWE.click();
        wait.until(ExpectedConditions.elementToBeClickable(stepTwoInputWE));
        stepTwoInputWE.sendKeys(stepTwo);
        submitButtonWE.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(useCasesXpath)));
    }

    private void enterDefaultInputFields(String title, String description, String expectedResult, String stepOne) {
        titleInputWE.clear();
        titleInputWE.sendKeys(title);
        descriptionWE.clear();
        descriptionWE.sendKeys(description);
        expectedResultWE.clear();
        expectedResultWE.sendKeys(expectedResult);
        stepOneInputWE.clear();
        stepOneInputWE.sendKeys(stepOne);
    }

    public String[] editUseCase(String title, String description, String expectedResult, String stepOne,
                                String stepTwo) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        getUseCases().get(0).click();
        wait.until(ExpectedConditions.elementToBeClickable(titleInputWE));
        String newValue = properties.getValue("NEW.INPUT.VALUE");
        title = newValue.split("(?<=had)")[0] + " " + title.length() + newValue.split("had")[1];
        description = newValue.split("(?<=had)")[0] + " " + description.length() + newValue.split("had")[1];
        expectedResult = newValue.split("(?<=had)")[0] + " " + expectedResult.length() + newValue.split("had")[1];
        stepOne = newValue.split("(?<=had)")[0] + " " + stepOne.length() + newValue.split("had")[1];
        stepTwo = newValue.split("(?<=had)")[0] + " " + stepTwo.length() + newValue.split("had")[1];
        String[] newValues = {title, description, expectedResult, stepOne, stepTwo};
        this.enterDefaultInputFields(title, description, expectedResult, stepOne);
        stepTwoInputWE.clear();
        stepTwoInputWE.sendKeys(stepTwo);
        submitButtonWE.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(useCasesXpath)));
        return newValues;
    }

    public void exitFromUseCase() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.findElement(By.xpath("//div/div/form/a")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(useCasesXpath)));
    }

    public void verifyUseCaseInputValidations() {
        String errorsClassName = "invalid-feedback";
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(createUseCaseButtonWE));
        createUseCaseButtonWE.click();
        wait.until(ExpectedConditions.elementToBeClickable(titleInputWE));
        this.enterDefaultInputFields("", "", "", "");
        submitButtonWE.click();
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className(errorsClassName)));
        List<WebElement> errors = driver.findElements(By.className(errorsClassName));
        assertEquals("There should be displayed three errors for three default required fields!", 3, errors.size());
        for (int i = 0; i < errors.size(); i++) {
            assertEquals("Error message is not correct!", properties.getValue("REQUIRED.ERROR." + i),
                    errors.get(i).getText());
        }
        this.enterDefaultInputFields("test", "test", "test", "test");
        submitButtonWE.click();
        wait.until(ExpectedConditions.numberOfElementsToBe(By.className(errorsClassName), 2));
        errors = driver.findElements(By.className(errorsClassName));
        assertEquals("Error message is not correct!", "Title " + properties.getValue("LENGTH.VALIDATION.ERROR"),
                errors.get(0).getText());
        assertEquals("Error message is not correct!", "Expected results " + properties.getValue("LENGTH.VALIDATION.ERROR"),
                errors.get(1).getText());
    }

}
