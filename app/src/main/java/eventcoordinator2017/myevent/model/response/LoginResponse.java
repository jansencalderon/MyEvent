package eventcoordinator2017.myevent.model.response;


import eventcoordinator2017.myevent.model.data.User;

/**
 * Created by Mark Jansen Calderon on 1/10/2017.
 */

public class LoginResponse extends ResultResponse {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
