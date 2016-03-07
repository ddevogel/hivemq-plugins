package com.artisan.hivemq.plugin;

import com.hivemq.spi.HiveMQPluginModule;
import com.hivemq.spi.PluginEntryPoint;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jackson.map.ObjectMapper;

public class AuthenticationPluginModule extends HiveMQPluginModule {

    /**
     * This method is provided to execute some custom plugin configuration stuff. Is is the place
     * to execute Google Guice bindings,etc if needed.
     */
    @Override
    protected void configurePlugin() {
        bind(ObjectMapper.class).toInstance(new ObjectMapper());
        bind(CloseableHttpClient.class).toInstance(HttpClients.createMinimal());

    }

    /**
     * This method needs to return the main class of the plugin.
     *
     * @return callback priority
     */
    @Override
    protected Class<? extends PluginEntryPoint> entryPointClass() {
        return AuthenticationPluginEntryPoint.class;
    }

}
