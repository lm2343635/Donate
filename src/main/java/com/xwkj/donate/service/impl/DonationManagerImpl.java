package com.xwkj.donate.service.impl;

import com.xwkj.common.util.DateTool;
import com.xwkj.common.util.Debug;
import com.xwkj.common.util.MathTool;
import com.xwkj.common.util.MengularDocument;
import com.xwkj.donate.bean.DonationBean;
import com.xwkj.donate.bean.JSAPIResult;
import com.xwkj.donate.component.WechatComponent;
import com.xwkj.donate.domain.Donation;
import com.xwkj.donate.domain.Wechater;
import com.xwkj.donate.service.DonationManager;
import com.xwkj.donate.service.WechaterManager;
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
import java.util.*;

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
    public boolean usingEmail() {
        return config.mail.using;
    }

    @RemoteMethod
    public int getMinMoney() {
        return config.global.min;
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
        if (money < config.global.min) {
            Debug.error("Money should be larger than min.");
            return false;
        }
        donation.setMoney(money);
        donationDao.update(donation);
        return true;
    }

    @RemoteMethod
    @Transactional
    public JSAPIResult pay(String did) {
        Donation donation = donationDao.get(did);
        if (donation == null) {
            Debug.error("Cannot find any donation object from session.");
            return null;
        }

        String openid = donation.getWechater().getOpenid();

        String tradeNo = donation.getCreateAt() + MathTool.getRandomStr(4);
        String body = config.text.tradeName + donation.getMoney() / 100.0 + "元";

        JSAPIResult result = wechatComponent.createJSAPI(openid, tradeNo, body, donation.getMoney(),
                WebContextFactory.get().getHttpServletRequest(), WebContextFactory.get().getHttpServletResponse());

        // Save nonce, trade no and wechater to persitent store.
        donation.setNonce(result.getNonceStr());
        donation.setTradeNo(tradeNo);
        donationDao.update(donation);

        return result;
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
        // Update pay related attributes.
        donation.setPayed(true);
        donation.setPayAt(System.currentTimeMillis());
        donation.setTransactionId(transactionId);
        donationDao.update(donation);

        // Send a certificate email to the donater.
        if (!donation.getEmail().equals("")) {
            MengularDocument document = new MengularDocument(config.rootPath, 0, "mail/certificate.html", null);
            document.setValue("httpProtocol", config.global.httpProtocol);
            document.setValue("domain", config.global.domain);
            document.setValue("tradeNo", donation.getTradeNo());
            document.setValue("name", donation.getName());
            document.setValue("sex", donation.getSex() ? config.text.male : config.text.female);
            document.setValue("money", String.valueOf((donation.getMoney() < 100 ? donation.getMoney() * 1.0 : donation.getMoney()) / 100));
            mailComponent.send(donation.getEmail(), config.text.tradeName, document.getDocument());
        }
        return true;
    }

    @RemoteMethod
    public List<DonationBean> getPayedDonations(HttpSession session) {
        if (!checkAdminSession(session)) {
            return null;
        }
        List<DonationBean> donationBeans = new ArrayList<DonationBean>();
        for (Donation donation : donationDao.findPayed()) {
            donationBeans.add(new DonationBean(donation, true));
        }
        return donationBeans;
    }

}