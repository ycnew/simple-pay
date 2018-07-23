<!DOCTYPE html>
<html>
<head>
    <#include "/sys/common/header.ftl">
    <title>用户信息</title>
</head>
  
<body>
  <div class="x-nav">
      <span class="layui-breadcrumb">
        <a href="">支付管理</a>
        <a href="">支付宝支付配置</a>
      </span>
      <a class="layui-btn layui-btn-small" style="line-height:1.6em;margin-top:3px;float:right" href="javascript:location.replace(location.href);" title="刷新">
          <i class="layui-icon" style="line-height:30px">ဂ</i></a>
  </div>
  <div class="x-body">
      <form class="layui-form">
          <div class="layui-form-item">
              <label class="layui-form-label">是否启用</label>
              <div class="layui-input-block">
                  <input type="checkbox"
                  <#if paymentSetting.enableFlag==1 >
                         checked=""
                  </#if> name="enableFlagStr" id="enableFlagStr" lay-skin="switch" lay-text="ON|OFF">
              </div>
          </div>
          <div class="layui-form-item">
              <label class="layui-form-label">帐号描述</label>
              <div class="layui-input-block" style="width: 50%;">
                  <input type="text" name="description" id="description" lay-verify="required" autocomplete="off"  class="layui-input" value="${paymentSetting.description}">
              </div>
          </div>
          <div class="layui-form-item">
              <label class="layui-form-label">支付appId</label>
              <div class="layui-input-block" style="width: 50%;">
                  <input type="text" name="payAppId" id="payAppId" lay-verify="required" autocomplete="off"  class="layui-input" value="${paymentSetting.payAppId}">
              </div>
          </div>
          <div class="layui-form-item">
              <label class="layui-form-label">商户号</label>
              <div class="layui-input-block" style="width: 50%;">
                  <input type="text" value="${paymentSetting.merchantId}" name="merchantId" id="merchantId" lay-verify="required" autocomplete="off"  class="layui-input">
              </div>
          </div>
          <div class="layui-form-item">
              <label class="layui-form-label">支付宝公钥</label>
              <div class="layui-input-block" style="width: 50%;">
                  <input type="text" value="${paymentSetting.payPublicKey}" name="payPublicKey" id="payPublicKey" lay-verify="required" autocomplete="off"  class="layui-input">
              </div>
          </div>
          <div class="layui-form-item">
              <label class="layui-form-label">商户私钥</label>
              <div class="layui-input-block" style="width: 50%;">
                  <input type="text" value="${paymentSetting.privateKey}" name="privateKey" id="privateKey" lay-verify="required" autocomplete="off"  class="layui-input">
              </div>
          </div>
          <div class="layui-form-item">
              <label class="layui-form-label">商户公钥</label>
              <div class="layui-input-block" style="width: 50%;float:left; margin-left:0px">
                  <input type="text" value="${paymentSetting.publicKey}" name="publicKey" id="publicKey" lay-verify="required" autocomplete="off"  class="layui-input">
              </div>
          </div>
          <div class="layui-form-item">
              <label class="layui-form-label">回调地址</label>
              <div class="layui-input-block" style="width: 50%;">
                  <input type="text" value="${paymentSetting.notifyUrl}" name="notifyUrl" id="notifyUrl" lay-verify="" autocomplete="off"  class="layui-input">
              </div>
          </div>
          <div class="layui-form-item">
              <label class="layui-form-label">回调密钥</label>
              <div class="layui-input-block" style="width: 50%;">
                  <input type="text" value="${paymentSetting.notifyKey}" name="notifyKey" id="notifyKey" lay-verify="" autocomplete="off"  class="layui-input">
              </div>
          </div>
          <div class="layui-form-item">
              <label class="layui-form-label">转账二维码</label>
              <div class="layui-input-block" style="width: 50%;float:left; margin-left:0px">
                  <input type="text" value="${paymentSetting.personalTransferStr}" name="personalTransferStr" id="personalTransferStr" lay-verify="required" autocomplete="off"  class="layui-input">
              </div>
              <div style="float: left;width: 10%;margin-left:10px;">
                  <button  type="button" class="layui-btn" id="uploadQrcodeFile" >
                      上传文件
                  </button>
              </div>
          </div>

          <div class="layui-form-item">
              <label for="save" class="layui-form-label">
              </label>
              <button  class="layui-btn" lay-filter="save" lay-submit="">
                  保存
              </button>
          </div>
          <input type="hidden" name="payMode" id="payMode" value="2">
      </form>
      <br>
      <br>
      <fieldset class="layui-elem-field">
          <legend>设置支付宝后台参数</legend>
          <div class="layui-field-box">
              <table class="layui-table">
                  <tbody>
                  <tr>
                      <th width="100px">网页授权域名</th>
                      <td>${authDomain}</td>
                  </tr>
                  </tbody>
              </table>
          </div>
      </fieldset>
  </div>

  <script>
      layui.use(['form','layer','upload'], function(){
          $ = layui.jquery;
          var form = layui.form
                  ,layer = layui.layer,
                  upload = layui.upload;

          //自定义验证规则
          form.verify({

          });

          upload.render({ //允许上传的文件后缀
              elem: '#uploadQrcodeFile'
              ,url: '${basePath}/sys/manager/payment/uploadQrcodeFile'
              ,accept: 'file' //普通文件
              ,size: 600
              ,exts: 'jpg|jpeg|png|bmp'
              ,done: function(res){
                  $('#personalTransferStr').val(res.filePath);
              }
          });

          //监听提交
          form.on('submit(save)', function(data){
              $.ajax({
                  url:'${basePath}/sys/manager/payment/savePaymentSetting',
                  method:'post',
                  data:data.field,
                  dataType:'JSON',
                  success:function(res){
                      alert(res.resultMessage);
                  },
                  error:function (data) {
                      alert('post异常，请重新尝试！')
                  }
              });

              return false;
          });
      });


  </script>

</body>

</html>