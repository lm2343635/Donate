package com.xwkj.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.dao.WechaterDao;
import com.xwkj.donate.domain.Wechater;

public class WechaterDaoHibernate extends BaseHibernateDaoSupport<Wechater> implements WechaterDao {

    public WechaterDaoHibernate() {
        super();
        setClass(Wechater.class);
    }
    
}
