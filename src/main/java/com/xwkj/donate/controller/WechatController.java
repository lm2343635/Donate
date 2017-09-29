package com.xwkj.donate.controller;

import com.xwkj.donate.bean.DonationBean;
import com.xwkj.donate.controller.common.BaseController;
import com.xwkj.donate.service.DonationManager;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.InputSource;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wechat")
public class WechatController extends BaseController {

    @RequestMapping(value = "/pay", method = RequestMethod.GET)
    public void pay(@RequestParam String did, @RequestParam int money, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!donationManager.setMoney(did, money)) {
            // Set money failed.
            response.sendRedirect("/donate/link.html");
            return;
        }
        response.sendRedirect("/oauth/authorize?did=" + did);
    }

    @RequestMapping(value = "/payed", method = RequestMethod.POST)
    public void payed(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String string = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        while ((line = reader.readLine()) != null) {
            string += line;
        }
        Map<String, String> result = parseResultXML(string);
        if (result == null) {
            response.getWriter().print("<xml><return_code>FAIL</return_code></xml>");
            return;
        }

        String nonce_str = result.get("nonce_str");
        String return_code = result.get("return_code");
        String result_code = result.get("result_code");
        String out_trade_no = result.get("out_trade_no");
        String transaction_id = result.get("transaction_id");
        String time_end = result.get("time_end");

        DonationBean donation = donationManager.getByTradeNo(out_trade_no);
        if (donation.isPayed()) {
            response.getWriter().print("<xml><return_code>SUCCESS</return_code></xml>");
            return;
        }

        // Cannot find the donation order.
        if (donation == null) {
            System.out.println("Cannot find the donation order.");
            response.getWriter().print("<xml><return_code>FAIL</return_code></xml>");
            return;
        }
        // Code contains failed info.
        if (return_code.equals("FAIL") || result_code.equals("FAIL")) {
            System.out.println("Code contains failed info.");
            response.getWriter().print("<xml><return_code>FAIL</return_code></xml>");
            return;
        }
        // Nonce string is not same with which in the donation saved in persistent store.
        if (!donation.getNonce().equals(nonce_str)) {
            System.out.println("Nonce string is not same with which in the donation saved in persistent store.");
            response.getWriter().print("<xml><return_code>FAIL</return_code></xml>");
            return;
        }

        if (!donationManager.payed(donation.getDid(), transaction_id)) {
            System.out.println("Save payed flag failed.");
            response.getWriter().print("<xml><return_code>FAIL</return_code></xml>");
            return;
        }

        // Return success info.
        response.getWriter().print("<xml><return_code>SUCCESS</return_code></xml>");
    }

    private static Map parseResultXML(String xml) {
        Map retMap = new HashMap();
        try {
            StringReader read = new StringReader(xml);
            InputSource source = new InputSource(read);
            SAXBuilder sb = new SAXBuilder();
            Document doc = (Document) sb.build(source);
            Element root = doc.getRootElement();
            List<Element> es = root.getChildren();
            if (es != null && es.size() != 0) {
                for (Element element : es) {
                    retMap.put(element.getName(), element.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retMap;
    }

}
