package com.trubitpro.uploadapkplugin.entry;

import com.google.gson.annotations.SerializedName;


public class LarkResult {

   @SerializedName("code")
   private Integer code;
   @SerializedName("data")
   private String data;
    @SerializedName("msg")
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
       return code;
   }

   public void setCode(Integer code) {
       this.code = code;
   }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
