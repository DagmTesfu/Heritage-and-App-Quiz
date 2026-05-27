package com.discoverethiopia.model;

public class CitySite extends HeritageSite {
    public CitySite(int siteId, String name, String region, String description,
            String amazingFacts, String imagePath, Integer addedByAdminId) {
        super(siteId, name, region, description, amazingFacts, imagePath, addedByAdminId);
    }

    @Override
    public String getType() {
        return "city";
    }

    @Override
    public String getCulturalTip() {
        return "Explore with local context and respect community rules.";
    }
}

