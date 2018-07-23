<!DOCTYPE html>
<html>
<head>
    <#include "/sys/common/header.ftl">
    <title>用户信息</title>
</head>
  
<body>
  <div class="x-nav">
      <span class="layui-breadcrumb">
        <a href="">个人信息</a>
        <a href="">基础信息</a>
      </span>
      <a class="layui-btn layui-btn-small" style="line-height:1.6em;margin-top:3px;float:right" href="javascript:location.replace(location.href);" title="刷新">
          <i class="layui-icon" style="line-height:30px">ဂ</i></a>
  </div>
  <div class="x-body">
        <form class="layui-form">
            <div class="layui-form-item">
                <label for="email" class="layui-form-label">
                    登录名
                </label>
                <label class="layui-form-label-col"> ${userLoginSession.loginName}</label>
            </div>
            <div class="layui-form-item">
                <label for="email" class="layui-form-label">
                    邮箱
                </label>
                <div class="layui-input-block" style="width: 50%;">
                    <input type="text" id="email" name="email" required="" lay-verify="email"
                           autocomplete="off" class="layui-input" value="${userLoginSession.email}">
                </div>
            </div>

            <div class="layui-form-item">
                <label for="mobile" class="layui-form-label">
                    手机
                </label>
                <div class="layui-input-block" style="width: 50%;">
                    <input type="text" id="mobile" name="mobile" required=""  autocomplete="off" class="layui-input" value="${userLoginSession.mobile}">
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

            });

          //监听提交
          form.on('submit(modify)', function(data){
            $.ajax({
                  url:'${basePath}/sys/manager/user/userInfoModify',
                  method:'post',
                  data:data.field,
                  dataType:'JSON',
                  success:function(res){
                      alert(res.resultMessage);
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