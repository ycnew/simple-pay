<!doctype html>
<html lang="en">
<head>
<#include "/sys/common/header.ftl">
    <title>简单付后台管理</title>
<body>
    <!-- 顶部开始 -->
    <div class="container">
        <div class="logo"><a href="#">简单付后台管理</a></div>
        <div class="left_open">
            <i title="展开左侧栏" class="iconfont">&#xe699;</i>
        </div>
        <ul class="layui-nav left fast-add" lay-filter="">
          <#--<li class="layui-nav-item">-->
            <#--<a href="javascript:;">+新增</a>-->
            <#--<dl class="layui-nav-child"> <!-- 二级菜单 &ndash;&gt;-->
              <#--<dd><a onclick="x_admin_show('资讯','http://www.baidu.com')"><i class="iconfont">&#xe6a2;</i>资讯</a></dd>-->
              <#--<dd><a onclick="x_admin_show('图片','http://www.baidu.com')"><i class="iconfont">&#xe6a8;</i>图片</a></dd>-->
               <#--<dd><a onclick="x_admin_show('用户','http://www.baidu.com')"><i class="iconfont">&#xe6b8;</i>用户</a></dd>-->
            <#--</dl>-->
          <#--</li>-->
        </ul>
        <ul class="layui-nav right" lay-filter="">
          <li class="layui-nav-item">
            <a href="javascript:;">${userLoginSession.loginName}</a>
            <dl class="layui-nav-child"> <!-- 二级菜单 -->
              <dd><a onclick="x_admin_show('修改密码','${basePath}/sys/manager/user/modifyPwd',400,350)">修改密码</a></dd>
              <dd><a href="${basePath}/sys/user/loginOut">退出</a></dd>
            </dl>
          </li>
          <li class="layui-nav-item to-index"><a href="/">前台首页</a></li>
        </ul>
        
    </div>
    <!-- 顶部结束 -->
    <!-- 中部开始 -->
     <!-- 左侧菜单开始 -->
    <div class="left-nav">
      <div id="side-nav">
        <ul id="nav">

            <li>
                <a href="javascript:;">
                    <i class="iconfont">&#xe6b8;</i>
                    <cite>个人信息</cite>
                    <i class="iconfont nav_right">&#xe697;</i>
                </a>
                <ul class="sub-menu">
                    <li>
                        <a _href="${basePath}/sys/manager/user/userInfo">
                            <i class="iconfont">&#xe6a7;</i>
                            <cite>基础信息</cite>
                        </a>
                    </li >
                </ul>
            </li>

            <li>
                <a href="javascript:;">
                    <i class="iconfont">&#xe6ce;</i>
                    <cite>交易记录</cite>
                    <i class="iconfont nav_right">&#xe697;</i>
                </a>
                <ul class="sub-menu">
                    <li>
                        <a _href="${basePath}/sys/manager/payment/journalList">
                            <i class="iconfont">&#xe6a7;</i>
                            <cite>交易记录管理</cite>
                        </a>
                    </li >
                </ul>
            </li>

            <li>
                <a href="javascript:;">
                    <i class="iconfont">&#xe723;</i>
                    <cite>支付管理</cite>
                    <i class="iconfont nav_right">&#xe697;</i>
                </a>
                <ul class="sub-menu">
                    <li>
                        <a _href="${basePath}/sys/manager/payment/qrcode">
                            <i class="iconfont">&#xe6a7;</i>
                            <cite>我的收款二维码</cite>
                        </a>
                    </li >
                    <li>
                        <a _href="${basePath}/sys/manager/payment/wechat">
                            <i class="iconfont">&#xe6a7;</i>
                            <cite>微信支付配置</cite>
                        </a>
                    </li >
                    <li>
                        <a _href="${basePath}/sys/manager/payment/alipay">
                            <i class="iconfont">&#xe6a7;</i>
                            <cite>支付宝支付配置</cite>
                        </a>
                    </li >
                </ul>
            </li>
        </ul>
      </div>
    </div>
    <!-- <div class="x-slide_left"></div> -->
    <!-- 左侧菜单结束 -->
    <!-- 右侧主体开始 -->
    <div class="page-content">
        <div class="layui-tab tab" lay-filter="xbs_tab" lay-allowclose="false">
          <ul class="layui-tab-title">
            <li class="home"><i class="layui-icon">&#xe68e;</i>我的桌面</li>
          </ul>
          <div class="layui-tab-content">
            <div class="layui-tab-item layui-show">
                <iframe src='${basePath}/sys/manager/welcome' frameborder="0" scrolling="yes" class="x-iframe"></iframe>
            </div>
          </div>
        </div>
    </div>
    <div class="page-content-bg"></div>
    <!-- 右侧主体结束 -->
    <!-- 中部结束 -->
    <!-- 底部开始 -->

    <#include "/sys/common/footer.ftl">
</body>
</html>