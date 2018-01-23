package com.vaisbleh.user.my_classes_and_lessons.activities;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vaisbleh.user.my_classes_and_lessons.R;
import com.vaisbleh.user.my_classes_and_lessons.db.ClassesLessonsDBHelper;
import com.vaisbleh.user.my_classes_and_lessons.model.Group;
import com.vaisbleh.user.my_classes_and_lessons.model.Student;

import java.util.ArrayList;

public class EditStudentActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean toUpdate = false;
    private ImageView imageEditView;
    private EditText editStudentName, editStudentPhotoAdr;
    private Spinner editStudentSpinner;
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayList<String> groupsName;
    private ClassesLessonsDBHelper helper;
    private Student student;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        imageEditView = findViewById(R.id.imageEditView);
        editStudentName = findViewById(R.id.editStudentName);
        editStudentPhotoAdr = findViewById(R.id.editStudentPhotoAdr);
        editStudentSpinner = findViewById(R.id.editStudentSpinner);
        groupsName = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        helper = new ClassesLessonsDBHelper(this);
        editStudentSpinner.setAdapter(spinnerAdapter);
        refreshSpinner();


        if(getIntent().getParcelableExtra(StudentsActivity.STUDENT_TO_EDIT) != null){
            toUpdate = true;
            getSupportActionBar().setTitle("Update student");
            student = getIntent().getParcelableExtra(StudentsActivity.STUDENT_TO_EDIT);
            editStudentName.setText(student.getStudentName().toString());
            editStudentPhotoAdr.setText(student.getPhotoAddress().toString());

            int position = 0;
            String groupName = student.getGroup().getGroupName().toString();
            for (int i = 0; i < groupsName.size(); i++) {
                if (groupName.equals(groupsName.get(i))) {
                    position = i;
                }
            }

            editStudentSpinner.setSelection(position);
            setPhoto(student.getPhotoAddress().toString());


        }else {
            getSupportActionBar().setTitle("New student");

        }
        findViewById(R.id.showBtn).setOnClickListener(this);
        findViewById(R.id.saveStudentBtn).setOnClickListener(this);
    }

    private void setPhoto(String photoAddress){


        if(!photoAddress.equals("")) {

            Picasso.with(this).load(photoAddress)
                    .error(R.drawable.no_image_available)
                    .into(imageEditView);
        }else {
            imageEditView.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.no_image_available));
        }
    }


    private void refreshSpinner(){
        spinnerAdapter.clear();
        groupsName = helper.getGroupsNames();
        spinnerAdapter.addAll(groupsName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.showBtn:

                setPhoto(editStudentPhotoAdr.getText().toString());

                break;

            case R.id.saveStudentBtn:

                if(editStudentName.getText().toString().equals("")|| editStudentSpinner.getSelectedItem().toString().equals("All groups")){
                    Toast.makeText(this, "Name mast be filled and group mast be chosen", Toast.LENGTH_SHORT).show();
                }else {
                    if (toUpdate) {


                        String name = editStudentName.getText().toString();
                        String photo = editStudentPhotoAdr.getText().toString();
                        String groupName = editStudentSpinner.getSelectedItem().toString();
                        Group group = helper.getGroup(groupName);
                        student.setStudentName(name);
                        student.setPhotoAddress(photo);
                        student.setGroup(group);
                        helper.updateStudent(student);

                    } else {
                        String name = editStudentName.getText().toString();
                        String photo = editStudentPhotoAdr.getText().toString();
                        String groupName = editStudentSpinner.getSelectedItem().toString();
                        Group group = helper.getGroup(groupName);
                        student = new Student(name, photo, group);
                        helper.insertStudent(student);


                    }
                    finish();
                }
                break;
        }
    }
}
