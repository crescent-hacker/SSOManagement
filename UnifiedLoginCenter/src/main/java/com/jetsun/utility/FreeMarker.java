package com.jetsun.utility;

import com.jetsun.bean.common.EventModel;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;

public class FreeMarker {
    private static Configuration config;
    private static String ROOT = "/";
    private static String ENCODING = "UTF-8";
    private static Logger logger = LoggerFactory.getLogger(FreeMarker.class);
    private static void init(String root) throws Exception {
        if (null == config) {
            try {
                ROOT = root;
                config = new Configuration();
                config.setDirectoryForTemplateLoading(new File(ROOT));
                config.setObjectWrapper(new DefaultObjectWrapper());
                Properties p = new Properties();
                p.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("freemarker.properties"));
                config.setSettings(p);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }

    }

    public static String processContent(String ftl, EventModel event) {
        try {
            //init(event.getSession().getServletContext().getRealPath("/"));
        	init(event.getContexturl());
            Template template = config.getTemplate(ftl, ENCODING);
            Writer out = new StringWriter();
            template.process(event, out);
            out.flush();
            String html = out.toString();
            return html;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
