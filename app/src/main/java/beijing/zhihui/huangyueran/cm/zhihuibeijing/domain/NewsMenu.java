package beijing.zhihui.huangyueran.cm.zhihuibeijing.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by huangyueran on 2016/11/2.
 *
 * @category 新闻分类信息封装实体
 * 1.遇到{}创建对象
 * 2.遇到[]创建集合
 * 3.字段名称一致
 */
public class NewsMenu implements Serializable {
    private int retcode;
    private ArrayList<Integer> extend;
    private ArrayList<NewsMenuData> data;


    /**
     * 侧边栏菜单对象
     */
    public class NewsMenuData implements Serializable {
        private int id;
        private String title;
        private int type;

        private ArrayList<NewsTabData> children;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public ArrayList<NewsTabData> getChildren() {
            return children;
        }

        public void setChildren(ArrayList<NewsTabData> children) {
            this.children = children;
        }

        @Override
        public String toString() {
            return "NewsMenuData{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", type=" + type +
                    ", children=" + children +
                    '}';
        }
    }

    /**
     * 页签名对象
     */
    public class NewsTabData implements Serializable {
        private int id;
        private String title;
        private int type;
        private String url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "NewsTabData{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", type=" + type +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public ArrayList<Integer> getExtend() {
        return extend;
    }

    public void setExtend(ArrayList<Integer> extend) {
        this.extend = extend;
    }

    public ArrayList<NewsMenuData> getData() {
        return data;
    }

    public void setData(ArrayList<NewsMenuData> data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "NewsMenu{" +
                "retcode=" + retcode +
                ", extend=" + extend +
                ", data=" + data +
                '}';
    }
}
