CREATE TABLE tour
(
    id              UUID PRIMARY KEY        DEFAULT uuidv7(),
    title           VARCHAR(150)   NOT NULL,
    description     TEXT,
    price           DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    arrival_date    DATE           NOT NULL,
    eviction_date   DATE           NOT NULL,
    tour_type       VARCHAR(30)    NOT NULL,
    hotel_type      VARCHAR(30)    NOT NULL,
    transfer_type   VARCHAR(30)    NOT NULL,
    available_seats INTEGER        NOT NULL CHECK (available_seats >= 0),
    hot             BOOLEAN        NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP      NOT NULL DEFAULT now(),
    created_by      UUID REFERENCES app_user (id),
    CHECK (eviction_date > arrival_date)
)
