create table users (
    id bigserial primary key,
    username varchar(50) not null unique,
    password varchar(80) not null,
    email varchar(50) not null unique
);

---

create table rooms (
    id bigserial primary key,
    name varchar(50) not null unique
);

---

create table events (
     id bigserial primary key,
     title varchar(100) not null,
     description text not null,
     date_from timestamp without time zone not null,
     date_to timestamp without time zone not null,
     room_id bigint not null ,
     author_id bigint not null ,

     foreign key (room_id) references rooms(id),
     foreign key (author_id) references users(id)
);

---

create index event_id_idx on events(id);

---

create table roles (
    id bigserial primary key,
    name varchar(50) not null
);

CREATE TABLE users_roles (
    user_id bigint not null,
    role_id int not null,

    primary key (user_id, role_id),

    foreign key (user_id) references users (id),
    foreign key (role_id) references roles (id)
);

---

insert into roles (name)
values
    ('ROLE_USER'), ('ROLE_ADMIN')
;

insert into rooms (name)
    values
('Большая на первом'), ('Комната с пуфиками'), ('Переговорка с проектором')
;

insert into users(username, password, email)
    values
('user', '$2a$12$KOp5TtlWiRvv2xz90F9C2Oj8SyfDIk45ZcA7rY7ChCZXr/sWbKSUe', 'user@mail.ru'),
('admin', '$2a$12$wPWdvRAuiYxa/7qXRFpHT.nvXDV.T/0nPPsngzB8O0rwo6IOGwxNS', 'admin@mail.ru')
;

insert into users_roles(user_id, role_id)
values
(1, 1), (2, 2)
;

insert into events(title, description, date_from, date_to, room_id, author_id)
values
    ('Собеседование', 'Собеседуем на джуна', '2021-10-25 11:00:00', '2021-10-25 12:00:00', 2, 1),
    ('Совещание', 'Обсуждаем задачи на следующую неделю', '2021-10-25 13:00:00', '2021-10-25 15:00:00', 3, 2),
    ('Встреча', 'Встреча с клиентом. Переговоры по будущему проекту.', '2021-10-25 15:00:00', '2021-10-25 18:00:00', 1, 1),
    ('Дискотека', 'Отмечаем выход в топ 3', '2021-10-26 20:00:00', '2021-10-26 23:00:00', 1, 1),
    ('Обсуждение новых фич', 'Обсуждаем задачи по проекту', '2021-10-26 09:30:00', '2021-10-26 12:00:00', 3, 2),
    ('Очередное совещание', 'Совещаемся, ставим цели на будущее.', '2021-10-27 15:00:00', '2021-10-27 18:00:00', 1, 1),
    ('Собрание администрации', 'Поговорим про вчерашнее событие', '2021-10-27 12:00:00', '2021-10-27 14:00:00', 2, 2),
    ('ДР Лёхи!', 'Пицца, пироги, приходите!', '2021-10-28 14:00:00', '2021-10-28 16:30:00', 2, 2)
;

