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
}
