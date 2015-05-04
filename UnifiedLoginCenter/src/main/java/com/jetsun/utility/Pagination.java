package com.jetsun.utility;

import com.jetsun.bean.common.TablesConstant;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/6/30
 * Desc:分页工具类
 */
public class Pagination {
    //每页展示条数
    private static int pageRecord = TablesConstant.PAGE_RECORD;
    //页码
    private int pageIndex;
    //总页数
    private int totalPages;
    //最后一页余数
    private int extra;
    //单页最小记录索引
    private int minRecord;
    //单页最大记录索引
    private int maxRecord;
    //记录总条数
    private int records;

    //无参构造函数
    public Pagination() {
    }

    //构造函数
    public Pagination(int records, int pageIndex) {
        this.records = records;
        this.extra = records % pageRecord;
        this.totalPages = (int) Math.ceil((double) records / pageRecord);
        //判断页面边界
        if (pageIndex >= totalPages) {
            this.pageIndex = totalPages;
        } else if (pageIndex <= 1) {
            this.pageIndex = 1;
        } else {
            this.pageIndex = pageIndex;
        }
        //计算最小记录
        countMinRecIdx();
        //计算最大记录
        countMaxRecIdx();
    }

    /**
     * 计算最小索引
     */
    private void countMinRecIdx() {
        this.minRecord = (pageIndex - 1) * pageRecord + 1;
    }

    /**
     * 计算最大索引
     *
     * @return
     */
    public void countMaxRecIdx() {
        if (pageIndex == totalPages && extra != 0) {
            this.maxRecord = (pageIndex-1) * pageRecord + extra;
        } else {
            this.maxRecord = pageIndex * pageRecord;
        }
    }

    /**
     * 获得总页数
     *
     * @return
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * 包装分页sql
     *
     * @param sql desc:如果想按顺序排列分页，需要传入带order的sql
     */
    public String getPaginSql(String sql) {
        String paginSql = "select * from (select rownum r,a.* from (" + sql + ") a)" +
                "where r<=" + maxRecord + " and r>=" + minRecord;
        return paginSql;
    }

    /**
     * 包装count记录数的sql
     *
     * @param sql
     */
    public static String getCountSql(String sql) {
        String countSql = "select count(1) as page_count from (" + sql + ")";
        return countSql;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getRecords() {
        return records;
    }
}
