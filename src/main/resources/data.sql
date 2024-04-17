-- Insert vans
INSERT INTO van (brand, model, capacity, remaining_capacity) VALUES ('Mercedes', 'Sprinter', 1200, 1200);
INSERT INTO van (brand, model, capacity, remaining_capacity) VALUES ('Ford', 'Transit', 1000, 1000);
INSERT INTO van (brand, model, capacity, remaining_capacity) VALUES ('Volkswagen', 'Crafter', 1400, 1400);
INSERT INTO van (brand, model, capacity, remaining_capacity) VALUES ('Renault', 'Master', 1100, 1100);

-- Insert deliveries
INSERT INTO delivery (delivery_date, from_ware_house, destination, is_active) VALUES ('2023-04-15', 'Warehouse A', '123 Main St', TRUE);
INSERT INTO delivery (delivery_date, from_ware_house, destination, is_active) VALUES ('2023-04-16', 'Warehouse B', '456 Elm St', TRUE);
INSERT INTO delivery (delivery_date, from_ware_house, destination, is_active) VALUES ('2023-04-17', 'Warehouse C', '789 Pine St', TRUE);
INSERT INTO delivery (delivery_date, from_ware_house, destination, is_active) VALUES ('2023-04-18', 'Warehouse D', '101 Apple Blvd', TRUE);

-- Insert products
INSERT INTO product (name, price, weight, is_active) VALUES ('Kyllingebryst', 25.00, 400, true);
INSERT INTO product (name, price, weight, is_active) VALUES ('Hakket oksekød', 50.00, 500, true);
INSERT INTO product (name, price, weight, is_active) VALUES ('Kyllingevinger', 74.00, 1200, true);
INSERT INTO product (name, price, weight, is_active) VALUES ('Fersk laks', 100.00, 300, true);
INSERT INTO product (name, price, weight, is_active) VALUES ('Økologiske æg', 40.00, 200, true);
INSERT INTO product (name, price, weight, is_active) VALUES ('Hjemmebagt brød', 30.00, 600, true);

-- Insert product orders
INSERT INTO product_order (delivery_id, product_id, quantity) VALUES (1, 1, 20);
INSERT INTO product_order (delivery_id, product_id, quantity) VALUES (1, 2, 30);
INSERT INTO product_order (delivery_id, product_id, quantity) VALUES (2, 2, 15);
INSERT INTO product_order (delivery_id, product_id, quantity) VALUES (2, 3, 25);
INSERT INTO product_order (delivery_id, product_id, quantity) VALUES (3, 4, 10);
INSERT INTO product_order (delivery_id, product_id, quantity) VALUES (3, 5, 20);
INSERT INTO product_order (delivery_id, product_id, quantity) VALUES (4, 5, 15);
INSERT INTO product_order (delivery_id, product_id, quantity) VALUES (4, 6, 30);