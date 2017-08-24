package com.xwkj.donate.service.impl;

import com.xwkj.common.util.Debug;
import com.xwkj.donate.domain.Donation;
import com.xwkj.donate.domain.Wechater;
import com.xwkj.donate.service.DonationManager;
import com.xwkj.donate.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RemoteProxy(name = "DonationManager")
public class DonationManagerImpl extends ManagerTemplate implements DonationManager {

    @Override
    public String createDonation(String name, String email, int money, HttpSession session) {
        Wechater wechater = getWechaterFromSession(session);
        if (wechater == null) {
            Debug.error("Cannot find a wechat user from session.");
            return null;
        }
        Donation donation = new Donation();
        donation.setCreateAt(System.currentTimeMillis());
        donation.setName(name);
        donation.setEmail(email);
        donation.setMoney(money);
        donation.setPayed(false);
        donation.setWechater(wechater);
        return donationDao.save(donation);
    }

    @Override
    public boolean pay(String did) {
        Donation donation = donationDao.get(did);
        if (donation == null) {
            Debug.error("Cannot get a donation by this did.");
            return false;
        }
        donation.setPayed(true);
        donation.setPayAt(System.currentTimeMillis());
        donationDao.update(donation);
        return false;
    }

}
