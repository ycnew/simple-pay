package cn._42pay.simplepay.framework.spring;

import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;

/**
 * Created by kevin on 2018/6/25.
 */
public class TraceContextHandler {
	private static final String TRACE_ID_KEY = "traceId";

	public static void setTraceId(String traceId) {
		MDC.put(TRACE_ID_KEY,traceId);
	}

	private static String getTraceId() {
		return MDC.get(TRACE_ID_KEY);
	}

	public static void clear() {
		MDC.remove(TRACE_ID_KEY);
	}

	public static boolean containsTraceId(){
		if(StringUtils.isBlank(getTraceId())){
			return false;
		}
		return true;
	}
}
