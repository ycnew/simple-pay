/**
 * 金额校验
 * @param money
 * @returns {*}
 */
function validateMoney(money) {
    var reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
    if (reg.test(money)) {
        return "Y";
    }
    return "请输入正确的金额,且最多两位小数!";
}