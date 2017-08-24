package com.xwkj.donate.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.donate.dao.WechaterDao;
import com.xwkj.donate.domain.Wechater;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WechaterDaoHibernate extends BaseHibernateDaoSupport<Wechater> implements WechaterDao {

    public WechaterDaoHibernate() {
        super();
        setClass(Wechater.class);
    }

    public Wechater getByOpenId(String openid) {
        String hql = "from Wechater where openid = ?";
        List<Wechater> wechaters = (List<Wechater>) getHibernateTemplate().find(hql, openid);
        if (wechaters.size() == 0) {
            return null;
        }
        return wechaters.get(0);
    }

    public List<Wechater> findByNickname(String keyword) {
        String hql = "from Wechater where nickname like ?";
        return (List<Wechater>) getHibernateTemplate().find(hql, "%" + keyword + "%");
    }

}
