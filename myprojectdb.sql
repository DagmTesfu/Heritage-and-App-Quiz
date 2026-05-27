CREATE DATABASE IF NOT EXISTS myprojectdb;
USE myprojectdb;

DROP TABLE IF EXISTS admin_logs;
DROP TABLE IF EXISTS user_suggestions;
DROP TABLE IF EXISTS wrong_answers;
DROP TABLE IF EXISTS quiz_attempts;
DROP TABLE IF EXISTS quiz_questions;
DROP TABLE IF EXISTS heritage_sites;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('user', 'admin') DEFAULT 'user',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE heritage_sites (
    site_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) UNIQUE NOT NULL,
    type VARCHAR(50) NOT NULL,
    region VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    amazing_facts TEXT NOT NULL,
    image_path VARCHAR(255),
    added_by_admin_id INT,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (added_by_admin_id) REFERENCES users(user_id)
);

CREATE TABLE quiz_questions (
    q_id INT PRIMARY KEY AUTO_INCREMENT,
    site_id INT NOT NULL,
    question_text TEXT NOT NULL,
    option_a VARCHAR(255) NOT NULL,
    option_b VARCHAR(255) NOT NULL,
    option_c VARCHAR(255) NOT NULL,
    option_d VARCHAR(255) NOT NULL,
    correct_option CHAR(1) NOT NULL,
    explanation TEXT,
    FOREIGN KEY (site_id) REFERENCES heritage_sites(site_id) ON DELETE CASCADE,
    CONSTRAINT chk_correct_option CHECK (correct_option IN ('A', 'B', 'C', 'D'))
);

CREATE TABLE quiz_attempts (
    attempt_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    site_id INT NOT NULL,
    score INT NOT NULL,
    total_questions INT NOT NULL,
    percentage INT NOT NULL,
    taken_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (site_id) REFERENCES heritage_sites(site_id)
);

CREATE TABLE wrong_answers (
    wrong_id INT PRIMARY KEY AUTO_INCREMENT,
    attempt_id INT NOT NULL,
    question_text TEXT NOT NULL,
    user_answer VARCHAR(255),
    correct_answer VARCHAR(255) NOT NULL,
    explanation TEXT,
    FOREIGN KEY (attempt_id) REFERENCES quiz_attempts(attempt_id) ON DELETE CASCADE
);

