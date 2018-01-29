package com.vaisbleh.user.my_classes_and_lessons.model;

/**
 * Created by User on 22-Jan-18.
 */

public class PresenceToShare {
    private String studentName, groupName, groupSite, lessonName, lessonTime;
    private boolean isPresence;

    public PresenceToShare(String studentName, String groupName, String groupSite, String lessonName, String lessonTime, boolean isPresence) {
        this.studentName = studentName;
        this.groupName = groupName;
        this.groupSite = groupSite;
        this.lessonName = lessonName;
        this.lessonTime = lessonTime;
        this.isPresence = isPresence;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupSite() {
        return groupSite;
    }

    public void setGroupSite(String groupSite) {
        this.groupSite = groupSite;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getLessonTime() {
        return lessonTime;
    }

    public void setLessonTime(String lessonTime) {
        this.lessonTime = lessonTime;
    }

    public boolean isPresence() {
        return isPresence;
    }

    public void setPresence(boolean presence) {
        isPresence = presence;
    }
}
