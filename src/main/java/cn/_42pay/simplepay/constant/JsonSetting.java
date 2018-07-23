package cn._42pay.simplepay.constant;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;

/**
 * Created by kevin on 2018/6/19.
 */
public class JsonSetting {
	private static  final SerializeConfig serializeConfig;

	private static final ParserConfig parserConfig;

	/**
	 * @link https://github.com/alibaba/fastjson/wiki/PropertyNamingStrategy_cn
	 */
	static {
		serializeConfig = new SerializeConfig();
		serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
		parserConfig = new ParserConfig();
		parserConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
	}

	public static SerializeConfig getSerializeConfig(){
		return serializeConfig;
	}

	public static ParserConfig getParserConfig(){
		return parserConfig;
	}
}
