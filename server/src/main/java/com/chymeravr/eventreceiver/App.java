package com.chymeravr.eventreceiver;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.chymeravr.eventreceiver.guice.CacheModule;
import com.chymeravr.eventreceiver.guice.ProcessingModule;
import com.chymeravr.eventreceiver.rqhandler.V1EntryPoint;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

public class App {
    @Parameter(names = {"--config", "-c"}, description = "config file", required = true)
    private String configFilePath;

    @Parameter(names = {"--port", "-p"}, description = "port", required = true)
    private int port;

    public static void main(String... args) throws Exception {
        App app = new App();
        new JCommander(app, args);
        app.run();
    }

    private void run() throws Exception {
        Server server = new Server(port);

        Injector injector = Guice.createInjector(new CacheModule(this.configFilePath), new ProcessingModule());
        V1EntryPoint v1EntryPoint = injector.getInstance(V1EntryPoint.class);

        ContextHandler context = new ContextHandler();
        context.setContextPath("/api/v1/ads/");
        context.setHandler(v1EntryPoint);

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[]{context});

        server.setHandler(contexts);

        server.start();
        server.join();
    }
}