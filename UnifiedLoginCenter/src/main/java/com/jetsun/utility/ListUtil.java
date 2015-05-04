package com.jetsun.utility;

import java.util.List;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/9/9
 * Desc:列表工具集
 */
public class ListUtil {
    /**
     * 辅助类-为list重复添加对象
     *
     * @param list  列表
     * @param obj   对象
     * @param times 次数
     */
    public static void addIntoList(List<Object> list, Object obj, int times) {
        for (int i = 0; i < times; i++) {
            list.add(obj);
        }
    }
}
