<html>
<head>
    <#include "/mobile/common/header.ftl">
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache">
    <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <meta HTTP-EQUIV="expires" CONTENT="0">
    <title>订单信息</title>
</head>
<body style="background-color: #F2F2F2;">
<#if payAmount==null || payAmount=="" >
<div style="padding: 20px; background-color: #F2F2F2;">
    <div class="layui-row">
        <div class="layui-col-md12">
            <div class="layui-card">
                <form method="post"  class="layui-form layui-col-space15"  id="viewForm">
                    <div class="layui-form-item">
                        <label class="layui-form-label">订单号</label>
                        <div class="layui-input-inline">
                            <input type="text" readonly name="merchantOrderNo"   autocomplete="off"  class="layui-input" value="${merchantOrderNo}" />
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label">描述</label>
                        <label class="layui-form-label-col">${description}</label>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">自费金额</label>
                        <div class="layui-input-inline">
                            <input type="text" name="payAmount" id="payAmount" lay-verify="required|validateMoney"  autocomplete="off" placeholder="请输入自费金额" class="layui-input" value="${payAmount}" />
                        </div>
                        <div class="layui-form-mid layui-word-aux">元</div>
                    </div>

                    <div class="layui-form-item">
                        <div class="layui-col-md12" style="text-align: center">
                            <button class="layui-btn" lay-submit="" lay-filter="pay">立即支付</button>
                        </div>
                    </div>
                </form>
                </div>
            </div>
    </div>
</div>
<#include "/mobile/common/footer.ftl">
</#if>

<form method="post" action="${basePath}mobile/payment/webPay" id="orderForm">
    <input type="hidden" id="merchantOrderNo" name="merchantOrderNo" value="${merchantOrderNo}" />
    <input type="hidden" id="payAmount" name="payAmount" value="${payAmount}" />
</form>

<script type="text/javascript" src="${basePath}mobile/order/orderInfo.js"></script>
<script type="text/javascript" src="${basePath}mobile/common.js"></script>
</body>
</html>