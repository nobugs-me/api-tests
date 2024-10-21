package com.teamcity.api.annotations;

import com.teamcity.api.models.BaseModel;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
// Поля с этой аннотацией будут заполняться значениями полей с такими же названиями из указанного класса
// Указанный класс должен генерироваться ранее в той же итерации генерации
public @interface Dependent {

    Class<? extends BaseModel> relatedClass();

}
