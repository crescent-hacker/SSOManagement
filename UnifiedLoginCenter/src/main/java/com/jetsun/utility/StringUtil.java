package com.jetsun.utility;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/7/23
 * Desc:字符串工具类
 */
public class StringUtil {
    /**
     * 包装like字符串
     *
     * @param likeString
     * @return
     */
    public static String wrapLike(String likeString) {
        return "%" + likeString + "%";
    }

    /**
     * 判断空
     */
    public static boolean isEmpty(String str) {
        boolean isEmpty = false;
        if (null == str || "".equals(str)) {
            isEmpty = true;
        }

        return isEmpty;
    }
    /**
     * 判断空
     */
    public static boolean isNotEmpty(String str) {
        boolean isEmpty = true;
        if (null == str || "".equals(str)) {
            isEmpty = false;
        }

        return isEmpty;
    }

    /**
     * 把null变成空字符串,非null正常返回
     */
    public static String transNull(String str){
        if(str == null){
            return "";
        }
        return str;
    }
    
    /**统计字符串出现次数**/
    public static int stringCount(String text,String sub){  
    	int count =0, start =0;  
    	while((start=text.indexOf(sub,start))>=0){  
    		start += sub.length();  
    		count ++;  
    	}  
    	return count;  
    }

    /**
     * 返回用分隔符隔开的拼接字符串
     * @param strings 字符串数组
     * @param seperate 分隔符
     * @return
     */
    public static String join(String[] strings,String seperate){
        if(strings.length==0){
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (String str:strings){
            if(isEmpty(sb.toString())){
                sb.append(str);
            }else{
                sb.append(seperate).append(str);
            }
        }
        return sb.toString();
    }

}
