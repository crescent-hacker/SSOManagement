package com.jetsun.utility.property;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/7/16
 * Desc:返回页面消息-配置文件读取器
 */
public class MessagePropertyUtil {
    /**
     * 日志记录器
     */
    private static Logger logger = LoggerFactory.getLogger(MessagePropertyUtil.class);
    /**
     * 配置对象
     */
    private static Properties config = null;
    /**
     * 信息缓存
     */
    private static Map<String,String> messageMap = new HashMap<String, String>();

    /**
     * 上次文件修改日期
     */
    private static long lastModifiedDate;

    /**
     * 配置文件路径和名称
     */
    private static String messagePropertyName = "/message.properties";

    /**
     * 本地文件路径
     */
    private static String realFilePath;

    static {
        init();//初始化
    }

    /**
     * 初始化
     */
    public static void init(){
        try {
//            InputStream input = IPPropertyUtil.class.getClassLoader().getResourceAsStream(ipPropertyName);
            InputStream input = new FileInputStream(MessagePropertyUtil.class.getResource(messagePropertyName).getFile());
            BufferedReader bf = new BufferedReader(new InputStreamReader(input, "utf-8"));
            config = new Properties();
            config.load(bf);
            readAllProperties();
            bf.close();
            realFilePath = MessagePropertyUtil.class.getClassLoader().getResource(messagePropertyName).getFile();
            lastModifiedDate = new File(realFilePath).lastModified();
        } catch (IOException e) {
            logger.error("message.properties read error");
        }
    }


    // 读取properties的全部信息
    public static void readAllProperties() {
        try {

            Enumeration en = config.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String property = config.getProperty(key);
                messageMap.put(key,property);
            }
        } catch (Exception e) {
            logger.error("ConfigInfoError",e);
        }
    }

    /**
     * 如果文件修改过就重新加载
     */
    public static void reload(){
        long currentModidiedDate = new File(realFilePath).lastModified();
        if(currentModidiedDate>lastModifiedDate) {//已经被修改过
            init();//重新初始化
        }
    }

    // 根据key读取value
    public static String getMessage(String key) {
        reload();//加载
        String value = null;
        try {
            value = messageMap.get(key);
            if(value == null){
                value = config.getProperty(key);
                messageMap.put(key,value);
            }
        } catch (Exception e) {
            logger.error("ConfigInfoError",e);
        }finally {
            return value;
        }
    }


}
