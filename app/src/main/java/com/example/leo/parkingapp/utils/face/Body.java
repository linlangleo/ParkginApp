package com.example.leo.parkingapp.utils.face;

/**
 * Created by terry on 2018/2/24.
 */

public class Body {
    public int type;

    public String content_1;
    public String content_2;

    public String image_url_1;
    public String image_url_2;

    public String ToString() {
        if (type == 1) {
            return "{\"type\":1,\"content_1\":\"" + content_1 + "\",\"content_2\":\"" + content_2 + "\"}";
        }

        return "{\"type\":0,\"image_url_1\":\"" + image_url_1 + "\",\"image_url_2\":\"" + image_url_2 + "\"}";
    }
}