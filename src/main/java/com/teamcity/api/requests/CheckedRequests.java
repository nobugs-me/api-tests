package com.teamcity.api.requests;

import com.teamcity.api.enums.Endpoint;
import com.teamcity.api.models.BaseModel;
import com.teamcity.api.requests.checked.CheckedBase;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;

import java.util.EnumMap;

@Getter
public final class CheckedRequests {

    private final EnumMap<Endpoint, CheckedBase<?>> checkedRequests = new EnumMap<>(Endpoint.class);

    public CheckedRequests(RequestSpecification spec) {
        // Создаем все виды реквестов (указанные в Endpoint) для юзера, переданного в spec
        for (var endpoint : Endpoint.values()) {
            checkedRequests.put(endpoint, new CheckedBase<>(spec, endpoint));
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseModel> CheckedBase<T> getRequest(Endpoint endpoint) {
        return (CheckedBase<T>) checkedRequests.get(endpoint);
    }

}
