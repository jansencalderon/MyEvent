package eventcoordinator2017.myevent.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Mark Jansen Calderon on 1/27/2017.
 */

public class Event extends RealmObject{

    @PrimaryKey
    @SerializedName("event_id")
    @Expose
    private String eventId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("package_id")
    @Expose
    private String packageId;
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
    @SerializedName("loc_name")
    @Expose
    private String locName;
    @SerializedName("loc_lat")
    @Expose
    private String locLat;
    @SerializedName("loc_long")
    @Expose
    private String locLong;
    @SerializedName("image_directory")
    @Expose
    private String imageDirectory;
    @SerializedName("packages")
    @Expose
    private Package packages;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
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

    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    public String getLocLat() {
        return locLat;
    }

    public void setLocLat(String locLat) {
        this.locLat = locLat;
    }

    public String getLocLong() {
        return locLong;
    }

    public void setLocLong(String locLong) {
        this.locLong = locLong;
    }

    public String getImageDirectory() {
        return imageDirectory;
    }

    public void setImageDirectory(String imageDirectory) {
        this.imageDirectory = imageDirectory;
    }

    public Package getPackages() {
        return packages;
    }

    public void setPackages(Package packages) {
        this.packages = packages;
    }
}
