ALTER TABLE account_owner
    ADD COLUMN user_account_id BIGINT NOT NULL;

ALTER TABLE account_owner
ADD CONSTRAINT fk_user_account_account_owner
FOREIGN KEY (user_account_id)
REFERENCES user_account(user_account_id);

ALTER TABLE account_owner
DROP COLUMN email;