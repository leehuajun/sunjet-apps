package com.sunjet.utils.common;

import com.sunjet.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;

/**
 * 自定义ContextLoaderListener
 * <p>
 * 在Tomcat服务器启动时自动加载数据到缓存。
 * <p>
 * Created by lhj on 16/9/16.
 */
public class CustomContextLoaderListener extends ContextLoaderListener {

    private static Logger logger = LoggerFactory.getLogger(CustomContextLoaderListener.class);

    private CacheManager cacheManager;

    @Override
    /**
     * @description 重写ContextLoaderListener的contextInitialized方法
     */
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());

        cacheManager = context.getBean(CacheManager.class);
        logger.info("CacheManager is : " + (cacheManager == null ? "Null" : "Not Null"));
        logger.info(cacheManager.toString());
        cacheManager.initCache();
    }
}
