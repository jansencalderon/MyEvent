package eventcoordinator2017.myevent.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Mark Jansen Calderon on 1/27/2017.
 */

public class Package extends RealmObject {

    @PrimaryKey
    @SerializedName("package_id")
    @Expose
    private String packageId;
    @SerializedName("company_id")
    @Expose
    private String companyId;
    @SerializedName("package_name")
    @Expose
    private String packageName;
    @SerializedName("package_type")
    @Expose
    private String packageType;
    @SerializedName("package_price")
    @Expose
    private String packagePrice;
    @SerializedName("category")
    @Expose
    private RealmList<Category> category;

    private int packagePriceInt;

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }

    public RealmList<Category> getCategory() {
        return category;
    }

    public void setCategory(RealmList<Category> category) {
        this.category = category;
    }

    public int getPackagePriceInt() {
        return Integer.parseInt(packagePrice);
    }
}
