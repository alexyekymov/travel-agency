CREATE TABLE cart
(
    id         UUID PRIMARY KEY   DEFAULT uuidv7(),
    user_id    UUID      NOT NULL UNIQUE REFERENCES app_user (id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE cart_item
(
    id             UUID PRIMARY KEY DEFAULT uuidv7(),
    cart_id        UUID NOT NULL REFERENCES cart (id) ON DELETE CASCADE,
    tour_id        UUID NOT NULL REFERENCES tour (id),
    reserved_seats INT  NOT NULL CHECK (reserved_seats > 0),
    CONSTRAINT cart_item_unique UNIQUE (cart_id, tour_id)
);
