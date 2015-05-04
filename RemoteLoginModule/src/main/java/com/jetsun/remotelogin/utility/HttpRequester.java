package com.jetsun.remotelogin.utility;

import com.jetsun.remotelogin.bean.SessionKey;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/9/26
 * Desc:http请求工具类
 */
public class HttpRequester {
    /**
     * 日志记录器
     */
    private static Logger logger = LoggerFactory.getLogger(com.jetsun.remotelogin.utility.HttpRequester.class);
    /**
     * 单例对象
     */
    private static com.jetsun.remotelogin.utility.HttpRequester httpRequester;

    /**
     * 单例构造函数
     */
    private HttpRequester(){}

    /**
     * 获取唯一实例
     * @return
     */
    public static com.jetsun.remotelogin.utility.HttpRequester getInstance(){
        if(httpRequester==null){
            httpRequester = new com.jetsun.remotelogin.utility.HttpRequester();
        }
        return httpRequester;
    }

    /**
     * http请求
     * @param url 请求地址
     * @param map 提交的信息
     * @return
     * @throws Exception
     */
    public String httpPost(String url,Map<String,String> map,Object session) throws Exception {
        //返回内容
        String retStr = null;
        //session中取HttpClient，无则新建
        CloseableHttpClient closeableHttpClient = getHttpClient(session);
        //http://127.0.0.1:8081/remoteLogin/doLogin.action
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(RequestConfig.DEFAULT);
        // 创建参数队列
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        for(String key:map.keySet()) {
            formParams.add(new BasicNameValuePair(key, map.get(key)));
        }
        UrlEncodedFormEntity entity;
        try {
            entity = new UrlEncodedFormEntity(formParams, "UTF-8");
            httpPost.setEntity(entity);

            CloseableHttpResponse httpResponse;
            //post请求
            long begin = System.currentTimeMillis();
            httpResponse = closeableHttpClient.execute(httpPost);
            long end = System.currentTimeMillis();
            //getEntity()
            try {
                logger.debug(httpResponse.toString());

                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    //打印响应内容
                    retStr = EntityUtils.toString(httpEntity, "UTF-8");
                    logger.debug("\nrequestMap:"+map.toString()+"\nrequestUrl:"+url+"\nresponse:" + retStr+"\nexecuteTime:"+(end-begin)/1000.0+"s");
                }
            }finally {
                httpResponse.close();
            }
        } catch (Exception e) {
            logger.error("httpPost error",e);
        }finally {
            //释放资源
//            closeableHttpClient.close();
        }
        return retStr;
    }

    private static void httpGet() {
        //创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        //HttpClient
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();

        HttpGet httpGet = new HttpGet("http://www.gxnu.edu.cn/default.html");
        System.out.println(httpGet.getRequestLine());
        try {
            //执行get请求
            HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
            //获取响应消息实体
            HttpEntity entity = httpResponse.getEntity();
            //响应状态
            System.out.println("status:" + httpResponse.getStatusLine());
            //判断响应实体是否为空
            if (entity != null) {
                System.out.println("contentEncoding:" + entity.getContentEncoding());
                System.out.println("response content:" + EntityUtils.toString(entity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流并释放资源
                closeableHttpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void httpsPost() throws Exception {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        //加载证书文件
        FileInputStream instream = new FileInputStream(new File("f:/server.keystore"));
        try {
            trustStore.load(instream, "123456".toCharArray());
        } finally {
            instream.close();
        }
        SSLContext sslcontext = SSLContexts.custom()
                .loadTrustMaterial(trustStore)
                .build();

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
            //访问支付宝
            HttpGet httpget = new HttpGet("https://www.alipay.com/");

            System.out.println("executing request" + httpget.getRequestLine());

            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();

                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    System.out.println(EntityUtils.toString(entity));
                }
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

    /**
     * 根据session信息获得httpclient对象
     * @param session 会话
     * @return
     */
    private CloseableHttpClient getHttpClient(Object session){
        //创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient httpClient = null;//httpClientBuilder.build();
        if(session instanceof HttpSession){
            httpClient = (CloseableHttpClient)((HttpSession)session).getAttribute(SessionKey.HTTP_CLIENT);
            if(httpClient==null){
                httpClient = httpClientBuilder.build();
                ((HttpSession)session).setAttribute(SessionKey.HTTP_CLIENT,httpClient);
            }
        }
        if(session instanceof Map){
            httpClient = (CloseableHttpClient)((Map)session).get(SessionKey.HTTP_CLIENT);
            if(httpClient==null){
                httpClient = httpClientBuilder.build();
                ((Map)session).put(SessionKey.HTTP_CLIENT,httpClient);
            }
        }
        return httpClient;
    }
}
