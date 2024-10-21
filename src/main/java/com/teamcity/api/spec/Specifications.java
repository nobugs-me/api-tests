package com.teamcity.api.spec;

import com.github.viclovsky.swagger.coverage.FileSystemOutputWriter;
import com.github.viclovsky.swagger.coverage.SwaggerCoverageRestAssured;
import com.teamcity.api.config.Config;
import com.teamcity.api.models.User;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.nio.file.Paths;
import java.util.List;

import static com.github.viclovsky.swagger.coverage.SwaggerCoverageConstants.OUTPUT_DIRECTORY;

public final class Specifications {

    private static Specifications spec;

    private Specifications() {
    }

    public static Specifications getSpec() {
        if (spec == null) {
            spec = new Specifications();
        }
        return spec;
    }

    public RequestSpecification unauthSpec() {
        return reqBuilder()
                .setBaseUri("http://" + Config.getProperty("host"))
                .build();
    }

    public RequestSpecification authSpec(User user) {
        return reqBuilder()
                .setBaseUri("http://%s:%s@%s".formatted(user.getUsername(), user.getPassword(),
                        Config.getProperty("host")))
                .build();
    }

    public RequestSpecification superUserSpec() {
        return reqBuilder()
                .setBaseUri("http://:%s@%s".formatted(Config.getProperty("superUserToken"), Config.getProperty("host")))
                .build();
    }

    public RequestSpecification mockSpec() {
        return reqBuilder()
                .setBaseUri("http://localhost:8081")
                .build();
    }

    private RequestSpecBuilder reqBuilder() {
        return new RequestSpecBuilder()
                // Фильтры для отображения реквестов и респонсов в Allure репорте и генерации Swagger Coverage репорта
                .addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured(),
                        new SwaggerCoverageRestAssured(new FileSystemOutputWriter(
                                Paths.get("target/" + OUTPUT_DIRECTORY)))))
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON);
    }

}
