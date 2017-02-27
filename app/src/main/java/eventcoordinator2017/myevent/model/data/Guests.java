package eventcoordinator2017.myevent.model.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Sen on 2/26/2017.
 */

public class Guests extends RealmObject {
    @SerializedName("going")
    private RealmList<User> goingUsers;
    @SerializedName("maybe")
    private RealmList<User> maybeUsers;
    @SerializedName("ignore")
    private RealmList<User> ignoreusers;

    public RealmList<User> getGoingUsers() {
        return goingUsers;
    }

    public void setGoingUsers(RealmList<User> goingUsers) {
        this.goingUsers = goingUsers;
    }

    public RealmList<User> getMaybeUsers() {
        return maybeUsers;
    }

    public void setMaybeUsers(RealmList<User> maybeUsers) {
        this.maybeUsers = maybeUsers;
    }

    public RealmList<User> getIgnoreusers() {
        return ignoreusers;
    }

    public void setIgnoreusers(RealmList<User> ignoreusers) {
        this.ignoreusers = ignoreusers;
    }
}
