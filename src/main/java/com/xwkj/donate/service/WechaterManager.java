package com.xwkj.donate.service;

import com.xwkj.donate.bean.JSAPIResult;
import com.xwkj.donate.bean.WechaterBean;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface WechaterManager {

    /**
     * Register wechat's openId
     *
     * @param code
     * @param did
     * @return
     */
    boolean registerWechatOpenId(String code, String did);

    /**
     * Get JS API config
     *
     * @param url
     * @return
     */
    Map<String, Object> getJsConfig(String url);

    /**
     * Search wechaters by thier nickname.
     *
     * @param nickname
     * @return
     */
    List<WechaterBean> searchByNickname(String nickname);

    /**
     * Get wechat user info by wechater id.
     *
     * @param wid
     * @return
     */
    WechaterBean getWechater(String wid);

}
