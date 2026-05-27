package com.discoverethiopia.service;

import com.discoverethiopia.dao.QuizDAO;
import com.discoverethiopia.model.Question;

import java.util.List;

public class DatabaseQuizProvider implements IQuizProvider {
    private final QuizDAO quizDAO;

    public DatabaseQuizProvider() {
        this(new QuizDAO());
    }

    public DatabaseQuizProvider(QuizDAO quizDAO) {
        this.quizDAO = quizDAO;
    }

    @Override
    public List<Question> getQuestionsForSite(int siteId) {
        return quizDAO.getQuestionsForSite(siteId);
    }
}

