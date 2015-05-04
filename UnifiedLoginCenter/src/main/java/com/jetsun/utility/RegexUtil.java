package com.jetsun.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/8/20
 * Desc:正则工具类
 */
public class RegexUtil {
    /**
     * 检查文件名
     * @return
     */
    public static boolean checkValidity(String fileName,String regexPattern){
        //检查结果
        boolean isValid = false;
        //正则
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            isValid = true;
        }
        return isValid;
    }
}
