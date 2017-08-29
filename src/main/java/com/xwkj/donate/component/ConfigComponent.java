package com.xwkj.donate.component;

import com.xwkj.common.util.JsonTool;
import com.xwkj.donate.component.config.Global;
import com.xwkj.donate.component.config.Mail;
import com.xwkj.donate.component.config.Wechat;
import org.springframework.stereotype.Component;

@Component
public class ConfigComponent {

    public static final String ConfigPath = "/WEB-INF/config.json";

    public String DefaultIcon = "/static/images/icon.png";
    public String DefaultAvatar = "/static/images/avatar.png";
    public String CategoryIconPath = "/files/category";
    public String AvatarPath = "/files/avatar";
    public String PicturePath = "/files/picture";

    public String rootPath;
    public JsonTool configTool = null;
    public Mail mail;
    public Global global;
    public Wechat wechat;

    public ConfigComponent() {
        rootPath = this.getClass().getClassLoader().getResource("/").getPath().split("WEB-INF")[0];
        load();
    }

    public void load() {
        String pathname = rootPath + ConfigPath;
        configTool = new JsonTool(pathname);
        global = new Global(configTool.getJSONObject("global"));
        mail = new Mail(configTool.getJSONObject("mail"));
        wechat = new Wechat(configTool.getJSONObject("wechat"));
    }

}
