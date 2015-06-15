package com.example.pc.myapplication.Infos;

public class DonateDataInfo {
    private String company;
    private String companyImg;
    private String companyUrl;
    private String charityName;
    private int seeds;

    public DonateDataInfo(String company, String companyImg, String companyUrl, String charityName, int seeds) {
        this.company = company;
        this.companyImg = companyImg;
        this.companyUrl = companyUrl;
        this.charityName = charityName;
        this.seeds = seeds;
    }

    public String getCompanyImg() {
        return companyImg;
    }

    public void setCompanyImg(String companyImg) {
        this.companyImg = companyImg;
    }

    public String getCompanyUrl() {
        return companyUrl;
    }

    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCharity_name() {
        return charity_name;
    }

    public void setCharity_name(String charity_name) {
        this.charity_name = charity_name;
    }

    public int getSeeds() {
        return seeds;
    }

    public void setSeeds(int seeds) {
        this.seeds = seeds;
    }
}
