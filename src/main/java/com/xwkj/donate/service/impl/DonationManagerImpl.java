package com.xwkj.donate.service.impl;

import com.xwkj.common.util.Debug;
import com.xwkj.donate.domain.Donation;
import com.xwkj.donate.domain.Wechater;
import com.xwkj.donate.service.DonationManager;
import com.xwkj.donate.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Service
@RemoteProxy(name = "DonationManager")
public class DonationManagerImpl extends ManagerTemplate implements DonationManager {

    @RemoteMethod
    @Transactional
    public String register(String name, boolean sex, int year, String email) {
        Donation donation = new Donation();
        donation.setCreateAt(System.currentTimeMillis());
        donation.setName(name);
        donation.setEmail(email);
        donation.setSex(sex);
        donation.setYear(year);
        donation.setPayed(false);
        return donationDao.save(donation);
    }

    @RemoteMethod
    @Transactional
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
