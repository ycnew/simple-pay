<!DOCTYPE html>
<html>
<head>
    <#include "/sys/common/header.ftl">
    <title>我的收款二维码</title>
        <script type="text/javascript" src="${basePath}js/jquery.qrcode.min.js"></script>
</head>
  
<body>
  <div class="x-nav">
      <span class="layui-breadcrumb">
        <a href="">支付管理</a>
        <a href="">我的收款二维码</a>
      </span>
      <a class="layui-btn layui-btn-small" style="line-height:1.6em;margin-top:3px;float:right" href="javascript:location.replace(location.href);" title="刷新">
          <i class="layui-icon" style="line-height:30px">ဂ</i></a>
  </div>
  <div class="x-body">
      <fieldset class="layui-elem-field">
          <legend></legend>
          <div class="layui-field-box">
              <table class="layui-table">
                  <tbody>
                  <tr>
                      <th width="200px">WAP支付(接口对接)</th>
                      <td>${wapPayInterface}</td>
                      <td></td>
                  </tr>
                  <tr>
                      <th>WAP支付（带界面）</th>
                      <td><div id="qrcode1" style="width:200px; height:200px;"></div></td>
                      <td>
                          <p class="col-lg-6 col-md-6" style="text-align: left;" >
                              <a id="download1" download="wappay.jpg"></a>
                              <a id="saveQrCode1" style="cursor: pointer;">下载二维码</a>
                          </p>
                      </td>
                  </tr>
                  <tr>
                      <th>条码支付(接口对接)</th>
                      <td>${barcodePayInterface}</td>
                      <td></td>
                  </tr>
                  <tr>
                      <th>条码支付(带界面)</th>
                      <td><div id="qrcode2" style="width:200px; height:200px;"></div></td>
                      <td>
                          <p class="col-lg-6 col-md-6" style="text-align: left;" >
                              <a id="download2" download="barcodepay.jpg"></a>
                              <a id="saveQrCode2" style="cursor: pointer;">下载二维码</a>
                          </p>
                      </td>
                  </tr>
                  <tr>
                      <th>个人转账</th>
                      <td>
                          <div id="qrcode3" style="width:200px; height:200px;"></div>

                      </td>
                      <td>
                          <p class="col-lg-6 col-md-6" style="text-align: left;" >
                              <a id="download3" download="transfer.jpg"></a>
                              <a id="saveQrCode3" style="cursor: pointer;">下载二维码</a>
                          </p>
                      </td>
                  </tr>
                  </tbody>
              </table>
          </div>
      </fieldset>
  </div>
<script type="text/javascript">


    $(function(){
        jQuery('#qrcode1').qrcode({
            render: "canvas",
            width: 200,
            height: 200,
            text: "${wapPayWeb}"
        })

        jQuery('#qrcode2').qrcode({
            render: "canvas",
            width: 200,
            height: 200,
            text: "${barcodePayWeb}"
        });

        jQuery('#qrcode3').qrcode({
            render: "canvas",
            width: 200,
            height: 200,
            text: "${personalTransferUrl}"
        });

        downloadImage($('#saveQrCode1'),$('#qrcode1'), $("#download1"));
        downloadImage($('#saveQrCode2'),$('#qrcode2'), $("#download2"));
        downloadImage($('#saveQrCode3'),$('#qrcode3'), $("#download3"));
    });

    function downloadImage(saveObj,qrcodeObj,downloadObj) {
        saveObj.click(function(){
            var canvas = qrcodeObj.find("canvas").get(0);
            try {//解决IE转base64时缓存不足，canvas转blob下载
                var blob = canvas.msToBlob();
                navigator.msSaveBlob(blob, 'transfer.jpg');
            } catch (e) {//如果为其他浏览器，使用base64转码下载
                var url = canvas.toDataURL('image/jpeg');
                downloadObj.attr('href', url).get(0).click();
            }
            return false;
        });
    }


</script>
</body>
</html>