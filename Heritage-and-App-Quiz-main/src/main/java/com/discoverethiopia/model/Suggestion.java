package com.discoverethiopia.model;

import java.time.LocalDateTime;

public class Suggestion {
    private int suggestionId;
    private int userId;
    private String suggestedName;
    private String suggestedRegion;
    private String suggestedDescription;
    private String reason;
    private SuggestionStatus status;
    private LocalDateTime submittedAt;

    public Suggestion(int suggestionId, int userId, String suggestedName, String suggestedRegion,
            String suggestedDescription, String reason, SuggestionStatus status, LocalDateTime submittedAt) {
        this.suggestionId = suggestionId;
        this.userId = userId;
        this.suggestedName = suggestedName;
        this.suggestedRegion = suggestedRegion;
        this.suggestedDescription = suggestedDescription;
        this.reason = reason;
        this.status = status;
        this.submittedAt = submittedAt;
    }

    public int getSuggestionId() {
        return suggestionId;
    }

    public void setSuggestionId(int suggestionId) {
        this.suggestionId = suggestionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSuggestedName() {
        return suggestedName;
    }

    public void setSuggestedName(String suggestedName) {
        this.suggestedName = suggestedName;
    }

    public String getSuggestedRegion() {
        return suggestedRegion;
    }

    public void setSuggestedRegion(String suggestedRegion) {
        this.suggestedRegion = suggestedRegion;
    }

    public String getSuggestedDescription() {
        return suggestedDescription;
    }

    public void setSuggestedDescription(String suggestedDescription) {
        this.suggestedDescription = suggestedDescription;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public SuggestionStatus getStatus() {
        return status;
    }

    public void setStatus(SuggestionStatus status) {
        this.status = status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}

