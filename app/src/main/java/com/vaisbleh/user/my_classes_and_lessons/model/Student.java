package com.vaisbleh.user.my_classes_and_lessons.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 27-Dec-17.
 */

public class Student implements Parcelable {

    private int studentId;
    private String studentName, photoAddress;
    private Group group;

    public Student(String studentName) {
        this.studentName = studentName;
    }

    public Student(String studentName, String photoAddress) {

        this.studentName = studentName;
        this.photoAddress = photoAddress;
    }

    public Student(String studentName, String photoAddress, Group group) {

        this.studentName = studentName;
        this.photoAddress = photoAddress;
        this.group = group;
    }

    public Student(int studentId, String studentName) {

        this.studentId = studentId;
        this.studentName = studentName;
    }

    public Student(int studentId, String studentName, String photoAddress) {

        this.studentId = studentId;
        this.studentName = studentName;
        this.photoAddress = photoAddress;
    }

    public Student(int studentId, String studentName, String photoAddress, Group group) {

        this.studentId = studentId;
        this.studentName = studentName;
        this.photoAddress = photoAddress;
        this.group = group;
    }

    protected Student(Parcel in) {
        studentId = in.readInt();
        studentName = in.readString();
        photoAddress = in.readString();
        group = in.readParcelable(Group.class.getClassLoader());
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getPhotoAddress() {
        return photoAddress;
    }

    public void setPhotoAddress(String photoAddress) {
        this.photoAddress = photoAddress;
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
        parcel.writeInt(studentId);
        parcel.writeString(studentName);
        parcel.writeString(photoAddress);
        parcel.writeParcelable(group, i);
    }
}

