package com.xwkj.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.dao.DonationDao;
import com.xwkj.donate.domain.Donation;

public class DonationDaoHibernate extends BaseHibernateDaoSupport<Donation> implements DonationDao {

    public DonationDaoHibernate() {
        super();
        setClass(Donation.class);
    }

}
