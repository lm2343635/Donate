package com.xwkj.donate.service.impl;

import com.xwkj.common.util.DateTool;
import com.xwkj.common.util.Debug;
import com.xwkj.common.util.MathTool;
import com.xwkj.donate.bean.DonationBean;
import com.xwkj.donate.component.WechatComponent;
import com.xwkj.donate.domain.Donation;
import com.xwkj.donate.domain.Wechater;
import com.xwkj.donate.service.DonationManager;
import com.xwkj.donate.service.common.ManagerTemplate;
import net.sf.json.JSONObject;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

@Service
@RemoteProxy(name = "DonationManager")
public class DonationManagerImpl extends ManagerTemplate implements DonationManager {

    @RemoteMethod
    public DonationBean get(String did) {
        Donation donation = donationDao.get(did);
        if (donation == null) {
            Debug.error("Cannot find the donation by this did.");
            return null;
        }
        return new DonationBean(donation, true);
    }

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
    public boolean setMoney(String did, int money) {
        Donation donation = donationDao.get(did);
        if (donation == null) {
            Debug.error("Cannot find the donation by this did.");
            return false;
        }
        if (donation.getPayed() || money <= 0) {
            Debug.error("This donation has been payed or illegal money!");
            return false;
        }
        donation.setMoney(money);
        donationDao.update(donation);
        return true;
    }

    public DonationBean getByTradeNo(String tradeNo) {
        Donation donation = donationDao.getByTradeNo(tradeNo);
        if (donation == null) {
            return null;
        }
        return new DonationBean(donation, false);
    }

    @Transactional
    public boolean payed(String did, String transactionId) {
        Donation donation = donationDao.get(did);
        if (donation == null) {
            Debug.error("Cannot get a donation by this did.");
            return false;
        }
        donation.setPayed(true);
        donation.setPayAt(System.currentTimeMillis());
        donation.setTransactionId(transactionId);
        donationDao.update(donation);
        return true;
    }

}