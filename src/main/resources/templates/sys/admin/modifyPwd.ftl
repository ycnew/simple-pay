<!DOCTYPE html>
<html>

<head>
    <#include "/sys/common/header.ftl">
    <title>修改密码</title>
    <script type="text/javascript" src="${basePath}js/jquery.md5.js"></script>
</head>
  
  <body>
    <div class="x-body layui-anim layui-anim-up">
        <form class="layui-form">

            <div class="layui-form-item">
                <label for="oldPassword" class="layui-form-label">
                    旧密码
                </label>
                <div class="layui-input-inline">
                    <input type="password" id="oldPassword" name="oldPassword" required="" lay-verify="pass"
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
              <label for="L_repass" class="layui-form-label">
              </label>
              <button  class="layui-btn" lay-filter="modify" lay-submit="">
                  修改
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
            pass: [/(.+){6,12}$/, '密码必须6到12位']
            ,repass: function(value){
                if($('#password').val()!=$('#repass').val()){
                    return '两次密码不一致';
                }
            }
          });

          //监听提交
          form.on('submit(modify)', function(data){
            console.log(data);
              data.field.oldPassword=$.md5(data.field.oldPassword);
            data.field.password=$.md5(data.field.password);
            data.field.repass=$.md5(data.field.repass);
            $.ajax({
                  url:'${basePath}/sys/manager/user/userModifyPwd',
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
                      }
                  },
                  error:function (data) {
                  }
              });

            return false;
          });
        });
    </script>

  </body>

</html>