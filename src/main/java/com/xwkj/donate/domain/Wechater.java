package com.xwkj.donate.domain;

import net.sf.json.JSONObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Base64;

@Entity
@Table(name = "donate_wechater")
public class Wechater {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String wid;

    @Column(nullable = false)
    private Long updateAt;

    @Column(nullable = false)
    private Boolean subscribe;

    @Column(nullable = false)
    private String openid;

    @Column
    private String nickname;

    @Column
    private Integer sex;

    @Column
    private String city;

    @Column
    private String province;

    @Column
    private String country;

    @Column
    private String headimgurl;

    @Column
    private String language;

    @Column
    private Integer subscribe_time;

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Long updateAt) {
        this.updateAt = updateAt;
    }

    public Boolean getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Boolean subscribe) {
        this.subscribe = subscribe;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public Integer getSubscribe_time() {
        return subscribe_time;
    }

    public void setSubscribe_time(Integer subscribe_time) {
        this.subscribe_time = subscribe_time;
    }

    public Wechater() {
        super();
    }

    public Wechater(JSONObject userInfo) {
        this.openid = userInfo.getString("openid");
        setInfo(userInfo);
    }

    public void update(JSONObject userInfo) {
        setInfo(userInfo);
    }

    private void setInfo(JSONObject userInfo) {
        try {
            this.nickname = Base64.getEncoder().encodeToString(userInfo.getString("nickname").getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            this.nickname = "";
        }
        this.sex = userInfo.getInt("sex");

        this.city = userInfo.getString("city");
        this.province = filterOffUtf8Mb4(userInfo.getString("province"));
        this.country = userInfo.getString("country");
        this.headimgurl = userInfo.getString("headimgurl");
        // Get subscribe state, if user has subscribed official account,
        // set subscribe time and user language
        if (userInfo.get("subscribe") != null) {
            this.subscribe = (userInfo.getInt("subscribe") == 1);
            if (this.subscribe) {
                this.subscribe_time = userInfo.getInt("subscribe_time");
                this.language = userInfo.getString("language");
            }
        } else {
            this.subscribe = false;
            this.subscribe_time = null;
        }
    }

    private static String filterOffUtf8Mb4(String text) {
        try {
            byte[] bytes = text.getBytes("utf-8");
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            int i = 0;
            while (i < bytes.length) {
                short b = bytes[i];
                if (b > 0) {
                    buffer.put(bytes[i++]);
                    continue;
                }
                b += 256;
                if ((b ^ 0xC0) >> 4 == 0) {
                    buffer.put(bytes, i, 2);
                    i += 2;
                } else if ((b ^ 0xE0) >> 4 == 0) {
                    buffer.put(bytes, i, 3);
                    i += 3;
                } else if ((b ^ 0xF0) >> 4 == 0) {
                    i += 4;
                }
            }
            buffer.flip();
            return new String(buffer.array(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
