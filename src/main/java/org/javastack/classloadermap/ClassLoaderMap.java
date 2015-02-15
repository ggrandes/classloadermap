package org.javastack.classloadermap;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;
import java.util.WeakHashMap;

public class ClassLoaderMap {
	private static final UUID PROP_CONFIG_KEY = UUID.fromString("9d574593-efe1-40fb-a45c-78e2da3b2f41");

	@SuppressWarnings("unchecked")
	private static final Map<ClassLoader, Map<String, String>> getConfig(final Properties prop) {
		return (Map<ClassLoader, Map<String, String>>) prop.get(PROP_CONFIG_KEY);
	}

	private static Map<String, String> getConfig(final Properties prop, final ClassLoader classLoader) {
		Map<ClassLoader, Map<String, String>> globalConfig = getConfig(prop);
		if (globalConfig == null) {
			globalConfig = new WeakHashMap<ClassLoader, Map<String, String>>();
			prop.put(PROP_CONFIG_KEY, globalConfig);
		}
		Map<String, String> classLoaderConfig = globalConfig.get(classLoader);
		if (classLoaderConfig == null) {
			classLoaderConfig = new HashMap<String, String>();
			globalConfig.put(classLoader, classLoaderConfig);
		}
		return classLoaderConfig;
	}

	/**
	 * Associates the specified value with the specified key in this map
	 * (optional operation). If the map previously contained a mapping for
	 * the key, the old value is replaced by the specified value.
	 * 
	 * @param clazz class reference (for classloader namespace)
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 */
	public static void put(final Class<?> clazz, final String key, final String value) {
		ClassLoader classLoader = clazz.getClassLoader();
		if (classLoader == null)
			classLoader = ClassLoader.getSystemClassLoader();
		final Properties prop = System.getProperties();
		synchronized (prop) {
			final Map<String, String> classLoaderConfig = getConfig(prop, classLoader);
			classLoaderConfig.put(key, value);
		}
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or {@code null} if this map contains no mapping for the key.
	 * 
	 * @param clazz class reference (for classloader namespace)
	 * @param key the key whose associated value is to be returned
	 * @return the value to which the specified key is mapped, or {@code null} if this map contains no mapping
	 *         for the key
	 */
	public static String get(final Class<?> clazz, final String key) {
		ClassLoader classLoader = clazz.getClassLoader();
		if (classLoader == null)
			classLoader = ClassLoader.getSystemClassLoader();
		final Properties prop = System.getProperties();
		synchronized (prop) {
			while (classLoader != null) {
				final Map<String, String> classLoaderConfig = getConfig(prop, classLoader);
				final String value = classLoaderConfig.get(key);
				if (value != null)
					return value;
				classLoader = classLoader.getParent();
			}
		}
		return null;
	}

	/**
	 * Dump ClassLoaderMap for debugging purposes
	 */
	public static String dump() {
		final StringBuilder sb = new StringBuilder();
		final Properties prop = System.getProperties();
		synchronized (prop) {
			final Map<ClassLoader, Map<String, String>> l1 = getConfig(prop);
			for (final Entry<ClassLoader, Map<String, String>> e1 : l1.entrySet()) {
				final ClassLoader cl1 = e1.getKey();
				final ClassLoader cl2 = cl1.getParent();
				final String key1 = cl1.getClass().getName() + "@" + cl1.hashCode();
				final String key2 = cl2.getClass().getName() + "@" + cl2.hashCode();
				final String value = e1.getValue().toString();
				sb.append(key2).append("/").append(key1).append("=").append(value).append("\n");
			}
			return sb.toString();
		}
	}
	
	public static void main(String[] args) {
		System.out.println("/hola/quease".replaceAll("[^a-zA-Z0-9]", "_"));;
	}
}
