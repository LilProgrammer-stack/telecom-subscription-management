CREATE TABLE plan_assignment
(
    plan_assignment_id BIGSERIAL NOT NULL PRIMARY KEY,
    start_date         DATE      NOT NULL,
    end_date           DATE,
    phone_line_id BIGINT NOT NULL,
    plan_id BIGINT NOT NULL,

    CONSTRAINT fk_phone_line_plan_assignment
        FOREIGN KEY (phone_line_id)
            REFERENCES phone_line (phone_line_id),

    CONSTRAINT fk_plan_plan_assignment
        FOREIGN KEY (plan_id)
            REFERENCES plan (plan_id)
)