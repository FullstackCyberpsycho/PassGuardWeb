DROP TABLE passwords;

CREATE TABLE passwords (
    id SERIAL PRIMARY KEY,
    service_name VARCHAR(255) NOT NULL,
    website VARCHAR(255),
    username VARCHAR(255) NOT NULL,
    password TEXT NOT NULL,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);
