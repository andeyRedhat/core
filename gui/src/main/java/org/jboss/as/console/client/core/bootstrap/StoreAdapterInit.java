package org.jboss.as.console.client.core.bootstrap;

import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.core.BootstrapContext;
import org.jboss.gwt.flow.client.Control;
import org.jboss.gwt.flow.client.Function;

/**
 * The only purpose of this class is to invoke the ctor of the adapter classes.
 *
 * @author Heiko Braun
 * @date 15/07/14
 */
public class StoreAdapterInit implements Function<BootstrapContext> {

    public StoreAdapterInit() {
        Console.MODULES.getHostStoreAdapter();
        Console.MODULES.getServerStoreAdapter();
        Console.MODULES.getLogStoreAdapter();
        Console.MODULES.getBufferPoolStoreAdapter();
        Console.MODULES.getWorkerStoreAdapter();
        Console.MODULES.getBatchStoreAdapter();
        Console.MODULES.getSubsystemStoreAdapter();
        Console.MODULES.getPerspectiveStoreAdapter();
        Console.MODULES.getRemotingStoreAdapter();
    }

    @Override
    public void execute(Control<BootstrapContext> control) {
        control.proceed();
    }
}