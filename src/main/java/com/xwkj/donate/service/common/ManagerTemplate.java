package com.xwkj.donate.service.common;

import com.xwkj.donate.component.WechatComponent;
import com.xwkj.donate.dao.DonationDao;
import com.xwkj.donate.dao.WechaterDao;
import com.xwkj.donate.domain.Wechater;
import com.xwkj.donate.service.AdminManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;

public class ManagerTemplate {

    @Autowired
    protected WechatComponent wechatComponent;

    @Autowired
    protected DonationDao donationDao;

    @Autowired
    protected WechaterDao wechaterDao;

    public WechatComponent getWechatComponent() {
        return wechatComponent;
    }

    public void setWechatComponent(WechatComponent wechatComponent) {
        this.wechatComponent = wechatComponent;
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
