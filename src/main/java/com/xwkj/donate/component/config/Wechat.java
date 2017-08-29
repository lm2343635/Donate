package com.xwkj.donate.component.config;

import net.sf.json.JSONObject;

public class Wechat {

    // Wechat offical account.
    public String appId;
    public String appSecret;

    // Wechat pay.
    public String partnerId;
    public String partnerKey;

    // If auth proxy is null or empty, use OAuthController to receive code directly.
    public String authProxy;

    public Wechat(JSONObject object) {
        this.appId = object.getString("appId");
        this.appSecret = object.getString("appSecret");
        this.partnerId = object.getString("partnerId");
        this.partnerKey = object.getString("partnerKey");
        this.authProxy = object.getString("authProxy");
    }

}
