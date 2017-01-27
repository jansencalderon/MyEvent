package eventcoordinator2017.myevent.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Mark Jansen Calderon on 1/27/2017.
 */

public class Item extends RealmObject{

    @PrimaryKey
    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("item_description")
    @Expose
    private String itemDescription;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

}
