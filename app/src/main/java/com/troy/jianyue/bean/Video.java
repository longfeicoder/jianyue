package com.troy.jianyue.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by chenlongfei on 15/5/9.
 */
@AVClassName("Video")
public class Video extends AVObject {
    private String url;
    private String summary;

    public String getUrl() {
        return getString("url");
    }

    public String getSummary() {
        return getString("summary");
    }
}