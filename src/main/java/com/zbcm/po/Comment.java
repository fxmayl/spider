package com.zbcm.po;

/**
 * Created by zbcm on 2017-09-21.
 */
public class Comment {
    private String name;
    private String comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return name + " --->>>>> " + comment;
    }
}
