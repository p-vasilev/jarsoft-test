CREATE TABLE IF NOT EXISTS banner (
    id          BIGINT      NOT NULL PRIMARY KEY,
    name      VARCHAR(32) NOT NULL UNIQUE,
    text        TEXT        NOT NULL,
    price       NUMERIC(10) NOT NULL,
    valid       BOOL        NOT NULL
);

CREATE TABLE IF NOT EXISTS category (
    id          BIGINT      NOT NULL PRIMARY KEY,
    name        VARCHAR(64) NOT NULL,
    request_id  VARCHAR(64) NOT NULL,
    valid       BOOL        NOT NULL
);

CREATE TABLE IF NOT EXISTS banner_category (
    banner_id   BIGINT  NOT NULL,
    category_id BIGINT  NOT NULL,

    FOREIGN KEY(banner_id)   REFERENCES banner(id),
    FOREIGN KEY(category_id) REFERENCES category(id)
);

CREATE TABLE IF NOT EXISTS log (
    id              BIGINT       NOT NULL PRIMARY KEY,
    ip              INT UNSIGNED NOT NULL,
    user_agent_id   BIGINT       NOT NULL,
    request_time    TIMESTAMP    NOT NULL,
    banner_id       BIGINT,
    price           DEC(10,2),
    reason          VARCHAR(64),

    FOREIGN KEY(banner_id)     REFERENCES banner(id),
    FOREIGN KEY(user_agent_id) REFERENCES user_agent(id)
);

CREATE TABLE IF NOT EXISTS user_agent (
    id      BIGINT     NOT NULL PRIMARY KEY,
    hash    BINARY(32) NOT NULL UNIQUE,
    string  TEXT       NOT NULL
);

CREATE TABLE IF NOT EXISTS log_banner_category (
    log_id      BIGINT NOT NULL,
    category_id BIGINT NOT NULL,

    FOREIGN KEY(log_id)      REFERENCES log(id),
    FOREIGN KEY(category_id) REFERENCES category(id)
)

