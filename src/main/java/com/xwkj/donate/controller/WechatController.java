package com.xwkj.donate.controller;

import com.xwkj.donate.controller.common.BaseController;
import com.xwkj.donate.service.DonationManager;
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
@RequestMapping("/wechat")
public class WechatController extends BaseController {

    @RequestMapping(value = "/pay", method = RequestMethod.GET)
    public void authorize(@RequestParam String did, @RequestParam int money, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!donationManager.setMoney(did, money)) {
            // Set money failed.

            return;
        }
        // Save donation id to session.
        request.getSession().setAttribute(DonationManager.DONATION_FLAG, did);
        // Get user info.
        String redirect = URLEncoder.encode("/pay.html?did=" + did, "utf-8");
        response.sendRedirect("/oauth/authorize?redirect=" + redirect);
    }

}
