package server;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import io.javalin.*;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import service.Service;
import model.*;

import java.lang.reflect.Field;

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
                .post("/session", this::login)
                .delete("/session", this::logout)
                .get("/game", this::listGames)
                .post("/game", this::createGame)
                .put("/game", this::joinGame)
                .exception(DataAccessException.class, this::exceptionHandler);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void exceptionHandler(DataAccessException e, Context ctx) {
        ctx.status(e.toHttpStatusCode());
        ctx.result(e.toJson());
    }

    private void delete(@NotNull Context ctx) throws DataAccessException {
        service.clear();
        ctx.status(200);
    }

    private void register(@NotNull Context ctx) throws DataAccessException, IllegalAccessException {
        UserData userData = new Gson().fromJson(ctx.body(), UserData.class);
        jsonToObjectCheck(userData);
        AuthData authData = service.createUser(userData);
        ctx.result(authData.toString());
    }

    private void login(@NotNull Context ctx) throws DataAccessException, IllegalAccessException {
        LoginRequest loginRequest = new Gson().fromJson(ctx.body(), LoginRequest.class);
        jsonToObjectCheck(loginRequest);
        AuthData authData = service.loginUser(loginRequest);
        ctx.result(authData.toString());
    }

    private void logout(@NotNull Context ctx) throws DataAccessException {
        String authToken = ctx.header("Authorization");
        service.logoutUser(authToken);
        ctx.status(200);
    }

    private void listGames(@NotNull Context ctx) throws DataAccessException {
        String authToken = ctx.header("Authorization");
        ListGamesResponse listGamesResponse = service.listGames(authToken);
        ctx.result(listGamesResponse.toString());
    }

    private void createGame(@NotNull Context ctx) throws DataAccessException, IllegalAccessException {
        String authToken = ctx.header("Authorization");
        CreateGameRequest createGameRequest = new Gson().fromJson(ctx.body(), CreateGameRequest.class);
        jsonToObjectCheck(createGameRequest);
        GameData gameData = service.createGame(authToken, createGameRequest.gameName());
        ctx.result(gameData.gameIDJson());
    }

    private void joinGame(@NotNull Context ctx) throws DataAccessException, IllegalAccessException {
        String authToken = ctx.header("Authorization");
        JoinGameRequest joinGameRequest = new Gson().fromJson(ctx.body(), JoinGameRequest.class);
        jsonToObjectCheck(joinGameRequest);
        GameData gameData = service.updateGame(authToken, joinGameRequest);
        ctx.status(200);
    }

    private void jsonToObjectCheck(Object requestObject) throws DataAccessException, IllegalAccessException {
        Class<?> clazz = requestObject.getClass();
        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.get(requestObject) == null) {
                    throw new DataAccessException(DataAccessException.Code.BadRequest);
                }
            }
        } catch (JsonParseException e) {
            throw new DataAccessException(DataAccessException.Code.BadRequest);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessException("cant access class fields");
        }
    }


}
