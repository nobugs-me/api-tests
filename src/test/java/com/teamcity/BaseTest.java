package com.teamcity;

import com.teamcity.api.config.Config;
import com.teamcity.api.generators.TestDataStorage;
import com.teamcity.api.models.TestData;
import com.teamcity.api.requests.CheckedRequests;
import com.teamcity.api.requests.UncheckedRequests;
import com.teamcity.api.spec.Specifications;
import com.teamcity.ui.BaseUiTest;
import io.qameta.allure.Allure;
import org.assertj.core.api.SoftAssertions;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import static com.teamcity.api.generators.TestDataGenerator.generate;
import static io.qameta.allure.util.ResultsUtils.TAG_LABEL_NAME;

public class BaseTest implements IHookable {

    protected final CheckedRequests checkedSuperUser = new CheckedRequests(Specifications.getSpec().superUserSpec());
    protected final UncheckedRequests uncheckedSuperUser = new UncheckedRequests(Specifications.getSpec().superUserSpec());
    protected TestData testData;
    protected SoftAssertions softy;

    @BeforeMethod(alwaysRun = true)
    public void generateBaseTestData() {
        // Генерируем одну testData перед каждым тестом (так как она всегда нужна), без добавления ее в какое-то хранилище
        testData = generate();
    }

    @AfterMethod(alwaysRun = true)
    public void deleteCreatedEntities() {
        TestDataStorage.getStorage().deleteCreatedEntities(uncheckedSuperUser);
    }

    // Если делать assertAll в @AfterMethod, то ничего не будет работать: тест не будет ретраиться, а все последующие тесты в его классе скипнутся.
    // Это происходит, потому что в таком случае фейлится не сам тест, а его After метод.
    // С помощью данного хука запускаем assertAll в конце каждого теста и фейлим сам тест.
    @Override
    public void run(IHookCallBack callBack, ITestResult testResult) {
        softy = new SoftAssertions();
        // Добавляем сьют и тэг для лучшей информативности и возможности фильтрации тестов в Allure репорте
        if (BaseUiTest.class.isAssignableFrom(testResult.getTestClass().getRealClass())) {
            var browser = Config.getProperty("browser");
            Allure.suite(browser);
            Allure.label(TAG_LABEL_NAME, browser);
        }
        callBack.runTestMethod(testResult);
        softy.assertAll();
    }

}
