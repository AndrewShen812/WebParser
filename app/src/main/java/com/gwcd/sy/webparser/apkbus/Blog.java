package com.gwcd.sy.webparser.apkbus;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Lenovo on 2017/8/18.
 */

public class Blog {
    String userHeadUrl;
    String userName;
    String title;
    String resume;
    String time;
    String readCount;
    String commentCount;
    String favorCount;
    String url;
    List<String> tags;

    @Override
    public String toString() {
        String[] tagsArr = new String[0];
        if (tags != null && !tags.isEmpty()) {
            tagsArr = new String[tags.size()];
            for (int i = 0; i < tags.size(); i++) {
                tagsArr[i] = tags.get(i);
            }
        }
        return "Blog{" +
                "userHeadUrl='" + userHeadUrl + '\'' +
                ", userName='" + userName + '\'' +
                ", title='" + title + '\'' +
                ", resume='" + resume + '\'' +
                ", time='" + time + '\'' +
                ", readCount='" + readCount + '\'' +
                ", commentCount='" + commentCount + '\'' +
                ", favorCount='" + favorCount + '\'' +
                ", url='" + url + '\'' +
                ", tags=" + Arrays.toString(tagsArr) +
                '}';
    }
}
