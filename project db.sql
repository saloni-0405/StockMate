
CREATE DATABASE IF NOT EXISTS stockmate_db;

USE stockmate_db;

-- Check database version
SELECT VERSION();

-- Display available tables
SHOW TABLES;

-- Display all products
SELECT * FROM Products;
