/*
 * Copyright (c) 2017 CEA.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    CEA - initial API and implementation
 */
package org.eclipse.sensinact.gateway.device.mosquitto.lite.sensinact;

import org.eclipse.sensinact.gateway.device.mosquitto.lite.Activator;
import org.eclipse.sensinact.gateway.device.mosquitto.lite.client.MQTTClient;
import org.eclipse.sensinact.gateway.device.mosquitto.lite.client.ServerConnectionCache;
import org.eclipse.sensinact.gateway.device.mosquitto.lite.device.MQTTPropertyFileConfig;
import org.eclipse.sensinact.gateway.device.mosquitto.lite.model.Provider;
import org.eclipse.sensinact.gateway.device.mosquitto.lite.model.Resource;
import org.eclipse.sensinact.gateway.device.mosquitto.lite.model.Service;
import org.eclipse.sensinact.gateway.device.mosquitto.lite.model.mqtt.MQTTBroker;
import org.eclipse.sensinact.gateway.device.mosquitto.lite.runtime.MQTTManagerRuntime;
import org.eclipse.sensinact.gateway.device.mosquitto.lite.smartTopic.SmartTopic;
import org.eclipse.sensinact.gateway.generic.ProtocolStackEndpoint;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Tracker responsible for detecting when new OSGi Service instance that configures an MQTT topic monitoring
 * @author <a href="mailto:Jander.BOTELHODONASCIMENTO@cea.fr">Jander Botelho do Nascimento</a>
 */
public class MQTTPropertyFileConfigTracker implements ServiceTrackerCustomizer {

    private static final Logger LOG = LoggerFactory.getLogger(Activator.class);
    private final BundleContext bundleContext;
    private final MQTTManagerRuntime runtime;
    private Map<String,ServiceRegistration> registration=new HashMap<>();
    /**
     * This is the list that will contains the processor formats supported by the mosquitto bridge.
     */
    public MQTTPropertyFileConfigTracker(ProtocolStackEndpoint<MQTTPacket> connector, BundleContext bundleContext,MQTTManagerRuntime runtime) {
        this.bundleContext=bundleContext;
        this.runtime=runtime;
    }

    private Provider buildProvider(MQTTPropertyFileConfig configFile) throws Exception{

        Provider provider=new Provider();
        provider.setName(configFile.getId());
        Service serviceAdmin=new Service(provider);
        serviceAdmin.setName("admin");
        Service serviceInfo=new Service(provider);
        serviceInfo.setName("info");
        provider.getServices().add(serviceAdmin);
        provider.getServices().add(serviceInfo);

        if(configFile.getProtocol()!=null){
            try {
                provider.getBroker().setProtocol(MQTTBroker.PROTOCOL.valueOf(configFile.getProtocol()));
            }catch(IllegalArgumentException e){
                LOG.error("Security protocol {} for MQTT broker {} does not exist, possible values are ssl/tcp, configuration was ignored", configFile.getProtocol(), configFile.getId());
                return null;
            }
        }else {
            provider.getBroker().setProtocol(MQTTBroker.PROTOCOL.tcp);
        }

        if(configFile.getHost()!=null){
            provider.getBroker().setHost(configFile.getHost());
        }else {
            provider.getBroker().setHost("127.0.0.1");
        }

        if(configFile.getPort()!=null){
            provider.getBroker().setPort(configFile.getPort());
        }else {
            provider.getBroker().setPort(1883l);
        }

        if(configFile.getUsername()!=null){
            provider.getBroker().getAuth().setUsername(configFile.getUsername());
        }else {
            //No default value for username or password
        }

        if(configFile.getPassword()!=null){
            provider.getBroker().getAuth().setPassword(configFile.getPassword());
        }else {
            //No default value for username or password
        }

        provider.setIsDiscoveryOnFirstMessage(configFile.getDiscoveryOnFirstMessage());

        if(configFile!=null&&configFile.getLatitude()!=null&&configFile.getLongitude()!=null)
            try {
                Resource locationResource=new Resource(serviceAdmin);
                locationResource.setName("location");
                locationResource.setValue(String.format("%s:%s",configFile.getLatitude(),configFile.getLongitude()));
                serviceAdmin.getResources().add(locationResource);
            }catch(Exception e){
                LOG.error("Failed to load location from device {}",configFile.getId(),e);
            }
        else {
            LOG.info("Latitude or longitude are null for {}",configFile.getId());
        }

        try {
            Resource valueResource=new Resource(serviceInfo);
            valueResource.setName("value");
            valueResource.setTopic(configFile.getTopic());
            valueResource.setProcessor(configFile.getProcessor());
            serviceInfo.getResources().add(valueResource);
        }catch(Exception e){
            LOG.info("Failed to process info/value received for device {}",configFile.getId());
        }

        if(configFile.getTopicType().equals("smarttopic")){
            LOG.info("This topic config {} is a SmartTopic",configFile.getId());
            final MQTTClient client= ServerConnectionCache.getInstance(configFile.getId(), provider.getBroker());
            SmartTopic st=new SmartTopic(configFile.getTopic(),client.getConnection(),this.runtime);
            if(configFile.getProcessor()!=null){
                st.setProcessor(configFile.getProcessor());
            }
            st.activate();
            LOG.info("SmartTopic service started.");
            return null;
        }else {
            return provider;
        }

    }

