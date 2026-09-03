CREATE TABLE account_owner (
                               account_owner_id BIGSERIAL PRIMARY KEY,
                               first_name VARCHAR(255) NOT NULL,
                               last_name VARCHAR(255) NOT NULL,
                               email VARCHAR(255) NOT NULL UNIQUE,
                               date_of_birth DATE NOT NULL
);