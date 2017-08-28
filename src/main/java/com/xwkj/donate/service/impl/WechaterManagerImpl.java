package com.xwkj.donate.service.impl;

import com.xwkj.common.pay.MD5Util;
import com.xwkj.common.pay.RequestHandler;
import com.xwkj.common.util.Debug;
import com.xwkj.common.util.HttpTool;
import com.xwkj.common.util.MathTool;
import com.xwkj.common.util.SHA1;
import com.xwkj.donate.bean.JSAPIResult;
import com.xwkj.donate.bean.WechaterBean;
import com.xwkj.donate.domain.Donation;
import com.xwkj.donate.domain.Wechater;
import com.xwkj.donate.service.DonationManager;
import com.xwkj.donate.service.WechaterManager;
import com.xwkj.donate.service.common.ManagerTemplate;
import net.sf.json.JSONObject;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
@RemoteProxy(name = "WechaterManager")
public class WechaterManagerImpl extends ManagerTemplate implements WechaterManager {

    @RemoteMethod
    @Transactional
    public boolean registerWechatOpenId(String code, HttpSession session) {
        JSONObject userInfo = wechatComponent.getUserInfoByCode(code);
        if (userInfo == null) {
            Debug.error("user info is nulls");
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
        session.setAttribute(WECHAT_USER_OPEN_ID, openid);
        return true;
    }

    @RemoteMethod
    public boolean checkOpenIdSession(HttpSession session) {
        return session.getAttribute(WECHAT_USER_OPEN_ID) != null;
    }

    @RemoteMethod
    public Map<String, Object> getJsConfig(String url) {
        final String nonceStr = UUID.randomUUID().toString().substring(0, 8);
        final String timestamp = String.valueOf(System.currentTimeMillis());
        String str = "jsapi_ticket=" + wechatComponent.getTicket() + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;
        final String signature = new SHA1().getDigestOfString(str.getBytes());
        return new HashMap<String, Object>() {{
            put("appId", wechatComponent.getAppId());
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

    @RemoteMethod
    @Transactional
    public JSAPIResult createJSAPI(HttpSession session) {
        String did = (String) session.getAttribute(DonationManager.DONATION_FLAG);
        Donation donation = donationDao.get(did);
        if (donation == null) {
            Debug.error("Cannot find any donation object from session.");
            return null;
        }
        String openid = (String) session.getAttribute(WechaterManager.WECHAT_USER_OPEN_ID);
        if (openid == null) {
            Debug.error("Cannot find openid from session.");
            return null;
        }
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        HttpServletResponse response = WebContextFactory.get().getHttpServletResponse();
        String trade_type = "JSAPI";
        String mch_id = wechatComponent.getPartnerId();
        String nonce_str = UUID.randomUUID().toString().substring(0, 8);
        String body = "龙泉中学110周年校庆" + donation.getMoney() / 100.0 + "元";
        String out_trade_no = donation.getCreateAt() + MathTool.getRandomStr(4);
        int total_fee = donation.getMoney();
        //订单生成的机器 IP
        String spbill_create_ip = request.getRemoteAddr();
        //这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
        String notify_url = "http://" + wechatComponent.getDomain() + "/wechat/payed";
        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid",wechatComponent.getAppId());
        packageParams.put("mch_id", mch_id);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", body);
        packageParams.put("openid", openid);
        packageParams.put("out_trade_no", out_trade_no);
        packageParams.put("total_fee", String.valueOf(total_fee));
        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("notify_url", notify_url);
        packageParams.put("trade_type", trade_type);
        RequestHandler reqHandler = new RequestHandler(request, response);
        reqHandler.init(wechatComponent.getAppId(), wechatComponent.getAppSecret(), wechatComponent.getPartnerKey());
        String sign = reqHandler.createSign(packageParams);
        String xml = "<xml>" +
                "<appid>" + wechatComponent.getAppId() + "</appid>" +
                "<mch_id>" + mch_id + "</mch_id>" +
                "<nonce_str>" + nonce_str + "</nonce_str>" +
                "<sign>" + sign + "</sign>" +
                "<body><![CDATA[" + body + "]]></body>" +
                "<openid>" + openid + "</openid>" +
                "<out_trade_no>" + out_trade_no + "</out_trade_no>" +
                "<total_fee>" + total_fee + "</total_fee>" +
                "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>" +
                "<notify_url>" + notify_url + "</notify_url>" +
                "<trade_type>" + trade_type + "</trade_type>" +
                "</xml>";
        String result = HttpTool.postWithString("https://api.mch.weixin.qq.com/pay/unifiedorder", xml);
        Document document = null;
        try {
            result = new String(result.getBytes("gbk"), "UTF-8");
            document = DocumentHelper.parseText(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (document == null) {
            return null;
        }
        Element root = document.getRootElement();
        if (root.elementText("return_code").equals("FAIL")) {
            return null;
        }
        String prepayId = root.elementText("prepay_id");

        // Save nonce to persitent store.
        donation.setNonce(nonce_str);
        donation.setTradeNo(out_trade_no);
        donationDao.update(donation);

        // Create timestamp
        String timestamp = String.valueOf(System.currentTimeMillis());
        //生成签名
        String str = "appId=" + wechatComponent.getAppId()
                + "&nonceStr=" + nonce_str
                + "&package=prepay_id=" + prepayId
                + "&signType=MD5"
                + "&timeStamp=" + timestamp
                + "&key=" + wechatComponent.getPartnerKey();
        System.out.println(str);
        String paySign = MD5Util.MD5Encode(str, "UTF-8");
        return new JSAPIResult(nonce_str, prepayId, timestamp, paySign);
    }

}
