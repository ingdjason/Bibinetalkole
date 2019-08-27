package com.bibinetalkole.app.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;


public class Shows implements Serializable {

    public String postTitle;
    public String image;

    public String getPostTitle() {
        return postTitle;
    }

    public String getImage() {
        return image;
    }

    public Shows (JSONObject jsonObject) throws JSONException {
        this.postTitle = jsonObject.getJSONObject("title").getString("rendered");
        this.image = jsonObject.getJSONObject("_embedded").getJSONArray("wp:featuredmedia").getJSONObject(0).getJSONObject("media_details").getJSONObject("sizes").getJSONObject("medium").getString("source_url");
    }

    public static ArrayList<Shows> fromJSONArray(JSONArray array) {
        ArrayList<Shows> results = new ArrayList<>();

        for (int x = 0; x < array.length(); x++){
            try {
                results.add (new Shows(array.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
