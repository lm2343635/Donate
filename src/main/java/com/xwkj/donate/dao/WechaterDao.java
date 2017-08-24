package com.xwkj.donate.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.donate.domain.Wechater;

import java.util.List;

public interface WechaterDao extends BaseDao<Wechater> {

    /**
     * Find a wechater by his openid
     *
     * @param openid
     * @return
     */
    Wechater getByOpenId(String openid);

    /**
     * Find wechaters by nickname keyword.
     *
     * @param keyword
     * @return
     */
    List<Wechater> findByNickname(String keyword);

}
