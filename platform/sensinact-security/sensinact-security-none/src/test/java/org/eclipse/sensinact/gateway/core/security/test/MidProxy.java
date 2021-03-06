/*
* Copyright (c) 2020 Kentyou.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
*    Kentyou - initial API and implementation
 */
package org.eclipse.sensinact.gateway.core.security.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

public class MidProxy<T> implements InvocationHandler {
	private BundleContextProvider contextProvider;
	private Class<T> serviceType;

	private Object contextualizedInstance;
	private FilterOSGiClassLoader classloader;

	public MidProxy(FilterOSGiClassLoader classloader, BundleContextProvider contextProvider, Class<T> serviceType) {
		this.contextProvider = contextProvider;
		this.classloader = classloader;
		this.serviceType = serviceType;
	}

	@SuppressWarnings("unchecked")
	public T buildProxy() throws ClassNotFoundException {
		String classname = this.serviceType.getCanonicalName();
		Class<?> contextualizedClazz = this.loadClass(classname);

		ServiceReference reference = null;

		if (contextualizedClazz != null
				&& (reference = this.contextProvider.getBundleContext()
						.getServiceReference(contextualizedClazz)) != null
				&& (this.contextualizedInstance = this.contextProvider.getBundleContext()
						.getService(reference)) != null) {
			return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
					new Class<?>[] { serviceType }, this);
		}
		return null;
	}

	private Class<?> loadClass(String classname) throws ClassNotFoundException {
		Class<?> contextualizedClazz = null;

		String bundleName = classloader.isAFilteredClass(classname);

		if (bundleName != null && Thread.currentThread().getContextClassLoader() != classloader) {

			Bundle[] bundles = this.contextProvider.getBundleContext().getBundles();
			int index = 0;
			int length = bundles == null ? 0 : bundles.length;
			for (; index < length; index++) {
				if (bundleName.equals(bundles[index].getSymbolicName())) {
					try {
						contextualizedClazz = bundles[index].loadClass(classname);

					} catch (ClassNotFoundException e) {
					}
					break;
				}
			}
		} else {
			contextualizedClazz = classloader.loadClass(classname);
		}
		return contextualizedClazz;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String methodName = method.getName();
		Class<?> clazz = method.getDeclaringClass();

		Class<?>[] parameterTypes = method.getParameterTypes();
		Class<?>[] contextualizedParameterTypes = new Class<?>[parameterTypes.length];

		int index = 0;
		int length = parameterTypes.length;

		for (; index < length; index++) {
			if (parameterTypes[index].isPrimitive() || parameterTypes[index] == String.class) {
				contextualizedParameterTypes[index] = parameterTypes[index];
				continue;
			}
			contextualizedParameterTypes[index] = this.loadClass(parameterTypes[index].getCanonicalName());

			// TODO:handle MidProxy object parameters
			if (contextualizedParameterTypes[index] == null
					|| contextualizedParameterTypes[index] != parameterTypes[index]) {
				throw new IllegalArgumentException("Invalid parameter Types ");
			}
		}
		Class<?> contextualizedClazz = this.loadClass(clazz.getCanonicalName());

		Method contextualizedMethod = contextualizedClazz.getMethod(methodName, contextualizedParameterTypes);

		return contextualizedMethod.invoke(this.contextualizedInstance, args);
	}
}
