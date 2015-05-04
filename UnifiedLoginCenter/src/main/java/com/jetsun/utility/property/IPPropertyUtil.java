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
 * Desc:IP检查-配置文件读取器
 */
public class IPPropertyUtil {
    /**
     * 日志记录器
     */
    private static Logger logger = LoggerFactory.getLogger(IPPropertyUtil.class);
    /**
     * 配置对象
     */
    private static Properties config = null;
    /**
     * 信息缓存
     */
    private static Map<String,String> ipMap = new HashMap<String, String>();

    /**
     * IP检查模式
     */
    public static final String IP_CHECK_MODE = "IP_CHECK_MODE";

    /**
     * IP检查通配符
     */
    public static final String IP_CHECK_PATTERN = "IP_CHECK_PATTERN";

    /**
     * IP白名单
     */
    public static final String IP_WHITE_LIST = "IP_WHITE_LIST";


    /**
     * 上次文件修改日期
     */
    private static long lastModifiedDate;

    /**
     * 配置文件路径和名称
     */
    private static String ipPropertyName = "/ip.properties";

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
            InputStream input = new FileInputStream(IPPropertyUtil.class.getResource(ipPropertyName).getFile());
            BufferedReader bf = new BufferedReader(new InputStreamReader(input, "utf-8"));
            config = new Properties();
            config.load(bf);
            readAllProperties();
            bf.close();
            realFilePath = IPPropertyUtil.class.getClassLoader().getResource(ipPropertyName).getFile();
            lastModifiedDate = new File(realFilePath).lastModified();
        } catch (IOException e) {
            logger.error("ip.properties read error");
        }
    }


    // 读取properties的全部信息
    public static void readAllProperties() {
        try {

            Enumeration en = config.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String property = config.getProperty(key);
                ipMap.put(key,property);
            }
        } catch (Exception e) {
            logger.error("readAllPropertiesError",e);
        }
    }

    /**
     * 如果文件修改过就重新加载
     */
    public static void reload(){
        long currentModidiedDate = new File(realFilePath).lastModified();
        if(currentModidiedDate>lastModifiedDate) {//已经被修改过
            System.out.println("更新前" + ipMap.toString());
            init();//重新初始化
            System.out.println("更新后"+ipMap.toString());
        }
    }

    // 根据key读取value
    public static String getProperty(String key) {
        reload();//加载
        String value = null;
        try {
            //缓存读
            value = ipMap.get(key);
            if(value == null){
                //配置文件读
                value = config.getProperty(key);
                if(value!=null) {//配置文件有才放入缓存
                    ipMap.put(key, value);
                }
            }
        } catch (Exception e) {
            logger.error("getIPError",e);
        }finally {
            return value;
        }
    }


}
