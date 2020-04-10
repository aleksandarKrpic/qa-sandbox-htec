package test;

import base.TestBase;
import base.PageBase;
import page.HomePage;
import page.LoginPage;
import page.UseCasesPage;
import org.testng.annotations.Test;

import java.io.IOException;

public class TestUseCases extends TestBase {

    public TestUseCases() throws IOException {
        super();
    }

    @Test
    public void testUseCasesScenario() throws IOException {
        UseCasesPage useCasesPage = this.getUseCasesPage();
        String title;
        String description;
        String expectedResult;
        String stepOne;
        String stepTwo;
        String[] newValues;
        for (int i = 0; i < 4; i++) {
            title = PageBase.getRandomString();
            description = PageBase.getRandomString();
            expectedResult = PageBase.getRandomString();
            stepOne = PageBase.getRandomString();
            stepTwo = PageBase.getRandomString();
            useCasesPage.addUseCase(title, description, expectedResult, stepOne, stepTwo);
            useCasesPage.verifyUseCase(title, description, expectedResult, stepOne, stepTwo);
            useCasesPage.exitFromUseCase();
            newValues = useCasesPage.editUseCase(title, description, expectedResult, stepOne, stepTwo);
            useCasesPage.verifyUseCase(newValues[0], newValues[1], newValues[2], newValues[3], newValues[4]);
            useCasesPage.exitFromUseCase();
        }
    }

    @Test
    public void testCreateUseCaseFormInputValidations() throws IOException {
        UseCasesPage useCasesPage = this.getUseCasesPage();
        useCasesPage.verifyUseCaseInputValidations();
    }

    private UseCasesPage getUseCasesPage() throws IOException {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = loginPage.login(properties.getValue("USER.MAIL"),
                properties.getValue("USER.PASSWORD"));
        homePage.verifyHomePage();
        UseCasesPage useCasesPage = homePage.openUseCasesPage();
        useCasesPage.verifyUseCasesPage();
        return useCasesPage;
    }
}
