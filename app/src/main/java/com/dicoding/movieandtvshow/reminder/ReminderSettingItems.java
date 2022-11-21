package com.dicoding.movieandtvshow.reminder;

import android.os.Parcel;
import android.os.Parcelable;

public class ReminderSettingItems implements Parcelable {

    private int id;
    private String typeRemidner, timeReminder, isReminder;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeRemidner() {
        return typeRemidner;
    }

    public void setTypeRemidner(String typeRemidner) {
        this.typeRemidner = typeRemidner;
    }

    public String getTimeReminder() {
        return timeReminder;
    }

    public void setTimeReminder(String timeReminder) {
        this.timeReminder = timeReminder;
    }

    public String getIsReminder() {
        return isReminder;
    }

    public void setIsReminder(String isReminder) {
        this.isReminder = isReminder;
    }


    public ReminderSettingItems() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.typeRemidner);
        dest.writeString(this.timeReminder);
        dest.writeString(this.isReminder);
    }

    protected ReminderSettingItems(Parcel in) {
        this.id = in.readInt();
        this.typeRemidner = in.readString();
        this.timeReminder = in.readString();
        this.isReminder = in.readString();
    }

    public static final Creator<ReminderSettingItems> CREATOR = new Creator<ReminderSettingItems>() {
        @Override
        public ReminderSettingItems createFromParcel(Parcel source) {
            return new ReminderSettingItems(source);
        }

        @Override
        public ReminderSettingItems[] newArray(int size) {
            return new ReminderSettingItems[size];
        }
    };
}
