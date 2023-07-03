
CREATE TABLE users
(
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL
);




CREATE TABLE roles
(
    id BIGSERIAL PRIMARY KEY,
    role VARCHAR(50) NOT NULL UNIQUE
);



CREATE TABLE users_roles
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    UNIQUE (user_id, role_id)
);



INSERT INTO public.users(id, username, password, first_name, last_name)
VALUES (1, 'Artur', '1234', 'Artur', 'Malashkov');
INSERT INTO public.users(id, username, password, first_name, last_name)
VALUES (2, 'Irina', '1234', 'Irina', 'Malashkova');
INSERT INTO public.users(id, username, password, first_name, last_name)
VALUES (3, 'Max', '1234', 'Max', 'Malashkov');
INSERT INTO public.users(id, username, password, first_name, last_name)
VALUES (4, 'Sergei', '1234', 'Sergei', 'Soldatkin');
INSERT INTO public.users(id, username, password, first_name, last_name)
VALUES (5, 'Anonimus', '1234', 'Anonimus', 'Anonim');
INSERT INTO public.users(id, username, password, first_name, last_name)
VALUES (6, 'Sauron', '1234', 'Sauron', 'Master');




INSERT INTO public.roles(id, role)
VALUES (1, 'ADMIN');
INSERT INTO public.roles(id, role)
VALUES (2, 'JOURNALIST');
INSERT INTO public.roles(id, role)
VALUES (3, 'SUBSCRIBER');




INSERT INTO public.users_roles(user_id, role_id)
VALUES (1, 1);
INSERT INTO public.users_roles(user_id, role_id)
VALUES (2, 2);
INSERT INTO public.users_roles(user_id, role_id)
VALUES (3, 2);
INSERT INTO public.users_roles(user_id, role_id)
VALUES (4, 2);
INSERT INTO public.users_roles(user_id, role_id)
VALUES (5, 3);
INSERT INTO public.users_roles(user_id, role_id)
VALUES (6, 3);