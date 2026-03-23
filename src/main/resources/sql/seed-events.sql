-- Seed Event Templates for Congregations
-- Using RRULE FREQ=WEEKLY;BYDAY=SU for weekly Sunday events

-- Cross of Christ (ID 1)
INSERT INTO event_template (name, description, congregation_id, start_date, start_time, duration_minutes, recurrence_rule, is_active, is_circuit_event) VALUES
('Sunday Service', 'A traditional Lutheran service with communion and fellowship.', 1, '2024-01-01', '10:30:00', 60, 'FREQ=WEEKLY;BYDAY=SU', true, false),
('Bible Study', 'Deep dive into scripture and congregational learning.', 1, '2024-01-01', '09:15:00', 60, 'FREQ=WEEKLY;BYDAY=SU', true, false);

-- Redeemer (ID 2)
INSERT INTO event_template (name, description, congregation_id, start_date, start_time, duration_minutes, recurrence_rule, is_active, is_circuit_event) VALUES
('Sunday Service', 'Join us for worship and fellowship.', 2, '2024-01-01', '10:00:00', 60, 'FREQ=WEEKLY;BYDAY=SU', true, false),
('Bible Study', 'Study of God''s Word together.', 2, '2024-01-01', '09:00:00', 60, 'FREQ=WEEKLY;BYDAY=SU', true, false);

-- St. John (ID 3)
INSERT INTO event_template (name, description, congregation_id, start_date, start_time, duration_minutes, recurrence_rule, is_active, is_circuit_event) VALUES
('Sunday Service', 'Join us for worship and fellowship.', 3, '2024-01-01', '10:00:00', 60, 'FREQ=WEEKLY;BYDAY=SU', true, false),
('Bible Study', 'Study of God''s Word together.', 3, '2024-01-01', '09:00:00', 60, 'FREQ=WEEKLY;BYDAY=SU', true, false);

-- Grace (Sandy) (ID 4)
INSERT INTO event_template (name, description, congregation_id, start_date, start_time, duration_minutes, recurrence_rule, is_active, is_circuit_event) VALUES
('Sunday Service', 'Join us for worship and fellowship.', 4, '2024-01-01', '10:00:00', 60, 'FREQ=WEEKLY;BYDAY=SU', true, false),
('Bible Study', 'Study of God''s Word together.', 4, '2024-01-01', '09:00:00', 60, 'FREQ=WEEKLY;BYDAY=SU', true, false);

-- Grace (Spanish Fork) (ID 5)
INSERT INTO event_template (name, description, congregation_id, start_date, start_time, duration_minutes, recurrence_rule, is_active, is_circuit_event) VALUES
('Sunday Service', 'Join us for worship and fellowship.', 5, '2024-01-01', '10:00:00', 60, 'FREQ=WEEKLY;BYDAY=SU', true, false);

-- Grace (Moab) (ID 6)
INSERT INTO event_template (name, description, congregation_id, start_date, start_time, duration_minutes, recurrence_rule, is_active, is_circuit_event) VALUES
('Sunday Service', 'Join us for worship and fellowship.', 6, '2024-01-01', '10:00:00', 60, 'FREQ=WEEKLY;BYDAY=SU', true, false);

-- Holy Trinity (Logan) (ID 7)
INSERT INTO event_template (name, description, congregation_id, start_date, start_time, duration_minutes, recurrence_rule, is_active, is_circuit_event) VALUES
('Sunday Service', 'Join us for worship and fellowship.', 7, '2024-01-01', '10:00:00', 60, 'FREQ=WEEKLY;BYDAY=SU', true, false),
('Bible Study', 'Study of God''s Word together.', 7, '2024-01-01', '09:00:00', 60, 'FREQ=WEEKLY;BYDAY=SU', true, false);

-- St. Paul (ID 8)
INSERT INTO event_template (name, description, congregation_id, start_date, start_time, duration_minutes, recurrence_rule, is_active, is_circuit_event) VALUES
('Sunday Service', 'Join us for worship and fellowship.', 8, '2024-01-01', '10:00:00', 60, 'FREQ=WEEKLY;BYDAY=SU', true, false),
('Bible Study', 'Study of God''s Word together.', 8, '2024-01-01', '09:00:00', 60, 'FREQ=WEEKLY;BYDAY=SU', true, false);

-- Christ (ID 9)
INSERT INTO event_template (name, description, congregation_id, start_date, start_time, duration_minutes, recurrence_rule, is_active, is_circuit_event) VALUES
('Sunday Service', 'Join us for worship and fellowship.', 9, '2024-01-01', '10:00:00', 60, 'FREQ=WEEKLY;BYDAY=SU', true, false),
('Bible Study', 'Study of God''s Word together.', 9, '2024-01-01', '09:00:00', 60, 'FREQ=WEEKLY;BYDAY=SU', true, false);

-- Holy Trinity (Riverton) (ID 10)
INSERT INTO event_template (name, description, congregation_id, start_date, start_time, duration_minutes, recurrence_rule, is_active, is_circuit_event) VALUES
('Sunday Service', 'Join us for worship and fellowship.', 10, '2024-01-01', '10:00:00', 60, 'FREQ=WEEKLY;BYDAY=SU', true, false);

-- Trinity (ID 11)
INSERT INTO event_template (name, description, congregation_id, start_date, start_time, duration_minutes, recurrence_rule, is_active, is_circuit_event) VALUES
('Sunday Service', 'Join us for worship and fellowship.', 11, '2024-01-01', '10:00:00', 60, 'FREQ=WEEKLY;BYDAY=SU', true, false),
('Bible Study', 'Study of God''s Word together.', 11, '2024-01-01', '09:00:00', 60, 'FREQ=WEEKLY;BYDAY=SU', true, false);

-- Good Shepherd (ID 12)
INSERT INTO event_template (name, description, congregation_id, start_date, start_time, duration_minutes, recurrence_rule, is_active, is_circuit_event) VALUES
('Sunday Service', 'Join us for worship and fellowship.', 12, '2024-01-01', '10:00:00', 60, 'FREQ=WEEKLY;BYDAY=SU', true, false);

-- First (ID 13)
INSERT INTO event_template (name, description, congregation_id, start_date, start_time, duration_minutes, recurrence_rule, is_active, is_circuit_event) VALUES
('Sunday Service', 'Join us for worship and fellowship.', 13, '2024-01-01', '10:00:00', 60, 'FREQ=WEEKLY;BYDAY=SU', true, false),
('Bible Study', 'Study of God''s Word together.', 13, '2024-01-01', '09:00:00', 60, 'FREQ=WEEKLY;BYDAY=SU', true, false);

-- Circuit-wide Events
INSERT INTO event_template (name, description, congregation_id, start_date, start_time, duration_minutes, recurrence_rule, is_active, is_circuit_event) VALUES
('Circuit Pastoral Conference', 'Meeting of all circuit pastors for study, worship, and business.', 1, '2024-03-24', '09:00:00', 480, 'FREQ=MONTHLY;BYDAY=4TU', true, true);
