package cn._42pay.simplepay.db.entity;

/**
*
* @Author auto_gen_by_tool
* @CreatedTime 2018-07-20 17:11:57
*/

import java.util.List;

public class PaymentSetting {

     /*主键id*/
     private Long paymentSettingId;
     public void setPaymentSettingId(Long paymentSettingId) {
          this.paymentSettingId = paymentSettingId;
     }
     public Long getPaymentSettingId() {
          return paymentSettingId;
     }

     /*支付appid*/
     private String payAppId;
     public void setPayAppId(String payAppId) {
          this.payAppId = payAppId;
     }
     public String getPayAppId() {
          return payAppId;
     }

     /*商户号*/
     private String merchantId;
     public void setMerchantId(String merchantId) {
          this.merchantId = merchantId;
     }
     public String getMerchantId() {
          return merchantId;
     }

     /*用户id*/
     private Long userId;
     public void setUserId(Long userId) {
          this.userId = userId;
     }
     public Long getUserId() {
          return userId;
     }

     /*用户姓名*/
     private String userName;
     public void setUserName(String userName) {
          this.userName = userName;
     }
     public String getUserName() {
          return userName;
     }

     /*授权密钥*/
     private String appSecret;
     public void setAppSecret(String appSecret) {
          this.appSecret = appSecret;
     }
     public String getAppSecret() {
          return appSecret;
     }

     /*接口密钥*/
     private String apiSecret;
     public void setApiSecret(String apiSecret) {
          this.apiSecret = apiSecret;
     }
     public String getApiSecret() {
          return apiSecret;
     }

     /*授权证书*/
     private String certificatePath;
     public void setCertificatePath(String certificatePath) {
          this.certificatePath = certificatePath;
     }
     public String getCertificatePath() {
          return certificatePath;
     }

     /**/
     private String certificatePwd;
     public void setCertificatePwd(String certificatePwd) {
          this.certificatePwd = certificatePwd;
     }
     public String getCertificatePwd() {
          return certificatePwd;
     }

     /*医疗支付接口密钥*/
     private String medicareApiSecret;
     public void setMedicareApiSecret(String medicareApiSecret) {
          this.medicareApiSecret = medicareApiSecret;
     }
     public String getMedicareApiSecret() {
          return medicareApiSecret;
     }

     /*公钥*/
     private String publicKey;
     public void setPublicKey(String publicKey) {
          this.publicKey = publicKey;
     }
     public String getPublicKey() {
          return publicKey;
     }

     /*私钥*/
     private String privateKey;
     public void setPrivateKey(String privateKey) {
          this.privateKey = privateKey;
     }
     public String getPrivateKey() {
          return privateKey;
     }

     /*第三方支付公钥*/
     private String payPublicKey;
     public void setPayPublicKey(String payPublicKey) {
          this.payPublicKey = payPublicKey;
     }
     public String getPayPublicKey() {
          return payPublicKey;
     }

     /*合作者id*/
     private String partnerId;
     public void setPartnerId(String partnerId) {
          this.partnerId = partnerId;
     }
     public String getPartnerId() {
          return partnerId;
     }

     /*支付编码*/
     private String payCode;
     public void setPayCode(String payCode) {
          this.payCode = payCode;
     }
     public String getPayCode() {
          return payCode;
     }

     /*支付方式              */
     private Integer payMode;
     public void setPayMode(Integer payMode) {
          this.payMode = payMode;
     }
     public Integer getPayMode() {
          return payMode;
     }

     /**/
     private String notifyUrl;
     public void setNotifyUrl(String notifyUrl) {
          this.notifyUrl = notifyUrl;
     }
     public String getNotifyUrl() {
          return notifyUrl;
     }

     /*异步通知到其它客户端的key*/
     private String notifyKey;
     public void setNotifyKey(String notifyKey) {
          this.notifyKey = notifyKey;
     }
     public String getNotifyKey() {
          return notifyKey;
     }

     /*是否启用 0-不可用  1-启用*/
     private Short enableFlag;
     public void setEnableFlag(Short enableFlag) {
          this.enableFlag = enableFlag;
     }
     public Short getEnableFlag() {
          return enableFlag;
     }

     /*描述*/
     private String description;
     public void setDescription(String description) {
          this.description = description;
     }
     public String getDescription() {
          return description;
     }

     /**/
     private String personalTransferStr;
     public void setPersonalTransferStr(String personalTransferStr) {
          this.personalTransferStr = personalTransferStr;
     }
     public String getPersonalTransferStr() {
          return personalTransferStr;
     }

     /*父id*/
     private Integer parentPaymentSettingId;
     public void setParentPaymentSettingId(Integer parentPaymentSettingId) {
          this.parentPaymentSettingId = parentPaymentSettingId;
     }
     public Integer getParentPaymentSettingId() {
          return parentPaymentSettingId;
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