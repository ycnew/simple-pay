package cn._42pay.simplepay.vo.sys;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by kevin on 2018/7/18.
 */
@Getter
@Setter
public class PaymentJournalListResponseVo extends BaseResponseVo {
	private List<PaymentJournalVo> paymentJournalVoList;
}
