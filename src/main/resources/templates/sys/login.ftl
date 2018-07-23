<!DOCTYPE html>
<html>
<head>
    <#include "/sys/common/header.ftl">
	<title>简单付-用户登录</title>
    <script type="text/javascript" src="${basePath}js/jquery.md5.js"></script>
</head>
<body class="login-bg">
    <div class="login layui-anim layui-anim-up">
        <div class="message">简单付-用户登录</div>
        <div id="darkbannerwrap"></div>
        
        <form method="post" class="layui-form" >
            <input name="loginName" placeholder="用户名"  type="text" lay-verify="required" class="layui-input" >
            <hr class="hr15">
            <input name="password" lay-verify="required" placeholder="密码"  type="password" class="layui-input">
            <hr class="15">
            <div>
                <input type="text" placeholder="验证码" style="width: 58%;" lay-verify="required"  id="validateCode" name="validateCode"   autocomplete="off"  value="" />
                <img src="${basePath}/sys/user/getValidCode" onclick="javascript:changeValidateCode();" id="validateCodeImage" name="validateCodeImage"  style="cursor:pointer;width: 40%;" height="50px">
            </div>

            <hr class="hr15">
            <input value="登录" lay-submit lay-filter="login" style="width:100%;" type="submit">
            <hr class="hr20" >
            <div style="text-align: center;">
                <a onclick="x_admin_show('注册','${basePath}/sys/user/register',600,500)" href="javascript:;">新用户注册</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">忘记密码</a>
            </div>

        </form>
    </div>

    <script>
        $(function  () {
            layui.use('form', function(){
              var form = layui.form;
              form.on('submit(login)', function(data){
                  data.field.password=$.md5(data.field.password);
                  $.ajax({
                      url:'${basePath}/sys/user/checkLogin',
                      method:'post',
                      data:data.field,
                      dataType:'JSON',
                      success:function(res){
                          if(res.resultCode=='0'){
                              window.location = "${basePath}sys/manager/index";
                          }
                          else{
                              alert(res.resultMessage);
                              changeValidateCode();
                          }
                      },
                      error:function (data) {
                          console.log(data);
                      }
                  });
                return false;
              });
            });
        });

        function changeValidateCode(){
            var timeNow = new Date().getTime();
            var imgUrl = "${basePath}/sys/user/getValidCode?time="+timeNow;
            $('#validateCodeImage').attr('src',imgUrl);
        }
    </script>
</body>
</html>