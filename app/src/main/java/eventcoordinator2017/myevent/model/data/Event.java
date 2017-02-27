package eventcoordinator2017.myevent.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Mark Jansen Calderon on 1/27/2017.
 */

public class Event extends RealmObject{

    @PrimaryKey
    @SerializedName("event_id")
    @Expose
    private Integer eventId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("package_id")
    @Expose
    private Integer packageId;
    @SerializedName("event_name")
    @Expose
    private String eventName;
    @SerializedName("event_description")
    @Expose
    private String eventDescription;
    @SerializedName("event_tags")
    @Expose
    private String eventTags;
    @SerializedName("event_date_from")
    @Expose
    private String eventDateFrom;
    @SerializedName("event_date_to")
    @Expose
    private String eventDateTo;
    @SerializedName("loc_id")
    @Expose
    private Integer locId;
    @SerializedName("image_directory")
    @Expose
    private String imageDirectory;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("packages")
    @Expose
    private Package packages;
    @SerializedName("guests")
    private RealmList<User> guests;

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventTags() {
        return eventTags;
    }

    public void setEventTags(String eventTags) {
        this.eventTags = eventTags;
    }

    public String getEventDateFrom() {
        return eventDateFrom;
    }

    public void setEventDateFrom(String eventDateFrom) {
        this.eventDateFrom = eventDateFrom;
    }

    public String getEventDateTo() {
        return eventDateTo;
    }

    public void setEventDateTo(String eventDateTo) {
        this.eventDateTo = eventDateTo;
    }

    public Integer getLocId() {
        return locId;
    }

    public void setLocId(Integer locId) {
        this.locId = locId;
    }

    public String getImageDirectory() {
        return imageDirectory;
    }

    public void setImageDirectory(String imageDirectory) {
        this.imageDirectory = imageDirectory;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Package getPackages() {
        return packages;
    }

    public void setPackages(Package packages) {
        this.packages = packages;
    }


    public RealmList<User> getGuests() {
        return guests;
    }

    public void setGuests(RealmList<User> guests) {
        this.guests = guests;
    }
}
