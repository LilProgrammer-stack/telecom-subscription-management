ALTER TABLE customer_credential
    ADD COLUMN registration_edit_token_hash VARCHAR(64),
    ADD COLUMN registration_edit_token_expires_at TIMESTAMP WITH TIME ZONE;