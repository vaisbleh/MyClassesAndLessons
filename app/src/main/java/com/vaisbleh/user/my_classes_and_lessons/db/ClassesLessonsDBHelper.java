package com.vaisbleh.user.my_classes_and_lessons.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vaisbleh.user.my_classes_and_lessons.activities.MainActivity;
import com.vaisbleh.user.my_classes_and_lessons.model.Group;
import com.vaisbleh.user.my_classes_and_lessons.model.Lesson;
import com.vaisbleh.user.my_classes_and_lessons.model.Student;
import com.vaisbleh.user.my_classes_and_lessons.model.StudentPresence;

import java.util.ArrayList;

/**
 * Created by User on 27-Dec-17.
 */

public class ClassesLessonsDBHelper extends SQLiteOpenHelper {

    private static final String STUDENT_TABLE_NAME = "students";
    private static final String COL_STUDENT_ID = "student_id";
    private static final String COL_STUDENT_NAME = "student_name";
    private static final String COL_STUDENT_PHOTO_ADDRESS = "student_photo_address";

    private static final String STUDENT_PRESENCE_TABLE_NAME = "student_presences";
    private static final String COL_STUDENT_PRESENCE_ID = "student_presences_id";
    private static final String COL_STUDENT_PRESENCE_ISPRESENCE = "student_presences_ispresence";

    private static final String GROUP_TABLE_NAME = "groups";
    private static final String COL_GROUP_NAME = "group_name";
    private static final String COL_GROUP_SITE = "group_site";

    private static final String LESSON_TABLE_NAME = "lessons";
    private static final String COL_LESSON_TIME = "lesson_time";
    private static final String COL_LESSON_NAME = "lesson_name";

    public static final String ALL_GROUPS = "All groups";

    public ClassesLessonsDBHelper(Context context) {
        super(context, "classes_lessons_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL, %s TEXT, %s TEXT)",
                STUDENT_TABLE_NAME, COL_STUDENT_ID, COL_STUDENT_NAME, COL_STUDENT_PHOTO_ADDRESS, COL_GROUP_NAME ));

