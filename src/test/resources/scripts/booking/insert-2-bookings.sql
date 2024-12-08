INSERT INTO booking (check_in_date, check_out_date, accommodation_id, user_id, status)
VALUES
('2025-12-10', '2025-12-15', 1, 2, 'CONFIRMED'),
('2024-12-27', '2025-01-02', 1, 1, 'PENDING'),
('2027-12-27', '2027-02-02', 1, 1, 'CANCELLED');
ALTER SEQUENCE booking_id_seq RESTART WITH 4;