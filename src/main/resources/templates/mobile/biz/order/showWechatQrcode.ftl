<html>
<head>
    <#include "/mobile/common/header.ftl">
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache">
    <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <meta HTTP-EQUIV="expires" CONTENT="0">
    <title>微信转账二维码</title>
        <script type="text/javascript" src="${basePath}js/jquery.qrcode.min.js"></script>
</head>
<body class="login-bg">
<div class="login layui-anim layui-anim-up">
    <div style="font-size: 18px">长按"识别图中二维码"进行转账</div>
    <div id="qrcode1" style="display: none"></div>
    <div id="imagQrDiv" style="width:320px; height:320px;"></div>
</div>
<script type="text/javascript">
    $(function(){
        jQuery('#qrcode1').qrcode({
            render: "canvas",
            width: 320,
            height: 320,
            text: "${targetUrl}"
        });

        var mycanvas1=document.getElementsByTagName('canvas')[0];
        //将转换后的img标签插入到html中
        var img=convertCanvasToImage(mycanvas1);
        $('#imagQrDiv').append(img);//imagQrDiv表示你要插入的容器id

    });

    //从 canvas 提取图片 image
    function convertCanvasToImage(canvas) {
        var image = new Image();
        image.src = canvas.toDataURL("image/png");
        return image;
    }



</script>
</body>
</html>