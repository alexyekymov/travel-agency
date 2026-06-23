CREATE TABLE user_role
(
    user_id UUID    NOT NULL,
    role    VARCHAR NOT NULL,
    CONSTRAINT user_role_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES app_user (id) ON DELETE CASCADE
);
