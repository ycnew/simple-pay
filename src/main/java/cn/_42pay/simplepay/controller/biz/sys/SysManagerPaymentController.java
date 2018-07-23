package cn._42pay.simplepay.controller.biz.sys;

import cn._42pay.simplepay.config.PayConfig;
import cn._42pay.simplepay.constant.ErrorCodeEnum;
import cn._42pay.simplepay.constant.PayCodeEnum;
import cn._42pay.simplepay.constant.PayStatusEnum;
import cn._42pay.simplepay.core.bo.RefundResponseBo;
import cn._42pay.simplepay.db.entity.PaymentSetting;
import cn._42pay.simplepay.db.entity.User;
import cn._42pay.simplepay.framework.exception.SimplePayException;
import cn._42pay.simplepay.framework.log.Log;
import cn._42pay.simplepay.framework.log.constant.LogScene;
import cn._42pay.simplepay.framework.session.HttpServerSession;
import cn._42pay.simplepay.framework.spring.SpringContextHandler;
import cn._42pay.simplepay.framework.util.*;
import cn._42pay.simplepay.service.mobile.impl.PayChannelServiceImpl;
import cn._42pay.simplepay.service.sys.impl.SysPaymentServiceImpl;
import cn._42pay.simplepay.vo.sys.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

/**
 * Created by kevin on 2018/7/17.
 */
@Component
@Controller
@RequestMapping("/sys/manager/payment")
public class SysManagerPaymentController {
	private final static String ON="on";

	@Autowired
	SysPaymentServiceImpl sysPaymentServiceImpl;

	@Autowired
	PayChannelServiceImpl payChannelServiceImpl;

	@RequestMapping(value = "/journalList")
	public ModelAndView journalList() throws IOException {
		ModelAndView modelAndView=new ModelAndView("/sys/admin/journalList");
		modelAndView.addObject("payStatusEnum", PayStatusEnum.values());
		modelAndView.addObject("payCodeEnum", PayCodeEnum.values());
		return modelAndView;
	}

	@RequestMapping(value = "/listPaymentJournal")
	public @ResponseBody
	PaymentJournalListResponseVo listPaymentJournal(@Validated PaymentJournalListRequestVo paymentJournalListRequestVo,
													HttpSession session) throws IOException {
		User user= HttpServerSession.getSysUserLoginSession(session);
		Long userId=user.getUserId();
		return sysPaymentServiceImpl.listPaymentJournal(userId,paymentJournalListRequestVo);
	}

	@RequestMapping(value = "/refund")
	public @ResponseBody
	BaseResponseVo refund(String paymentDealNo, HttpSession session) throws IOException {
		BaseResponseVo baseResponseVo=new BaseResponseVo();
		if(StringUtils.isBlank(paymentDealNo)){
			baseResponseVo.setCodeAndMessage(ErrorCodeEnum.SYS_PAYMENT_DEAL_NO_EMPTY);
			return baseResponseVo;
		}
		User user= HttpServerSession.getSysUserLoginSession(session);
		Long userId=user.getUserId();

		try{
			RefundResponseBo refundResponseBo= payChannelServiceImpl.refund(userId,paymentDealNo);
			if(refundResponseBo.isSuccess()){
				baseResponseVo.setCodeAndMessage(ErrorCodeEnum.SUCCESS);
			}else{
				baseResponseVo.setCodeAndMessage(refundResponseBo.getResultCode(),refundResponseBo.getResultMessage());
			}
			return baseResponseVo;
		}catch (SimplePayException spe){
			baseResponseVo.setCodeAndMessage(spe.getResultCode(),spe.getMessage());
			return baseResponseVo;
		}
	}

	@RequestMapping(value = "/wechat")
	public ModelAndView wechat(HttpSession session,HttpServletRequest request) throws IOException {
		ModelAndView modelAndView=new ModelAndView("/sys/admin/wechat");
		User user= HttpServerSession.getSysUserLoginSession(session);
		Long userId=user.getUserId();
		PaymentSetting paymentSetting=sysPaymentServiceImpl.getPaymentSettingByUserIdAndPayMode(userId,1);
		if(paymentSetting==null){
			paymentSetting=new PaymentSetting();
			paymentSetting.setNotifyKey(IDUtil.getRandomUUID32());
		}
		modelAndView.addObject("authDomain",request.getServerName());
		modelAndView.addObject("authPay", HttpUtil.getBaseUrl(request)+"/mobile/payment/");
		modelAndView.addObject("authIp",IpUtil.getV4IP());
		modelAndView.addObject("paymentSetting",paymentSetting);
		return modelAndView;
	}

	@RequestMapping(value = "/alipay")
	public ModelAndView alipay(HttpSession session,HttpServletRequest request) throws IOException {
		ModelAndView modelAndView=new ModelAndView("/sys/admin/alipay");
		User user= HttpServerSession.getSysUserLoginSession(session);
		Long userId=user.getUserId();
		PaymentSetting paymentSetting=sysPaymentServiceImpl.getPaymentSettingByUserIdAndPayMode(userId,2);
		if(paymentSetting==null){
			paymentSetting=new PaymentSetting();
			paymentSetting.setNotifyKey(IDUtil.getRandomUUID32());
		}
		modelAndView.addObject("authDomain",request.getServerName());
		modelAndView.addObject("paymentSetting",paymentSetting);
		return modelAndView;
	}

