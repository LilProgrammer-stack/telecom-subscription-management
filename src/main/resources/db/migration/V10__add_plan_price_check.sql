ALTER TABLE plan
DROP CONSTRAINT chk_plan_price;

ALTER TABLE plan
    ADD CONSTRAINT chk_plan_price
        CHECK (price > 0);