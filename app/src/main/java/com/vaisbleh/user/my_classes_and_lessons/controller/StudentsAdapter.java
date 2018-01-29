package com.vaisbleh.user.my_classes_and_lessons.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vaisbleh.user.my_classes_and_lessons.R;
import com.vaisbleh.user.my_classes_and_lessons.activities.EditStudentActivity;
import com.vaisbleh.user.my_classes_and_lessons.activities.StudentsActivity;
import com.vaisbleh.user.my_classes_and_lessons.db.ClassesLessonsDBHelper;
import com.vaisbleh.user.my_classes_and_lessons.model.Student;

import java.util.ArrayList;

/**
 * Created by User on 16-Jan-18.
 */

public class StudentsAdapter extends RecyclerView.Adapter <StudentsAdapter.StudentsHolder>{

    private Context context;
    private ArrayList<Student> students = new ArrayList<>();
    private int position;

    public StudentsAdapter(Context context, ArrayList<Student> students) {
        this.context = context;
        this.students = students;
    }

    public void refresh (ArrayList<Student> newStudents){
        students.clear();
        students.addAll(newStudents);
        notifyDataSetChanged();
    }

    public void remove(Student student){
        students.remove(student);
        notifyItemRemoved(position);
    }

    @Override
    public StudentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StudentsHolder(LayoutInflater.from(context).inflate(R.layout.student_item, parent, false));
    }

    @Override
    public void onBindViewHolder(StudentsHolder holder, int position) {
            holder.bind(students.get(position));
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    //*******************************************************************************
    public class StudentsHolder extends RecyclerView.ViewHolder implements View.OnClickListener, DialogInterface.OnClickListener{

        private ImageView imageStudentItem;
        private TextView studentItemNameText, studentItemGroupText;
        private ClassesLessonsDBHelper helper;
        private AlertDialog deleteDialog;
        private Student currentStudent;


        public StudentsHolder(View itemView) {
            super(itemView);

            imageStudentItem = itemView.findViewById(R.id.imageStudentPresenceItem);
            studentItemNameText = itemView.findViewById(R.id.studentPresenceItemNameText);
            studentItemGroupText = itemView.findViewById(R.id.studentItemGroupText);
            itemView.findViewById(R.id.deleteStudentBtn).setOnClickListener(this);
            imageStudentItem.setOnClickListener(this);
            helper = new ClassesLessonsDBHelper(context);

        }

        public void bind (Student student){
            currentStudent = student;
            studentItemNameText.setText(student.getStudentName());
            studentItemGroupText.setText(student.getGroup().getGroupName());
            setPhoto(student.getPhotoAddress());

        }

        @Override
        public void onClick(View view) {

            switch (view.getId()){

                case R.id.deleteStudentBtn:

                    deleteDialog = new AlertDialog.Builder(context)
                            .setTitle(context.getResources().getString(R.string.delete_student))
                            .setMessage(context.getResources().getString(R.string.dialog_message))
                            .setPositiveButton(context.getResources().getString(R.string.positive_btn),this)
                            .setNegativeButton(context.getResources().getString(R.string.negative_btn), this)
                            .create();
                    deleteDialog.show();

                    break;

                case R.id.imageStudentPresenceItem:

                    Intent studentIntent = new Intent(context, EditStudentActivity.class);
                    studentIntent.putExtra(StudentsActivity.STUDENT_TO_EDIT, currentStudent);
                    context.startActivity(studentIntent);
                    break;

            }

        }

        private void setPhoto(String photoAddress){


            if(!photoAddress.equals("")) {

                Picasso.with(context).load(photoAddress)
                        .error(R.drawable.no_image_available_sm)
                        .into(imageStudentItem);
            }else {
                imageStudentItem.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image_available_sm));
            }
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int button) {

            switch (button){
                case DialogInterface.BUTTON_POSITIVE:

                    helper.deleteStudentPresenceByStudent(currentStudent.getStudentId());
                    helper.deleteStudent(currentStudent.getStudentId());
                    position = getAdapterPosition();
                    remove(currentStudent);

                    break;

                case DialogInterface.BUTTON_NEGATIVE:

                    deleteDialog.dismiss();

                    break;
            }
        }
    }
}
