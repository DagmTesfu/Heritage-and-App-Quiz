package com.discoverethiopia.model;

public class ChurchSite extends HeritageSite {
    public ChurchSite(int siteId, String name, String region, String description,
            String amazingFacts, String imagePath, Integer addedByAdminId) {
        super(siteId, name, region, description, amazingFacts, imagePath, addedByAdminId);
    }

    @Override
    public String getType() {
        return "church";
    }

    @Override
    public String getCulturalTip() {
        return "Respect worship areas and remove shoes where required.";
    }
}

