package cn._42pay.simplepay.framework.util;

import cn._42pay.simplepay.constant.PayConst;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class MapUtil {

	/**
	 * 排序后组合字符串
	 * @param data
	 * @return
	 */
	public static String coverMap2SignString(Map<String, String> data) {
		TreeMap<String, String> tree = new TreeMap<String, String>();
		Iterator<Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			if ("sign".equals(en.getKey().trim())) {
				continue;
			}
			tree.put(en.getKey(), en.getValue());
		}
		
		it = tree.entrySet().iterator();
		StringBuffer sf = new StringBuffer();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			sf.append(String.valueOf(en.getValue()));
		}
		return sf.toString();
	}
	
	/**
	 * 将Map中的数据转换成key1=value1&key2=value2的形式 
	 *
	 * @param data
	 *            待拼接的Map数据
	 * @return 拼接好后的字符串
	 */
	public static String coverMap2String(Map<String, String> data) {
		TreeMap<String, String> tree = new TreeMap<String, String>();
		Iterator<Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			tree.put(en.getKey(), en.getValue());
		}
		it = tree.entrySet().iterator();
		StringBuffer sf = new StringBuffer();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			sf.append(en.getKey() + "=" + en.getValue() + "&");
		}
		return sf.substring(0, sf.length() - 1);
	}
	
	/**
	 * 将形如key=value&key=value的字符串转换为相应的Map对象
	 *
	 * @param res
	 *            形如key=value&key=value的字符串
	 * @return 转换后的Map对象
	 */
	private static Map<String, String> convertResultString2Map(String res) {
		Map<String, String> map = null;
		if (null != res && !"".equals(res.trim())) {
			String[] resArray = res.split(PayConst.AMPERSAND);
			if (0 != resArray.length) {
				map = new HashMap<String, String>(resArray.length);
				for (String arrayStr : resArray) {
					if (null == arrayStr || "".equals(arrayStr.trim())) {
						continue;
					}
					int index = arrayStr.indexOf(PayConst.EQUAL);
					if (-1 == index) {
						continue;
					}
					map.put(arrayStr.substring(0, index), arrayStr.substring(index + 1));
				}
			}
		}
		return map;
	}


	/**
	 * 将key=value形式的字符串加入到指定Map中
	 *
	 * @param res
	 * @param map
	 */
	private static void convertResultStringJoinMap(String res, Map<String, String> map) {
		if (null != res && !"".equals(res.trim())) {
			String[] resArray = res.split(PayConst.AMPERSAND);
			if (0 != resArray.length) {
				for (String arrayStr : resArray) {
					if (null == arrayStr || "".equals(arrayStr.trim())) {
						continue;
					}
					int index = arrayStr.indexOf(PayConst.EQUAL);
					if (-1 == index) {
						continue;
					}
					map.put(arrayStr.substring(0, index), arrayStr.substring(index + 1));
				}
			}
		}
	}
}
