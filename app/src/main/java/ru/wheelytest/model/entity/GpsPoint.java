package ru.wheelytest.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Yuriy Chekashkin
 */
public class GpsPoint implements Parcelable {

    public static final Creator<GpsPoint> CREATOR = new Creator<GpsPoint>() {
        @Override
        public GpsPoint createFromParcel(Parcel in) {
            return new GpsPoint(in);
        }

        @Override
        public GpsPoint[] newArray(int size) {
            return new GpsPoint[size];
        }
    };

    @Expose(serialize = false, deserialize = true)
    private long id;

    @SerializedName("lat")
    private float latitude;

    @SerializedName("lon")
    private float longitude;

    public GpsPoint(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GpsPoint(double latitude, double longitude) {
        this.latitude = (float) latitude;
        this.longitude = (float) longitude;
    }

    private GpsPoint(Parcel in) {
        id = in.readLong();
        latitude = in.readFloat();
        longitude = in.readFloat();
    }

    public long getId() {
        return id;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeFloat(latitude);
        dest.writeFloat(longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
