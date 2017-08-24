package com.xwkj.donate.service;

import javax.servlet.http.HttpSession;

public interface DonationManager {

    /**
     * Create a donation order without paying.
     *
     * @param name
     * @param email
     * @param money
     * @param session
     * @return
     */
    String createDonation(String name, String email, int money, HttpSession session);

    /**
     * Pay a donation order after receiving the callback from Wechat pay.
     * @param did
     * @return
     */
    boolean pay(String did);

}
