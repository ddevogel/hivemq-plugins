package com.artisan.hivemq.plugin;

import com.artisan.hivemq.callbacks.AuthenticationCallback;
import com.artisan.hivemq.callbacks.AuthorizationCallback;
import com.artisan.hivemq.callbacks.advanced.AddSubscriptionOnClientConnect;
import com.hivemq.spi.PluginEntryPoint;
import com.hivemq.spi.callback.registry.CallbackRegistry;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class AuthenticationPluginEntryPoint extends PluginEntryPoint {

    private final AuthenticationCallback authenticationCallback;
    private final AuthorizationCallback authorizationCallback;
    private final AddSubscriptionOnClientConnect addSubscriptionOnClientConnect;

    @Inject
    public AuthenticationPluginEntryPoint(
            final AuthenticationCallback authenticationCallback,
            final AuthorizationCallback authorizationCallback,
            final AddSubscriptionOnClientConnect addSubscriptionOnClientConnect){
        this.authenticationCallback = authenticationCallback;
        this.authorizationCallback = authorizationCallback;
        this.addSubscriptionOnClientConnect = addSubscriptionOnClientConnect;
    }

    /**
     * This method is executed after the instantiation of the whole class. It is used to initialize
     * the implemented callbacks and make them known to the HiveMQ core.
     */
    @PostConstruct
    public void postConstruct() {
        CallbackRegistry callbackRegistry = getCallbackRegistry();
        callbackRegistry.addCallback(authenticationCallback);
        callbackRegistry.addCallback(authorizationCallback);
        //callbackRegistry.addCallback(addSubscriptionOnClientConnect);
    }
}
