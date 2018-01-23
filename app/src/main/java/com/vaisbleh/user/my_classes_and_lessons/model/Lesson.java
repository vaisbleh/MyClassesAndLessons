package com.vaisbleh.user.my_classes_and_lessons.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 27-Dec-17.
 */

public class Lesson implements Parcelable{

    private long time;
    private String lessonName;
    private Group group;

    public Lesson(long time, String lessonName, Group group) {
        this.time = time;
        this.lessonName = lessonName;
        this.group = group;
    }

    protected Lesson(Parcel in) {
        time = in.readLong();
        lessonName = in.readString();
        group = in.readParcelable(Group.class.getClassLoader());
    }

    public static final Creator<Lesson> CREATOR = new Creator<Lesson>() {
        @Override
        public Lesson createFromParcel(Parcel in) {
            return new Lesson(in);
        }

        @Override
        public Lesson[] newArray(int size) {
            return new Lesson[size];
        }
    };

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(time);
        parcel.writeString(lessonName);
        parcel.writeParcelable(group, i);
    }
}
