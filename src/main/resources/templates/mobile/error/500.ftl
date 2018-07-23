<html>
<head>
<#include "/mobile/common/header.ftl">
    <title>温馨提示</title>
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache">
    <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <meta HTTP-EQUIV="expires" CONTENT="0">
<style type="text/css">
    body {
        background-color: rgb(238, 238, 238);
    }
    .errormsg {
        text-align: center;
        padding-top: 100px;
    }
</style>
</head>
<body>
    <div id="error-part" class="errormsg">
        <#if errorMessage??>
            ${errorMessage}
        <#else>
           服务器繁忙，请稍后再试
        </#if>
    </div>

<#include "/mobile/common/footer.ftl">
</body>
</html>
