package com.teamcity.api.requests.checked;

import com.teamcity.api.models.Agents;
import com.teamcity.api.models.BaseModel;
import com.teamcity.api.requests.CrudInterface;
import com.teamcity.api.requests.Request;
import com.teamcity.api.requests.unchecked.UncheckedAgents;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

public final class CheckedAgents extends Request implements CrudInterface {

    public CheckedAgents(RequestSpecification spec) {
        super(spec, null);
    }

    @Override
    public Object create(BaseModel model) {
        return null;
    }

    @Override
    public Agents read(String id) {
        return new UncheckedAgents(spec)
                .read(id)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(Agents.class);
    }

    @Override
    public BaseModel update(String id, BaseModel model) {
        var operation = model.getClass().getSimpleName();
        // Превращаем переданную модель в операцию (так как данный эндпоинт поддерживает несколько видов операций)
        // Если model принадлежит классу AuthorizedInfo, то после айди допишется операция /authorizedInfo
        operation = "/" + StringUtils.uncapitalize(operation);
        return new UncheckedAgents(spec)
                .update(id + operation, model)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(model.getClass());
    }

    @Override
    public Object delete(String id) {
        return null;
    }

}
