package cn._42pay.simplepay.db.entity;

/**
*
* @Author auto_gen_by_tool
* @CreatedTime 2018-06-22 10:43:59
*/

import java.util.List;

public class PaymentJournal {

     /*主键id*/
     private Long paymentJournalId;
     public void setPaymentJournalId(Long paymentJournalId) {
          this.paymentJournalId = paymentJournalId;
     }
     public Long getPaymentJournalId() {
          return paymentJournalId;
     }

     /*支付流水号*/
     private String paymentDealNo;
     public void setPaymentDealNo(String paymentDealNo) {
          this.paymentDealNo = paymentDealNo;
     }
     public String getPaymentDealNo() {
          return paymentDealNo;
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

     /*支付appid*/
     private String payAppId;
     public void setPayAppId(String payAppId) {
          this.payAppId = payAppId;
     }
     public String getPayAppId() {
          return payAppId;
     }

     /*支付商户号*/
     private String merchantId;
     public void setMerchantId(String merchantId) {
          this.merchantId = merchantId;
     }
     public String getMerchantId() {
          return merchantId;
     }

     /*用户订单号*/
     private String merchantOrderNo;
     public void setMerchantOrderNo(String merchantOrderNo) {
          this.merchantOrderNo = merchantOrderNo;
     }
     public String getMerchantOrderNo() {
          return merchantOrderNo;
     }

     /*第三方交易流水号*/
     private String paymentDealId;
     public void setPaymentDealId(String paymentDealId) {
          this.paymentDealId = paymentDealId;
     }
     public String getPaymentDealId() {
          return paymentDealId;
     }

     /*0-创建（待支付）              1-唤起收银台              2-支付              3-退款*/
     private Integer payStatus;
     public void setPayStatus(Integer payStatus) {
          this.payStatus = payStatus;
     }
     public Integer getPayStatus() {
          return payStatus;
     }

     /*支付编码*/
     private String payCode;
     public void setPayCode(String payCode) {
          this.payCode = payCode;
     }
     public String getPayCode() {
          return payCode;
     }

     /*支付方式*/
     private Integer payMode;
     public void setPayMode(Integer payMode) {
          this.payMode = payMode;
     }
     public Integer getPayMode() {
          return payMode;
     }

     /*自费金额 最小单位:分*/
     private Integer payAmount;
     public void setPayAmount(Integer payAmount) {
          this.payAmount = payAmount;
     }
     public Integer getPayAmount() {
          return payAmount;
     }

     /*个帐金额 最小单位：分*/
     private Integer accountAmount;
     public void setAccountAmount(Integer accountAmount) {
          this.accountAmount = accountAmount;
     }
     public Integer getAccountAmount() {
          return accountAmount;
     }

     /*统筹金额 最小单位：分*/
     private Integer medicareAmount;
     public void setMedicareAmount(Integer medicareAmount) {
          this.medicareAmount = medicareAmount;
     }
     public Integer getMedicareAmount() {
          return medicareAmount;
     }

     /*记账合计金额 最小单位：分*/
     private Integer insuranceAmount;
     public void setInsuranceAmount(Integer insuranceAmount) {
          this.insuranceAmount = insuranceAmount;
     }
     public Integer getInsuranceAmount() {
          return insuranceAmount;
     }

     /*总金额 最小单位：分*/
     private Integer totalAmount;
     public void setTotalAmount(Integer totalAmount) {
          this.totalAmount = totalAmount;
     }
     public Integer getTotalAmount() {
          return totalAmount;
     }

     /*描述*/
     private String description;
     public void setDescription(String description) {
          this.description = description;
     }
     public String getDescription() {
          return description;
     }

     /*扩展字段 JSON格式串*/
     private String extraParams;
     public void setExtraParams(String extraParams) {
          this.extraParams = extraParams;
     }
     public String getExtraParams() {
          return extraParams;
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

     /*交易时间*/
     private String payTime;
     public void setPayTime(String payTime) {
          this.payTime = payTime;
     }
     public String getPayTime() {
          return payTime;
     }

     private String beginPayTime;
     public void setBeginPayTime(String beginPayTime) {
          this.beginPayTime = beginPayTime;
     }
     public String getBeginPayTime() {
          return beginPayTime;
     }

     private String endPayTime;
     public void setEndPayTime(String endPayTime) {
          this.endPayTime = endPayTime;
     }
     public String getEndPayTime() {
          return endPayTime;
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

     /*数据来源*/
     private String dataSource;
     public void setDataSource(String dataSource) {
          this.dataSource = dataSource;
     }
     public String getDataSource() {
          return dataSource;
     }

     private String otherSql;
     public void setOtherSql(String otherSql) {
          this.otherSql = otherSql;
     }
     public String getOtherSql() {
          return otherSql;
     }

}