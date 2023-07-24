package com.example.accountbook_db_php;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class getDATA_Request extends StringRequest {

    final static private String URL = "";
    private Map<String,String> map;

    public getDATA_Request(int num, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);
        map = new HashMap<>();
        map.put("num",num+"");
    }
    protected Map<String,String> getParams() throws AuthFailureError{
        return map;
    }
}
