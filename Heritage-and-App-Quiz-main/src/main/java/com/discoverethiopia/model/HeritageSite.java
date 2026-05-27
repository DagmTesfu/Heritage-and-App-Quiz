package com.discoverethiopia.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class HeritageSite {
    private int siteId;
    private String name;
    private String region;
    private String description;
    private String amazingFacts;
    private String imagePath;
    private Integer addedByAdminId;

    protected HeritageSite(
            int siteId,
            String name,
            String region,
            String description,
            String amazingFacts,
            String imagePath,
            Integer addedByAdminId) {
        this.siteId = siteId;
        this.name = name;
        this.region = region;
        this.description = description;
        this.amazingFacts = amazingFacts;
        this.imagePath = imagePath;
        this.addedByAdminId = addedByAdminId;
    }

    public abstract String getType();

    public abstract String getCulturalTip();

    public String displayDetails() {
        return name + " (" + getType() + ") - " + region + System.lineSeparator()
                + description + System.lineSeparator()
                + "Tip: " + getCulturalTip();
    }

    public List<String> getAmazingFactsList() {
        if (amazingFacts == null || amazingFacts.isBlank()) {
            return List.of();
        }
        return Arrays.stream(amazingFacts.split(";"))
                .map(String::trim)
                .filter(fact -> !fact.isEmpty())
                .collect(Collectors.toList());
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmazingFacts() {
        return amazingFacts;
    }

    public void setAmazingFacts(String amazingFacts) {
        this.amazingFacts = amazingFacts;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getAddedByAdminId() {
        return addedByAdminId;
    }

    public void setAddedByAdminId(Integer addedByAdminId) {
        this.addedByAdminId = addedByAdminId;
    }
}

