package shared;

import java.io.Serializable;
import java.lang.String;

public class AuthentificationRequest  implements Serializable {

    private String username, password;
    private boolean accountExist;
    

    public AuthentificationRequest(String username, String password,boolean accountExist) {
        this.username = username;
        this.password = password;
        this.accountExist =accountExist;
        
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public boolean getAccountExist() {
        return accountExist;
    }
}
