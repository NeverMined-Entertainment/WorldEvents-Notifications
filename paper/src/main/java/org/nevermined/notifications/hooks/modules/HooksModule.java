package org.nevermined.notifications.hooks.modules;

import com.google.inject.AbstractModule;
import org.nevermined.notifications.hooks.Placeholders;

public class HooksModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Placeholders.class);
    }
}
