package com.xwkj.donate.controller;

import com.xwkj.donate.controller.common.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/oauth")
public class OAuthController extends BaseController {

    @RequestMapping(value = "/authorize", method = RequestMethod.GET)
    public void authorize(@RequestParam String did, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String [] strings = request.getRequestURL().toString().split("://");
        String base = strings[0] + "://" + strings[1].split("/")[0];

        // Create redirect URI for Wechat OAuth.
        // If auth proxy is null or empty, use OAuthController to receive code directly.
        String redirect_uri = config.wechat.authProxy;
        if (redirect_uri == null || redirect_uri.equals("")) {
            redirect_uri = URLEncoder.encode(base + "/oauth/register", "utf-8");
        }
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + config.wechat.appId
                + "&redirect_uri=" + redirect_uri + "&response_type=code&scope=snsapi_userinfo&state=" + did + "#wechat_redirect";
        response.sendRedirect(url);
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public void registerOpenIdSession(@RequestParam String state, @RequestParam String code,
                                      HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (state == null) {
            response.sendRedirect("/donate/link.html");
            return;
        }
        if (wechaterManager.registerWechatOpenId(code, state)) {
            response.sendRedirect("/donate/pay.html?did=" + state);
        } else {
            response.sendRedirect("/error/oauth.html");
        }
    }

}
