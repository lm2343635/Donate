package com.xwkj.donate.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.donate.dao.DonationDao;
import com.xwkj.donate.domain.Donation;
import org.springframework.stereotype.Repository;

@Repository
public class DonationDaoHibernate extends BaseHibernateDaoSupport<Donation> implements DonationDao {

    public DonationDaoHibernate() {
        super();
        setClass(Donation.class);
    }

}
