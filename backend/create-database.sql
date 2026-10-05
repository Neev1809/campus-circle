-- Run this in your PostgreSQL client (psql or pgAdmin) before starting the backend
-- Only needed once; the backend auto-creates all tables via JPA.

CREATE DATABASE campus_circular;

-- If your PostgreSQL user is not 'postgres', replace below:
-- CREATE USER campus_user WITH PASSWORD 'your_password';
-- GRANT ALL PRIVILEGES ON DATABASE campus_circular TO campus_user;
-- Then update application.properties accordingly.
