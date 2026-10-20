ALTER TABLE device_payment_agreement
    ADD COLUMN account_id BIGINT NOT NULL,
ADD CONSTRAINT fk_device_payment_agreement_account
    FOREIGN KEY (account_id)
    REFERENCES account (account_id);