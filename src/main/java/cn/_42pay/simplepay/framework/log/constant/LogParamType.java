package cn._42pay.simplepay.framework.log.constant;

/**
 * Created by kevin on 2017/12/4.
 */
public enum LogParamType {
    REQUEST                    (1,"入参"),
    RESPONSE                   (2,"出参"),
    NORMAL                     (3,"普通"),
    EXCEPTION                  (4,"异常"),
    ;
    private int code;

    private String desc;

    LogParamType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}
