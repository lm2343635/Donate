package com.xwkj.donate.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.donate.dao.WechaterDao;
import com.xwkj.donate.domain.Wechater;
import org.springframework.stereotype.Repository;

@Repository
public class WechaterDaoHibernate extends BaseHibernateDaoSupport<Wechater> implements WechaterDao {

    public WechaterDaoHibernate() {
        super();
        setClass(Wechater.class);
    }

}
