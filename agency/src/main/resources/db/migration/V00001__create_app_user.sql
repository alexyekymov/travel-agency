CREATE TABLE app_user
(
    id             UUID PRIMARY KEY                  DEFAULT uuidv7(),
    first_name     VARCHAR(50)              NOT NULL,
    last_name      VARCHAR(50)              NOT NULL,
    email          VARCHAR(100)             NOT NULL UNIQUE,
    password       VARCHAR(255)             NOT NULL,
    phone_number   VARCHAR(20),
    account_status BOOLEAN                  NOT NULL DEFAULT TRUE,
    created_at     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);
