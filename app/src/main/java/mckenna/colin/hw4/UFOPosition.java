package mckenna.colin.hw4;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cmckenna on 11/5/2015.
 */
public class UFOPosition implements Parcelable {

    private int shipNum;
    private double lat;
    private double lon;

    public UFOPosition(){}
    public UFOPosition(int shipNum, double lat, double lon){
        this.shipNum = shipNum;
        this.lat = lat;
        this.lon = lon;

    }

    public int getShipNum(){return shipNum;}
    public void setShipNum(int shipNum){this.shipNum = shipNum;}

    public double getLat(){return lat;}
    public void setLat(double lat){this.lat = lat;}

    public double getLon(){return lon;}
    public void setLon(double lon){this.lon = lon;}



    public static final Parcelable.Creator<UFOPosition> CREATOR
            = new Parcelable.Creator<UFOPosition>() {
        public UFOPosition createFromParcel(Parcel in) {
            return new UFOPosition(in);
        }

        public UFOPosition[] newArray(int size) {
            return new UFOPosition[size];
        }
    };

    private UFOPosition(Parcel in){
        this.shipNum = in.readInt();
        this.lat = in.readDouble();
        this.lon = in.readDouble();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(shipNum);
        dest.writeDouble(lat);
        dest.writeDouble(lon);

    }
}
