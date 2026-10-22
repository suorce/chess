package server;

import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import io.javalin.*;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
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
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db", this::delete);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void delete(@NotNull Context ctx) throws DataAccessException {
        service.clear();
        ctx.status(200);
    }
}
