CREATE TABLE orders
(
    id          UUID PRIMARY KEY        DEFAULT uuidv7(),
    user_id     UUID           NOT NULL REFERENCES app_user (id),
    total_price DECIMAL(10, 2) NOT NULL CHECK (total_price >= 0),
    status      VARCHAR(20)    NOT NULL,
    created_at  TIMESTAMP      NOT NULL DEFAULT now()
);

CREATE TABLE order_item
(
    id             UUID PRIMARY KEY        DEFAULT uuidv7(),
    order_id       UUID           NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    tour_id        UUID           NOT NULL REFERENCES tour (id),
    reserved_seats INT            NOT NULL CHECK (reserved_seats > 0),
    unit_price     DECIMAL(10, 2) NOT NULL CHECK (unit_price >= 0)
);