	@RequestMapping(value = "/qrcode")
	public ModelAndView qrcode(HttpServletRequest request) throws IOException {
		ModelAndView modelAndView=new ModelAndView("/sys/admin/qrcode");
		String basePath=HttpUtil.getBaseUrl(request);
		User user= HttpServerSession.getSysUserLoginSession(request.getSession());
		Long userId=user.getUserId();
		String wapPayWeb=basePath+"/mobile/order/webPayOrderInfo?userId="+userId;
		String wapPayInterface=basePath+"/mobile/order/webPayOrderInfo?userId="+userId+"&payAmount=金额(最小单位：分)";
		String barcodePayInterface=basePath+"//mobile/order/barcodePayOrderInfo?userId="+userId+"&payAmount=金额(最小单位：分)&barcode=支付条码";
		String barcodePayWeb=basePath+"/mobile/order/barcodePayOrderInfo?userId="+userId;
		String personalTransferUrl=	basePath+"/mobile/payment/personalTransfer?userId="+userId;
		modelAndView.addObject("wapPayWeb",wapPayWeb);
		modelAndView.addObject("wapPayInterface",wapPayInterface);
		modelAndView.addObject("barcodePayInterface",barcodePayInterface);
		modelAndView.addObject("barcodePayWeb",barcodePayWeb);
		modelAndView.addObject("personalTransferUrl",personalTransferUrl);
		return modelAndView;
	}

	@RequestMapping(value = "/uploadCertFile")
	public @ResponseBody FileUploadResponseVo uploadCertFile(@RequestParam("file") MultipartFile file)  {
		return uploadFile(file);
	}

	@RequestMapping(value = "/uploadQrcodeFile")
	public @ResponseBody FileUploadResponseVo uploadQrcodeFile(@RequestParam("file") MultipartFile file)  {
		FileUploadResponseVo fileUploadResponseVo= uploadFile(file);
		if(fileUploadResponseVo.isSuccess()){
			String content=QRCodeUtil.parseQRCode(fileUploadResponseVo.getFilePath());
			fileUploadResponseVo.setFilePath(content);
		}
		return fileUploadResponseVo;
	}

	@RequestMapping(value = "/savePaymentSetting")
	public @ResponseBody BaseResponseVo savePaymentSetting(@Validated  PaymentSettingRequestVo paymentSettingRequestVo, HttpSession session)  {
		BaseResponseVo baseResponseVo=new BaseResponseVo();
		User user= HttpServerSession.getSysUserLoginSession(session);
		Long userId=user.getUserId();
		String userName=user.getUserName();
		paymentSettingRequestVo.setUserId(userId);
		paymentSettingRequestVo.setUserName(userName);
		if(ON.equalsIgnoreCase(paymentSettingRequestVo.getEnableFlagStr())){
			paymentSettingRequestVo.setEnableFlag((short)1);
		}else{
			paymentSettingRequestVo.setEnableFlag((short)0);
		}
		PaymentSetting paymentSetting=sysPaymentServiceImpl.getPaymentSettingByUserIdAndPayMode(userId,paymentSettingRequestVo.getPayMode());
		if(paymentSetting==null){
			paymentSettingRequestVo.setUpdateFlag(false);
			paymentSettingRequestVo.setPaymentSettingId(SnowflakeUtil.getInstance().nextId());
		}else{
			paymentSettingRequestVo.setUpdateFlag(true);
			paymentSettingRequestVo.setPaymentSettingId(paymentSetting.getPaymentSettingId());
		}
		sysPaymentServiceImpl.updatePaymentSetting(paymentSettingRequestVo);
		baseResponseVo.setCodeAndMessage(ErrorCodeEnum.SUCCESS);
		return baseResponseVo;
	}

	/**
	 * 上传文件
	 * @param file
	 * @return
	 */
	private FileUploadResponseVo uploadFile(MultipartFile file){
		FileUploadResponseVo responseVo=new FileUploadResponseVo();
		String uploadFolder= SpringContextHandler.getBean(PayConfig.class).getUploadFolder();
		if (file.isEmpty()) {
			responseVo.setCodeAndMessage(ErrorCodeEnum.SYS_UPLOAD_FILE_EMPTY);
			return responseVo;
		}

		File f=new File(uploadFolder);
		if(!f.isDirectory()&&!f.exists()){
			f.mkdirs();
		}

		try {
			byte[] bytes = file.getBytes();
			String filePath=uploadFolder
					+ DateUtil.dateToStringMillonSecondNoDivide(new Date())
					+"_"
					+file.getOriginalFilename();
			Path path = Paths.get(filePath);
			Files.write(path, bytes);
			responseVo.setCodeAndMessage(ErrorCodeEnum.SUCCESS);
			responseVo.setFilePath(filePath);
			return  responseVo;
		} catch (IOException e) {
			Log.e(LogScene.MANAGER, "上传文件异常", e);
			responseVo.setCodeAndMessage(ErrorCodeEnum.SYS_UPLOAD_FILE_EXCEPTION);
			return responseVo;
		}
	}

}
