# RoomsBooking
Руководство по запуску тестового задания «Rooms Booking»
Скачайте проект с GitHub …
Для запуска проекта установите Docker с официального сайта https://www.docker.com/ 
Настройка и запуск Docker контейнера
Откройте скаченный проект в Intelij IDEA и запустите файл /data/docker-calendar-app/docker-compose.yml 
 

У вас развернется контейнер и образ с базой данных Postgres 9.6. 
Вот ссылка для подключения к базе данных jdbc:postgresql://localhost:5432/calendar-app
Если порт 5423 у вас занят другим приложением, то просто измените порт на другой в файле docker-compose.yml 
Пароль и пользователь базы данных захардкожены, чтобы было уодобнее тестировать приложение. При необходимости вы можете изменить их в этом файле.

Настройка приложения
Приложение будет запускаться на порту 8080. Если этот порт у вас занят другим приложением то в файле необходимо изменить этот параметр  
Так же необходимо будет изменить url подключения к базе данных, если на прошлом шаге вы меняли порт docker контейнера с 5432 на какой-то другой. 
И если на прошлом шаге вы меняли пользователя или пароль для подключения к базе данных, то здесь необходимо их указать. 
Запуск приложения
К приложению подключен flyway, это значит что, при первом запуске flyway прочитает информацию хранящуюся в папке resources/db/migration , а там, при первом, запуске лежит sql скрипт который подготовит базу данных т.е. сформирует необходимую мета информацию и создаст изначальные тестовые данные, такие как пользователи, комнаты и несколько событий.  

Авторизация в сервисе
После того как локальный сервер поднялся и приложение было запущено, можно перейти на домашнюю страницу проекта по адресу:  http://localhost:8080/calendar 
Программа не покажет вам календарь забронированных комнат до тех пор пока вы не войдете под своим аккаунтом или зарегистрируетесь.
 
Процесс регистрации очень простой. 
 
Однако вам нужно указать уникальный логин и email. Пароль можно указывать какой угодно, он будет зашифрован алгоритмом BCrypt и в базе данных будет выглядеть примерно так: «$2a$12$KOp5TtlWiRvv2xz90F8C2Oj8SyfDIk45ZcA7rY7ChCZXr/sWbKSUe» 
Мы заботимся о безопасности ваших данных!
После того как мы зарегистрировались, мы можем войти в систему под своим именем.
 
Работа с приложением
Календарь на неделю
Первое что мы увидим после авторизации это большую таблицу-календарь на неделю.
Программа автоматически вычисляет дату и показывает текущую неделю.
Для навигации по неделям есть кнопки «Previous week» и «Next week» который перелистывают календарь на прошлую или следующую неделю соответственно.
 
В правом верхнем углу отображется логин текущего пользователя и кнопка «Logout» для завершения текущей сессии и выхода из приложения. 
Находится в стадии разработки «Начало дня»: скоро появится возможность настроить начало рабочего дня. Таким образом календарь будет строится не от начала суток 00:00 а от указанного вами времени. 
Бронирование
Для того чтобы забронировать переговорную комнату нажмите кнопку «BOOKING»
 
Откроется форма с информацией необходимой для успешного создания брони.  
Title – заголовок события (макс 15 символов)
Description – Описание вашего события (макс 150 символов)
Date – выберите дату и время бронирования.
Room – выберите комнату из списка доступных.
Author – Поле заполнится автоматически и оно будет доступно только для чтения, после того как вы успешно сохраните бронь. 
Ниже показано отображение забронированной комнаты в календаре.
 
Минимальный шаг времени по бртонированию – 30 минут, что равно одной ячейки. Забронированная на час комната будет занимать 2 ячейки.
Нажав на кнопку с событием в календаре вы можете просмотреть информацию в отдельном окне.
 
Если вы являетесь автором события, то вам доступны кнопки «Save changes» и «Delete». Т.е. вы можете внести изменения в событие и сохранить его с новой информацией или при необходимости удалить совсем.
 
Если вы не являетесь автором события, то эти кнопки вам не доступны 
   

На этом все, приятного бронирования!
