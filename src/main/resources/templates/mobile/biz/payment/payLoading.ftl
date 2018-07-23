<html>
<head>
<#include "/mobile/common/header.ftl">
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache">
    <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <meta HTTP-EQUIV="expires" CONTENT="0">
    <title>支付中</title>
</head>
<body>
<div class="layui-row">
    <div class="layui-col-xs8 layui-col-md-offset4">
        <img src="${basePath}/images/loading.gif" width="110"/>
    </div>
</div>
<div class="layui-row">
    <div class="layui-col-xs8 layui-col-md-offset4" id="msginfo">
        正在支付中,请耐心等待.
    </div>
</div>
<div class="layui-row" style="text-align: center;" id="buttonDiv">
    <button class="layui-btn layui-btn-normal" onclick="goNext()">确定</button>
</div>
<script type="text/javascript">
    $(function () {
        payConfirm();
        $("#buttonDiv").hide();
    });

    function payConfirm() {
        var reqUrl = "${basePath}/mobile/payment/payConfirm";
        var datas = '';
        $.ajax({
            type: 'POST',
            url: reqUrl,
            data: datas,
            dataType: 'json',
            timeout: 60000,
            success: function (data) {
                if(data.isSuccess){
                    goNext();
                }else{
                    $("#msginfo").html('支付失败')
                }
            },
            error: function (data) {
                layer.open({
                    type: 1
                    ,offset: 'auto' //具体配置参考：http://www.layui.com/doc/modules/layer.html#offset
                    ,id: 'layerDemo1' //防止重复弹出
                    ,content: '<div style="padding: 20px 100px;">支付异常</div>'
                    ,btn: '关闭全部'
                    ,btnAlign: 'c' //按钮居中
                    ,shade: 1 //不显示遮罩
                    ,yes: function(){
                        layer.closeAll();
                    }
                });
            }
        });
    }

    function goNext() {
        var now = new Date();
        var timeTemp = now.getTime();
        window.location = "${basePath}/mobile/order/queryOrderInfo?timeTemp=" + timeTemp;
    }
</script>
</body>
</html>