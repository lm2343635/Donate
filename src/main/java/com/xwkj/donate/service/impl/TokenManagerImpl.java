package com.xwkj.donate.service.impl;

import com.xwkj.common.util.HttpTool;
import com.xwkj.donate.domain.Token;
import com.xwkj.donate.service.TokenManager;
import com.xwkj.donate.service.common.ManagerTemplate;
import net.sf.json.JSONObject;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RemoteProxy
public class TokenManagerImpl extends ManagerTemplate implements TokenManager {

    public static final int RequestInterval = 300;

    @Transactional
    public Token getTokenEntity() {
        Token token = tokenDao.getSignle();
        if (token == null) {
            token = new Token();
            token.setAccessToken(refreshToken());
            token.setTicket(refreshTicket(token.getAccessToken()));
            token.setTokenUpdate(System.currentTimeMillis() / 1000L);
            token.setTicketUpdate(System.currentTimeMillis() / 1000L);
            tokenDao.save(token);
        }
        return token;
    }

    @RemoteMethod
    @Transactional
    public String getAccessToken() {
        Token token = getTokenEntity();
        if (System.currentTimeMillis() / 1000L - token.getTokenUpdate() > RequestInterval) {
            token.setAccessToken(refreshToken());
            token.setTokenUpdate(System.currentTimeMillis() / 1000L);
            tokenDao.update(token);
        }
        return token.getAccessToken();
    }

    @Transactional
    public String getTicket() {
        Token token = getTokenEntity();
        if (System.currentTimeMillis() / 1000L - token.getTicketUpdate() > RequestInterval) {
            token.setTicket(refreshTicket(getAccessToken()));
            token.setTicketUpdate(System.currentTimeMillis() / 1000L);
            tokenDao.update(token);
        }
        return token.getTicket();
    }

    private String refreshToken() {
        String getTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                + config.wechat.appId + "&secret=" + config.wechat.appSecret;
        JSONObject object = JSONObject.fromObject(HttpTool.httpRequest(getTokenUrl));
        if (object.get("access_token") == null) {
            return null;
        }
        return object.getString("access_token");
    }

    private String refreshTicket(String accessToken) {
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="
                + accessToken + "&type=jsapi";
        JSONObject object = JSONObject.fromObject(HttpTool.httpRequest(requestUrl));
        if (object.get("ticket") == null) {
            return null;
        }
        return object.getString("ticket");
    }

}
