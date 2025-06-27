CREATE TABLE IF NOT EXISTS event (
                              ID BINARY(16) NOT NULL PRIMARY KEY,
                              title VARCHAR(255) NOT NULL,
                              description TEXT,
                              start_date_time TIMESTAMP NOT NULL,
                              end_date_time TIMESTAMP NOT NULL,
                              location TEXT
);