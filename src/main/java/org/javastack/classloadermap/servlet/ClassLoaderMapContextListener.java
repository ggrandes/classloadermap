package org.javastack.classloadermap.servlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import org.javastack.classloadermap.ClassLoaderMap;

/**
 * Helper ContextListener for register ContextPath and ContextName in ClassLoaderMap
 * 
 * <pre>
 * &lt;listener&gt;
 * &lt;description&gt;Register "servlet.ContextPath" and "servlet.ContextName" in ClassLoaderMap&lt;/description&gt;
 * &lt;listener-class&gt;org.javastack.classloadermap.servlet.ClassLoaderMapContextListener&lt;/listener-class&gt;
 * &lt;/listener&gt;
 * </pre>
 * 
 * @see #SERVLET_CONTEXT_PATH_KEY
 * @see #SERVLET_CONTEXT_NAME_KEY
 * @see ClassLoaderMap#get(Class, String)
 */
@WebListener
public class ClassLoaderMapContextListener implements ServletContextListener {
	/**
	 * Constant for: <b>servlet.ContextPath</b>
	 */
	public static final String SERVLET_CONTEXT_PATH_KEY = "servlet.ContextPath";
	/**
	 * Constant for: <b>servlet.ContextName</b>
	 */
	public static final String SERVLET_CONTEXT_NAME_KEY = "servlet.ContextName";

	@Override
	public void contextInitialized(final ServletContextEvent contextEvent) {
		final ServletContext ctx = contextEvent.getServletContext();
		final String path = ctx.getContextPath();
		final String name = (path.isEmpty() ? "ROOT" : path.substring(1).replace('/', '#'));
		ctx.log("ClassLoaderMapContextListener: " + //
				SERVLET_CONTEXT_PATH_KEY + "=" + path + " " + //
				SERVLET_CONTEXT_NAME_KEY + "=" + name);
		ClassLoaderMap.put(this.getClass(), SERVLET_CONTEXT_PATH_KEY, path);
		ClassLoaderMap.put(this.getClass(), SERVLET_CONTEXT_NAME_KEY, name);
	}

	@Override
	public void contextDestroyed(final ServletContextEvent contextEvent) {
	}
}
