<html>
<head>
<#include "/mobile/common/header.ftl">
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache">
    <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <meta HTTP-EQUIV="expires" CONTENT="0">
    <title>订单支付信息</title>
</head>
<body style="background-color: #F2F2F2;">
<div style="padding: 20px; background-color: #F2F2F2;">
    <div class="layui-row">
        <div class="layui-col-md12">
            <div class="layui-card">
                <form method="post"  class="layui-form layui-col-space15" >
                    <div class="layui-form-item">
                        <label class="layui-form-label">订单号</label>
                        <label class="layui-form-label-col">${merchantOrderNo}</label>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">描述</label>
                        <label class="layui-form-label-col">${desc}</label>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">支付金额</label>
                        <label class="layui-form-label-col">${payAmount}元</label>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">支付状态</label>
                        <label class="layui-form-label-col">${payStatus}</label>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<#--调试-->
<#--<script src="//cdn.jsdelivr.net/npm/eruda"></script>-->
<#--<script>eruda.init();</script>-->

<#include "/mobile/common/footer.ftl">
</body>
</html>