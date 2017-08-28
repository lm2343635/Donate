package com.xwkj.donate.service;

import com.xwkj.donate.bean.DonationBean;

import javax.servlet.http.HttpSession;

public interface DonationManager {

    public static final String DONATION_FLAG = "80815ff80e214be0015e214d008c1d00";

    /**
     *
     * @param did
     * @return
     */
    DonationBean get(String did);

    /**
     * Register and create a donation.
     * @param name
     * @param sex
     * @param year
     * @param email
     * @return
     */
    String register(String name, boolean sex, int year, String email);

    /**
     *
     * @param did
     * @return
     */
    boolean setMoney(String did, int money);

    boolean payed(String did);

}
