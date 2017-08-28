package com.xwkj.donate.bean;

import com.xwkj.donate.domain.Donation;
import org.directwebremoting.annotations.DataTransferObject;

import java.util.Date;

@DataTransferObject
public class DonationBean {

    private String did;
    private Date createAt;
    private String name;
    private int year;
    private boolean sex;
    private String email;
    private int money;
    private boolean payed;
    private Date payAt;
    private String wid;

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    public Date getPayAt() {
        return payAt;
    }

    public void setPayAt(Date payAt) {
        this.payAt = payAt;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public DonationBean(Donation donation) {
        this.did = donation.getDid();
        this.createAt = new Date(donation.getCreateAt());
        this.name = donation.getName();
        this.year = donation.getYear();
        this.sex = donation.getSex();
        this.email = donation.getEmail();
        this.money = donation.getMoney() == null ? 0 : donation.getMoney();
        this.payed = donation.getPayed();
        this.payAt = donation.getPayAt() == null ? null : new Date(donation.getPayAt());
        this.wid = donation.getWechater() == null ? null : donation.getWechater().getWid();
    }

}
