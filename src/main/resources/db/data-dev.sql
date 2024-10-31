INSERT INTO hi_user(id, first_name, last_name, email, password) VALUES ('4f162c22-065d-4726-bf53-603a9c5c4d06', 'Jane', 'Doe', 'jane.doe@xmdf.live', '$2a$10$0HP.FlC2DUveTIWB3qTPo.hw.dbiRL694BxTruwCYSQG0x6rrNN5S');
INSERT INTO hi_user(id, first_name, last_name, email, password) VALUES ('2b2f5cfb-5749-4f56-8768-142ea7875e46', 'John', 'Doe', 'john.doe@xmdf.live', '$2a$10$0HP.FlC2DUveTIWB3qTPo.hw.dbiRL694BxTruwCYSQG0x6rrNN5S');

INSERT INTO hi_user_role(user_id, role_id) VALUES ('4f162c22-065d-4726-bf53-603a9c5c4d06', '9db5a75d-33c0-4934-acc2-45604f24f9fa');
INSERT INTO hi_user_role(user_id, role_id) VALUES ('2b2f5cfb-5749-4f56-8768-142ea7875e46', 'effd219f-1f68-47ea-b804-ba6a78567f40');

INSERT INTO hi_device(NAME, BRAND) VALUES ('Light 1', 'Philips');
INSERT INTO hi_device(NAME, BRAND) VALUES ('Light 2', 'Aqara');
INSERT INTO hi_device(NAME, BRAND) VALUES ('Light 3', 'NanoLeaf');
INSERT INTO hi_device(NAME, BRAND) VALUES ('Sensor 1', 'Philips');
INSERT INTO hi_device(NAME, BRAND) VALUES ('Sensor 2', 'Aqara');
INSERT INTO hi_device(NAME, BRAND) VALUES ('Sensor 3', 'Philips');
INSERT INTO hi_device(NAME, BRAND) VALUES ('Switch 1', 'Philips');
INSERT INTO hi_device(NAME, BRAND) VALUES ('Switch 2', 'Aqara');
INSERT INTO hi_device(NAME, BRAND) VALUES ('Switch 3', 'Philips');