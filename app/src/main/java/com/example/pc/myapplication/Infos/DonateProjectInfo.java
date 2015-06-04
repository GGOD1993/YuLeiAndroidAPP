package com.example.pc.myapplication.Infos;

public class DonateProjectInfo {

  private String name;
  private String img_url;
  private String brief;
  private String contact;
  private String addr;

  public DonateProjectInfo(String n, String i, String b, String c, String a) {
    name = n;
    img_url = i;
    brief = b;
    contact = c;
    addr = a;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getImg_url() {
    return img_url;
  }

  public void setImg_url(String img_url) {
    this.img_url = img_url;
  }

  public String getBrief() {
    return brief;
  }

  public void setBrief(String brief) {
    this.brief = brief;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public String getAddr() {
    return addr;
  }

  public void setAddr(String addr) {
    this.addr = addr;
  }
}
