package com.dy.colony.mvp.model.entity;

/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛Code is far away from bug with the animal protecting
 * 　　　　┃　　　┃    神兽保佑,代码无bug
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * <p>
 * Created by wangzhenxiong on 2019-10-06.
 */
public class UpdateFileMessage {


    private ResultBean result;
    private String resultCode;
    private String resultDescripe;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getResultCode() {
        return resultCode == null ? "" : resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode == null ? "" : resultCode;
    }

    public String getResultDescripe() {
        return resultDescripe == null ? "" : resultDescripe;
    }

    public void setResultDescripe(String resultDescripe) {
        this.resultDescripe = resultDescripe == null ? "" : resultDescripe;
    }


    public static class ResultBean {
        private String appversion;
        private String desc;
        private String fgItemlink;
        private String foodlink;
        private String jtjlink;
        //预留1
        private String link3;
        private String link4;
        private String link5;


        public String getAppversion() {
            return appversion == null ? "" : appversion;
        }

        public void setAppversion(String appversion) {
            this.appversion = appversion == null ? "" : appversion;
        }

        public String getDesc() {
            return desc == null ? "" : desc;
        }

        public void setDesc(String desc) {
            this.desc = desc == null ? "" : desc;
        }

        public String getFgItemlink() {
            return fgItemlink == null ? "" : fgItemlink;
        }

        public void setFgItemlink(String fgItemlink) {
            this.fgItemlink = fgItemlink == null ? "" : fgItemlink;
        }

        public String getFoodlink() {
            return foodlink == null ? "" : foodlink;
        }

        public void setFoodlink(String foodlink) {
            this.foodlink = foodlink == null ? "" : foodlink;
        }

        public String getJtjlink() {
            return jtjlink == null ? "" : jtjlink;
        }

        public void setJtjlink(String jtjlink) {
            this.jtjlink = jtjlink == null ? "" : jtjlink;
        }

        public String getLink3() {
            return link3 == null ? "" : link3;
        }

        public void setLink3(String link3) {
            this.link3 = link3 == null ? "" : link3;
        }

        public String getLink4() {
            return link4 == null ? "" : link4;
        }

        public void setLink4(String link4) {
            this.link4 = link4 == null ? "" : link4;
        }

        public String getLink5() {
            return link5 == null ? "" : link5;
        }

        public void setLink5(String link5) {
            this.link5 = link5 == null ? "" : link5;
        }
    }
}
