package com.teamcity.api.generators;

import com.teamcity.api.enums.Endpoint;
import com.teamcity.api.models.BaseModel;
import com.teamcity.api.requests.UncheckedRequests;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class TestDataStorage {

    private static TestDataStorage testDataStorage;
    /* Набор, хранящий список созданных сущностей, с привязкой их к эндпоинтам (несколько сущностей на 1 эндпоинт).
    При обращении ко всем элементам EnumMap, например, с помощью .forEach(),
    порядок гарантировано будет соответствовать тому, в котором элементы определены в Enum файле.
    Для класса Endpoint это всегда будет BUILD_QUEUE > BUILDS > BUILD_TYPES > USERS > PROJECTS,
    даже если добавление элементов в Map было в другом порядке. В нашем случае, это полезно для
    удаления сущностей в правильном порядке (чтобы не пытаться удалять build type после project и т.п.) */
    private final EnumMap<Endpoint, Set<String>> createdEntitiesMap;

    private TestDataStorage() {
        createdEntitiesMap = new EnumMap<>(Endpoint.class);
    }

    public static TestDataStorage getStorage() {
        if (testDataStorage == null) {
            testDataStorage = new TestDataStorage();
        }
        return testDataStorage;
    }

    /* В Map добавляется только id созданной сущности, этого достаточно для удаления
    Условие .computeIfAbsent() создает пустое множество, если данному эндпоинту еще не соответствует ни одно.
    Далее в созданное или в ранее существовавшее множество добавляется новый id */
    public void addCreatedEntity(Endpoint endpoint, String id) {
        if (id != null) {
            createdEntitiesMap.computeIfAbsent(endpoint, key -> new HashSet<>()).add(id);
        }
    }

    public void addCreatedEntity(Endpoint endpoint, BaseModel model) {
        addCreatedEntity(endpoint, getEntityId(model));
    }

    public void deleteCreatedEntities(UncheckedRequests uncheckedSuperUser) {
        createdEntitiesMap.forEach((endpoint, ids) -> ids.forEach(id ->
                uncheckedSuperUser.getRequest(endpoint).delete(id)));
        // Очистка Map необходима, так как если этого не делать и запускать более 1-ого теста, то со второго
        // будут попытки удалить уже удаленные сущности
        createdEntitiesMap.clear();
    }

    // Так как не все классы, наследующие BaseModel, имеют поле id, то получаем его с помощью рефлексии
    private String getEntityId(BaseModel model) {
        try {
            var idField = model.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            var idFieldValue = Objects.toString(idField.get(model), null);
            idField.setAccessible(false);
            return idFieldValue;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("Cannot get entity id", e);
        }
    }

}
