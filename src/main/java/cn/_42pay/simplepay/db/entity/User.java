package cn._42pay.simplepay.db.entity;

/**
*
* @Author auto_gen_by_tool
* @CreatedTime 2018-06-22 10:44:00
*/

import java.util.List;

public class User {

     /*主键id*/
     private Long userId;
     public void setUserId(Long userId) {
          this.userId = userId;
     }
     public Long getUserId() {
          return userId;
     }

     /*登录用户*/
     private String loginName;
     public void setLoginName(String loginName) {
          this.loginName = loginName;
     }
     public String getLoginName() {
          return loginName;
     }

     /*密码*/
     private String password;
     public void setPassword(String password) {
          this.password = password;
     }
     public String getPassword() {
          return password;
     }

     /*盐*/
     private String salt;
     public void setSalt(String salt) {
          this.salt = salt;
     }
     public String getSalt() {
          return salt;
     }

     /*用户姓名*/
     private String userName;
     public void setUserName(String userName) {
          this.userName = userName;
     }
     public String getUserName() {
          return userName;
     }

     /*邮件地址*/
     private String email;
     public void setEmail(String email) {
          this.email = email;
     }
     public String getEmail() {
          return email;
     }

     /*手机*/
     private String mobile;
     public void setMobile(String mobile) {
          this.mobile = mobile;
     }
     public String getMobile() {
          return mobile;
     }

     /*创建时间*/
     private String createTime;
     public void setCreateTime(String createTime) {
          this.createTime = createTime;
     }
     public String getCreateTime() {
          return createTime;
     }

     private String beginCreateTime;
     public void setBeginCreateTime(String beginCreateTime) {
          this.beginCreateTime = beginCreateTime;
     }
     public String getBeginCreateTime() {
          return beginCreateTime;
     }

     private String endCreateTime;
     public void setEndCreateTime(String endCreateTime) {
          this.endCreateTime = endCreateTime;
     }
     public String getEndCreateTime() {
          return endCreateTime;
     }

     /*更新时间*/
     private String updateTime;
     public void setUpdateTime(String updateTime) {
          this.updateTime = updateTime;
     }
     public String getUpdateTime() {
          return updateTime;
     }

     private String beginUpdateTime;
     public void setBeginUpdateTime(String beginUpdateTime) {
          this.beginUpdateTime = beginUpdateTime;
     }
     public String getBeginUpdateTime() {
          return beginUpdateTime;
     }

     private String endUpdateTime;
     public void setEndUpdateTime(String endUpdateTime) {
          this.endUpdateTime = endUpdateTime;
     }
     public String getEndUpdateTime() {
          return endUpdateTime;
     }

     private String otherSql;
     public void setOtherSql(String otherSql) {
          this.otherSql = otherSql;
     }
     public String getOtherSql() {
          return otherSql;
     }

}