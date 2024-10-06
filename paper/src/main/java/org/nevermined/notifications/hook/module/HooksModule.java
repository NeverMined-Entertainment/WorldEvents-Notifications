package org.nevermined.notifications.hook.module;

import com.google.inject.AbstractModule;
import org.nevermined.notifications.hook.Placeholders;

public class HooksModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Placeholders.class);
    }
}
