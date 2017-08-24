package com.xwkj.donate.service;

import com.xwkj.donate.bean.WechaterBean;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface WechaterManager {

    public static final String WECHAT_USER_OPEN_ID = "8897e01543b0297e9e795431c7490030";

    /**
     * Register wechat's openId
     *
     * @param code
     * @param session
     * @return
     */
    boolean registerWechatOpenId(String code, HttpSession session);

    /**
     * Check openid session.
     *
     * @param session
     * @return
     */
    boolean checkOpenIdSession(HttpSession session);

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
