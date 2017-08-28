package com.xwkj.common.pay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestHandler {

    private String gateUrl;
    private String notifyUrl;
    private String appid;
    private String appkey;
    private String partnerkey;
    private String appsecret;

    private String key;
    private SortedMap<Object, Object> parameters;
    private String Token;
    private String charset;
    private String debugInfo;
    private String last_errcode;

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public String getAppid() {
        return appid;
    }

    public String getAppkey() {
        return appkey;
    }

    public String getPartnerkey() {
        return partnerkey;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public SortedMap<Object, Object> getParameters() {
        return parameters;
    }

    public String getToken() {
        return Token;
    }

    public String getCharset() {
        return charset;
    }

    public String getLast_errcode() {
        return last_errcode;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setGateUrl(String gateUrl) {
        this.gateUrl = gateUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public void setParameters(SortedMap<Object, Object> parameters) {
        this.parameters = parameters;
    }

    public void setToken(String token) {
        Token = token;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setLast_errcode(String last_errcode) {
        this.last_errcode = last_errcode;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    private HttpServletRequest request;

    private HttpServletResponse response;

    public RequestHandler(HttpServletRequest request, HttpServletResponse response) {
        this.last_errcode = "0";
        this.request = request;
        this.response = response;
        //this.charset = "GBK";
        this.charset = "UTF-8";
        this.parameters = new TreeMap<Object, Object>();
        // 验证notify支付订单网关
        notifyUrl = "https://gw.tenpay.com/gateway/simpleverifynotifyid.xml";

    }

    public void init(String app_id, String app_secret, String partner_key) {
        this.last_errcode = "0";
        this.Token = "token_";
        this.debugInfo = "";
        this.appid = app_id;
        this.partnerkey = partner_key;
        this.appsecret = app_secret;
        this.key = partner_key;
    }

    public void init() {

    }

    /**
     * 获取最后错误号
     */
    public String getLasterrCode() {
        return last_errcode;
    }

    /**
     * 获取入口地址,不包含参数值
     */
    public String getGateUrl() {
        return gateUrl;
    }

    /**
     * 获取参数值
     *
     * @param parameter 参数名称
     * @return String
     */
    public String getParameter(String parameter) {
        String s = (String) this.parameters.get(parameter);
        return (null == s) ? "" : s;
    }


    //设置密钥

    public void setKey(String key) {
        this.partnerkey = key;
    }

    //设置微信密钥
    public void setAppKey(String key) {
        this.appkey = key;
    }

    // 特殊字符处理
    public String UrlEncode(String src) throws UnsupportedEncodingException {
        return URLEncoder.encode(src, this.charset).replace("+", "%20");
    }

    // 获取package的签名包
    @SuppressWarnings("rawtypes")
    public String genPackage(SortedMap<String, String> packageParams)
            throws UnsupportedEncodingException {
        String sign = createSign(packageParams);

        StringBuffer sb = new StringBuffer();
        Set<?> es = packageParams.entrySet();
        Iterator<?> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            sb.append(k + "=" + UrlEncode(v) + "&");
        }

        // 去掉最后一个&
        String packageValue = sb.append("sign=" + sign).toString();
//		System.out.println("UrlEncode后 packageValue=" + packageValue);
        return packageValue;
    }

    /**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     */
    @SuppressWarnings("rawtypes")
    public String createSign(SortedMap<String, String> packageParams) {
        StringBuffer sb = new StringBuffer();
        Set<?> es = packageParams.entrySet();
        Iterator<?> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k)
                    && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + this.getKey());
        String sign = MD5Util.MD5Encode(sb.toString(), this.charset).toUpperCase();
        return sign;

    }

    /**
     * 创建package签名
     */
    @SuppressWarnings("rawtypes")
    public boolean createMd5Sign(String signParams) {
        StringBuffer sb = new StringBuffer();
        Set<?> es = this.parameters.entrySet();
        Iterator<?> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (!"sign".equals(k) && null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
            }
        }

        // 算出摘要
        String enc = TenpayUtil.getCharacterEncoding(this.request,
                this.response);
        String sign = MD5Util.MD5Encode(sb.toString(), enc).toLowerCase();

        String tenpaySign = this.getParameter("sign").toLowerCase();

        // debug信息
        this.setDebugInfo(sb.toString() + " => sign:" + sign + " tenpaySign:"
                + tenpaySign);

        return tenpaySign.equals(sign);
    }


    //输出XML
    @SuppressWarnings("rawtypes")
    public String parseXML() {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set<?> es = this.parameters.entrySet();
        Iterator<?> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"appkey".equals(k)) {

                sb.append("<" + k + ">" + getParameter(k) + "</" + k + ">\n");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * 设置debug信息
     */
    protected void setDebugInfo(String debugInfo) {
        this.debugInfo = debugInfo;
    }

    public void setPartnerkey(String partnerkey) {
        this.partnerkey = partnerkey;
    }

    public String getDebugInfo() {
        return debugInfo;
    }

    public String getKey() {
        return partnerkey;
    }

}
