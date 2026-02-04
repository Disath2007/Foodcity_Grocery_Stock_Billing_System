-- Run these commands in your database to update the schema

-- 1. Add buying_price to product table
ALTER TABLE product ADD COLUMN buying_price DECIMAL(10, 2) NOT NULL DEFAULT 0.00;

-- 2. Remove buying_price from supplier_product table (optional, based on your request to remove it from generic supplier view)
-- Note: You might want to back up this data if it varies per supplier before dropping!
-- To migrate existing data (if you want to keep the max buying price for the product):
-- OPTIONAL: UPDATE product p JOIN (SELECT product_id, MAX(buying_price) as max_bp FROM supplier_product GROUP BY product_id) sp ON p.product_id = sp.product_id SET p.buying_price = sp.max_bp;

ALTER TABLE supplier_product DROP COLUMN buying_price;
