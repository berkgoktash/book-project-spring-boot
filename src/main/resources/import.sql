-- Sample users (password is 'password' encoded with BCrypt)
INSERT INTO users (id, username, password, email) VALUES ('a8d6f984-8c88-4cd0-90ca-0cf631ae8baa', 'user1', '$2a$10$3K8SJ6jj7MX82.H.xH1wYekYpX5/bcYTBQ7N4R/W.Zt.q5Z3nFkzu', 'user1@example.com');
INSERT INTO users (id, username, password, email) VALUES ('9c2f8e2e-4e6f-4f4d-8c7b-1a2b3c4d5e6f', 'user2', '$2a$10$3K8SJ6jj7MX82.H.xH1wYekYpX5/bcYTBQ7N4R/W.Zt.q5Z3nFkzu', 'user2@example.com');

-- Sample books with owners
INSERT INTO books (id, title, author, description, page_count, release_year, owner_id) VALUES ('f47ac10b-58cc-4372-a567-0e02b2c3d479', 'The Great Gatsby', 'F. Scott Fitzgerald', 'A novel about the American Dream', 180, '1925', 'a8d6f984-8c88-4cd0-90ca-0cf631ae8baa');
INSERT INTO books (id, title, author, description, page_count, release_year, owner_id) VALUES ('550e8400-e29b-41d4-a716-446655440000', 'To Kill a Mockingbird', 'Harper Lee', 'A novel about racial inequality', 281, '1960', 'a8d6f984-8c88-4cd0-90ca-0cf631ae8baa');
INSERT INTO books (id, title, author, description, page_count, release_year, owner_id) VALUES ('6ba7b810-9dad-11d1-80b4-00c04fd430c8', '1984', 'George Orwell', 'A dystopian social science fiction novel', 328, '1949', '9c2f8e2e-4e6f-4f4d-8c7b-1a2b3c4d5e6f');
