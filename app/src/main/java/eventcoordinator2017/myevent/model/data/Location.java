package eventcoordinator2017.myevent.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Mark Jansen Calderon on 2/14/2017.
 */

public class Location extends RealmObject {
    @PrimaryKey
    @SerializedName("loc_id")
    @Expose
    private Integer locId;
    @SerializedName("loc_name")
    @Expose
    private String locName;
    @SerializedName("loc_address")
    @Expose
    private String locAddress;
    @SerializedName("loc_lat")
    @Expose
    private Double locLat;
    @SerializedName("loc_long")
    @Expose
    private Double locLong;
    @SerializedName("loc_features")
    @Expose
    private String locFeatures;
    @SerializedName("loc_catered_events")
    @Expose
    private String locCateredEvents;
    @SerializedName("loc_venue_type")
    @Expose
    private String locVenueType;
    @SerializedName("loc_setup")
    @Expose
    private String locSetup;
    @SerializedName("loc_capacity")
    @Expose
    private Integer locCapacity;
    @SerializedName("loc_image")
    @Expose
    private String locImage;

    public Integer getLocId() {
        return locId;
    }

    public void setLocId(Integer locId) {
        this.locId = locId;
    }

    public String getLocName() {
        return locName.trim();
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    public String getLocAddress() {
        return locAddress.trim();
    }

    public void setLocAddress(String locAddress) {
        this.locAddress = locAddress;
    }

    public Double getLocLat() {
        return locLat;
    }

    public void setLocLat(Double locLat) {
        this.locLat = locLat;
    }

    public Double getLocLong() {
        return locLong;
    }

    public void setLocLong(Double locLong) {
        this.locLong = locLong;
    }

    public String getLocFeatures() {
        return locFeatures;
    }

    public void setLocFeatures(String locFeatures) {
        this.locFeatures = locFeatures;
    }

    public String getLocCateredEvents() {
        return locCateredEvents;
    }

    public void setLocCateredEvents(String locCateredEvents) {
        this.locCateredEvents = locCateredEvents;
    }

    public String getLocVenueType() {
        return locVenueType;
    }

    public void setLocVenueType(String locVenueType) {
        this.locVenueType = locVenueType;
    }

    public String getLocSetup() {
        return locSetup.trim();
    }

    public void setLocSetup(String locSetup) {
        this.locSetup = locSetup;
    }

    public Integer getLocCapacity() {
        return locCapacity;
    }

    public void setLocCapacity(Integer locCapacity) {
        this.locCapacity = locCapacity;
    }

    public String getLocImage() {
        return locImage;
    }

    public void setLocImage(String locImage) {
        this.locImage = locImage;
    }
}
