<html>
<head>
<#include "/mobile/common/header.ftl">
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache">
    <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <meta HTTP-EQUIV="expires" CONTENT="0">
    <title>统一支付</title>
</head>
<body>
<#if payCode == "ALIPAY_WEB" ||  payCode == "ALIPAY_WAP">
    ${unifiedPayResponse.frontSubmitForm}
<#elseif payCode=="WX_JSAPI">
    <script type="text/javascript">
        $(function () {
            if (typeof WeixinJSBridge == "undefined"){
                if( document.addEventListener ){
                    document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
                }else if (document.attachEvent){
                    document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
                    document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
                }
            }else{
                onBridgeReady();
            }
        });
        function onBridgeReady(){
            WeixinJSBridge.invoke(
                    'getBrandWCPayRequest', {
                        "appId":"${unifiedPayResponse.appId}",
                        "timeStamp":"${unifiedPayResponse.timeStamp}",
                        "nonceStr":"${unifiedPayResponse.nonceStr}",
                        "package":"${unifiedPayResponse.prepayId}",
                        "signType":"${unifiedPayResponse.signType}",
                        "paySign":"${unifiedPayResponse.paySign}"
                    },
                    function(res){
                        if(res.err_msg == "get_brand_wcpay_request:ok" ) {
                            window.location = "${basePath}mobile/payment/payLoading";
                        }else{
                            layer.open({
                                type: 1
                                ,offset: 'auto' //具体配置参考：http://www.layui.com/doc/modules/layer.html#offset
                                ,id: 'payFail' //防止重复弹出
                                ,content: '<div style="padding: 20px 100px;">提交失败: '+res.err_msg+'</div>'
                                ,btn: '关闭'
                                ,btnAlign: 'c' //按钮居中
                                ,shade: 1 //不显示遮罩
                                ,yes: function(){
                                    layer.closeAll();
                                }
                            });
                        }
                    }
            );
        }
    </script>
<#elseif payCode=="WX_H5">

<#else>
    (不支持该支付渠道)
</#if>
</body>
</html>