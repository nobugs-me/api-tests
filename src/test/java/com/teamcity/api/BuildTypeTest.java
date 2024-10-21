package com.teamcity.api;

import com.teamcity.api.enums.UserRole;
import com.teamcity.api.generators.RandomData;
import com.teamcity.api.models.BuildType;
import com.teamcity.api.models.Roles;
import com.teamcity.api.requests.checked.CheckedBase;
import com.teamcity.api.requests.unchecked.UncheckedBase;
import com.teamcity.api.spec.Specifications;
import io.qameta.allure.Feature;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.teamcity.api.enums.Endpoint.BUILD_TYPES;
import static com.teamcity.api.enums.Endpoint.PROJECTS;
import static com.teamcity.api.enums.Endpoint.USERS;
import static com.teamcity.api.generators.TestDataGenerator.generate;

@Feature("Build type")
public class BuildTypeTest extends BaseApiTest {

    private static final int BUILD_TYPE_ID_CHARACTERS_LIMIT = 225;
    private CheckedBase<BuildType> checkedBuildTypeRequest;
    private UncheckedBase uncheckedBuildTypeRequest;

    @BeforeMethod(alwaysRun = true)
    public void getRequests() {
        checkedBuildTypeRequest = new CheckedBase<>(Specifications.getSpec()
                .authSpec(testData.getUser()), BUILD_TYPES);
        uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(testData.getUser()), BUILD_TYPES);
    }

    @Test(description = "User should be able to create build type", groups = {"Regression"})
    public void userCreatesBuildTypeTest() {
        checkedSuperUser.getRequest(USERS).create(testData.getUser());
        checkedSuperUser.getRequest(PROJECTS).create(testData.getNewProjectDescription());

        var buildType = checkedBuildTypeRequest.create(testData.getBuildType());

        softy.assertThat(buildType.getId()).as("buildTypeId").isEqualTo(testData.getBuildType().getId());
    }

    @Test(description = "User should not be able to create two build types with the same id", groups = {"Regression"})
    public void userCreatesTwoBuildTypesWithSameIdTest() {
        checkedSuperUser.getRequest(USERS).create(testData.getUser());
        checkedSuperUser.getRequest(PROJECTS).create(testData.getNewProjectDescription());

        checkedBuildTypeRequest.create(testData.getBuildType());

        var secondTestData = generate();
        var buildTypeTestData = testData.getBuildType();
        var secondBuildTypeTestData = secondTestData.getBuildType();
        secondBuildTypeTestData.setId(buildTypeTestData.getId());
        secondBuildTypeTestData.setProject(buildTypeTestData.getProject());

        uncheckedBuildTypeRequest.create(secondTestData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "User should not be able to create build type with id exceeding the limit", groups = {"Regression"})
    public void userCreatesBuildTypeWithIdExceedingLimitTest() {
        checkedSuperUser.getRequest(USERS).create(testData.getUser());
        checkedSuperUser.getRequest(PROJECTS).create(testData.getNewProjectDescription());

        var buildTypeTestData = testData.getBuildType();
        buildTypeTestData.setId(RandomData.getString(BUILD_TYPE_ID_CHARACTERS_LIMIT + 1));

        uncheckedBuildTypeRequest.create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);

        buildTypeTestData.setId(RandomData.getString(BUILD_TYPE_ID_CHARACTERS_LIMIT));

        checkedBuildTypeRequest.create(testData.getBuildType());
    }

    @Test(description = "Unauthorized user should not be able to create build type", groups = {"Regression"})
    public void unauthorizedUserCreatesBuildTypeTest() {
        checkedSuperUser.getRequest(PROJECTS).create(testData.getNewProjectDescription());

        var uncheckedUnauthBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .unauthSpec(), BUILD_TYPES);
        uncheckedUnauthBuildTypeRequest.create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);

        uncheckedSuperUser.getRequest(BUILD_TYPES).read(testData.getBuildType().getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("Could not find the entity requested"));
    }

    @Test(description = "User should be able to delete build type", groups = {"Regression"})
    public void userDeletesBuildTypeTest() {
        checkedSuperUser.getRequest(USERS).create(testData.getUser());
        checkedSuperUser.getRequest(PROJECTS).create(testData.getNewProjectDescription());

        checkedBuildTypeRequest.create(testData.getBuildType());
        checkedBuildTypeRequest.delete(testData.getBuildType().getId());

        uncheckedBuildTypeRequest.read(testData.getBuildType().getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("Could not find the entity requested"));
    }

    @Test(description = "Project admin should be able to create build type for their project", groups = {"Regression"})
    public void projectAdminCreatesBuildTypeTest() {
        checkedSuperUser.getRequest(PROJECTS).create(testData.getNewProjectDescription());

        testData.getUser().setRoles(generate(Roles.class, UserRole.PROJECT_ADMIN, "p:" + testData.getProject().getId()));

        checkedSuperUser.getRequest(USERS).create(testData.getUser());

        var buildType = checkedBuildTypeRequest.create(testData.getBuildType());

        softy.assertThat(buildType.getId()).as("buildTypeId").isEqualTo(testData.getBuildType().getId());
    }

    @Test(description = "Project admin should not be able to create build type for not their project", groups = {"Regression"})
    public void projectAdminCreatesBuildTypeForAnotherUserProjectTest() {
        var secondTestData = generate();
        checkedSuperUser.getRequest(PROJECTS).create(testData.getNewProjectDescription());
        checkedSuperUser.getRequest(PROJECTS).create(secondTestData.getNewProjectDescription());

        testData.getUser().setRoles(generate(Roles.class, UserRole.PROJECT_ADMIN, "p:" + testData.getProject().getId()));
        secondTestData.getUser().setRoles(generate(Roles.class, UserRole.PROJECT_ADMIN, "p:" + secondTestData.getProject().getId()));

        checkedSuperUser.getRequest(USERS).create(testData.getUser());
        checkedSuperUser.getRequest(USERS).create(secondTestData.getUser());

        uncheckedBuildTypeRequest.create(secondTestData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .body(Matchers.containsString("Access denied"));
    }

}
