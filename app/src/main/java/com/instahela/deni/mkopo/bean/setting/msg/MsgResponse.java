package com.instahela.deni.mkopo.bean.setting.msg;

import java.util.ArrayList;

public class MsgResponse {
    private ArrayList<Msg> items;

    public ArrayList<Msg> getItems() {
        return items;
    }

    public void setItems(ArrayList<Msg> items) {
        this.items = items;
    }

    public static class Msg {
        private String content;
        private String ctime;
        private int id;
        private int isRead;
        private String title;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIsRead() {
            return isRead;
        }

        public void setIsRead(int isRead) {
            this.isRead = isRead;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
