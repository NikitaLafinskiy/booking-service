DELETE FROM booking;
ALTER SEQUENCE booking_id_seq RESTART WITH 1;

DELETE FROM user_role WHERE user_id = 2;
DELETE FROM "user" WHERE id = 2;
ALTER SEQUENCE user_id_seq RESTART WITH 2;

DELETE FROM amenity;
DELETE FROM accommodation;
ALTER SEQUENCE amenity_id_seq RESTART WITH 1;
ALTER SEQUENCE accommodation_id_seq RESTART WITH 1;