-- Insert accommodations
INSERT INTO accommodation (id, type, location, size, daily_rate, availability)
VALUES
    (1, 'HOUSE', '123 Elm Street, Springfield', 'Large', 120.00, 10),
    (2, 'APARTMENT', '456 Oak Avenue, Shelby ville', 'Medium', 80.00, 15);

-- Insert amenities for accommodation 1
INSERT INTO amenity (id, name, accommodation_id)
VALUES
    (1, 'WiFi', 1),
    (2, 'Parking', 1),
    (3, 'Pool', 1);

-- Insert amenities for accommodation 2
INSERT INTO amenity (id, name, accommodation_id)
VALUES
    (4, 'Air Conditioning', 2),
    (5, 'Elevator Access', 2),
    (6, 'Gym', 2);

-- Update sequences to avoid conflicts
ALTER SEQUENCE amenity_id_seq RESTART WITH 7;
ALTER SEQUENCE accommodation_id_seq RESTART WITH 3;