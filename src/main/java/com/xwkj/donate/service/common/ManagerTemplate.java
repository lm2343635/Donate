package com.xwkj.donate.service.common;

import com.xwkj.donate.component.ConfigComponent;
import com.xwkj.donate.component.MailComponent;
import com.xwkj.donate.component.WechatComponent;
import com.xwkj.donate.dao.DonationDao;
import com.xwkj.donate.dao.TokenDao;
import com.xwkj.donate.dao.WechaterDao;
import com.xwkj.donate.domain.Wechater;
import com.xwkj.donate.service.AdminManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;

public class ManagerTemplate {

    @Autowired
    protected ConfigComponent config;

    @Autowired
    protected WechatComponent wechatComponent;

    @Autowired
    protected MailComponent mailComponent;

    @Autowired
    protected TokenDao tokenDao;

    @Autowired
    protected DonationDao donationDao;

    @Autowired
    protected WechaterDao wechaterDao;

    public ConfigComponent getConfig() {
        return config;
    }

    public void setConfig(ConfigComponent config) {
        this.config = config;
    }

    public WechatComponent getWechatComponent() {
        return wechatComponent;
    }

    public void setWechatComponent(WechatComponent wechatComponent) {
        this.wechatComponent = wechatComponent;
    }

    public MailComponent getMailComponent() {
        return mailComponent;
    }

    public void setMailComponent(MailComponent mailComponent) {
        this.mailComponent = mailComponent;
    }

    public TokenDao getTokenDao() {
        return tokenDao;
    }

    public void setTokenDao(TokenDao tokenDao) {
        this.tokenDao = tokenDao;
    }

    public DonationDao getDonationDao() {
        return donationDao;
    }

    public void setDonationDao(DonationDao donationDao) {
        this.donationDao = donationDao;
    }

    public WechaterDao getWechaterDao() {
        return wechaterDao;
    }

    public void setWechaterDao(WechaterDao wechaterDao) {
        this.wechaterDao = wechaterDao;
    }

    public boolean checkAdminSession(HttpSession session) {
        return session.getAttribute(AdminManager.ADMIN_FLAG) != null;
    }

    public Wechater getWechaterFromSession(HttpSession session) {
        return null;
    }

}
