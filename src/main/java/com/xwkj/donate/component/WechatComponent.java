package com.xwkj.donate.component;

import com.xwkj.common.util.Debug;
import com.xwkj.common.util.HttpTool;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class WechatComponent {

    public static final int RequestInterval = 300;

    private String appId;
    private String appSecret;
    private String partnerId;
    private String partnerKey;
    private String authProxy;
    private String domain;

    // Token and ticket
    private String token = null;
    private long tokenUpdate = 0;
    private String ticket;
    private long ticketUpdate = 0;

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public static int getRequestInterval() {
        return RequestInterval;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerKey() {
        return partnerKey;
    }

    public void setPartnerKey(String partnerKey) {
        this.partnerKey = partnerKey;
    }

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

    public void setAuthProxy(String authProxy) {
        this.authProxy = authProxy;
    }

    public String getAuthProxy() {
        return authProxy;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
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
                + appId + "&secret=" + appSecret;
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
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret="
                + appSecret + "&code=" + code + "&grant_type=authorization_code";
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

}
