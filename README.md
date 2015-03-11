# ClassLoaderMap

When you want same "System Property" name, but with different value by ClassLoader. ClassLoaderMap is Hierarchical Map associated to a ClassLoader. Open Source Java project under Apache License v2.0

### Current Stable Version is [1.0.0](https://search.maven.org/#search|ga|1|g%3Aorg.javastack%20a%3Aclassloadermap)

---

## DOC

#### Usage Example


```java
// Sample ClassLoader Hierarchy (Tomcat 7)
//
//      Bootstrap
//          |
//       System
//          |
//       Common
//       /     \
//  Webapp1   Webapp2
//

// Generic Code (
public class ExampleConstants {
	public final String BASE_ID = "api.acme.com:";
	public final String PROP = "myName";
}
public class ExampleID {
	public String getAppID() {
		return ExampleConstants.BASE_ID +
			// get name in decoupled mode 
			ClassLoaderMap.get(this.getClass(), ExampleConstants.PROP);
	}
}

// Tomcat Context Listener
@WebListener
public class ExampleContextListener implements ServletContextListener {
	@Override
	public void contextInitialized(final ServletContextEvent contextEvent) {
		final ServletContext ctx = contextEvent.getServletContext();
		final String path = ctx.getContextPath();
		final String name = path.isEmpty() ? 
			"ROOT" : path.replaceAll("[^a-zA-Z0-9]", "_");
		// put name in decoupled mode
		ClassLoaderMap.put(this.getClass(), ExampleConstants.PROP, name);
	}
	
	// ...
}

// Tomcat Servlet
public class ExampleServlet extends HttpServlet {
	@Override
	protected void doGet(final HttpServletRequest request, 
			final HttpServletResponse response)
			throws ServletException, IOException {
		final PrintWriter out = response.getWriter();
		out.println(new ExampleID().getAppID());
	}
}
```

---

## MAVEN

Add the dependency to your pom.xml:

    <dependency>
        <groupId>org.javastack</groupId>
        <artifactId>classloadermap</artifactId>
        <version>1.0.0</version>
    </dependency>

---
Inspired in [ClassLoader](http://docs.oracle.com/javase/7/docs/api/java/lang/ClassLoader.html), this code is Java-minimalistic version.
