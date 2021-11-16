package com.instahela.deni.mkopo.bean;

import android.text.TextUtils;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.JsonUtils;
import com.blankj.utilcode.util.StringUtils;

import org.json.JSONObject;

public class BaseResponseBean {

    private Object body;

    private Head head;

    public Object getBody() {
        return body;
    }

    public String getBodyStr(){
        return GsonUtils.toJson(body);
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public class Head {
        private String code;

        private String msg;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public boolean isRequestSuccess(){
        return head != null && TextUtils.equals(head.code, "200") && TextUtils.equals(head.msg, "SUCCESS");
    }
}
