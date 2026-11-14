package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.*;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void clear() throws Exception {

    }

    public AuthData register() throws Exception {
        return null;
    }

    public AuthData login() throws Exception {
        return null;
    }

    public void logout() throws Exception {

    }

    public ListGamesResponse listGames() throws Exception {
        return null;
    }

    public String createGame() throws Exception {
        return null;
    }

    public GameData joinGame() throws Exception {
        return null;
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws DataAccessException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw DataAccessException.fromJson(body);
            }

            throw new DataAccessException(DataAccessException.fromHttpStatusCode(status));
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
