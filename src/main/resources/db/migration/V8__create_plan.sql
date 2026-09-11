CREATE TABLE plan
(
    plan_id          BIGSERIAL    NOT NULL PRIMARY KEY,
    plan_name        VARCHAR(255) NOT NULL UNIQUE,
    price            BIGINT       NOT NULL,
    data_limit_mb    BIGINT,
    roaming_limit_mb BIGINT,
    hotspot_limit_mb BIGINT,
    description      VARCHAR(255) NOT NULL,
    active           BOOLEAN      NOT NULL,

    CONSTRAINT chk_plan_price
        CHECK (price >= 0),

    CONSTRAINT chk_data_limit_mb
        CHECK (data_limit_mb >= 0),

    CONSTRAINT chk_roaming_limit_mb
        CHECK (roaming_limit_mb >= 0),

    CONSTRAINT chk_hotspot_limit_mb
        CHECK (hotspot_limit_mb >= 0)
)

