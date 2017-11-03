package com.sinieco.moudle1;

import java.util.List;

/**
 * Created by BaiMeng on 2017/11/3.
 */
public class HomeBean {
    /**
     * tabInfo : {"tabList":[{"id":0,"name":"热门","apiUrl":"http://baobab.kaiyanapp.com/api/v4/discovery/hot"},{"id":1,"name":"分类","apiUrl":"http://baobab.kaiyanapp.com/api/v4/discovery/category"},{"id":2,"name":"作者","apiUrl":"http://baobab.kaiyanapp.com/api/v4/pgcs/all"}],"defaultIdx":0}
     */

    public TabInfoBean tabInfo;

    @Override
    public String toString() {
        return "HomeBean{" +
                "tabInfo=" + tabInfo +
                '}';
    }

    public class TabInfoBean
    {
        /**
         * tabList : [{"id":0,"name":"热门","apiUrl":"http://baobab.kaiyanapp.com/api/v4/discovery/hot"},{"id":1,"name":"分类","apiUrl":"http://baobab.kaiyanapp.com/api/v4/discovery/category"},{"id":2,"name":"作者","apiUrl":"http://baobab.kaiyanapp.com/api/v4/pgcs/all"}]
         * defaultIdx : 0
         */

        public int defaultIdx;
        public List<TabListBean> tabList;

        @Override
        public String toString() {
            return "TabInfoBean{" +
                    "defaultIdx=" + defaultIdx +
                    ", tabList=" + tabList +
                    '}';
        }

        public class TabListBean
        {
            /**
             * id : 0
             * name : 热门
             * apiUrl : http://baobab.kaiyanapp.com/api/v4/discovery/hot
             */

            public int id;
            public String name;
            public String apiUrl;

            @Override
            public String toString() {
                return "TabListBean{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", apiUrl='" + apiUrl + '\'' +
                        '}';
            }
        }
    }
}
