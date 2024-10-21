package com.teamcity.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
// Поля с этой аннотацией будут параметризироваться при генерации, если параметры были переданы
// Например, как в api.BuildTypeTest.projectAdminCreatesBuildTypeForAnotherUserProjectTest
public @interface Parameterizable {
}
