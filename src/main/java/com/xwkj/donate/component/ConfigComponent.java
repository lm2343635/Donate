package com.xwkj.donate.component;

import com.xwkj.common.util.JsonTool;
import com.xwkj.donate.component.config.Global;
import com.xwkj.donate.component.config.Mail;
import com.xwkj.donate.component.config.Text;
import com.xwkj.donate.component.config.Wechat;
import org.springframework.stereotype.Component;

@Component
public class ConfigComponent {

    public static final String ConfigPath = "/WEB-INF/config.json";

    public String rootPath;
    public JsonTool configTool = null;
    public Mail mail;
    public Global global;
    public Wechat wechat;
    public Text text;

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
        text = new Text(configTool.getJSONObject("text"));
    }

}
