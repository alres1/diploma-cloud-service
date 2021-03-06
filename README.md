# Дипломная работа “Облачное хранилище”

## Описание проекта

Приложение - REST-сервис. Сервис предоставляет REST интерфейс для возможности загрузки файлов и вывода списка уже загруженных файлов пользователя.
Все запросы к сервису авторизованы. Заранее подготовленное веб-приложение (FRONT) должно подключаться к разработанному сервису без доработок, а также использовать функционал FRONT для авторизации, загрузки и вывода списка файлов пользователя.

## Описание приложения:

- Сервис предоставляет REST интерфейс для интеграции с FRONT
- Методы описанны в спецификации [yaml файле](https://github.com/alres1/diploma-cloud-service/blob/master/src/main/resources/CloudServiceSpecification.yaml):
    1. Вывод списка файлов
    2. Добавление файла
    3. Удаление файла
    4. Авторизация
- Приложение разработано с использованием Spring Boot
- Информация о пользователях сервиса (логины для авторизации) и их файлы храниться в базе данных MySQL
- Код покрыт unit тестами с использованием mockito
- Использован сборщик пакетов maven

## Запуск FRONT

1. Установить nodejs (версия не ниже 14.15.0) на компьютер следуя инструкции: https://nodejs.org/ru/download/
2. Скачать [FRONT](./netology-diplom-frontend) (JavaScript)
3. Перейти в папку FRONT приложения и все команды для запуска выполнять из нее.
4. Следуя описанию README.md FRONT проекта запустить nodejs приложение (npm install...)
5. Можно задать url для вызова своего backend сервиса:
    1. В файле `.env` FRONT (находится в корне проекта) приложения нужно изменить url до backend, например: `VUE_APP_BASE_URL=http://localhost:8081`
    2. Пересобрать и запустить FRONT снова: `npm run build`
    3. Измененный `url` сохранится для следующих запусков.
6. По-умолчанию FRONT запускается на порту 8081 и доступен по url в браузере `http://localhost:8081`

> Для запуска FRONT приложения с расширенным логированием нужно использовать команду: npm run serve

## Запуск BACKEND

1. Необходимо собрать jar архив с приложением. Для этого скачать проект, затем в терминале выполните команду: ./mvnw clean package
2. Запустить docker-compose командой: $ docker-compose up -d

## Авторизация приложения:

FRONT приложение использует header `auth-token` в котором отправляет токен (ключ-строка) для идентификации пользователя на BACKEND.
Для получения токена нужно пройти авторизацию на BACKEND и отправить на метод /login пару логин и пароль, в случае успешной проверки в ответ BACKEND должен вернуть json объект
с полем `auth-token` и значением токена. Все дальейшие запросы с FRONTEND, кроме метода /login отправляются с этим header.
Для выхода из приложения нужно вызвать метод BACKEND /logout, который удалит/деактивирует токен и последующие запросы с этим токеном будут не авторизованы и возвращать код 401.


____________
