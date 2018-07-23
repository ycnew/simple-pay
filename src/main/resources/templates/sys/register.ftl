<!DOCTYPE html>
<html>
<head>
    <#include "/sys/common/header.ftl">
    <title>简单付-用户注册</title>
    <script type="text/javascript" src="${basePath}js/jquery.md5.js"></script>
</head>
  
  <body>
    <div class="x-body layui-anim layui-anim-up">
        <form class="layui-form">

          <div class="layui-form-item">
              <label for="loginName" class="layui-form-label">
                  <span class="x-red">*</span>登录名
              </label>
              <div class="layui-input-inline">
                  <input type="text" id="loginName" name="loginName" required="" lay-verify="nikename"
                  autocomplete="off" class="layui-input">
              </div>
          </div>
          <div class="layui-form-item">
              <label for="password" class="layui-form-label">
                  <span class="x-red">*</span>密码
              </label>
              <div class="layui-input-inline">
                  <input type="password" id="password" name="password" required="" lay-verify="pass"
                  autocomplete="off" class="layui-input">
              </div>
              <div class="layui-form-mid layui-word-aux">
                  6到16个字符
              </div>
          </div>

          <div class="layui-form-item">
              <label for="repass" class="layui-form-label">
                  <span class="x-red">*</span>确认密码
              </label>
              <div class="layui-input-inline">
                  <input type="password" id="repass" name="repass" required="" lay-verify="repass"
                  autocomplete="off" class="layui-input">
              </div>
          </div>

            <div class="layui-form-item">
                <label for="email" class="layui-form-label">
                    邮箱
                </label>
                <div class="layui-input-inline">
                    <input type="text" id="email" name="email" required="" lay-verify="email"
                           autocomplete="off" class="layui-input">
                </div>
            </div>

            <div class="layui-form-item">
                <label for="mobile" class="layui-form-label">
                    手机
                </label>
                <div class="layui-input-inline">
                    <input type="text" id="mobile" name="mobile" required=""  autocomplete="off" class="layui-input">
                </div>
            </div>

            <div class="layui-form-item">
                <label for="mobile" class="layui-form-label">
                    验证码
                </label>
                <div class="layui-input-inline">
                    <input type="text" placeholder="验证码" lay-verify="required"  id="validateCode" name="validateCode" class="layui-input"   autocomplete="off"  value="" />
                </div>
                <div class="layui-input-inline">
                    <img src="${basePath}/sys/user/getRegisterValidCode" onclick="javascript:changeValidateCode();" id="validateCodeImage"  name="validateCodeImage"  style="cursor:pointer;width: 40%;" height="35px">
                </div>

            </div>

          <div class="layui-form-item">
              <label for="L_repass" class="layui-form-label">
              </label>
              <button  class="layui-btn" lay-filter="add" lay-submit="">
                  注册
              </button>
          </div>
      </form>
    </div>

    <script>
        layui.use(['form','layer'], function(){
            $ = layui.jquery;
          var form = layui.form
          ,layer = layui.layer;
        
          //自定义验证规则
          form.verify({
            nikename: function(value){
              if(value.length < 5){
                return '昵称至少得5个字符啊';
              }
            }
            ,pass: [/(.+){6,12}$/, '密码必须6到12位']
            ,repass: function(value){
                if($('#password').val()!=$('#repass').val()){
                    return '两次密码不一致';
                }
            }
          });

          //监听提交
          form.on('submit(add)', function(data){
            console.log(data);
            data.field.password=$.md5(data.field.password);
            data.field.repass=$.md5(data.field.repass);
            $.ajax({
                  url:'${basePath}/sys/user/userRegister',
                  method:'post',
                  data:data.field,
                  dataType:'JSON',
                  success:function(res){
                      if(res.resultCode=='0'){
                          layer.alert(res.resultMessage, {icon: 6},function () {
                              var index = parent.layer.getFrameIndex(window.name);
                              parent.layer.close(index);
                          });
                      }
                      else{
                          alert(res.resultMessage);
                          changeValidateCode();
                      }
                  },
                  error:function (data) {
                  }
              });

            return false;
          });
        });

        function changeValidateCode(){
            var timeNow = new Date().getTime();
            var imgUrl = "${basePath}/sys/user/getRegisterValidCode?time="+timeNow;
            $('#validateCodeImage').attr('src',imgUrl);
        }
    </script>

  </body>

</html>