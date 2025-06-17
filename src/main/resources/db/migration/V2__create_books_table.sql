CREATE TABLE IF NOT EXISTS books (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    description TEXT,
    page_count INTEGER,
    release_year VARCHAR(4),
    owner_id UUID REFERENCES users(id) ON DELETE SET NULL
); 