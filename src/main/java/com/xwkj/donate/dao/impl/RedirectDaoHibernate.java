package com.xwkj.donate.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.donate.dao.RedirectDao;
import com.xwkj.donate.domain.Redirect;
import org.springframework.stereotype.Repository;

@Repository
public class RedirectDaoHibernate extends BaseHibernateDaoSupport<Redirect> implements RedirectDao {

    public RedirectDaoHibernate() {
        super();
        setClass(Redirect.class);
    }

}
