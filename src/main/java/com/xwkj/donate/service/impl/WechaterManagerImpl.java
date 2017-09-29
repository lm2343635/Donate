package com.xwkj.donate.service.impl;

import com.xwkj.common.util.Debug;
import com.xwkj.common.util.SHA1;
import com.xwkj.donate.bean.WechaterBean;
import com.xwkj.donate.domain.Donation;
import com.xwkj.donate.domain.Wechater;
import com.xwkj.donate.service.TokenManager;
import com.xwkj.donate.service.WechaterManager;
import com.xwkj.donate.service.common.ManagerTemplate;
import net.sf.json.JSONObject;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
@RemoteProxy(name = "WechaterManager")
public class WechaterManagerImpl extends ManagerTemplate implements WechaterManager {

    @Autowired
    private TokenManager tokenManager;

    @RemoteMethod
    @Transactional
    public boolean registerWechatOpenId(String code, String did) {
        JSONObject userInfo = wechatComponent.getUserInfoByCode(code);
        if (userInfo == null) {
            Debug.error("user info is null");
            return false;
        }
        String openid = userInfo.getString("openid");
        Wechater wechater = wechaterDao.getByOpenId(openid);
        if (wechater == null) {
            wechater = new Wechater(userInfo);
            wechater.setUpdateAt(System.currentTimeMillis());
            wechaterDao.save(wechater);
        } else {
            wechater.update(userInfo);
            wechater.setUpdateAt(System.currentTimeMillis());
            wechaterDao.update(wechater);
        }
        Donation donation = donationDao.get(did);
        if (donation == null) {
            Debug.error("Cannot find a donation by this did");
            return false;
        }
        donation.setWechater(wechater);
        donationDao.update(donation);
        return true;
    }

    @RemoteMethod
    public Map<String, Object> getJsConfig(String url) {
        final String nonceStr = UUID.randomUUID().toString().substring(0, 8);
        final String timestamp = String.valueOf(System.currentTimeMillis());
        String str = "jsapi_ticket=" + tokenManager.getTicket() + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;
        final String signature = new SHA1().getDigestOfString(str.getBytes());
        return new HashMap<String, Object>() {{
            put("appId", config.wechat.appId);
            put("nonceStr", nonceStr);
            put("timestamp", timestamp);
            put("signature", signature);
        }};
    }

    @RemoteMethod
    public List<WechaterBean> searchByNickname(String nickname) {
        String keyword = "";
        try {
            keyword = Base64.getEncoder().encodeToString(nickname.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<WechaterBean> wechaterBeans = new ArrayList<WechaterBean>();
        for (Wechater wechater : wechaterDao.findByNickname(keyword)) {
            wechaterBeans.add(new WechaterBean(wechater));
        }
        return wechaterBeans;
    }

    @RemoteMethod
    public WechaterBean getWechater(String wid) {
        Wechater wechater = wechaterDao.get(wid);
        if (wechater == null) {
            Debug.error("wechater is null, cannot find a wechater by this wid.");
        }
        return new WechaterBean(wechater);
    }

}
