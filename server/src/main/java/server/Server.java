package server;

import com.google.gson.Gson;
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
                .delete("/db", this::delete)
                .post("/user", this::register)
                .post("/session", this::login);
//                .delete("/session", this::logout)
//                .get("/game", this::listGames)
//                .post("/game", this::createGame)
//                .put("/game", this::joinGame)
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

    private void register(@NotNull Context ctx) throws DataAccessException {
        UserData userData = new Gson().fromJson(ctx.body(), UserData.class);
        AuthData authData = service.createUser(userData);
        ctx.result(authData.toString());
    }

    private void login(@NotNull Context ctx) {
        LoginData loginData = new Gson().fromJson(ctx.body(), LoginData.class);
        AuthData authData = service.loginUser(loginData);
        ctx.result(authData.toString());
    }

}
