package com.dy.colony.mvp.model.entity;

import java.util.List;

/**
 * 　 ┏┓　  ┏┓+ +
 * 　┏┛┻━━ ━┛┻┓ + +
 * 　┃　　　　 ┃
 * 　┃　　　　 ┃  ++ + + +
 * 　┃████━████+
 * 　┃　　　　 ┃ +
 * 　┃　　┻　  ┃
 * 　┃　　　　 ┃ + +
 * 　┗━┓　  ┏━┛
 * 　  ┃　　┃
 * 　  ┃　　┃　　 + + +
 * 　  ┃　　┃
 * 　  ┃　　┃ + 神兽保佑,代码无bug
 * 　  ┃　　┃
 * 　  ┃　　┃　　+
 * 　  ┃　 　┗━━━┓ + +
 * 　　┃ 　　　　 ┣┓
 * 　　┃ 　　　 ┏┛
 * 　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　 ┃┫┫ ┃┫┫
 * 　　 ┗┻┛ ┗┻┛+ + + +
 *
 * @author: wangzhenxiong
 * @data: 3/30/24 2:21 PM
 * Description:
 */
public class Platform_UploadBack {
    /**
     * msg : 操作成功
     * obj : {"failNum":1,"successNum":0,"failRecords":[{"errMsg":"regId不存在","id":"201807111630166300007"}]}
     * resultCode : 0X00000
     */

    private String msg;
    private ObjBean obj;
    private String resultCode;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public static class ObjBean {
        /**
         * failNum : 1
         * successNum : 0
         * failRecords : [{"errMsg":"regId不存在","id":"201807111630166300007"}]
         */

        private int failNum;
        private int successNum;
        private List<FailRecordsBean> failRecords;

        public int getFailNum() {
            return failNum;
        }

        public void setFailNum(int failNum) {
            this.failNum = failNum;
        }

        public int getSuccessNum() {
            return successNum;
        }

        public void setSuccessNum(int successNum) {
            this.successNum = successNum;
        }

        public List<FailRecordsBean> getFailRecords() {
            return failRecords;
        }

        public void setFailRecords(List<FailRecordsBean> failRecords) {
            this.failRecords = failRecords;
        }

        @Override
        public String toString() {
            return "ObjBean{" +
                    "failNum=" + failNum +
                    ", successNum=" + successNum +
                    ", failRecords=" + failRecords +
                    '}';
        }

        public static class FailRecordsBean {
            /**
             * errMsg : regId不存在
             * id : 201807111630166300007
             */

            private String errMsg;
            private String id;

            public String getErrMsg() {
                return errMsg;
            }

            public void setErrMsg(String errMsg) {
                this.errMsg = errMsg;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            @Override
            public String toString() {
                return "失败原因：'" + errMsg + '\'' +
                        "条目ID：" + id + '\'' ;
            }
        }
    }
}
