CREATE TABLE voucher
(
    id            UUID PRIMARY KEY     DEFAULT uuidv7(),
    code          VARCHAR(36) NOT NULL UNIQUE,
    status        VARCHAR(20) NOT NULL,
    issued_at     TIMESTAMP   NOT NULL DEFAULT now(),
    order_item_id UUID        NOT NULL UNIQUE REFERENCES order_item (id) ON DELETE CASCADE
);
