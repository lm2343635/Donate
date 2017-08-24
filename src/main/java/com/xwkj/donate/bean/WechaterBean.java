package com.xwkj.donate.bean;

import com.xwkj.deduct.domain.Wechater;
import com.xwkj.donate.domain.Wechater;
import org.directwebremoting.annotations.DataTransferObject;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;

@DataTransferObject
public class WechaterBean {

    private String wid;
    private Date updateAt;
    private boolean subscribe;
    private String openid;
    private String nickname;
    private int sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private int subscribe_time;

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public int getSubscribe_time() {
        return subscribe_time;
    }

    public void setSubscribe_time(int subscribe_time) {
        this.subscribe_time = subscribe_time;
    }

    public WechaterBean(Wechater wechater) {
        this.wid = wechater.getWid();
        this.updateAt = new Date(wechater.getUpdateAt());
        this.subscribe = wechater.getSubscribe();
        this.openid = wechater.getOpenid();
        try {
            this.nickname = new String(Base64.getDecoder().decode(wechater.getNickname()), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.sex = wechater.getSex();

        this.city = wechater.getCity();
        this.province = wechater.getProvince();
        this.country = wechater.getCountry();
        this.headimgurl = wechater.getHeadimgurl();
        if (this.subscribe) {
            this.language = wechater.getLanguage();
            this.subscribe_time = wechater.getSubscribe_time();
        }
    }
    
}
