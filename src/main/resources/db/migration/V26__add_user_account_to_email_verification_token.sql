ALTER TABLE email_verification_token
DROP
CONSTRAINT fk_email_verification_token_customer_credential;

ALTER TABLE email_verification_token
    DROP COLUMN customer_credential_id;

ALTER TABLE email_verification_token
    ADD COLUMN user_account_id BIGINT NOT NULL UNIQUE;

ALTER TABLE email_verification_token
    ADD CONSTRAINT fk_email_verification_token_user_account
        FOREIGN KEY (user_account_id)
            REFERENCES user_account (user_account_id);