package com.example.tabletrentalapplication.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RentalRequest {
    private int id;
    private String tabletBrand;
    private boolean includeCable;
    private String cableType;
    private String lenderName;
    private String contactNumber;
    private String submissionDate;

    public RentalRequest(String tabletBrand, boolean includeCable, String cableType, String lenderName, String contactNumber) {
        this.tabletBrand = tabletBrand;
        this.includeCable = includeCable;
        this.cableType = cableType;
        this.lenderName = lenderName;
        this.contactNumber = contactNumber;

        // Automatically set the submission date
        this.submissionDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getTabletBrand() { return tabletBrand; }
    public void setTabletBrand(String tabletBrand) { this.tabletBrand = tabletBrand; }

    public boolean isIncludeCable() { return includeCable; }  // Ensure this method exists
    public void setIncludeCable(boolean includeCable) { this.includeCable = includeCable; }

    public String getCableType() { return cableType; }
    public void setCableType(String cableType) { this.cableType = cableType; }

    public String getLenderName() { return lenderName; }
    public void setLenderName(String lenderName) { this.lenderName = lenderName; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(String submissionDate) { this.submissionDate = submissionDate; }
}