package com.xwkj.donate.controller.common;

import com.xwkj.donate.component.WechatComponent;
import com.xwkj.donate.service.DonationManager;
import com.xwkj.donate.service.WechaterManager;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {

    @Autowired
    protected WechatComponent wechatComponent;

    @Autowired
    protected DonationManager donationManager;

    @Autowired
    protected WechaterManager wechaterManager;

    public DonationManager getDonationManager() {
        return donationManager;
    }

    public void setDonationManager(DonationManager donationManager) {
        this.donationManager = donationManager;
    }

    public WechatComponent getWechatComponent() {
        return wechatComponent;
    }

    public void setWechatComponent(WechatComponent wechatComponent) {
        this.wechatComponent = wechatComponent;
    }

    public WechaterManager getWechaterManager() {
        return wechaterManager;
    }

    public void setWechaterManager(WechaterManager wechaterManager) {
        this.wechaterManager = wechaterManager;
    }
}
