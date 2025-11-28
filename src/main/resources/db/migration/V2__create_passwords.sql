CREATE TABLE passwords (
    id SERIAL PRIMARY KEY,
    serviceName VARCHAR(255) NOT NULL,
    website VARCHAR(255),
    username VARCHAR(255) NOT NULL,
    password TEXT NOT NULL,
    user_id SERIAL NOT NULL REFERENCES users(id) ON DELETE CASCADE
);