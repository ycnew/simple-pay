# simple-pay

#### 项目介绍
simple-pay源于本人刚好接手同事交接的支付项目，加上本人对支付的理解，利用业余时间全新开发而成。 简单付（simple-pay）的目的是让开发者对接第三方支付更加简单，便捷。聚合第三方支付通道并且实现零代码就能聚合收款的目的。<br>
官网：[https://www.42pay.cn](https://www.42pay.cn)<br>
QQ交流群：826351281

#### 技术框架
* 核心框架：Spring-Boot 2.0
* 视图框架：FreeMarker 2.3.28
* 持久层框架：MyBatis 3.4.6
* 数据库连接池: Druid 1.0.15
* 日志管理：Logback 1.2.3
* JS框架：Jquery 3.2.1
* UI框架: Layui 2.2.6
* 项目管理框架: Maven 3.3.9

#### 开发环境
建议开发者使用以下环境，可以避免版本带来的问题
* IDE: intellij idea
* DB: Mysql5.7
* JDK: JDK1.8+
* Maven: 3.3.9

#### 运行环境
* 数据库服务器：Mysql5.7
* JAVA平台: JRE1.8+
* 操作系统：Windows、Linux等

#### 系统演示
体验simple-pay, 请访问 [https://www.42pay.cn](https://www.42pay.cn)<br>
一. 注册新的帐号<br>
二. 配置支付参数

#### 代码结构
├--java<br/>
├----cn._42pay.simpleypay<br/>
├------config     	    配置<br/>
├------constant  	    常量和枚举值<br/>
├------controller	    接受页面的请求<br/>
├--------biz			业务<br/>
├----------mobile      手机端业务请求<br/>
├----------sys		    用户登录后台管理业务请求<br/>
├--------notify		    支付回调<br/>
├------core			核心代码(主要实现支付接口：支付宝和微信)<br/>
├------db			    数据库相关<br/>
├------framework		框架代码<br/>
├------report		    处理支付回调的业务代码<br/>
├--------convert		将不同支付渠道的报文转换成统一报文<br/>
├------service		    实现层代码<br/>
├------vo			    接收界面传入的参数<br/>
├--resource            资源<br/>
├----static			静态资源<br/>
├----templates	        页面模板<br/>

#### 聚合通道(PAY_CODE说明)
```
"WX_H5","微信手机网站H5支付"(未实现)
"ALIPAY_WEB","支付宝电脑网站支付"
```
```
"WX_JSAPI","微信公众号支付"
"ALIPAY_WAP","支付宝手机网站支付"
```
```
"WX_MICROPAY","微信刷卡付"
"ALIPAY_BARPAY","支付宝条码支付"
```
```
个人转账聚合没有PAY_CODE
```

#### 聚合支付URL说明

一：WAP支付（跳转界面，需要输入金额）
```
http://域名/mobile/order/webPayOrderInfo?userId=用户ID
```
![输入图片说明](http://7xno76.com1.z0.glb.clouddn.com/%E8%B7%B3%E8%BD%AC%E8%BE%93%E5%85%A5%E9%87%91%E9%A2%9D.PNG "简单付，Wap支付")<br/>
二：WAP支付(直接唤起支付宝或微信收银台)
```
http://域名/mobile/order/webPayOrderInfo?userId=用户ID&payAmount=金额(最小单位：分)
```
三：条码支付（跳转界面，需要输入金额和支付码）
```
http://域名/mobile/order/barcodePayOrderInfo?userId=用户ID
```
![输入图片说明](http://7xno76.com1.z0.glb.clouddn.com/%E6%9D%A1%E7%A0%81%E4%BB%98.PNG "简单付，条码支付")<br/>
四：条码支付（直接扣款）
```
http://域名//mobile/order/barcodePayOrderInfo?userId=用户ID&payAmount=金额(最小单位：分)&barcode=支付条码
```
五：个人转账（1.支付宝直接唤起转账界面 2.微信跳转二维码，长按识别，跳入转账界面）
```
http://域名/mobile/payment/personalTransfer?userId=用户ID
```
#### 使用说明

一：新建数据库simple_pay,并执行simple_pay.sql，修改application.yml配置文件上的数据库连接信息<br>
二：用maven编译<br>
三：直接执行java -jar -server simple-pay-0.0.1-SNAPSHOT.jar<br>
四：端口默认监听在7878上面,访问http://[域名|ip]:端口<br>
五：注册用户<br>
![输入图片说明](http://7xno76.com1.z0.glb.clouddn.com/%E6%B3%A8%E5%86%8C.png "简单付，注册")<br/>
六：配置微信支付参数<br>
![输入图片说明](http://7xno76.com1.z0.glb.clouddn.com/%E5%BE%AE%E4%BF%A1%E9%85%8D%E7%BD%AE.png "简单付，微信配置")<br/>
七：配置支付宝支付参数<br>
![输入图片说明](http://7xno76.com1.z0.glb.clouddn.com/%E6%94%AF%E4%BB%98%E5%AE%9D%E9%85%8D%E7%BD%AE.png "简单付，支付宝配置")<br/>
八：获取个人支付二维码<br>
![输入图片说明](http://7xno76.com1.z0.glb.clouddn.com/%E6%88%91%E7%9A%84%E6%94%B6%E6%AC%BE%E4%BA%8C%E7%BB%B4%E7%A0%81.png "简单付，获取个人二维码")<br/>
九：查看交易记录<br>
![输入图片说明](http://7xno76.com1.z0.glb.clouddn.com/%E4%BA%A4%E6%98%93%E8%AE%B0%E5%BD%95%E6%9F%A5%E8%AF%A2.png "简单付，查看交易记录")<br/>

#### 商户回调签名说明
收到第三方支付回调后，会转换成统一的参数，对参数进行ASCII码升序排序，取里面的值+设置到后台的Key，用MD5算一个签名<br>
具体参考类ReportMechant上的buildNotifySign方法<br>
商户收到报文之后，成功需要应答字符串“SUCCESS”，失败应答字符串 “FAILURE”
#### 开源说明
* 本系统100%开源，遵守Apache2.0协议
