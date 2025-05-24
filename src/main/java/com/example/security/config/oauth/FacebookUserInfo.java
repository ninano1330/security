package com.example.security.config.oauth;

import java.util.Map;

public class FacebookUserInfo implements OAuth2UserInfo{
    private Map<String, Object> attributes; // oauth2User.getAttributes()

    public FacebookUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "facebook";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id"); // google이랑 다름
    }


    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
