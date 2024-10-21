package com.teamcity.api.enums;

public enum UserRole {

    // Убираем параметр с указанием "SYSTEM_ADMIN" и т.д., так как тот же результат можно получить методом .toString()
    SYSTEM_ADMIN,
    PROJECT_ADMIN,
    PROJECT_DEVELOPER,
    PROJECT_VIEWER,
    AGENT_MANAGER

}
