package com.xwkj.donate.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.donate.dao.TokenDao;
import com.xwkj.donate.domain.Token;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TokenDaoHibernate extends BaseHibernateDaoSupport<Token> implements TokenDao {

    public TokenDaoHibernate() {
        super();
        setClass(Token.class);
    }

    public Token getSignle() {
        List<Token> tokens = findAll();
        if (tokens.size() == 0) {
            return null;
        }
        return tokens.get(0);
    }

}
