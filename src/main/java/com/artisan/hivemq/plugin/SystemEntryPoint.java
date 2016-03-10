package com.artisan.hivemq.plugin;

import com.artisan.hivemq.callbacks.*;
import com.hivemq.spi.PluginEntryPoint;
import com.hivemq.spi.callback.registry.CallbackRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class SystemEntryPoint extends PluginEntryPoint {
    Logger log = LoggerFactory.getLogger(SystemEntryPoint.class);

    private final ClientConnect clientConnect;
    private final ClientDisconnect clientDisconnect;
    private final PublishReceived publishReceived;
    private final SimpleScheduledCallback simpleScheduledCallback;
    private final HiveMQStart hiveMQStart;

    @Inject
    public SystemEntryPoint(
            final ClientConnect clientConnect,
            final ClientDisconnect clientDisconnect,
            final PublishReceived publishReceived,
            final SimpleScheduledCallback simpleScheduledCallback,
            final HiveMQStart hiveMQStart){
        this.clientConnect = clientConnect;
        this.clientDisconnect = clientDisconnect;
        this.publishReceived = publishReceived;
        this.simpleScheduledCallback = simpleScheduledCallback;
        this.hiveMQStart = hiveMQStart;
    }

    /**
     * This method is executed after the instantiation of the whole class. It is used to initialize
     * the implemented callbacks and make them known to the HiveMQ core.
     */
    @PostConstruct
    public void postConstruct() {
        CallbackRegistry callbackRegistry = getCallbackRegistry();
        callbackRegistry.addCallback(hiveMQStart);
        callbackRegistry.addCallback(clientConnect);
        callbackRegistry.addCallback(clientDisconnect);
        callbackRegistry.addCallback(publishReceived);
        callbackRegistry.addCallback(simpleScheduledCallback);
    }

}
