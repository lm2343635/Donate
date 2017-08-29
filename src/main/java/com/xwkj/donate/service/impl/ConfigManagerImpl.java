package com.xwkj.donate.service.impl;

import com.xwkj.donate.service.ConfigManager;
import com.xwkj.donate.service.common.ManagerTemplate;
import net.sf.json.JSONObject;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RemoteProxy(name = "ConfigManager")
public class ConfigManagerImpl extends ManagerTemplate implements ConfigManager {

    @RemoteMethod
    public boolean refreshConfig(HttpSession session) {
        if (!checkAdminSession(session)) {
            return false;
        }
        config.load();
        return true;
    }

    @RemoteMethod
    public JSONObject getConfigObject(HttpSession session) {
        if (!checkAdminSession(session)) {
            return null;
        }
        return config.configTool.getJSONConfig();
    }

    @RemoteMethod
    public boolean saveConfig(String jsonString, HttpSession session) {
        if (!checkAdminSession(session)) {
            return false;
        }
        config.configTool.setJSONConfig(JSONObject.fromObject(jsonString));
        config.configTool.writeObject();
        return true;
    }

}
