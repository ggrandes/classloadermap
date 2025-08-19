# ClassLoaderMap

When you want same "System Property" name, but with different value by ClassLoader. ClassLoaderMap is Hierarchical Map associated to a ClassLoader. Open Source Java project under Apache License v2.0

### Current Stable Version is [2.0.0](https://search.maven.org/#search|ga|1|g%3Aorg.javastack%20a%3Aclassloadermap-jakarta)

---

## DOC

#### Usage Example

```xml
<!-- Context Listener for Servlet Container -->
<!-- tomcat/conf/web.xml or WEB-INF/web.xml -->
<listener>
	<description>Register "servlet.ContextPath" and "servlet.ContextName" in ClassLoaderMap</description>
	<listener-class>org.javastack.classloadermap.servlet.ClassLoaderMapContextListener</listener-class>
</listener>
```


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

/**
 * Generic Code
 */
public class ExampleConstants {
	public final String BASE_ID = "api.acme.com:";
}
public class ExampleID {
    private static final CTX_PROP = "servlet.ContextName";
	public String getAppID() {
	    // Usage 1
	    Class<?> ref = ExampleID.class;
	    // Usage 2
	    // ClassLoader ref = Thread.currentThread().getContextClassLoader();
	    String webAppName = ClassLoaderMap.get(ref, CTX_PROP);
		return ExampleConstants.BASE_ID +
			// get name in decoupled mode 
			(webAppName == null ? "standalone" : webAppName);
	}
}

/**
 * Tomcat Servlet
 */
public class ExampleServlet extends HttpServlet {
	@Override
	protected void doGet(final HttpServletRequest request, 
			final HttpServletResponse response)
			throws ServletException, IOException {
		final PrintWriter out = response.getWriter();
		out.println(new ExampleID().getAppID());
	}
}

// Output will be:
// api.acme.com:Webapp1
// api.acme.com:Webapp2
```

---

## MAVEN

Add the dependency to your pom.xml:

###### jakarta.servlet (tomcat 10+)

    <dependency>
        <groupId>org.javastack</groupId>
        <artifactId>classloadermap-jakarta</artifactId>
        <version>2.0.0</version>
    </dependency>

###### javax.servlet (tomcat 7, 8.5, 9)

    <dependency>
        <groupId>org.javastack</groupId>
        <artifactId>classloadermap</artifactId>
        <version>1.0.1</version>
    </dependency>

###### Note: If you want use ClassLoaderMapContextListener in global Tomcat web.xml (as infra-structure code), you can copy classloadermap-X.X.X.jar into ```${CATALINA_HOME}/lib/```

---
Inspired in [ClassLoader](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/ClassLoader.html), this code is Java-minimalistic version.
