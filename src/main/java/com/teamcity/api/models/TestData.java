package com.teamcity.api.models;

import lombok.Data;

@Data
// Набор тестовых данных, используемых в тестах. Генерируется с помощью TestDataGenerator.generate
// Порядок определения важен и влияет на этот метод
public class TestData {

    private NewProjectDescription newProjectDescription;
    private Project project;
    private User user;
    private BuildType buildType;

}
