package com.xwkj.donate.service.impl;

import com.xwkj.donate.domain.Redirect;
import com.xwkj.donate.service.RedirectManager;
import com.xwkj.donate.service.common.ManagerTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RedirectManagerImpl extends ManagerTemplate implements RedirectManager {

    @Transactional
    public String push(String path) {
        Redirect redirect = new Redirect();
        redirect.setPath(path);
        redirect.setCreateAt(System.currentTimeMillis());
        return redirectDao.save(redirect);
    }

    @Transactional
    public String pop(String state) {
        Redirect redirect = redirectDao.get(state);
        if (redirect == null) {
            return null;
        }
        String path = redirect.getPath();
        redirectDao.delete(redirect);
        return path;
    }

}
