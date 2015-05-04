package com.jetsun.remotelogin.filter;

import com.jetsun.remotelogin.utility.StringUtil;
import net.sf.xsshtmlfilter.HTMLFilter;
import org.apache.struts2.dispatcher.StrutsRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/9/1
 * Desc:跨站脚本的请求处理类
 */
public class XSSHttpServletRequestWrapper extends StrutsRequestWrapper {
    public XSSHttpServletRequestWrapper(HttpServletRequest req) {
        super(req);
    }

    public XSSHttpServletRequestWrapper(HttpServletRequest req, boolean disableRequestAttributeValueStackLookup) {
        super(req, disableRequestAttributeValueStackLookup);
    }


    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> paramMap = super.getParameterMap();
        Set<String> keySet = paramMap.keySet();
        for (Iterator iterator = keySet.iterator(); iterator.hasNext(); ) {
            String key = (String) iterator.next();
            String[] str = paramMap.get(key);
            if (str.length > 0) {
                //取得key和值
                String val = str[0];
                //值非空则进行过滤,并重新放回request
                if (StringUtil.isNotEmpty(val)) {
                    String clean = new HTMLFilter().filter(val);
                    str[0] = clean;
                }
            }
        }
        return paramMap;
    }

    @Override
    public String getParameter(String key){
        String val = super.getParameter(key);
        if(StringUtil.isEmpty(val)||val.contains("系统导航")||val.contains("'")||val.contains("\"")){
            return val;
        }
        val = new HTMLFilter().filter(val);
        return  val;
    }
}
