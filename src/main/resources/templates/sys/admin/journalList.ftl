<!DOCTYPE html>
<html>
  
  <head>
  <#include "/sys/common/header.ftl">
      <title>交易记录管理</title>
  </head>
  
  <body>
    <div class="x-nav">
      <span class="layui-breadcrumb">
        <a href="">交易记录</a>
        <a href="">交易记录管理</a>
      </span>
      <a class="layui-btn layui-btn-small" style="line-height:1.6em;margin-top:3px;float:right" href="javascript:location.replace(location.href);" title="刷新">
        <i class="layui-icon" style="line-height:30px">ဂ</i></a>
    </div>
    <div class="x-body">
      <div class="layui-row">
        <form class="layui-form layui-col-md12 x-so">
          <input class="layui-input" placeholder="开始日" name="beginCreateTime"  lay-verify="required|date" autocomplete="off" id="beginCreateTime">
          <input class="layui-input" placeholder="截止日" name="endCreateTime" lay-verify="required|date" autocomplete="off" id="endCreateTime">
          <div class="layui-input-inline">
            <select name="payStatus" id="payStatus">
                <option value="-1" selected>支付状态</option>
                <#list payStatusEnum as val>
                    <option value="${val.getIndex()}">${val.getMessage()}</option>
                </#list>
            </select>
          </div>
          <div class="layui-input-inline">
            <select name="payCode" id="payCode">
                <option value="" selected>支付方式</option>
                <#list payCodeEnum as val>
                    <#if val.getCode()!="WECHAT" && val.getCode()!="ALIPAY" >
                        <option value="${val.getCode()}">${val.getDesc()}</option>
                    </#if>
                </#list>
            </select>
          </div>
          <input type="text" name="merchantOrderNo" id="merchantOrderNo"  placeholder="请输入订单号" autocomplete="off" class="layui-input">
          <button class="layui-btn"  lay-submit="" lay-filter="search"><i class="layui-icon">&#xe615;</i></button>
        </form>
      </div>
      <table class="layui-table">
        <thead>
          <tr>
            <th>交易订单号</th>
            <th>商户订单号</th>
            <th>AppId</th>
            <th>商户号</th>
            <th>交易流水号</th>
            <th>交易状态</th>
            <th>支付方式</th>
            <th>支付金额(元)</th>
            <th>描述</th>
            <th>创建时间</th>
            <th>支付时间</th>
            <th >操作</th>
            </tr>
        </thead>
        <tbody id="content">
        </tbody>
      </table>
      <div style="text-align: center">
          <div id="page"></div>
      </div>

    </div>
    <script type="text/javascript">
       var pageNum=1;
       var pageSize=10;
       var totalCount=0;

      layui.use(['laydate','layer','form'], function(){
        var laydate = layui.laydate;
        var form = layui.form;

        //监听提交
        form.on('submit(search)', function(data){
            search();
            return false;
        });

        initPage();

        laydate.render({
          elem: '#beginCreateTime' //指定元素
        });
        laydate.render({
          elem: '#endCreateTime' //指定元素
        });
      });

      function search() {
          var beginCreateTime=$('#beginCreateTime').val();
          var endCreateTime=$('#endCreateTime').val();
          var payStatus=$('#payStatus').val();
          var payCode=$('#payCode').val();
          var merchantOrderNo=$('#merchantOrderNo').val();

          $.ajax({
              url:'${basePath}/sys/manager/payment/listPaymentJournal',
              method:'post',
              data:{beginCreateTime:beginCreateTime,endCreateTime:endCreateTime,payStatus:payStatus,payCode:payCode,merchantOrderNo:merchantOrderNo,pageSize:pageSize,pageNum:pageNum},
              dataType:'JSON',
              success:function(res){
                  $("#content").empty();
                  if(res.resultCode=='0'){
                      var sList = '';
                      $.each(res.paymentJournalVoList, function(i, item) {
                          sList += '<tr>';
                          sList += '<td>'+item.paymentDealNo+'</td>';
                          sList += '<td>'+item.merchantOrderNo+'</td>';
                          sList += '<td>'+item.payAppId+'</td>';
                          sList += '<td>'+item.merchantId+'</td>';
                          sList += '<td>'+item.paymentDealId+'</td>';
                          sList += '<td>'+item.payStatus+'</td>';
                          sList += '<td>'+item.payCode+'</td>';
                          sList += '<td>'+item.totalAmount+'</td>';
                          sList += '<td>'+item.description+'</td>';
                          sList += '<td>'+item.createTime+'</td>';
                          sList += '<td>'+item.payTime+'</td>';
                          if(item.payStatus=='支付成功'){
                              sList += '<td class="td-manage"> <a title="退费" onclick="refund(this,\''+item.paymentDealNo+'\')" href="javascript:;"><i class="layui-icon">&#xe65e;</i></a></td>';
                          }else{
                              sList += '<td></td>';
                          }
                          sList += '</tr>';
                      });
                      $('#content').append(sList);
                      totalCount=res.pageInfo.total;
                      initPage();
                  }
                  else{
                      alert(res.resultMessage);
                      $("#content").empty();
                  }
              },
              error:function (data) {
                  $("#content").empty();
              }
          });
      }
      
      function initPage() {
          layui.use('laypage', function(){
              laypage=layui.laypage;
              laypage.render({
                  elem: 'page'
                  ,count: totalCount
                  ,curr: pageNum
                  ,limit: pageSize
                  ,skip: true
                  ,layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
                  ,jump: function(obj,first){
                      console.log('jump:'+obj);
                      pageNum=obj.curr;
                      pageSize=obj.limit;
                      if(!first){
                          search();
                      }
                  }
              });
          });
      }

      function refund(obj,paymentDealNo){
          layer.confirm('确认要退费吗，误操作可能造成资金损失？',function(index){
              $.ajax({
                  url:'${basePath}/sys/manager/payment/refund',
                  method:'post',
                  data:{paymentDealNo:paymentDealNo},
                  dataType:'JSON',
                  success:function(res){
                      layer.msg(res.resultMessage, {icon: 1});
                  },
                  error:function (data) {
                  }
              });

          });
      }

    </script>

  </body>

</html>