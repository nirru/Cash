package com.oxilo.cash.modal;

/**
 * Created by ericbasendra on 02/12/15.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductList implements Parcelable{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("taxclass")
    @Expose
    private String taxclass;

    private boolean isChecked;

    private int currentCount;

    protected ProductList(Parcel in) {
        id = in.readString();
        name = in.readString();
        price = in.readString();
        taxclass = in.readString();
        isChecked = in.readByte() != 0;
        currentCount = in.readInt();
    }

    public static final Creator<ProductList> CREATOR = new Creator<ProductList>() {
        @Override
        public ProductList createFromParcel(Parcel in) {
            return new ProductList(in);
        }

        @Override
        public ProductList[] newArray(int size) {
            return new ProductList[size];
        }
    };

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The price
     */
    public String getPrice() {
        return price;
    }

    /**
     *
     * @param price
     * The price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     *
     * @return
     * The taxclass
     */
    public String getTaxclass() {
        return taxclass;
    }

    /**
     *
     * @param taxclass
     * The taxclass
     */
    public void setTaxclass(String taxclass) {
        this.taxclass = taxclass;
    }

    public boolean getChecked(){
        return isChecked;
    }

    public void setChecked(boolean isChecked){
        this.isChecked = isChecked;
    }


    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(taxclass);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeInt(currentCount);
    }
}