    @Override
    public Object addingService(ServiceReference serviceReference) {
        LOG.info("Attaching MQTT Bus service");
        final MQTTPropertyFileConfig configFile=(MQTTPropertyFileConfig)bundleContext.getService(serviceReference);
        LOG.debug("Updating MQTT Bus service {}",configFile.getId());

        Provider provider= null;
        try {
            provider = buildProvider(configFile);
            Dictionary<String,String> properties=new Hashtable<String,String>();

            if(provider!=null){
                registration.put(serviceReference.getProperty("service.pid").toString(),bundleContext.registerService(Provider.class,provider,properties));
            }

        } catch (Exception e) {
            LOG.error("Failed to create MQTT device for file {}",configFile.getId(),e);
        }

        /***
        try {

            String instanceFileName=serviceReference.getProperty("service.pid").toString();
            final MQTTClient client=ServerConnectionCache.getInstance(instanceFileName,busClient.getHost(),busClient.getPort());
            LOG.info("Subscribing to topic: {}", busClient.getTopic());
            client.getConnection().subscribe(busClient.getTopic(),new IMqttMessageListener(){
                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    LOG.info("message received: {}", new String(mqttMessage.getPayload()));
                    try {
                        String payload=new String(mqttMessage.getPayload());
                        MQTTPacket packet = null;
                        if(busClient.getProcessor()!=null){
                            List<SelectorIface> selectors= ProcessorUtil.transformProcessorListInSelector(busClient.getProcessor());
                            ProcessorExecutor processor=new ProcessorExecutor(supportedProcessorFormat);
                            payload=processor.execute(payload, selectors);
                            processData(busClient, busClient.getId(), payload);
                        }else {
                            processData(busClient, busClient.getId(), payload);
                        }
                    }catch (Exception e){
                        LOG.error("Failed to process MQTT message",e);
                    }

                }
            });
            LOG.info("Subscribed to topic: {}", busClient.getTopic());

            if(!busClient.getDiscoveryOnFirstMessage()){
                processData(busClient, busClient.getId(),null);
            }else {
                LOG.info("Device {} ({}) will appear as soon as the topic associated received the first message",busClient.getId(),busClient.getTopic());
            }

            LOG.info("Sensinact Device created with the id {}",generateID(busClient));
        } catch (Exception e) {
            LOG.warn("Failed to create device {}, ignoring device",generateID(busClient),e);
        }
         */

        return bundleContext.getService(serviceReference);
    }

    @Override
    public void modifiedService(ServiceReference serviceReference, Object o) {
        //Not used
    }

    @Override
    public void removedService(ServiceReference serviceReference, Object o) {


        ServiceRegistration record=registration.get(serviceReference.getProperty("service.pid").toString());

        LOG.info("Detaching devices MQTT Bus service");

        if(record!=null){

            try{
                record.unregister();
                LOG.info("Sensinact Device {} removed", ((MQTTPropertyFileConfig)o).getId());
            }catch (Exception e){
                LOG.error("Failed to read sensinact package", e);
            }

        }

    }
}
