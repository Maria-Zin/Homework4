# Домашнее задание №4: Внедрение Hilt и Room

**ФИО:** [Зинченко Мария Сергеевна]  
**Группа:** [Б9123-09.03.03ПИКД]  

---

## API
**REST Countries API v2** (https://restcountries.com/v2/)  
- Предоставляет информацию о всех странах мира: названия, столицы, население, площадь, регионы
- Не требует API-ключа
- Бесплатный и открытый
- Пример запроса: `GET https://restcountries.com/v2/all`

---

## Технологический стек (ДЗ №4)

Реализованы следующие обязательные требования:

1.  **Hilt (Dependency Injection):**
    *   Полностью внедрена система управления зависимостями.
    *   Удалено ручное создание объектов (Service Locator/Singletons).
    *   Используются Hilt-модули (`NetworkModule`, `DatabaseModule`) для поставки Retrofit, API и DAO.
    *   Зависимости внедряются в `CountryRepository` и `CountryViewModel` через `@Inject`.

2.  **Room (Локальная БД):**
    *   Реализован сценарий **Favourites** (Избранное).
    *   Данные о выбранных странах сохраняются в локальную базу данных и остаются доступными после полного перезапуска приложения.
    *   Используется `FavouriteEntity` для хранения данных и `FavouriteDao` для операций записи/удаления.

3.  **Архитектура и UI:**
    *   **Navigation Compose:** Навигация между тремя экранами (Список, Детали, Избранное).
    *   **UI States:** Обработка состояний `Loading`, `Error` (с кнопкой Retry), `Empty` и `Success`.
    *   **Coroutines:** Сетевые и базовые запросы выполняются в `viewModelScope`.

---


## Как проверить:

1.  **Запуск:** Откройте приложение. В списке стран выберите любую страну.
2.  **Добавление:** Нажмите на иконку «☆» на карточке страны или на кнопку «Добавить в избранное» на экране деталей.
3.  **Проверка:** Перейдите на экран **Избранное** (кнопка с сердцем/звездой вверху). Убедитесь, что страна появилась там.
4.  **Перезапуск:** Полностью закройте приложение (смахните из списка задач). Запустите его снова.
5.  **Результат:** Зайдите в раздел избранного. Страна **осталась в списке**, так как данные были считаны из Room.

---


## Скриншоты
1. ![Loading screen](https://github.com/Maria-Zin/Homework4/screenshots/loading.jpg)
2. ![List of countries](https://github.com/Maria-Zin/Homework4/screenshots/list.jpg)
3. ![Country details](https://github.com/Maria-Zin/Homework4/screenshots/detail.jpg)
4. ![Favourites screen](https://github.com/Maria-Zin/Homework4/screenshots/favourites.jpg)
5. ![Error](https://github.com/Maria-Zin/Homework4/screenshots/error.jpg)
6. ![Room](https://github.com/Maria-Zin/Homework4/screenshots/room.jpg)