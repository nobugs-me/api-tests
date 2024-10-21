package com.teamcity.api.requests.checked;

import com.teamcity.api.enums.Endpoint;
import com.teamcity.api.generators.TestDataStorage;
import com.teamcity.api.models.BaseModel;
import com.teamcity.api.requests.CrudInterface;
import com.teamcity.api.requests.Request;
import com.teamcity.api.requests.SearchInterface;
import com.teamcity.api.requests.unchecked.UncheckedBase;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import java.util.List;

@SuppressWarnings("unchecked")
// Реализация checked реквестов с помощью дженериков. Позволяет получать респонс с конкретным нужным типом модели
public final class CheckedBase<T extends BaseModel> extends Request implements CrudInterface, SearchInterface {

    private final UncheckedBase uncheckedBase;

    // Все реквесты, имеющие одинаковую реализацию CRUD методов, можно создать через общий конструктор
    public CheckedBase(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
        uncheckedBase = new UncheckedBase(spec, endpoint);
    }

    @Override
    public T create(BaseModel model) {
        var createdModel = (T) uncheckedBase
                .create(model)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());
        // После создания сущности ее айди добавляется в список созданных сущностей (для их удаления в конце)
        TestDataStorage.getStorage().addCreatedEntity(endpoint, createdModel);
        return createdModel;
    }

    @Override
    public T read(String id) {
        return (T) uncheckedBase
                .read(id)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());
    }

    @Override
    public T update(String id, BaseModel model) {
        return (T) uncheckedBase
                .update(id, model)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());
    }

    @Override
    public String delete(String id) {
        return uncheckedBase
                .delete(id)
                .then().assertThat().statusCode(HttpStatus.SC_NO_CONTENT)
                .extract().asString();
    }

    @Override
    public List<T> search() {
        return (List<T>) uncheckedBase
                .search()
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().jsonPath()
                .getList(StringUtils.uncapitalize(endpoint.getModelClass().getSimpleName()), endpoint.getModelClass());
    }

}
