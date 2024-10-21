package com.teamcity.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
// Поля с этой аннотацией не будут генерироваться автоматически, при генерации классов, их содержащих, или их родителей
// Такие поля надо сетать вручную. Например, как Steps в api.StartBuildTest.userStartsBuildTest
public @interface Optional {
}
