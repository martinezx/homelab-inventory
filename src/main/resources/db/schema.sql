CREATE TABLE IF NOT EXISTS hi_user (
    id         uuid DEFAULT UUID() PRIMARY KEY,
    first_name varchar(60) NOT NULL,
    last_name  varchar(60) NOT NULL,
    email      varchar(90) NOT NULL UNIQUE,
    password   varchar(60) NOT NULL
);

CREATE TABLE IF NOT EXISTS hi_role (
    id      uuid DEFAULT UUID() PRIMARY KEY,
    name    varchar(256)
);

CREATE TABLE IF NOT EXISTS hi_user_role (
    user_id    uuid,
    role_id    uuid,
    CONSTRAINT  fk_user_role_user FOREIGN KEY (user_id) REFERENCES hi_user (id),
    CONSTRAINT  fk_user_role_role FOREIGN KEY (role_id) REFERENCES hi_role (id),
    PRIMARY KEY(role_id, user_id)
);

CREATE TABLE IF NOT EXISTS hi_device (
    id      uuid DEFAULT UUID() PRIMARY KEY,
    name    varchar(256) NOT NULL,
    brand   varchar(256) NOT NULL
);