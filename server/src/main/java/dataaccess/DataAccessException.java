package dataaccess;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{

    public enum Code {
        BadRequest,
        AlreadyTaken,
        Unauthorized,
        ServerError,
    }

    final private Code code;

    public DataAccessException(Code code, String message) {
        super(message);
        this.code = code;
    }

//    public DataAccessException(String message, Throwable ex) {
//        super(message, ex);
//    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage(), "status", code));
    }

    public static DataAccessException fromJson(String json) {
        var map = new Gson().fromJson(json, HashMap.class);
        var status = Code.valueOf(map.get("status").toString());
        String message = map.get("message").toString();
        return new DataAccessException(status, message);
    }

    public Code code() {
        return code;
    }

    public static Code fromHttpStatusCode(int httpStatusCode) {
        return switch (httpStatusCode) {
            case 400 -> Code.BadRequest;
            case 403 -> Code.AlreadyTaken;
            case 401 -> Code.Unauthorized;
            case 500 -> Code.ServerError;
            default -> throw new IllegalArgumentException("Unknown HTTP status code: " + httpStatusCode);
        };
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
