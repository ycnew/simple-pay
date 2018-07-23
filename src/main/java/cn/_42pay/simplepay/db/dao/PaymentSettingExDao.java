package cn._42pay.simplepay.db.dao;

import cn._42pay.simplepay.db.entity.PaymentSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by kevin on 2018/6/27.
 */
@Mapper
public interface PaymentSettingExDao extends PaymentSettingDao{
	PaymentSetting selectPaymentSettingByUserIdAndPayCode(@Param("userId") Long userId,@Param("payCode") String payCode);
}
