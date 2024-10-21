package com.teamcity.api.requests.unchecked;

import com.teamcity.api.models.BaseModel;
import com.teamcity.api.requests.CrudInterface;
import com.teamcity.api.requests.Request;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public final class UncheckedServerAuthSettings extends Request implements CrudInterface {

    private static final String SERVER_AUTH_SETTINGS_URL = "/app/rest/server/authSettings";

    public UncheckedServerAuthSettings(RequestSpecification spec) {
        super(spec, null);
    }

    @Override
    public Object create(BaseModel model) {
        return null;
    }

    @Override
    @Step("Read ServerAuthSettings")
    public Response read(String id) {
        return RestAssured.given()
                .spec(spec)
                .get(SERVER_AUTH_SETTINGS_URL);
    }

    @Override
    @Step("Update ServerAuthSettings")
    public Response update(String id, BaseModel model) {
        return RestAssured.given()
                .spec(spec)
                .body(model)
                .put(SERVER_AUTH_SETTINGS_URL);
    }

    @Override
    public Object delete(String id) {
        return null;
    }

}
