package com.vaisbleh.user.my_classes_and_lessons.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 27-Dec-17.
 */

public class Group implements Parcelable {


    private String groupName, site;

    public Group(String name) {
        this.groupName = name;
    }

    public Group(String groupName, String site) {
        this.groupName = groupName;
        this.site = site;
    }

    protected Group(Parcel in) {
        groupName = in.readString();
        site = in.readString();
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(groupName);
        parcel.writeString(site);
    }
}
