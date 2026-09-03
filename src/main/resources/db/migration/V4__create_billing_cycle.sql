CREATE TABLE billing_cycle
(
    billing_cycle_id  BIGSERIAL PRIMARY KEY,
    account_id        BIGINT  NOT NULL,
    cycle_number      INTEGER NOT NULL,
    period_start_date DATE    NOT NULL,
    period_end_date   DATE    NOT NULL,
    due_date          DATE    NOT NULL,

    constraint fk_billing_cycle_account
        FOREIGN KEY (account_id)
            references account (account_id),

            CONSTRAINT uq_billing_cycle_account_number
            UNIQUE (account_id, cycle_number)
);