CREATE TABLE IF NOT EXISTS user (
    id          BIGINT          PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name        VARCHAR(64)     NOT NULL UNIQUE,
    password    VARCHAR(128)    NOT NULL,
    enabled     BOOL            NOT NULL
);
