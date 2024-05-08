CREATE TABLE MEMBERS (
     id SERIAL,
     member_id VARCHAR(36),
     reservation_id VARCHAR(36),
     first_name VARCHAR(50),
     last_name VARCHAR(50),
     email VARCHAR(100),
     benefits VARCHAR(200),
     member_type VARCHAR(50),
     street VARCHAR(100),
     city VARCHAR(50),
     province VARCHAR(50),
     country VARCHAR(50),
     PRIMARY KEY (id)
);
