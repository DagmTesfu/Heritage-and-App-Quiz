package com.discoverethiopia.dao;

import com.discoverethiopia.db.DatabaseConnection;
import com.discoverethiopia.model.Question;
import com.discoverethiopia.model.QuizAttempt;
import com.discoverethiopia.model.WrongAnswer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuizDAO {
    private final DatabaseConnection databaseConnection;

    public QuizDAO() {
        this(DatabaseConnection.getInstance());
    }

    public QuizDAO(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public List<Question> getQuestionsForSite(int siteId) {
        String sql = """
                SELECT q_id, site_id, question_text, option_a, option_b, option_c, option_d,
                       correct_option, explanation
                FROM quiz_questions
                WHERE site_id = ?
                ORDER BY q_id
                """;
        List<Question> questions = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, siteId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    questions.add(mapQuestion(resultSet));
                }
            }
            return questions;
        } catch (SQLException e) {
            throw new DaoException("Could not load quiz questions.", e);
        }
    }

    public int addQuestion(Question question) {
        String sql = """
                INSERT INTO quiz_questions
                (site_id, question_text, option_a, option_b, option_c, option_d, correct_option, explanation)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, question.getSiteId());
            statement.setString(2, question.getQuestionText());
            statement.setString(3, question.getOptionA());
            statement.setString(4, question.getOptionB());
            statement.setString(5, question.getOptionC());
            statement.setString(6, question.getOptionD());
            statement.setString(7, String.valueOf(question.getCorrectOption()));
            statement.setString(8, question.getExplanation());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            throw new DaoException("Question was created but no generated key was returned.", null);
        } catch (SQLException e) {
            throw new DaoException("Could not add quiz question.", e);
        }
    }

    public boolean deleteQuestion(int questionId) {
        String sql = "DELETE FROM quiz_questions WHERE q_id = ?";
        try (Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, questionId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Could not delete quiz question.", e);
        }
    }

    public QuizAttempt saveQuizAttempt(int userId, int siteId, List<Question> questions,
            Map<Integer, Character> answersByQuestionId) {
        if (questions == null || questions.isEmpty()) {
            throw new IllegalArgumentException("Cannot save an empty quiz attempt.");
        }

        int score = 0;
        for (Question question : questions) {
            Character answer = answersByQuestionId.get(question.getQuestionId());
            if (answer != null && question.isCorrect(answer)) {
                score++;
            }
        }
        int total = questions.size();
        int percentage = Math.round((score * 100f) / total);

        try (Connection connection = databaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int attemptId = insertAttempt(connection, userId, siteId, score, total, percentage);
                insertWrongAnswers(connection, attemptId, questions, answersByQuestionId);
                connection.commit();
                return new QuizAttempt(attemptId, userId, siteId, score, total, percentage, LocalDateTime.now());
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DaoException("Could not save quiz attempt.", e);
        }
    }

    public List<QuizAttempt> getAttemptsForUser(int userId) {
        String sql = """
                SELECT attempt_id, user_id, site_id, score, total_questions, percentage, taken_at
                FROM quiz_attempts
                WHERE user_id = ?
                ORDER BY taken_at DESC
                """;
        List<QuizAttempt> attempts = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    attempts.add(mapAttempt(resultSet));
                }
            }
            return attempts;
        } catch (SQLException e) {
            throw new DaoException("Could not load quiz attempts.", e);
        }
    }

    public List<WrongAnswer> getWrongAnswersForAttempt(int attemptId) {
        String sql = """
                SELECT wrong_id, attempt_id, question_text, user_answer, correct_answer, explanation
                FROM wrong_answers
                WHERE attempt_id = ?
                ORDER BY wrong_id
                """;
        List<WrongAnswer> wrongAnswers = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, attemptId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    wrongAnswers.add(mapWrongAnswer(resultSet));
                }
            }
            return wrongAnswers;
        } catch (SQLException e) {
            throw new DaoException("Could not load wrong answers.", e);
        }
    }

    private int insertAttempt(Connection connection, int userId, int siteId, int score, int total, int percentage)
            throws SQLException {
        String sql = """
                INSERT INTO quiz_attempts (user_id, site_id, score, total_questions, percentage)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, userId);
            statement.setInt(2, siteId);
            statement.setInt(3, score);
            statement.setInt(4, total);
            statement.setInt(5, percentage);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            throw new SQLException("No attempt id returned.");
        }
    }

    private void insertWrongAnswers(Connection connection, int attemptId, List<Question> questions,
            Map<Integer, Character> answersByQuestionId) throws SQLException {
        String sql = """
                INSERT INTO wrong_answers
                (attempt_id, question_text, user_answer, correct_answer, explanation)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Question question : questions) {
                Character answer = answersByQuestionId.get(question.getQuestionId());
                if (answer == null || !question.isCorrect(answer)) {
                    statement.setInt(1, attemptId);
                    statement.setString(2, question.getQuestionText());
                    statement.setString(3, answer == null ? "No answer" : question.getAnswerText(answer));
                    statement.setString(4, question.getCorrectAnswerText());
                    statement.setString(5, question.getExplanation());
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        }
    }

    private Question mapQuestion(ResultSet resultSet) throws SQLException {
        return new Question(
                resultSet.getInt("q_id"),
                resultSet.getInt("site_id"),
                resultSet.getString("question_text"),
                resultSet.getString("option_a"),
                resultSet.getString("option_b"),
                resultSet.getString("option_c"),
                resultSet.getString("option_d"),
                resultSet.getString("correct_option").charAt(0),
                resultSet.getString("explanation"));
    }

    private QuizAttempt mapAttempt(ResultSet resultSet) throws SQLException {
        return new QuizAttempt(
                resultSet.getInt("attempt_id"),
                resultSet.getInt("user_id"),
                resultSet.getInt("site_id"),
                resultSet.getInt("score"),
                resultSet.getInt("total_questions"),
                resultSet.getInt("percentage"),
                resultSet.getTimestamp("taken_at").toLocalDateTime());
    }

    private WrongAnswer mapWrongAnswer(ResultSet resultSet) throws SQLException {
        return new WrongAnswer(
                resultSet.getInt("wrong_id"),
                resultSet.getInt("attempt_id"),
                resultSet.getString("question_text"),
                resultSet.getString("user_answer"),
                resultSet.getString("correct_answer"),
                resultSet.getString("explanation"));
    }
}

