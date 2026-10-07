CREATE TABLE phone_line
(
    phone_line_id BIGSERIAL   NOT NULL PRIMARY KEY,
    phone_number  VARCHAR(10) NOT NULL UNIQUE,
    status        VARCHAR(20) NOT NULL,
    account_id    BIGINT      NOT NULL,

    CONSTRAINT fk_phone_line_account
        FOREIGN KEY (account_id)
            REFERENCES account (account_id),

    CONSTRAINT chk_phone_number_format
        CHECK (phone_number ~ '^[0-9]{10}$')
    );