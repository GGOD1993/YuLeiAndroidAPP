package com.example.pc.myapplication.Infos;

public class DonateDataInfo {
    private String company;
    private String companyImg;
    private String companyContact;
    private String charityName;
    private int seeds;

    public DonateDataInfo(String company, String companyImg, String companyContact, String charityName, int seeds) {
        this.company = company;
        this.companyImg = companyImg;
        this.companyContact = companyContact;
        this.charityName = charityName;
        this.seeds = seeds;
    }

    public String getCompanyImg() {
        return companyImg;
    }

    public void setCompanyImg(String companyImg) {
        this.companyImg = companyImg;
    }

    public String getCompanyContact() {
        return companyContact;
    }

    public void setCompanyContact(String companyContact) {
        this.companyContact = companyContact;
    }

    public String getCharityName() {
        return charityName;
    }

    public void setCharityName(String charityName) {
        this.charityName = charityName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getSeeds() {
        return seeds;
    }

    public void setSeeds(int seeds) {
        this.seeds = seeds;
    }
}
