package com.xwkj.donate.service;

import javax.servlet.http.HttpSession;

public interface DonationManager {

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
     * Pay a donation order after receiving the callback from Wechat pay.
     * @param did
     * @return
     */
    boolean pay(String did);

}
