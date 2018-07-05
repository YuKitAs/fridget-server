CREATE TABLE IF NOT EXISTS flatshares (
    id CHAR(36) UNIQUE,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS users (
    id CHAR(36) UNIQUE,
    google_user_id VARCHAR(255) NOT NULL,
    google_name VARCHAR(255) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS access_codes (
    id CHAR(36) UNIQUE,
    flatshare_id CHAR(36) NOT NULL,
    content CHAR(5) NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(flatshare_id)
        REFERENCES flatshares(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS memberships (
    id CHAR(36) UNIQUE,
    flatshare_id CHAR(36) NOT NULL,
    user_id CHAR(36) NOT NULL,
    magnet_color CHAR(6) NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(flatshare_id)
        REFERENCES flatshares(id)
        ON DELETE CASCADE,
    FOREIGN KEY(user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS cool_notes (
    id CHAR(36) UNIQUE,
    title TEXT NOT NULL,
    content TEXT,
    creator_membership_id CHAR(36) NOT NULL,
    importance INT,
    position INT NOT NULL,
    created_at TIMESTAMP,
    PRIMARY KEY(id),
    FOREIGN KEY(creator_membership_id)
        REFERENCES memberships(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS frozen_notes (
    id CHAR(36) UNIQUE,
    flatshare_id CHAR(36) NOT NULL,
    title TEXT,
    content TEXT,
    position INT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(flatshare_id)
        REFERENCES flatshares(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS read_confirmations (
    id CHAR(36) UNIQUE,
    cool_note_id CHAR(36) NOT NULL,
    membership_id CHAR(36) NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(cool_note_id)
        REFERENCES cool_notes(id)
        ON DELETE CASCADE,
    FOREIGN KEY(membership_id)
        REFERENCES memberships(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tagged_members (
    id CHAR(36) UNIQUE,
    cool_note_id CHAR(36) NOT NULL,
    membership_id CHAR(36) NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(cool_note_id)
        REFERENCES cool_notes(id)
        ON DELETE CASCADE,
    FOREIGN KEY(membership_id)
        REFERENCES memberships(id)
        ON DELETE CASCADE
);

