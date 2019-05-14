package com.example.demo.data;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class JsonMessage {
    private String toUrl;
    private int status;
    private String message;
    private JSONObject data;
}
