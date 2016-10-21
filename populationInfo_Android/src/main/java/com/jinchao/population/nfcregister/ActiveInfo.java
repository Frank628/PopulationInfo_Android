package com.jinchao.population.nfcregister;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gaofeng on 2016-07-27.
 */
public class ActiveInfo implements Parcelable {
    private String actCode;// 激活码
    private long startTime;// 权限开始时间
    private long endTime;// 权限截止时间

    public ActiveInfo(){}
    protected ActiveInfo(Parcel in) {
        actCode = in.readString();
        startTime = in.readLong();
        endTime = in.readLong();
    }

    public static final Creator<ActiveInfo> CREATOR = new Creator<ActiveInfo>() {
        @Override
        public ActiveInfo createFromParcel(Parcel in) {
            return new ActiveInfo(in);
        }

        @Override
        public ActiveInfo[] newArray(int size) {
            return new ActiveInfo[size];
        }
    };

    public String getActCode() {
        return actCode;
    }

    public void setActCode(String actCode) {
        this.actCode = actCode;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(actCode);
        parcel.writeLong(startTime);
        parcel.writeLong(endTime);
    }
}
