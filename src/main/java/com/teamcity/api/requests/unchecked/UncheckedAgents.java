package com.teamcity.api.requests.unchecked;

import com.teamcity.api.models.BaseModel;
import com.teamcity.api.requests.CrudInterface;
import com.teamcity.api.requests.Request;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public final class UncheckedAgents extends Request implements CrudInterface {

    private static final String AGENTS_URL = "/app/rest/agents";

    public UncheckedAgents(RequestSpecification spec) {
        super(spec, null);
    }

    @Override
    public Object create(BaseModel model) {
        return null;
    }

    @Override
    @Step("Read Agents")
    public Response read(String locator) {
        return RestAssured.given()
                .spec(spec)
                .get(AGENTS_URL + "?locator=" + locator);
    }

    @Override
    @Step("Update Agent")
    public Response update(String id, BaseModel model) {
        return RestAssured.given()
                .spec(spec)
                .body(model)
                .put(AGENTS_URL + "/id:" + id);
    }

    @Override
    public Object delete(String id) {
        return null;
    }

}
