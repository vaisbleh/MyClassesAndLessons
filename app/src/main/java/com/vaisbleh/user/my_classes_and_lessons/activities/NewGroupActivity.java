package com.vaisbleh.user.my_classes_and_lessons.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vaisbleh.user.my_classes_and_lessons.R;
import com.vaisbleh.user.my_classes_and_lessons.db.ClassesLessonsDBHelper;
import com.vaisbleh.user.my_classes_and_lessons.model.Group;

public class NewGroupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editGroupName, editSite;
    private ClassesLessonsDBHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        getSupportActionBar().setTitle("Enter new group");

        editGroupName = findViewById(R.id.editGroupName);
        editSite = findViewById(R.id.editSite);

        findViewById(R.id.saveGroupBtn).setOnClickListener(this);

        helper = new ClassesLessonsDBHelper(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.saveGroupBtn:

                boolean isExist = helper.testGroupName(editGroupName.getText().toString());

                if(isExist){
                    Toast.makeText(this, "The group is exist", Toast.LENGTH_SHORT).show();
                }else if(editGroupName.getText().toString().equals("")||editSite.getText().toString().equals("")) {
                        Toast.makeText(this, "all fields must be filled", Toast.LENGTH_SHORT).show();
                    }else {
                        helper.insertGroup(new Group(editGroupName.getText().toString(), editSite.getText().toString()));
                        finish();
                    }
                break;
        }
    }
}
