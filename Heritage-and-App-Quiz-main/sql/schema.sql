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
