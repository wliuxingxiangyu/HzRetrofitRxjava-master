package com.mi.hz.hzretrofit.rxjavademo;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
public class UserDAO {

    public User parseJO(JSONObject json) {
        User user = new User();
        user.setUid(json.optInt("userid"));
        user.setName(json.optString("name"));
        Log.d("hz--","UserDAO.java--user.toString()="+user.toString()+
                ",json.optInt(uid)="+json.optInt("uid")+",,json.optString(name)="+json.optString("name"));
        return user;
    }

    public User parseStr(String result) {
        try {
            JSONObject userJson = new JSONObject(result).getJSONObject("user");
            Log.d("hz--","UserDAO.java--userJson.toString()="+userJson.toString());
            return parseJO(userJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new User();
    }
}
