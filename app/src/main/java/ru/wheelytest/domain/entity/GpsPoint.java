package ru.wheelytest.domain.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

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
    private float lat;

    private float lon;

    public GpsPoint(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }

    private GpsPoint(Parcel in) {
        id = in.readLong();
        lat = in.readFloat();
        lon = in.readFloat();
    }

    public long getId() {
        return id;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeFloat(lat);
        dest.writeFloat(lon);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
