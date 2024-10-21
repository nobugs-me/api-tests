package com.teamcity.api;

import com.teamcity.api.generators.RandomData;
import com.teamcity.api.models.Project;
import com.teamcity.api.requests.checked.CheckedBase;
import com.teamcity.api.requests.unchecked.UncheckedBase;
import com.teamcity.api.spec.Specifications;
import io.qameta.allure.Feature;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.teamcity.api.enums.Endpoint.PROJECTS;
import static com.teamcity.api.enums.Endpoint.USERS;
import static com.teamcity.api.generators.TestDataGenerator.generate;

@Feature("Project")
public class ProjectTest extends BaseApiTest {

    private static final int PROJECT_ID_CHARACTERS_LIMIT = 225;
    private CheckedBase<Project> checkedProjectRequest;
    private UncheckedBase uncheckedProjectRequest;

    @BeforeMethod(alwaysRun = true)
    public void getRequests() {
        checkedProjectRequest = new CheckedBase<>(Specifications.getSpec()
                .authSpec(testData.getUser()), PROJECTS);
        uncheckedProjectRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(testData.getUser()), PROJECTS);
    }

    @Test(description = "User should be able to create project", groups = {"Regression"})
    public void userCreatesProjectTest() {
        checkedSuperUser.getRequest(USERS).create(testData.getUser());

        var project = checkedProjectRequest.create(testData.getNewProjectDescription());

        softy.assertThat(project.getId()).as("projectId").isEqualTo(testData.getProject().getId());
    }

    @Test(description = "User should not be able to create two projects with the same id", groups = {"Regression"})
    public void userCreatesTwoProjectsWithSameIdTest() {
        checkedSuperUser.getRequest(USERS).create(testData.getUser());

        checkedProjectRequest.create(testData.getNewProjectDescription());

        var secondTestData = generate();
        secondTestData.getNewProjectDescription().setId(testData.getNewProjectDescription().getId());

        uncheckedProjectRequest.create(secondTestData.getNewProjectDescription())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "User should not be able to create project with id exceeding the limit", groups = {"Regression"})
    public void userCreatesProjectWithIdExceedingLimitTest() {
        checkedSuperUser.getRequest(USERS).create(testData.getUser());

        testData.getNewProjectDescription().setId(RandomData.getString(PROJECT_ID_CHARACTERS_LIMIT + 1));

        uncheckedProjectRequest.create(testData.getNewProjectDescription())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);

        testData.getNewProjectDescription().setId(RandomData.getString(PROJECT_ID_CHARACTERS_LIMIT));

        checkedProjectRequest.create(testData.getNewProjectDescription());
    }

    @Test(description = "Unauthorized user should not be able to create project", groups = {"Regression"})
    public void unauthorizedUserCreatesProjectTest() {
        var uncheckedUnauthProjectRequest = new UncheckedBase(Specifications.getSpec()
                .unauthSpec(), PROJECTS);
        uncheckedUnauthProjectRequest.create(testData.getNewProjectDescription())
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);

        uncheckedSuperUser.getRequest(PROJECTS)
                .read(testData.getProject().getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("Could not find the entity requested"));
    }

    @Test(description = "User should be able to delete project", groups = {"Regression"})
    public void userDeletesProjectTest() {
        checkedSuperUser.getRequest(USERS).create(testData.getUser());

        checkedProjectRequest.create(testData.getNewProjectDescription());
        checkedProjectRequest.delete(testData.getProject().getId());

        uncheckedProjectRequest.read(testData.getProject().getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("Could not find the entity requested"));
    }

}
