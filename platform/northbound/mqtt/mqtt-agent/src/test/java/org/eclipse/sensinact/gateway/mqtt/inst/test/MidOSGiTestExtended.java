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
package org.eclipse.sensinact.gateway.mqtt.inst.test;

/**
 * @author <a href="mailto:christophe.munilla@cea.fr">Christophe Munilla</a>
 */
//public class MidOSGiTestExtended extends MidOSGiTest {
public class MidOSGiTestExtended {
    //********************************************************************//
    //						NESTED DECLARATIONS			  			      //
    //********************************************************************//
    //********************************************************************//
    //						ABSTRACT DECLARATIONS						  //
    //********************************************************************//
    //********************************************************************//
    //						STATIC DECLARATIONS							  //
    //********************************************************************//
    //********************************************************************//
    //						INSTANCE DECLARATIONS						  //
    //********************************************************************//
//    private File confDir;
//
//    private final Stack<String> stack = new Stack<>();
//    private Session s = null;
//
//    /**
//     * @throws Exception
//     */
//    public MidOSGiTestExtended(int count) throws Exception {
//        super();
//        super.cacheDir.delete();
//        cacheDir = new File(felixDir, String.format("felix-cache%s", count));
//        if (!cacheDir.exists()) {
//            cacheDir.mkdir();
//        }
//        confDir = new File(felixDir, String.format("conf%s", count));
//        if (!confDir.exists()) {
//            confDir.mkdir();
//        }
//    }
//
//    /**
//     * @throws Exception
//     */
//    @BeforeEach
//    @Override
//    @SuppressWarnings({"unchecked", "rawtypes", "serial"})
//    public void init() throws Exception {
//        final Map configuration = new HashMap();
//        if (System.getSecurityManager() == null) {
//            configuration.put("org.osgi.framework.security", "osgi");
//        }
//
//        configuration.put("felix.cache.rootdir", felixDir.getPath());
//        configuration.put("org.osgi.framework.storage", cacheDir.getName());
//        configuration.put("org.osgi.framework.bootdelegation", "*");
//        configuration.put("org.osgi.framework.system.packages.extra", 
//        	"org.eclipse.sensinact.gateway.test," +
//            "org.slf4j,org.slf4j.impl," + 
//        	"com.sun.net.httpserver," + 
//        	"javax.activation," + 
//        	"javax.net.ssl," + 
//        	"javax.xml.parsers," + 
//        	"javax.imageio," + 
//        	"javax.management," + 
//        	"javax.naming," + 
//        	"javax.sql," + 
//        	"javax.swing," + 
//        	"javax.swing.border," + 
//        	"javax.swing.event," + 
//        	"javax.management.modelmbean," + 
//        	"javax.management.remote," + 
//        	"javax.security.auth," + 
//        	"javax.security.cert," + 
//        	"org.w3c.dom," + 
//        	"org.xml.sax," + 
//        	"org.xml.sax.helpers," + 
//        	"sun.misc," + 
//        	"javax.mail," + 
//        	"javax.mail.internet," + 
//        	"sun.security.action");
//        configuration.put("org.osgi.framework.storage.clean", "onFirstInit");
//        configuration.put("felix.auto.deploy.action", "install");
//        configuration.put("felix.log.level", "4");
//        configuration.put("felix.fileinstall.log.level", "4");
//        configuration.put("felix.fileinstall.dir", confDir.getPath());
//        configuration.put("felix.fileinstall.noInitialDelay", "true");
//        configuration.put("felix.fileinstall.poll", "1000");
//        configuration.put("felix.cm.dir", confDir.getPath());
//        configuration.put("felix.fileinstall.bundles.new.start", "true");
//        configuration.put("org.osgi.framework.startlevel.beginning", "5");
//        configuration.put("felix.startlevel.bundle", "5");
//        configuration.put("org.eclipse.sensinact.simulated.gui.enabled", "false");
//        configuration.put("felix.bootdelegation.classloaders", new HashMap() {
//            public Object get(Object key) {
//                if (Bundle.class.isAssignableFrom(key.getClass())) {
//                    if (MidOSGiTestExtended.this.isExcluded(((Bundle) key).getSymbolicName())) {
//                        return null;
//                    }
//                    return classloader;
//                }
//                return super.get(key);
//            }
//        });
//
//        configuration.put("org.eclipse.sensinact.gateway.test.codeBase", getAllowedCodeBase());
//        this.doInit(configuration);
//
//        final Class<?> factoryClass = classloader.loadClass(FELIX_FRAMEWORK_FACTORY);
//        final Class<?> bundleClass = classloader.loadClass(BUNDLE);
//        final Class<?> autoProcessorClass = classloader.loadClass(AUTO_PROCESSOR);
//
//        frameworkClass = classloader.loadClass(FELIX_FRAMEWORK);
//        if (!new File(bundleDir, "dynamicBundle.jar").exists()) {
//            File manifestFile = new File("./target/generated-test-sources/META-INF/MANIFEST.MF");
//            this.createDynamicBundle(manifestFile, bundleDir, new File[]{new File("./target/classes")});
//        }
//        Object factory = factoryClass.newInstance();
//
//        felix = factoryClass.getDeclaredMethod(FRAMEWORK_FACTORY_INIT_FRAMEWORK, FRAMEWORK_FACTORY_INIT_FRAMEWORK_TYPES).invoke(factory, new Object[]{configuration});
//        frameworkClass.getDeclaredMethod(FRAMEWORK_INIT).invoke(felix);
//
//        context = (BundleContext) bundleClass.getDeclaredMethod(BUNDLE_GET_CONTEXT).invoke(felix);
//        autoProcessorClass.getDeclaredMethod("process", new Class<?>[]{Map.class, BundleContext.class}).invoke(null, new Object[]{configuration, context});
//        frameworkClass.getDeclaredMethod(FRAMEWORK_START).invoke(felix);
//
//        Assert.assertTrue(bundleClass == Bundle.class);
//        Assert.assertTrue(((Integer) bundleClass.getDeclaredMethod(BUNDLE_STATE).invoke(felix)) == Bundle.ACTIVE);
//
//        //the following is needed, to avoid AccessControllerException
//        //TODO: find the elegant (more appropriate) way of doing this
//        try {
//            Method m = felix.getClass().getDeclaredMethod("getSecurityProvider");
//            m.setAccessible(true);
//            Object s = m.invoke(felix);
//
//            Field f = s.getClass().getDeclaredField("m_pai");
//            f.setAccessible(true);
//            Object p = f.get(s);
//
//            Class<?> pic = p.getClass().getClassLoader().loadClass("org.osgi.service.permissionadmin.PermissionInfo");
//
//            m = p.getClass().getDeclaredMethod("setPermissions", new Class<?>[]{String.class, Array.newInstance(pic, 0).getClass()});
//            Object a = Array.newInstance(pic, 1);
//            Object pi = pic.getConstructor(new Class<?>[]{String.class, String.class, String.class}).newInstance(new Object[]{AllPermission.class.getName(), "", ""});
//            Array.set(a, 0, pi);
//            m.invoke(p, new Object[]{"file:target/felix/bundle/org.apache.felix.fileinstall.jar", a});
//            m.invoke(p, new Object[]{"file:target/felix/bundle/org.apache.felix.configadmin.jar", a});
//
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * @inheritDoc
//     * @see org.eclipse.sensinact.gateway.test.MidOSGiTest#doInit(java.util.Map)
//     */
//    @SuppressWarnings({ "unchecked", "rawtypes" })
//	@Override
//    protected void doInit(Map configuration) {
//    	configuration.put("felix.auto.start.1",  
//        "file:target/felix/bundle/org.osgi.service.component.jar "+  
//        "file:target/felix/bundle/org.osgi.service.cm.jar "+  
//        "file:target/felix/bundle/org.osgi.service.metatype.jar "+  
//        "file:target/felix/bundle/org.osgi.namespace.extender.jar "+  
//        "file:target/felix/bundle/org.osgi.util.promise.jar "+  
//        "file:target/felix/bundle/org.osgi.util.function.jar "+  
//        "file:target/felix/bundle/org.osgi.util.pushstream.jar "+
//        "file:target/felix/bundle/org.osgi.service.log.jar "  +
//        "file:target/felix/bundle/org.apache.felix.log.jar " + 
//        "file:target/felix/bundle/org.apache.felix.scr.jar " +
//		"file:target/felix/bundle/org.apache.felix.fileinstall.jar " +
//		"file:target/felix/bundle/org.apache.felix.configadmin.jar " + 
//		"file:target/felix/bundle/org.apache.felix.framework.security.jar ");
//        configuration.put("felix.auto.install.2", 
//        "file:target/felix/bundle/slf4j-api.jar "
//        + "file:target/felix/bundle/slf4j-impl.jar  "
//        + "file:target/felix/bundle/sensinact-utils.jar " 
//        + "file:target/felix/bundle/sensinact-common.jar " 
//        + "file:target/felix/bundle/sensinact-datastore-api.jar "
//        + "file:target/felix/bundle/sensinact-framework-extension.jar "  
//        + "file:target/felix/bundle/sensinact-test-configuration.jar " 
//        + "file:target/felix/bundle/sensinact-security-none.jar " 
//        + "file:target/felix/bundle/sensinact-generic.jar ");
//        configuration.put("felix.auto.start.2",
//        "file:target/felix/bundle/sensinact-signature-validator.jar " 
//        + "file:target/felix/bundle/sensinact-core.jar ");        
//        configuration.put("felix.auto.install.3",
//        "file:target/felix/bundle/org.eclipse.paho.client.mqttv3.jar " 
//        + "file:target/felix/bundle/mqtt-generic-agent.jar ");    
//        configuration.put("felix.auto.start.3","file:target/felix/bundle/dynamicBundle.jar ");
//        configuration.put("felix.auto.start.4", "file:target/felix/bundle/slider.jar " );
//
//        configuration.put("org.osgi.framework.system.packages.extra",
//          "javax.activation, "
//        + "javax.mail,"
//        + "javax.mail.internet,"
//        + "javax.swing, "
//        + "sun.misc");
//        configuration.put("org.eclipse.sensinact.gateway.security.jks.filename", "target/felix/bundle/keystore.jks");
//        configuration.put("org.eclipse.sensinact.gateway.security.jks.password", "sensiNact_team");
//    }
//
//    /**
//     * @inheritDoc
//     * @see org.eclipse.sensinact.gateway.test.MidOSGiTest#isExcluded(java.lang.String)
//     */
//    @Override
//    protected boolean isExcluded(String filename) {
//        if ("org.apache.felix.framework.security.jar".equals(filename)) {
//            return true;
//        }
//        return false;
//    }
//
//    public void moveSlider(int value) throws ClassNotFoundException, IOException, InvalidSyntaxException {
//        MidProxy<SliderSetterItf> sliderProxy = new MidProxy<SliderSetterItf>(classloader, this, SliderSetterItf.class);
//        SliderSetterItf slider = sliderProxy.buildProxy();
//        slider.move(value);
//    }
//
//    public String get(String provider, String service, String resource) throws ClassNotFoundException, IOException, InvalidSyntaxException {
//        MidProxy<Core> mid = new MidProxy<Core>(classloader, this, Core.class);
//
//        Core core = mid.buildProxy();
//        if(s == null) {
//        	s = core.getAnonymousSession();
//        }
//        MidProxy<Session> mids = (MidProxy<Session>) Proxy.getInvocationHandler(s);
//        try {
//            Object o = mids.toOSGi(Session.class.getDeclaredMethod("get", 
//            	new Class<?>[]{String.class, String.class, String.class, String.class, Object[].class}), 
//            	new Object[]{provider, service, resource, DataResource.VALUE, (Object[])null});
//            Object j = o.getClass().getDeclaredMethod("getJSON").invoke(o);
//            System.out.println(j);
//            return (String) j;
//
//        } catch (Throwable e) {
//            //e.printStackTrace();
//        }
//        return null;
//    }
//
//    public String set(String provider, String service, String resource, Object value) throws Exception {
//        MidProxy<Core> mid = new MidProxy<Core>(classloader, this, Core.class);
//        Core core = mid.buildProxy();
//        if(s == null) {
//        	s = core.getAnonymousSession();
//        }
//        MidProxy<Session> mids = (MidProxy<Session>) Proxy.getInvocationHandler(s);
//        try {
//            Object o = mids.toOSGi(Session.class.getDeclaredMethod("set", 
//            	new Class<?>[]{String.class, String.class, String.class, String.class, Object.class,
//            	Object[].class}), new Object[]{provider, service, resource, DataResource.VALUE, value, (Object[])null});
//            Object j = o.getClass().getDeclaredMethod("getJSON").invoke(o);
//            System.out.println(j);
//            return (String) j;
//
//        } catch (Throwable e) {
//            //e.printStackTrace();
//        }
//        return null;
//    }
//
//    public String act(String provider, String service, String resource, Object... args) throws Exception {
//        MidProxy<Core> mid = new MidProxy<Core>(classloader, this, Core.class);
//        Core core = mid.buildProxy();
//        if(s == null) {
//        	s = core.getAnonymousSession();
//        }
//        MidProxy<Session> mids = (MidProxy<Session>) Proxy.getInvocationHandler(s);
//        try {
//            Object o = mids.toOSGi(Session.class.getDeclaredMethod("act", 
//            	new Class<?>[]{String.class, String.class, String.class, Object[].class}),
//            	new Object[]{provider, service, resource, args});
//            Object j = o.getClass().getDeclaredMethod("getJSON").invoke(o);
//            System.out.println(j);
//            return (String) j;
//
//        } catch (Throwable e) {
//            //e.printStackTrace();
//        }
//        return null;
//    }
//
//    public String subscribe(String provider, String service, String resource, Recipient recipient) throws Exception {
//        MidProxy<Core> mid = new MidProxy<Core>(classloader, this, Core.class);
//        Core core = mid.buildProxy();
//        if(s == null) {
//        	s = core.getAnonymousSession();
//        }
//        MidProxy<Session> mids = (MidProxy<Session>) Proxy.getInvocationHandler(s);
//        try {
//            Object o = mids.toOSGi(Session.class.getDeclaredMethod("subscribe", 
//            	new Class<?>[]{String.class, String.class, String.class, Recipient.class, JSONArray.class, Object[].class}), 
//            	new Object[]{provider, service, resource, recipient, null, (Object[])null});
//            Object j = o.getClass().getDeclaredMethod("getJSON").invoke(o);
//            System.out.println(j);
//            return (String) j;
//
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public String unsubscribe(String provider, String service, String resource, String subscriptionId) 
//    		throws ClassNotFoundException, IOException, InvalidSyntaxException {
//        MidProxy<Core> mid = new MidProxy<Core>(classloader, this, Core.class);
//        Core core = mid.buildProxy();
//        if(s == null) {
//        	s = core.getAnonymousSession();
//        }
//        MidProxy<Session> mids = (MidProxy<Session>) Proxy.getInvocationHandler(s);
//        try {
//            Object o = mids.toOSGi(Session.class.getDeclaredMethod("unsubscribe", 
//            	new Class<?>[]{String.class, String.class, String.class, String.class, Object[].class}), 
//            	new Object[]{provider, service, resource, subscriptionId, (Object[])null});
//            Object j = o.getClass().getDeclaredMethod("getJSON").invoke(o);
//            System.out.println(j);
//            return (String) j;
//
//        } catch (Throwable e) {
//            //e.printStackTrace();
//        }
//        return null;
//    }
//
//    public String providers() throws Throwable {
//        MidProxy<Core> mid = new MidProxy<Core>(classloader, this, Core.class);
//        Core core = mid.buildProxy();
//        if(s == null) {
//        	s = core.getAnonymousSession();
//        }
//        MidProxy<Session> mids = (MidProxy<Session>) Proxy.getInvocationHandler(s);
//        Method m = Session.class.getMethod("getProviders");
//        Object o = mids.toOSGi(m, null);
//        Object j = o.getClass().getDeclaredMethod("getJSON").invoke(o);
//        return (String) j;
//    }
//
//    public void cleanAgent() throws Throwable {
//    	synchronized(this.stack) {
//        	this.stack.clear();
//        }
//    }
//
//    public List<String> listAgentMessages() {
//        List<String> messages = new ArrayList<>();
//        synchronized(this.stack) {
//        	messages.addAll(this.stack);
//        }
//        return Collections.unmodifiableList(messages);
//    }
}
