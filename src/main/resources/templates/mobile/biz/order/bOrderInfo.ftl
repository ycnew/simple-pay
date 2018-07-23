<html>
<head>
    <#include "/mobile/common/header.ftl">
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache">
    <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <meta HTTP-EQUIV="expires" CONTENT="0">
    <title>订单信息</title>
</head>
<body style="background-color: #F2F2F2;">
<#if barcodePayOrderInfoVo.payAmount==null || barcodePayOrderInfoVo.payAmount=="" || barcodePayOrderInfoVo.barcode==null || barcodePayOrderInfoVo.barcode=="">
<div style="padding: 20px; background-color: #F2F2F2;">
    <div class="layui-row">
        <div class="layui-col-md12">
            <div class="layui-card">
                <form method="post"  class="layui-form layui-col-space15"  id="viewForm">
                    <div class="layui-form-item">
                        <label class="layui-form-label">订单号</label>
                        <div class="layui-input-inline">
                            <input type="text" readonly name="merchantOrderNo"   autocomplete="off"  class="layui-input" value="${barcodePayOrderInfoVo.merchantOrderNo}" />
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label">描述</label>
                        <label class="layui-form-label-col">${barcodePayOrderInfoVo.description}</label>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">自费金额</label>
                        <div class="layui-input-inline">
                            <input type="text" name="payAmount" id="payAmount" lay-verify="required|validateMoney"  autocomplete="off" placeholder="请输入自费金额" class="layui-input" value="${barcodePayOrderInfoVo.payAmount}" />
                        </div>
                        <div class="layui-form-mid layui-word-aux">元</div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">条码</label>
                        <div class="layui-input-inline">
                            <input type="text" name="barcode" id="barcode"  lay-verify="required"  autocomplete="off" placeholder="请输入条码" class="layui-input" value="${barcodePayOrderInfoVo.barcode}" />
                        </div>
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

<form method="post" action="${basePath}mobile/payment/barcodePay" id="orderForm">
    <input type="hidden" id="merchantOrderNo" name="merchantOrderNo" value="${barcodePayOrderInfoVo.merchantOrderNo}" />
    <input type="hidden" id="payAmount" name="payAmount" value="${barcodePayOrderInfoVo.payAmount}" />
    <input type="hidden" id="barcode" name="barcode" value="${barcodePayOrderInfoVo.barcode}" />
    <input type="hidden" id="payAppId" name="payAppId" value="${barcodePayOrderInfoVo.payAppId}" />
    <input type="hidden" id="userId" name="userId" value="${barcodePayOrderInfoVo.userId}" />
    <input type="hidden" id="description" name="description" value="${barcodePayOrderInfoVo.description}" />
</form>

<script type="text/javascript" src="${basePath}mobile/order/bOrderInfo.js"></script>
<script type="text/javascript" src="${basePath}mobile/common.js"></script>
</body>
</html>