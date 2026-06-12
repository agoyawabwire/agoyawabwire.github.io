package com.poultryflow.pro.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class DailyRecord {
    private String id;
    private String date;
    private int activeBirds;
    private int eggsCollected;
    private int damagedEggs;
    private double feedKg;
    private int mortality;

    public DailyRecord() {
        // Default constructor required for calls to DataSnapshot.getValue(DailyRecord.class)
    }

    public DailyRecord(String id, String date, int activeBirds, int eggsCollected, int damagedEggs, double feedKg, int mortality) {
        this.id = id;
        this.date = date;
        this.activeBirds = activeBirds;
        this.eggsCollected = eggsCollected;
        this.damagedEggs = damagedEggs;
        this.feedKg = feedKg;
        this.mortality = mortality;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getActiveBirds() {
        return activeBirds;
    }

    public void setActiveBirds(int activeBirds) {
        this.activeBirds = activeBirds;
    }

    public int getEggsCollected() {
        return eggsCollected;
    }

    public void setEggsCollected(int eggsCollected) {
        this.eggsCollected = eggsCollected;
    }

    public int getDamagedEggs() {
        return damagedEggs;
    }

    public void setDamagedEggs(int damagedEggs) {
        this.damagedEggs = damagedEggs;
    }

    public double getFeedKg() {
        return feedKg;
    }

    public void setFeedKg(double feedKg) {
        this.feedKg = feedKg;
    }

    public int getMortality() {
        return mortality;
    }

    public void setMortality(int mortality) {
        this.mortality = mortality;
    }
}
