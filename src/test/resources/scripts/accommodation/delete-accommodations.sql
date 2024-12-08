DELETE FROM amenity;
DELETE FROM accommodation;
ALTER SEQUENCE amenity_id_seq RESTART WITH 1;
ALTER SEQUENCE accommodation_id_seq RESTART WITH 1;