package com.xwkj.donate.controller.common;

import com.xwkj.donate.component.ConfigComponent;
import com.xwkj.donate.component.WechatComponent;
import com.xwkj.donate.service.DonationManager;
import com.xwkj.donate.service.WechaterManager;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {

    @Autowired
    protected ConfigComponent config;

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

    public ConfigComponent getConfig() {
        return config;
    }

    public void setConfig(ConfigComponent config) {
        this.config = config;
    }

    public WechaterManager getWechaterManager() {
        return wechaterManager;
    }

    public void setWechaterManager(WechaterManager wechaterManager) {
        this.wechaterManager = wechaterManager;
    }
}
