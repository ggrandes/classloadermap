package org.javastack.classloadermap.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.javastack.classloadermap.ClassLoaderMap;

/**
 * Helper ContextListener for register ContextPath in ClassLoaderMap
 * 
 * <pre>
 * &lt;listener&gt;
 * &lt;listener-class&gt;org.javastack.classloadermap.servlet.ClassLoaderMapContextListener&lt;/listener-class&gt;
 * &lt;/listener&gt;
 * </pre>
 */
@WebListener
public class ClassLoaderMapContextListener implements ServletContextListener {
	public static final String SERVLET_CONTEXT_PATH_KEY = "servlet.ContextPath";

	@Override
	public void contextInitialized(final ServletContextEvent contextEvent) {
		final ServletContext ctx = contextEvent.getServletContext();
		final String path = ctx.getContextPath();
		ctx.log("ClassLoaderMapContextListener: " + SERVLET_CONTEXT_PATH_KEY + "=" + path);
		ClassLoaderMap.put(this.getClass(), SERVLET_CONTEXT_PATH_KEY, path);
	}

	@Override
	public void contextDestroyed(final ServletContextEvent contextEvent) {
	}
}
