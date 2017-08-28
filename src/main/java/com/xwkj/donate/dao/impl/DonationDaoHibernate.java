package com.xwkj.donate.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.donate.dao.DonationDao;
import com.xwkj.donate.domain.Donation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DonationDaoHibernate extends BaseHibernateDaoSupport<Donation> implements DonationDao {

    public DonationDaoHibernate() {
        super();
        setClass(Donation.class);
    }

    public Donation getByTradeNo(String tradeNo) {
        String hql = "from Donation where tradeNo = ?";
        List<Donation> donations = (List<Donation>) getHibernateTemplate().find(hql, tradeNo);
        if (donations.size() == 0) {
            return null;
        }
        return donations.get(0);
    }

}