CREATE TABLE user_suggestions (
    suggestion_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    suggested_name VARCHAR(100) NOT NULL,
    suggested_region VARCHAR(100) NOT NULL,
    suggested_description TEXT NOT NULL,
    reason TEXT NOT NULL,
    status ENUM('pending', 'approved', 'rejected') DEFAULT 'pending',
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE admin_logs (
    log_id INT PRIMARY KEY AUTO_INCREMENT,
    admin_id INT NOT NULL,
    action TEXT NOT NULL,
    log_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (admin_id) REFERENCES users(user_id)
);



USE myprojectdb;

-- SHA-256 hash of "admin123"
INSERT INTO users (username, email, password_hash, role) VALUES
('admin', 'admin@discoverethiopia.local', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'admin');

-- SHA-256 hash of "user123"
INSERT INTO users (username, email, password_hash, role) VALUES
('demo_user', 'demo@discoverethiopia.local', 'e606e38b0d8c19b24cf0ee3808183162ea7cd63ff7912dbb22b5e803286b4446', 'user');

INSERT INTO heritage_sites (name, type, region, description, amazing_facts, image_path, added_by_admin_id) VALUES
('Lalibela Rock Churches', 'church', 'Amhara',
 'Lalibela is famous for eleven medieval monolithic churches carved from rock. The churches are still active places of worship and are one of Ethiopia''s most recognized UNESCO World Heritage sites.',
 'Built during the Zagwe dynasty; Some churches are connected by tunnels; The churches are divided into northern and southern groups; Bete Giyorgis is carved in a cross shape.',
 'images/lalibela.jpg', 1),
('Aksum Obelisks', 'archaeological', 'Tigray',
 'Aksum was the center of the ancient Aksumite Kingdom. Its tall carved stelae, royal tombs, and archaeological remains show the power of one of Africa''s great ancient civilizations.',
 'Aksum was a major trading empire; The tallest standing stele is more than 20 meters high; Aksum minted its own coins; Ethiopian tradition connects Aksum with the Ark of the Covenant.',
 'images/aksum.jpg', 1),
('Simien Mountains National Park', 'natural', 'Amhara',
 'The Simien Mountains are known for dramatic cliffs, deep valleys, and rare wildlife. The park protects unique species and some of Ethiopia''s most spectacular highland landscapes.',
 'Home to the gelada monkey; Ras Dashen is Ethiopia''s highest mountain; The park is a UNESCO natural heritage site; The Walia ibex is found only in Ethiopia.',
 'simien_mountains.png', 1);

INSERT INTO quiz_questions
(site_id, question_text, option_a, option_b, option_c, option_d, correct_option, explanation) VALUES
(1, 'How many rock-hewn churches are in Lalibela?', '5', '11', '13', '7', 'B', 'Lalibela has eleven rock-hewn churches grouped into two main clusters.'),
(1, 'Which dynasty is strongly linked with the building of Lalibela?', 'Zagwe', 'Solomonic', 'Aksumite', 'Gondarine', 'A', 'The churches are commonly associated with King Lalibela and the Zagwe dynasty.'),
(1, 'Which Lalibela church is famous for its cross-shaped plan?', 'Bete Maryam', 'Bete Medhane Alem', 'Bete Giyorgis', 'Bete Gabriel', 'C', 'Bete Giyorgis is widely known for its cross-shaped design.'),
(1, 'What type of heritage site is Lalibela in this app?', 'Natural', 'Church', 'City', 'Museum', 'B', 'The project models Lalibela as a church heritage site.'),
(1, 'What material are Lalibela churches carved from?', 'Wood', 'Brick', 'Rock', 'Glass', 'C', 'They are rock-hewn churches carved directly from stone.'),

(2, 'Aksum is most famous for which ancient monuments?', 'Castles', 'Obelisks and stelae', 'Rock churches', 'Hot springs', 'B', 'Aksum is famous for its tall carved stelae and obelisks.'),
(2, 'Which ancient kingdom had Aksum as a major center?', 'Aksumite Kingdom', 'Mali Empire', 'Roman Empire', 'Zulu Kingdom', 'A', 'Aksum was the center of the Aksumite Kingdom.'),
(2, 'In which Ethiopian region is Aksum located?', 'Oromia', 'Tigray', 'Somali', 'Sidama', 'B', 'Aksum is located in the Tigray region.'),
(2, 'What did ancient Aksum mint as a sign of its economy?', 'Paper money', 'Coins', 'Shell beads only', 'Gold crowns only', 'B', 'The Aksumite Kingdom minted its own coins.'),
(2, 'What site type is Aksum in this app?', 'Archaeological', 'Church', 'Natural', 'City park', 'A', 'Aksum is represented as an archaeological heritage site.'),

(3, 'What rare animal is strongly associated with the Simien Mountains?', 'Walia ibex', 'Polar bear', 'Kangaroo', 'Tiger', 'A', 'The Walia ibex is native to Ethiopia and protected in the Simien Mountains.'),
(3, 'Which mountain in the Simien range is Ethiopia''s highest?', 'Mount Entoto', 'Ras Dashen', 'Mount Zuqualla', 'Mount Batu', 'B', 'Ras Dashen is the highest mountain in Ethiopia.'),
(3, 'What kind of UNESCO heritage site is Simien Mountains National Park?', 'Natural', 'Church', 'Archaeological', 'Palace', 'A', 'The Simien Mountains are a natural heritage site.'),
(3, 'Which primate is commonly seen in the Simien Mountains?', 'Gelada monkey', 'Gorilla', 'Chimpanzee', 'Lemur', 'A', 'Geladas live in the Ethiopian highlands and are common in the Simien Mountains.'),
(3, 'What landscape feature is Simien especially known for?', 'Desert dunes', 'Dramatic cliffs and valleys', 'Coral reefs', 'Rainforest canopy', 'B', 'The park is known for cliffs, escarpments, and deep valleys.');








