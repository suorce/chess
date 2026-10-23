package dataaccess;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{

    public enum Code {
        BadRequest("Error: bad request"),
        AlreadyTaken("Error: already taken"),
        Unauthorized("Error: unauthorized"),
        ServerError("Error: server error");

        private final String message;

        Code(String message) {
            this.message = message;
        }

        public String message() {
            return message;
        }
    }

    final private Code code;

    public DataAccessException(Code code) {
        super(code.message());
        this.code = code;
    }

    public Code code() {
        return code;
    }

    public String message() {
        return code.message();
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage(), "status", code));
    }

    public int toHttpStatusCode() {
        return switch (code) {
            case BadRequest -> 400;
            case AlreadyTaken -> 403;
            case Unauthorized -> 401;
            case ServerError -> 500;
        };
    }
}
