package shared;

import java.io.Serializable;
import java.lang.String;

public class Response implements Serializable {

    private String message;

    public Response(String message) {
        // this.message = message.substring(0,1).toUpperCase() + message.substring(1).toLowerCase();
        this.message = message;
    }

    public String toString() { return this.message; }
}