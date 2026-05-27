package com.discoverethiopia.service;

import com.discoverethiopia.model.Question;

import java.util.List;

public interface IQuizProvider {
    List<Question> getQuestionsForSite(int siteId);
}

