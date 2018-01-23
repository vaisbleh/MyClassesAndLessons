package com.vaisbleh.user.my_classes_and_lessons.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 27-Dec-17.
 */

public class StudentPresence implements Parcelable{

        private int studentPresenceID;
        private Student student;
        private Lesson lesson;
        private boolean presence;

    public StudentPresence(Student student, Lesson lesson, boolean presence) {
        this.student = student;
        this.lesson = lesson;
        this.presence = presence;
    }

    public StudentPresence(int studentPresenceID, Student student, Lesson lesson, boolean presence) {
        this.studentPresenceID = studentPresenceID;
        this.student = student;
        this.lesson = lesson;
        this.presence = presence;


    }

    protected StudentPresence(Parcel in) {
        studentPresenceID = in.readInt();
        student = in.readParcelable(Student.class.getClassLoader());
        lesson = in.readParcelable(Lesson.class.getClassLoader());
        presence = in.readByte() != 0;
    }

    public static final Creator<StudentPresence> CREATOR = new Creator<StudentPresence>() {
        @Override
        public StudentPresence createFromParcel(Parcel in) {
            return new StudentPresence(in);
        }

        @Override
        public StudentPresence[] newArray(int size) {
            return new StudentPresence[size];
        }
    };

    public int getStudentPresenceID() {
        return studentPresenceID;
    }

    public void setStudentPresenceID(int studentPresenceID) {
        this.studentPresenceID = studentPresenceID;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public boolean isPresence() {
        return presence;
    }

    public void setPresence(boolean presence) {
        this.presence = presence;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(studentPresenceID);
        parcel.writeParcelable(student, i);
        parcel.writeParcelable(lesson, i);
        parcel.writeByte((byte) (presence ? 1 : 0));
    }
}
