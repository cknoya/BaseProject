package com.yuxi.jxs.update.bean;

/**
 * author 陈开.
 * Date: 2019/6/28
 * Time: 9:01
 */
public class UpdateBean {


    /**
     * isupdate : 1
     * version_data : {"app_id":29,"versionCode":"3","versionName":"3","apk_name":"3","updateUrl":"3","updateInfo":"3","force":0,"chanelnum":null,"create_date":"2019-03-28 10:06:31","status":1,"create_uid":126,"appversion_hash":"a5bcd2360d8acd94bba8d35b1b700fa8","app_type":"android","step":2,"update_date":null,"update_uid":null}
     */

    private int isupdate;
    private VersionDataBean version_data;

    public int getIsupdate() {
        return isupdate;
    }

    public void setIsupdate(int isupdate) {
        this.isupdate = isupdate;
    }

    public VersionDataBean getVersion_data() {
        return version_data;
    }

    public void setVersion_data(VersionDataBean version_data) {
        this.version_data = version_data;
    }

    public static class VersionDataBean {
        /**
         * app_id : 29
         * versionCode : 3
         * versionName : 3
         * apk_name : 3
         * updateUrl : 3
         * updateInfo : 3
         * force : 0
         * chanelnum : null
         * create_date : 2019-03-28 10:06:31
         * status : 1
         * create_uid : 126
         * appversion_hash : a5bcd2360d8acd94bba8d35b1b700fa8
         * app_type : android
         * step : 2
         * update_date : null
         * update_uid : null
         */

        private int app_id;
        private int versionCode;
        private String versionName;
        private String apk_name;
        private String updateUrl;
        private String updateInfo;
        private int force;
        private Object chanelnum;
        private String create_date;
        private int status;
        private int create_uid;
        private String appversion_hash;
        private String app_type;
        private int step;
        private Object update_date;
        private Object update_uid;

        public int getApp_id() {
            return app_id;
        }

        public void setApp_id(int app_id) {
            this.app_id = app_id;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getApk_name() {
            return apk_name;
        }

        public void setApk_name(String apk_name) {
            this.apk_name = apk_name;
        }

        public String getUpdateUrl() {
            return updateUrl;
        }

        public void setUpdateUrl(String updateUrl) {
            this.updateUrl = updateUrl;
        }

        public String getUpdateInfo() {
            return updateInfo;
        }

        public void setUpdateInfo(String updateInfo) {
            this.updateInfo = updateInfo;
        }

        public int getForce() {
            return force;
        }

        public void setForce(int force) {
            this.force = force;
        }

        public Object getChanelnum() {
            return chanelnum;
        }

        public void setChanelnum(Object chanelnum) {
            this.chanelnum = chanelnum;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getCreate_uid() {
            return create_uid;
        }

        public void setCreate_uid(int create_uid) {
            this.create_uid = create_uid;
        }

        public String getAppversion_hash() {
            return appversion_hash;
        }

        public void setAppversion_hash(String appversion_hash) {
            this.appversion_hash = appversion_hash;
        }

        public String getApp_type() {
            return app_type;
        }

        public void setApp_type(String app_type) {
            this.app_type = app_type;
        }

        public int getStep() {
            return step;
        }

        public void setStep(int step) {
            this.step = step;
        }

        public Object getUpdate_date() {
            return update_date;
        }

        public void setUpdate_date(Object update_date) {
            this.update_date = update_date;
        }

        public Object getUpdate_uid() {
            return update_uid;
        }

        public void setUpdate_uid(Object update_uid) {
            this.update_uid = update_uid;
        }
    }
}
