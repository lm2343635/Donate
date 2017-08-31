package com.xwkj.donate.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.donate.domain.Token;

public interface TokenDao extends BaseDao<Token> {

    /**
     * Get token
     *
     * @return
     */
    Token getSignle();

}
