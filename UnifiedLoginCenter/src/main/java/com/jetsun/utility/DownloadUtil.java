package com.jetsun.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/10/8
 * Desc:流下载工具类
 */
public class DownloadUtil {
    /**
     * 日志记录器
     */
    private static Logger logger = LoggerFactory.getLogger(DownloadUtil.class);

    /**
     * 流下载方法
     * @param response 响应对象
     * @param fileName 文件名
     * @param fileDisplayName 下载时显示的文件名
     * @throws UnsupportedEncodingException
     */
    public static void download(HttpServletResponse response, String fileName, String fileDisplayName) throws UnsupportedEncodingException {
        //进行文件名转码，防止中文乱码
        fileDisplayName = URLEncoder.encode(fileDisplayName, "UTF-8");
        logger.debug("开始下载文件："+fileName+";下载名称："+fileDisplayName);
        //初始化
        response.reset();
        response.setContentType("application/x-download");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileDisplayName);

        OutputStream outp = null;
        InputStream in = null;
        try {
            outp = response.getOutputStream();
            in = new FileInputStream(fileName);
            byte[] b = new byte[1024];
            int i;
            while ((i = in.read(b)) > 0) {
                outp.write(b, 0, i);
            }
            outp.flush();
        } catch (Exception e) {
            logger.error("文件下载出错", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (outp != null) {
                    outp.close();
                }
            } catch (IOException e) {
                logger.error("文件下载流关闭错误", e);
            }
        }
    }
}
