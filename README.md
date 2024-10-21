# Воркшоп - Строим автоматизацию с нуля

Чтобы **запустить воркфлоу** *(.github/workflows/test.yml)* локально, используйте команду **act** -
https://github.com/nektos/act.

Для локального запуска тестов через maven, необходимо сначала добавить в *src/main/resources/config.properties*
значения следующих параметров:

```
host=<IP>:8111, где <IP> - результат вывода команды ipconfig getifaddr en0 или аналогичной
superUserToken=<TOKEN>, где <TOKEN> - Super user authentication token вашего TeamCity сервера
```

Чтобы **запустить тесты**, используйте следующую команду (если не указать группу, то запустятся и тесты с группой Setup,
которые необходимы только для настройки сервера и агента при первом запуске через воркфлоу):

```
./mvnw clean test -Dgroups=Regression
```

Чтобы **сгенерировать Allure репорт**, используйте следующую команду после завершения предыдущей:

```
./mvnw allure:report
```

Allure репорт будет находиться в *target/site/allure-maven-plugin/**index.html***.