SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE users;
TRUNCATE TABLE devices;
TRUNCATE TABLE flatshares;
TRUNCATE TABLE access_codes;
TRUNCATE TABLE memberships;
TRUNCATE TABLE cool_notes;
TRUNCATE TABLE frozen_notes;
TRUNCATE TABLE tagged_members;
TRUNCATE TABLE read_confirmations;

SET FOREIGN_KEY_CHECKS = 1;