        sqLiteDatabase.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s INTEGER, %s INTEGER)",
                STUDENT_PRESENCE_TABLE_NAME, COL_STUDENT_PRESENCE_ID, COL_STUDENT_ID, COL_STUDENT_NAME, COL_LESSON_TIME, COL_STUDENT_PRESENCE_ISPRESENCE ));

        sqLiteDatabase.execSQL(String.format("CREATE TABLE %s ( %s TEXT PRIMARY KEY, %s TEXT)",
                GROUP_TABLE_NAME, COL_GROUP_NAME, COL_GROUP_SITE ));

        sqLiteDatabase.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY, %s TEXT, %s TEXT)",
                LESSON_TABLE_NAME, COL_LESSON_TIME, COL_LESSON_NAME, COL_GROUP_NAME ));


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //*******student presence******************************



    public void deleteStudentPresence(int studentPresenceID) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(STUDENT_PRESENCE_TABLE_NAME, COL_STUDENT_PRESENCE_ID + "=" + studentPresenceID, null);
        db.close();
    }

    public void deleteStudentPresenceByStudent(int studentID) {
        ArrayList<StudentPresence>studentPresences = getStudentPresencesById(studentID);
        Log.e("list size", studentPresences.size() + "");

        for (int i = 0; i <studentPresences.size() ; i++) {
            deleteStudentPresence(studentPresences.get(i).getStudentPresenceID());
        }
    }



    public void deletePresenceByLesson(Lesson lesson){

        ArrayList<StudentPresence>studentPresences = getStudentPresences(lesson);
        for (int i = 0; i < studentPresences.size(); i++) {
            deleteStudentPresence(studentPresences.get(i).getStudentPresenceID());
        }


    }

    public void deletePresenceByGroup(String groupName){

        ArrayList<Lesson>lessons = getLessonsByGroup(groupName);
        for (int i = 0; i <lessons.size() ; i++) {
            deletePresenceByLesson(lessons.get(i));
        }


    }


    public ArrayList<StudentPresence> getStudentPresences(Lesson lesson) {
        ArrayList<StudentPresence> studentPresences = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        long lessonTime = lesson.getTime();
        Cursor cursor = db.query(STUDENT_PRESENCE_TABLE_NAME, null, COL_LESSON_TIME + "=" + lessonTime, null, COL_STUDENT_NAME, null, null);
        while (cursor.moveToNext()) {
            int studentPresenceID = cursor.getInt(cursor.getColumnIndex(COL_STUDENT_PRESENCE_ID));
            int studentId = cursor.getInt(cursor.getColumnIndex(COL_STUDENT_ID));
            Student student = getStudent(studentId);
            int presence = cursor.getInt(cursor.getColumnIndex(COL_STUDENT_PRESENCE_ISPRESENCE));
            boolean isPresence = false;
            if(presence == 1){
                isPresence = true;
            }
            studentPresences.add(new StudentPresence(studentPresenceID, student, lesson, isPresence));
        }
        db.close();
        return studentPresences;
    }

    public ArrayList<StudentPresence> getStudentPresencesById(int studentId) {
        ArrayList<StudentPresence> studentPresences = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(STUDENT_PRESENCE_TABLE_NAME, null, COL_STUDENT_ID + "=" + studentId, null, null, null, null);
        while (cursor.moveToNext()) {
            int studentPresenceID = cursor.getInt(cursor.getColumnIndex(COL_STUDENT_PRESENCE_ID));
            Student student = getStudent(studentId);
            long lessonTime = cursor.getLong(cursor.getColumnIndex(COL_LESSON_TIME));
            Lesson lesson = getLesson(lessonTime);
            int presence = cursor.getInt(cursor.getColumnIndex(COL_STUDENT_PRESENCE_ISPRESENCE));
            boolean isPresence = false;
            if(presence == 1){
                isPresence = true;
            }
            studentPresences.add(new StudentPresence(studentPresenceID, student, lesson, isPresence));
        }
        db.close();
        return studentPresences;
    }

    public void updateStudentPresence(StudentPresence studentPresence) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STUDENT_PRESENCE_ISPRESENCE, studentPresence.isPresence());
        db.update(STUDENT_PRESENCE_TABLE_NAME, values, COL_STUDENT_PRESENCE_ID + "=" + studentPresence.getStudentPresenceID(), null);
        db.close();
    }

    public void insertStudentPresence(StudentPresence studentPresence) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STUDENT_ID, studentPresence.getStudent().getStudentId());
        values.put(COL_STUDENT_NAME, studentPresence.getStudent().getStudentName());
        values.put(COL_LESSON_TIME, studentPresence.getLesson().getTime());
        values.put(COL_STUDENT_PRESENCE_ISPRESENCE, studentPresence.isPresence());
        db.insertOrThrow(STUDENT_PRESENCE_TABLE_NAME, null, values);
        db.close();
    }

    //***lessons*************************************************************************************************

    public void deleteLesson(long lessonTime) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(LESSON_TABLE_NAME, COL_LESSON_TIME + "=" + lessonTime, null);
        db.close();
    }

    public Lesson getLesson(long lessonTime){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(LESSON_TABLE_NAME, null, COL_LESSON_TIME + "=" + lessonTime, null, COL_LESSON_TIME, null, null);
        cursor.moveToFirst();
        String lessonName = cursor.getString(cursor.getColumnIndex(COL_LESSON_NAME));
        String groupName = cursor.getString(cursor.getColumnIndex(COL_GROUP_NAME));
        Group group = getGroup(groupName);
        db.close();
        return new Lesson(lessonTime, lessonName, group);
    }

    public ArrayList<Lesson> getLessonsByGroup(String groupName) {
        ArrayList<Lesson> lessons = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(LESSON_TABLE_NAME, null, COL_GROUP_NAME + "= '" + groupName + "' ", null, COL_LESSON_TIME, null, null);
        while (cursor.moveToNext()) {
            long time = cursor.getLong(cursor.getColumnIndex(COL_LESSON_TIME));
            String name = cursor.getString(cursor.getColumnIndex(COL_LESSON_NAME));

            Group group = getGroup(groupName);
            lessons.add(0, new Lesson(time, name, group));
        }
        db.close();
        return lessons;
    }

    public void deleteLessonsByGroup(String groupName){
        ArrayList<Lesson> lessons = getLessonsByGroup(groupName);
        for (int i = 0; i <lessons.size() ; i++) {
            deleteLesson(lessons.get(i).getTime());
        }
    }

    public ArrayList<Lesson> getAllLessons() {
        ArrayList<Lesson> lessons = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(LESSON_TABLE_NAME, null, null, null, COL_LESSON_TIME, null, null);
        while (cursor.moveToNext()) {
            long time = cursor.getLong(cursor.getColumnIndex(COL_LESSON_TIME));
            String name = cursor.getString(cursor.getColumnIndex(COL_LESSON_NAME));
            String groupName = cursor.getString(cursor.getColumnIndex(COL_GROUP_NAME));
            Group group = getGroup(groupName);
            lessons.add(0, new Lesson(time, name, group));
        }
        db.close();
        return lessons;
    }

    public void insertLesson(Lesson lesson) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_LESSON_TIME, lesson.getTime());
        values.put(COL_LESSON_NAME, lesson.getLessonName());
        values.put(COL_GROUP_NAME, lesson.getGroup().getGroupName());
        db.insertOrThrow(LESSON_TABLE_NAME, null, values);
        db.close();
    }

    //***groups****************************************************

    public void deleteGroup(String groupName) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(GROUP_TABLE_NAME, COL_GROUP_NAME + "= '"+ groupName  + "' ", null);
        db.close();
    }

    public Group getGroup(String groupName){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(GROUP_TABLE_NAME, null, COL_GROUP_NAME + "= '"+ groupName  + "' ", null, null, null, null);
        cursor.moveToFirst();
        String site = cursor.getString(cursor.getColumnIndex(COL_GROUP_SITE));
        db.close();
        return new Group(groupName, site);
    }

    public ArrayList<String> getGroupsNames() {
        ArrayList<String> groupsNames = new ArrayList<>();
        groupsNames.add(ALL_GROUPS);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(GROUP_TABLE_NAME, null, null, null, COL_GROUP_NAME, null, null);
        while (cursor.moveToNext()) {
            String groupName = cursor.getString(cursor.getColumnIndex(COL_GROUP_NAME));
            groupsNames.add(groupName);
        }

        db.close();
        return groupsNames;
    }

    public boolean testGroupName(String groupNameToCheck){
        boolean isExist = false;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(GROUP_TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String groupName = cursor.getString(cursor.getColumnIndex(COL_GROUP_NAME));
            if(groupNameToCheck.equalsIgnoreCase(groupName)){
                isExist = true;
            }
        }
        return isExist;
    }

    public ArrayList<String> getSites() {
        ArrayList<String> sites = new ArrayList<>();
        sites.add("All sites");
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(GROUP_TABLE_NAME, null, null, null, COL_GROUP_SITE, null, null);
        while (cursor.moveToNext()) {
            String site = cursor.getString(cursor.getColumnIndex(COL_GROUP_SITE));
            int check = 0;
            for (int i = 0; i < sites.size(); i++) {
                if (site.equals(sites.get(i))) {
                    check++;
                }

            }
            if(check == 0){
                sites.add(site);
            }
        }

        db.close();
        return sites;


    }

    public ArrayList<Group> getGroupsBySite(String selectedSite) {
        ArrayList<Group> groups = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(GROUP_TABLE_NAME, null, COL_GROUP_SITE + "= '"+ selectedSite  + "' ", null, COL_GROUP_NAME, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(COL_GROUP_NAME));
            String site = cursor.getString(cursor.getColumnIndex(COL_GROUP_SITE));
            groups.add(new Group(name, site));
        }
        db.close();
        return groups;
    }

    public ArrayList<Group> getAllGroups() {
        ArrayList<Group> groups = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(GROUP_TABLE_NAME, null, null, null, COL_GROUP_NAME, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(COL_GROUP_NAME));
            String site = cursor.getString(cursor.getColumnIndex(COL_GROUP_SITE));
            groups.add(new Group(name, site));
        }
        db.close();
        return groups;
    }


    public void insertGroup(Group group) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_GROUP_NAME, group.getGroupName());
        values.put(COL_GROUP_SITE, group.getSite());
        db.insertOrThrow(GROUP_TABLE_NAME, null, values);
        db.close();
    }

    //**Students***************************

    public void deleteStudent(int studentId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(STUDENT_TABLE_NAME, COL_STUDENT_ID + "=" + studentId, null);
        db.close();
    }

    public Student getStudent(int studentId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(STUDENT_TABLE_NAME, null, COL_STUDENT_ID + "=" + studentId, null, null, null, null);
        cursor.moveToFirst();
        String studentName = cursor.getString(cursor.getColumnIndex(COL_STUDENT_NAME));
        String photoAddress = cursor.getString(cursor.getColumnIndex(COL_STUDENT_PHOTO_ADDRESS));
        String groupName = cursor.getString(cursor.getColumnIndex(COL_GROUP_NAME));
        Group group = getGroup(groupName);
        db.close();
        return new Student(studentId, studentName, photoAddress, group);
    }


    public ArrayList<Student> getStudents() {
        ArrayList<Student> students = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(STUDENT_TABLE_NAME, null, null, null, COL_STUDENT_NAME, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(COL_STUDENT_ID));
            String name = cursor.getString(cursor.getColumnIndex(COL_STUDENT_NAME));
            String photoAddress = cursor.getString(cursor.getColumnIndex(COL_STUDENT_PHOTO_ADDRESS));
            String grName = cursor.getString(cursor.getColumnIndex(COL_GROUP_NAME));
            Group group = getGroup(grName);
            students.add(new Student(id, name, photoAddress, group));
        }
        db.close();
        return students;
    }

    public ArrayList<Student> getStudentsByGroup(String groupName){
        ArrayList<Student> students = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(STUDENT_TABLE_NAME, null, COL_GROUP_NAME  + "= '" + groupName + "' ", null, COL_STUDENT_NAME, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(COL_STUDENT_ID));
            String name = cursor.getString(cursor.getColumnIndex(COL_STUDENT_NAME));
            String photoAddress = cursor.getString(cursor.getColumnIndex(COL_STUDENT_PHOTO_ADDRESS));
            String grName = cursor.getString(cursor.getColumnIndex(COL_GROUP_NAME));
            Group group = getGroup(grName);
            students.add(new Student(id, name, photoAddress, group));
        }
        db.close();
        return students;
    }

    public void updateDeletedGroupStudents(String groupName){
        ArrayList<Student> students = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(STUDENT_TABLE_NAME, null, COL_GROUP_NAME  + "= '" + groupName + "' ", null, COL_STUDENT_NAME, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(COL_STUDENT_ID));
            String name = cursor.getString(cursor.getColumnIndex(COL_STUDENT_NAME));
            String photoAddress = cursor.getString(cursor.getColumnIndex(COL_STUDENT_PHOTO_ADDRESS));
            String grName = cursor.getString(cursor.getColumnIndex(COL_GROUP_NAME));
            Group group = getGroup(grName);
            students.add(new Student(id, name, photoAddress, group));
        }

        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            Group group = getGroup(MainActivity.NONE);
            student.setGroup(group);
            updateStudent(student);
        }
        db.close();
    }

    public void updateStudent(Student student) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STUDENT_NAME, student.getStudentName());
        values.put(COL_STUDENT_PHOTO_ADDRESS, student.getPhotoAddress());
        values.put(COL_GROUP_NAME, student.getGroup().getGroupName());
        db.update(STUDENT_TABLE_NAME, values, COL_STUDENT_ID + "=" + student.getStudentId(), null);
        db.close();
    }

    public void insertStudent(Student student) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STUDENT_NAME, student.getStudentName());
        values.put(COL_STUDENT_PHOTO_ADDRESS, student.getPhotoAddress());
        values.put(COL_GROUP_NAME, student.getGroup().getGroupName());
        db.insertOrThrow(STUDENT_TABLE_NAME, null, values);
        db.close();
    }
}
