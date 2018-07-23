package cn._42pay.simplepay.db.entity;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

/**
 * Created by kevin on 2017/7/21.
 */
@Setter
@Getter
public class PageInfo implements Serializable{

    private static final long serialVersionUID = -1355473715045253430L;

    /**
     * 默认分页显示
     */
    public static final Integer MAX_PAGE_SIZE = 100;

    public static final Integer FIRST_PAGE_NUM=1;


    private Long total;       // 总记录数
    private Integer pageNum;  // 第几页
    private Integer pageSize; // 每页记录数
    private Integer pages;    // 总页数
    private Integer size;     // 当前页的数量 <= pageSize，该属性来自ArrayList的size属性


    public PageInfo(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public PageInfo(){
        this.pageNum = FIRST_PAGE_NUM;
        this.pageSize = MAX_PAGE_SIZE;
    }
}
