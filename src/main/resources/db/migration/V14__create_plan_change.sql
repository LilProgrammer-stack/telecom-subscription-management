CREATE TABLE plan_change
(
    plan_change_id    BIGSERIAL  NOT NULL PRIMARY KEY,
    current_plan_id   BIGINT     NOT NULL,
    requested_plan_id BIGINT     NOT NULL,
    requested_at      DATE       NOT NULL,
    effective_at      DATE       NOT NULL,
    status            VARCHAR(9) NOT NULL,
    phone_line_id     BIGINT     NOT NULL,

    CONSTRAINT fk_plan_change_current_plan
        FOREIGN KEY (current_plan_id)
            REFERENCES plan (plan_id),

    CONSTRAINT fk_plan_change_requested_plan
        FOREIGN KEY (requested_plan_id)
            REFERENCES plan (plan_id),

    CONSTRAINT fk_plan_change_phone_line
        FOREIGN KEY (phone_line_id)
            REFERENCES phone_line (phone_line_id)

)