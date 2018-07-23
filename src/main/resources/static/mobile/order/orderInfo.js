$(function(){
    var payAmount=$("#orderForm").find("#payAmount").val();
    var result = validateMoney(payAmount);
    if (result == "Y") {
        $("#orderForm").find("#payAmount").val(payAmount * 100)
        $("#orderForm").submit();
    }
});

layui.use(['form','jquery'], function () {
    var form = layui.form

    //自定义验证规则
    form.verify({
        validateMoney: function (value) {
            var result = validateMoney(value);
            if (result != "Y") {
                return result;
            }
        }
    });

    //监听提交
    form.on('submit(pay)', function (data) {
        $("#orderForm").find("#payAmount").val(data.field.payAmount*100);
        $("#orderForm").submit();
        return false;
    });
});

