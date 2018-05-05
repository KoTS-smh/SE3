package com.sec.server.model;

/**
 * 与前端主页中的picture_card 结构对应
 * name
 * id
 * url
 * description
 * views 待完成
 * comments 待完成
 */
public class Picture_CardModel {
    private String name;
    private long id;
    private String url;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
