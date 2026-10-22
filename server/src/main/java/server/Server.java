package server;

import dataaccess.MemoryDataAccess;
import io.javalin.*;
import service.Service;
import model.*;

public class Server {

    private final Service service;
    private final Javalin javalin;

    public Server() {
        this(new Service(new MemoryDataAccess()));
    }

    public Server(Service service) {
        this.service = service;
        javalin = Javalin.create(config -> config.staticFiles.add("web"));


    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
