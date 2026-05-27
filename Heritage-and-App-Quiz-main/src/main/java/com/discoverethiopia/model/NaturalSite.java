package com.discoverethiopia.model;

public class NaturalSite extends HeritageSite {
    public NaturalSite(int siteId, String name, String region, String description,
            String amazingFacts, String imagePath, Integer addedByAdminId) {
        super(siteId, name, region, description, amazingFacts, imagePath, addedByAdminId);
    }

    @Override
    public String getType() {
        return "natural";
    }

    @Override
    public String getCulturalTip() {
        return "Protect wildlife, stay on marked paths, and carry out your trash.";
    }
}

