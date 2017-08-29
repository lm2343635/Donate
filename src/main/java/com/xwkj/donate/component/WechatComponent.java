package com.xwkj.donate.component;

import com.xwkj.common.pay.MD5Util;
import com.xwkj.common.pay.RequestHandler;
import com.xwkj.common.util.Debug;
import com.xwkj.common.util.HttpTool;
import com.xwkj.donate.bean.JSAPIResult;
import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

@Component
public class WechatComponent {

    @Autowired
    private ConfigComponent config;

    public static final int RequestInterval = 300;

    // Token and ticket
    private String token = null;
    private long tokenUpdate = 0;
    private String ticket;
    private long ticketUpdate = 0;

    public void setToken(String token) {
        this.token = token;
    }

    public long getTokenUpdate() {
        return tokenUpdate;
    }

    public void setTokenUpdate(long tokenUpdate) {
        this.tokenUpdate = tokenUpdate;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public long getTicketUpdate() {
        return ticketUpdate;
    }

    public void setTicketUpdate(long ticketUpdate) {
        this.ticketUpdate = ticketUpdate;
    }

    public WechatComponent() {
        super();
    }

    public String getToken() {
        if (token == null) {
            refreshToken();
        }
        if (System.currentTimeMillis() / 1000L - tokenUpdate > RequestInterval) {
            refreshToken();
        }
        return token;
    }

    public String getTicket() {
        if (ticket == null) {
            refreshTicket();
        }
        if (System.currentTimeMillis() / 1000L - ticketUpdate > RequestInterval) {
            refreshTicket();
        }
        return ticket;
    }

    private void refreshToken() {
        String getTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                + config.wechat.appId + "&secret=" + config.wechat.appSecret;
        JSONObject tokenResult = JSONObject.fromObject(HttpTool.httpRequest(getTokenUrl));
        if (tokenResult.get("access_token") == null) {
            return;
        }
        token = tokenResult.getString("access_token");
        tokenUpdate = System.currentTimeMillis() / 1000L;
    }

    private void refreshTicket() {
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="
                + getToken() + "&type=jsapi";
        JSONObject object = JSONObject.fromObject(HttpTool.httpRequest(requestUrl));
        ticket = object.getString("ticket");
        ticketUpdate = System.currentTimeMillis() / 1000L;
    }

    public JSONObject getUserInfoByCode(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + config.wechat.appId + "&secret="
                + config.wechat.appSecret + "&code=" + code + "&grant_type=authorization_code";
        JSONObject result = JSONObject.fromObject(HttpTool.httpRequest(url));
        String openid = result.getString("openid");
        if (openid == null) {
            Debug.error("Get open id by code failed, open id is null.");
            return null;
        }
        JSONObject userInfo = getUserInfo(openid);
        // If user has not subscribed official account, try to get simple user info by SNS user info API.
        if (userInfo.getInt("subscribe") == 0) {
            String SNSAccessToken = result.getString("access_token");
            if (SNSAccessToken != null && !SNSAccessToken.equals("")) {
                userInfo = getSNSUserInfo(openid, SNSAccessToken);
            }
        }
        return userInfo;
    }

    // Get user info with cgi-bin API, get full user info for subscribed Wechat user.
    private JSONObject getUserInfo(String openid) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="
                + getToken() + "&openid=" + openid + "&lang=zh_CN";
        return JSONObject.fromObject(HttpTool.httpRequest(url));
    }

    // Get user info with SNS user info API, get simple user info.
    private JSONObject getSNSUserInfo(String openid, String SNSAccessToken) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + SNSAccessToken + "&openid=" + openid + "&lang=zh_CN";
        return JSONObject.fromObject(HttpTool.httpRequest(url));
    }

    public JSAPIResult createJSAPI(String openid, String tradeNo, String body, int totalFee, HttpServletRequest request, HttpServletResponse response) {
        String tradeType = "JSAPI";
        String mchId = config.wechat.partnerId;
        String nonce = UUID.randomUUID().toString().substring(0, 8);
        String spbillCreateIp = request.getRemoteAddr();

        // Callback url after paying successfully.
        String notifyURL = config.global.httpProtocol + "://" + config.global.domain + "/wechat/payed";

        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", config.wechat.appId);
        packageParams.put("mch_id", mchId);
        packageParams.put("nonce_str", nonce);
        packageParams.put("body", body);
        packageParams.put("openid", openid);
        packageParams.put("out_trade_no", tradeNo);
        packageParams.put("total_fee", String.valueOf(totalFee));
        packageParams.put("spbill_create_ip", spbillCreateIp);
        packageParams.put("notify_url", notifyURL);
        packageParams.put("trade_type", tradeType);
        RequestHandler reqHandler = new RequestHandler(request, response);
        reqHandler.init(config.wechat.appId, config.wechat.appSecret, config.wechat.partnerKey);
        String sign = reqHandler.createSign(packageParams);
        String xml = "<xml>" +
                "<appid>" + config.wechat.appId + "</appid>" +
                "<mch_id>" + mchId + "</mch_id>" +
                "<nonce_str>" + nonce + "</nonce_str>" +
                "<sign>" + sign + "</sign>" +
                "<body><![CDATA[" + body + "]]></body>" +
                "<openid>" + openid + "</openid>" +
                "<out_trade_no>" + tradeNo + "</out_trade_no>" +
                "<total_fee>" + totalFee + "</total_fee>" +
                "<spbill_create_ip>" + spbillCreateIp + "</spbill_create_ip>" +
                "<notify_url>" + notifyURL + "</notify_url>" +
                "<trade_type>" + tradeType + "</trade_type>" +
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
        // Create timestamp
        String timestamp = String.valueOf(System.currentTimeMillis());
        // Create sign for JSAPI pay.
        String str = "appId=" + config.wechat.appId
                + "&nonceStr=" + nonce
                + "&package=prepay_id=" + prepayId
                + "&signType=MD5"
                + "&timeStamp=" + timestamp
                + "&key=" + config.wechat.partnerKey;
        String paySign = MD5Util.MD5Encode(str, "UTF-8");
        return new JSAPIResult(nonce, prepayId, timestamp, paySign);
    }

}
