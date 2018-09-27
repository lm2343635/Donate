package com.xwkj.donate.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.donate.dao.DonationDao;
import com.xwkj.donate.domain.Donation;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
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

    public List<Donation> findPayed() {
        String hql = "from Donation where payed = true order by payAt desc";
        return (List<Donation>) getHibernateTemplate().find(hql);
    }

    public int getPayedCount() {
        final String hql = "select count(*) from Donation where payed = true";
        return getHibernateTemplate().execute(new HibernateCallback<Long>() {
            public Long doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hql);
                return (Long) query.uniqueResult();
            }
        }).intValue();
    }

    public List<Donation> findByPage(int offset, int pageSize) {
        final String hql = "from Donation where payed = true order by createAt desc";
        return findByPage(hql, offset, pageSize);
    }
}
