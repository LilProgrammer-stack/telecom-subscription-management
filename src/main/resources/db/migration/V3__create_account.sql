CREATE TABLE account
(
    account_id       BIGSERIAL PRIMARY KEY,
    account_number   VARCHAR(50) NOT NULL UNIQUE,
    billing_language VARCHAR(20) NOT NULL,
    status           VARCHAR(20) NOT NULL,
    creation_date    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    customer_id      BIGINT      NOT NULL,

    CONSTRAINT fk_account_customer
        FOREIGN KEY (customer_id)
            REFERENCES account_owner (account_owner_id)


)