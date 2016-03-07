package com.artisan.hivemq.plugin;

import com.hivemq.spi.HiveMQPluginModule;
import com.hivemq.spi.PluginEntryPoint;


public class SystemModule extends HiveMQPluginModule {
    @Override
    protected void configurePlugin() {}

    @Override
    protected Class<? extends PluginEntryPoint> entryPointClass() {
        return SystemEntryPoint.class;
    }

}
