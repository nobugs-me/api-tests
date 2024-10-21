package com.teamcity.api.requests;

import com.teamcity.api.enums.Endpoint;
import io.restassured.specification.RequestSpecification;

public abstract class Request {

    /* Придерживаемся паттерна, что все переменные по умолчанию final,
    если реализация целенаправленно не требует обратного
    То же самое с модификаторами доступа: по умолчанию private,
    при необходимости расширяем доступ в минимально достаточной мере */
    protected final RequestSpecification spec;
    protected final Endpoint endpoint;

    public Request(RequestSpecification spec, Endpoint endpoint) {
        this.spec = spec;
        this.endpoint = endpoint;
    }

}
