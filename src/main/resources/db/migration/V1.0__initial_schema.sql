CREATE TABLE page
(
    id        UUID         NOT NULL PRIMARY KEY,
    title     VARCHAR(255) NOT NULL,
    url       VARCHAR(255) NOT NULL
        CONSTRAINT url_unique UNIQUE,
    tag       VARCHAR(255) NOT NULL,
    available BOOLEAN      NOT NULL,
    created   TIMESTAMP    NOT NULL,
    updated   TIMESTAMP    NOT NULL
);
