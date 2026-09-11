CREATE SEQUENCE invoice_number_seq
    START WITH 100001
    INCREMENT BY 1;

CREATE TABLE invoice
(

    invoice_id         BIGSERIAL PRIMARY KEY,

    invoice_number     VARCHAR(20) NOT NULL UNIQUE,

    billing_cycle_id   BIGINT      NOT NULL,

    total_amount_cents BIGINT      NOT NULL,

    currency           VARCHAR(3)  NOT NULL,

    issue_date         DATE        NOT NULL,

    due_date           DATE        NOT NULL,

    status             VARCHAR(20) NOT NULL,

    CONSTRAINT fk_billing_cycle
        FOREIGN KEY (billing_cycle_id)
            REFERENCES billing_cycle (billing_cycle_id),

    CONSTRAINT uq_invoice_billing_cycle
        UNIQUE (billing_cycle_id),

    CONSTRAINT chk_invoice_total_amount
        CHECK (total_amount_cents >= 0)

);