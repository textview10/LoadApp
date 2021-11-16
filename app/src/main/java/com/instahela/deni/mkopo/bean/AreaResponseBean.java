package com.instahela.deni.mkopo.bean;

import java.util.List;

public class AreaResponseBean {

    public List<Province> provinces;

    //省
    public static class Province {
        public String provinceName;
        public List<State> states;
    }

    //州
    public static class State {
        public String stateName;
        public List<String> areas;   //区
    }



}
