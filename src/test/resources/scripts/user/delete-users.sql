DELETE FROM user_role WHERE user_id = 2;
DELETE FROM "user" WHERE id = 2;
ALTER SEQUENCE user_id_seq RESTART WITH 2;