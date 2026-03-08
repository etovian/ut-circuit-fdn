-- Seed Addresses
INSERT INTO address (street_address, address_line_2, city, state, zip_code) VALUES
('1840 South 75 East', NULL, 'Bountiful', 'UT', '84010'),
('1955 E Stratford Ave', NULL, 'Salt Lake City', 'UT', '84106'),
('1030 South 500 East', NULL, 'Salt Lake City', 'UT', '84105'),
('475 East Herbert Ave', NULL, 'Salt Lake City', 'UT', '84111'),
('1815 E 9800 S', NULL, 'Sandy', 'UT', '84092'),
('165 N Main St', 'Inside Angelus Theatre', 'Spanish Fork', 'UT', '84660'),
('360 West 400 North', NULL, 'Moab', 'UT', '84532'),
('581 N 700 E', NULL, 'Logan', 'UT', '84321'),
('3329 Harrison Blvd', NULL, 'Ogden', 'UT', '84403'),
('240 East 5600 South', NULL, 'Murray', 'UT', '84107'),
('13249 S Redwood Road', NULL, 'Riverton', 'UT', '84065'),
('2260 Red Cliffs Drive', NULL, 'St. George', 'UT', '84790'),
('1310 W 1800 S', NULL, 'Richfield', 'UT', '84701'),
('349 North 7th Street', NULL, 'Tooele', 'UT', '84074');

-- Seed Congregation Addresses Mapping
INSERT INTO congregation_address (congregation_id, address_id, address_type) VALUES
((SELECT id FROM congregation WHERE name = 'Cross of Christ'), (SELECT id FROM address WHERE street_address = '1840 South 75 East'), 'PHYSICAL'),
((SELECT id FROM congregation WHERE name = 'Redeemer'), (SELECT id FROM address WHERE street_address = '1955 E Stratford Ave'), 'PHYSICAL'),
((SELECT id FROM congregation WHERE name = 'St. John'), (SELECT id FROM address WHERE street_address = '1030 South 500 East'), 'PHYSICAL'),
((SELECT id FROM congregation WHERE name = 'St. John'), (SELECT id FROM address WHERE street_address = '475 East Herbert Ave'), 'MAILING'),
((SELECT id FROM congregation WHERE name = 'Grace (Sandy)'), (SELECT id FROM address WHERE street_address = '1815 E 9800 S'), 'PHYSICAL'),
((SELECT id FROM congregation WHERE name = 'Grace (Spanish Fork)'), (SELECT id FROM address WHERE street_address = '165 N Main St'), 'PHYSICAL'),
((SELECT id FROM congregation WHERE name = 'Grace (Moab)'), (SELECT id FROM address WHERE street_address = '360 West 400 North'), 'PHYSICAL'),
((SELECT id FROM congregation WHERE name = 'Holy Trinity (Logan)'), (SELECT id FROM address WHERE street_address = '581 N 700 E'), 'PHYSICAL'),
((SELECT id FROM congregation WHERE name = 'St. Paul'), (SELECT id FROM address WHERE street_address = '3329 Harrison Blvd'), 'PHYSICAL'),
((SELECT id FROM congregation WHERE name = 'Christ'), (SELECT id FROM address WHERE street_address = '240 East 5600 South'), 'PHYSICAL'),
((SELECT id FROM congregation WHERE name = 'Holy Trinity (Riverton)'), (SELECT id FROM address WHERE street_address = '13249 S Redwood Road'), 'PHYSICAL'),
((SELECT id FROM congregation WHERE name = 'Trinity'), (SELECT id FROM address WHERE street_address = '2260 Red Cliffs Drive'), 'PHYSICAL'),
((SELECT id FROM congregation WHERE name = 'Good Shepherd'), (SELECT id FROM address WHERE street_address = '1310 W 1800 S'), 'PHYSICAL'),
((SELECT id FROM congregation WHERE name = 'First'), (SELECT id FROM address WHERE street_address = '349 North 7th Street'), 'PHYSICAL');
