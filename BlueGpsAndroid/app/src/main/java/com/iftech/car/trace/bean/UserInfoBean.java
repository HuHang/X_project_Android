package com.iftech.car.trace.bean;

import java.io.Serializable;

/**
 * created by tanghuosong 2017/5/2
 * description: 用户信息bean
 **/
public class UserInfoBean implements Serializable{
    //{"Success":true,"Content":"登陆成功","UserInfo":{"id":129,"name":"系统管理员","loginId":"admin","email":"adf56ds46","phone":"18612451245","roleType":100,"roleTypeDisplay":"超级管理员", "shopId":127,"shopName":"江苏乾丰汽车销售有限公司","bankId":null,"bankName":""}}
//    {"id":217,"name":"bb","loginId":"bb","email":"","phone":"18611326879","roleType":100,"roleTypeDisplay":"超级管理员"}
    boolean Success;
    String Content;
    UserInfo UserInfo;

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public UserInfoBean.UserInfo getUserInfo() {
        return UserInfo;
    }

    public void setUserInfo(UserInfoBean.UserInfo userInfo) {
        UserInfo = userInfo;
    }

    public class UserInfo{
        private int id;
        private String name;
        private String loginId;
        private String email;
        private String phone;
        private int roleType;
        private String roleTypeDisplay;
        private String shopId;
        private String shopName;
        private String bankId;
        private String bankName;
        private String token;


        public UserInfo() {
        }

        public UserInfo(int id, String name, String loginId, String email, String phone, int roleType, String roleTypeDisplay, String shopId, String shopName, String bankId, String bankName, String token) {
            this.id = id;
            this.name = name;
            this.loginId = loginId;
            this.email = email;
            this.phone = phone;
            this.roleType = roleType;
            this.roleTypeDisplay = roleTypeDisplay;
            this.shopId = shopId;
            this.shopName = shopName;
            this.bankId = bankId;
            this.bankName = bankName;
            this.token = token;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getBankId() {
            return bankId;
        }

        public void setBankId(String bankId) {
            this.bankId = bankId;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLoginId() {
            return loginId;
        }

        public void setLoginId(String loginId) {
            this.loginId = loginId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getRoleType() {
            return roleType;
        }

        public void setRoleType(int roleType) {
            this.roleType = roleType;
        }

        public String getRoleTypeDisplay() {
            return roleTypeDisplay;
        }

        public void setRoleTypeDisplay(String roleTypeDisplay) {
            this.roleTypeDisplay = roleTypeDisplay;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

}
