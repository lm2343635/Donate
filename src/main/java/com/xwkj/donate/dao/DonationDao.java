package com.xwkj.donate.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.donate.domain.Donation;

import java.util.List;

public interface DonationDao extends BaseDao<Donation> {

    /**
     * Get a donation by trade no.
     *
     * @param tradeNo
     * @return
     */
    Donation getByTradeNo(String tradeNo);

    /**
     * Find all peyed donations.
     *
     * @return
     */
    List<Donation> findPayed();

}
