package com.discoverethiopia.model;

public class ArchaeologicalSite extends HeritageSite {
    public ArchaeologicalSite(int siteId, String name, String region, String description,
            String amazingFacts, String imagePath, Integer addedByAdminId) {
        super(siteId, name, region, description, amazingFacts, imagePath, addedByAdminId);
    }

    @Override
    public String getType() {
        return "archaeological";
    }

    @Override
    public String getCulturalTip() {
        return "Avoid touching ancient structures and follow site guide instructions.";
    }
}

