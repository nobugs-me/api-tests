package com.teamcity.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
// Необходимая аннотация для десериализации с помощью Jackson, позволяет отказаться от использования Gson
@Jacksonized
// Без этой аннотации сериализация в объект производилась бы по всем полям, которые пришли в респонсе,
// даже если такие поля не указаны в классе-модели (в таком случае, был бы exception)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthModule extends BaseModel {

    @Builder.Default
    private String name = "HTTP-Basic";

}
