CREATE TABLE IF NOT EXISTS hi_device (
    ID      UUID DEFAULT UUID() PRIMARY KEY,
    NAME    VARCHAR(256) NOT NULL,
    BRAND   VARCHAR(256) NOT NULL
);