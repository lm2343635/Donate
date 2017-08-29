package com.xwkj.donate.service;

import com.xwkj.donate.bean.DonationBean;
import com.xwkj.donate.bean.JSAPIResult;

import javax.servlet.http.HttpSession;

public interface DonationManager {

    public static final String DONATION_FLAG = "80815ff80e214be0015e214d008c1d00";

    /**
     * Get donation by physical id.
     *
     * @param did
     * @return
     */
    DonationBean get(String did);


    /**
     * Register and create a donation.
     *
     * @param name
     * @param sex
     * @param year
     * @param email
     * @return
     */
    String register(String name, boolean sex, int year, String email);

    /**
     * Set money for a donation.
     *
     * @param did
     * @return
     */
    boolean setMoney(String did, int money);

    /**
     * Pay the donation by Wechat JSAPI.
     *
     * @param session
     * @return
     */
    JSAPIResult pay(HttpSession session);


    /**
     * Get donation by trade no.
     *
     * @param tradeNo
     * @return
     */
    DonationBean getByTradeNo(String tradeNo);

    /**
     * Call this method after paying successfully.
     *
     * @param did
     * @param transactionId
     * @return
     */
    boolean payed(String did, String transactionId);

}
