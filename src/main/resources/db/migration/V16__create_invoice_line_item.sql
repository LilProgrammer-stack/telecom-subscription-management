CREATE TABLE invoice_line_item
(
    invoice_line_item_id BIGSERIAL    NOT NULL PRIMARY KEY,
    item_type            VARCHAR(50)  NOT NULL,
    description          VARCHAR(300) NOT NULL,
    amount               BIGINT       NOT NULL,
    currency             VARCHAR(3)   NOT NULL,
    invoice_id           BIGINT       NOT NULL,

    CONSTRAINT fk_invoice_line_item_invoice
        FOREIGN KEY (invoice_id)
            REFERENCES invoice (invoice_id)
);