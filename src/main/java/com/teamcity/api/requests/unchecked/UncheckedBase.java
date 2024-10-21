package com.teamcity.api.requests.unchecked;

import com.teamcity.api.enums.Endpoint;
import com.teamcity.api.models.BaseModel;
import com.teamcity.api.requests.CrudInterface;
import com.teamcity.api.requests.Request;
import com.teamcity.api.requests.SearchInterface;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public final class UncheckedBase extends Request implements CrudInterface, SearchInterface {

    public UncheckedBase(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
    }

    @Override
    @Step("Create {model}")
    public Response create(BaseModel model) {
        return RestAssured.given()
                .spec(spec)
                .body(model)
                .post(endpoint.getUrl());
    }

    @Override
    @Step("Read {id}")
    public Response read(String id) {
        return RestAssured.given()
                .spec(spec)
                .get(endpoint.getUrl() + "/id:" + id);
    }

    @Override
    @Step("Update {id}")
    public Response update(String id, BaseModel model) {
        return RestAssured.given()
                .spec(spec)
                .body(model)
                .put(endpoint.getUrl() + "/id:" + id);
    }

    @Override
    @Step("Delete {id}")
    public Response delete(String id) {
        return RestAssured.given()
                .spec(spec)
                .delete(endpoint.getUrl() + "/id:" + id);
    }

    @Override
    @Step("Search models")
    public Response search() {
        return RestAssured.given()
                .spec(spec)
                .get(endpoint.getUrl());
    }

}
