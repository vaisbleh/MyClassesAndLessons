package com.vaisbleh.user.my_classes_and_lessons.controller;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vaisbleh.user.my_classes_and_lessons.R;
import com.vaisbleh.user.my_classes_and_lessons.db.ClassesLessonsDBHelper;
import com.vaisbleh.user.my_classes_and_lessons.model.StudentPresence;

import java.util.ArrayList;

/**
 * Created by User on 22-Jan-18.
 */

public class StudentsPresenceAdapter extends RecyclerView.Adapter<StudentsPresenceAdapter.StudentsPresenceHolder> {

    private Context context;
    private ArrayList<StudentPresence> studentPresences = new ArrayList<>();
    private OnPresenceChangedListener listener;

    private ClassesLessonsDBHelper helper;

    public StudentsPresenceAdapter(Context context, ArrayList<StudentPresence> studentPresences) {
        this.context = context;
        this.studentPresences = studentPresences;
        listener = (OnPresenceChangedListener) context;
        helper = new ClassesLessonsDBHelper(context);

    }

    @Override
    public StudentsPresenceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StudentsPresenceHolder(LayoutInflater.from(context).inflate(R.layout.student_presence_item, parent, false));
    }

    @Override
    public void onBindViewHolder(StudentsPresenceHolder holder, int position) {
        holder.bind(studentPresences.get(position));
    }

    @Override
    public int getItemCount() {
        return studentPresences.size();
    }

    //****************************************************************************
    public class StudentsPresenceHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        private ImageView imageStudentPresenceItem;
        private TextView studentPresenceItemNameText;
        private CheckBox checkBox;
        private StudentPresence currentStudentPresence;



        public StudentsPresenceHolder(View itemView) {
            super(itemView);

            imageStudentPresenceItem = itemView.findViewById(R.id.imageStudentPresenceItem);
            studentPresenceItemNameText = itemView.findViewById(R.id.studentPresenceItemNameText);
            checkBox = itemView.findViewById(R.id.checkBox);
            checkBox.setOnCheckedChangeListener(this);

        }

        public void bind(StudentPresence studentPresence){
            currentStudentPresence = studentPresence;
            setPhoto(studentPresence.getStudent().getPhotoAddress());
            studentPresenceItemNameText.setText(studentPresence.getStudent().getStudentName());
            checkBox.setChecked(studentPresence.isPresence());

        }

        private void setPhoto(String photoAddress){


            if(!photoAddress.equals("")) {

                Picasso.with(context).load(photoAddress)
                        .error(R.drawable.no_image_available_sm)
                        .into(imageStudentPresenceItem);
            }else {
                imageStudentPresenceItem.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image_available_sm));
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            int position = getAdapterPosition();
            currentStudentPresence.setPresence(b);
            helper.updateStudentPresence(currentStudentPresence);
            listener.onPresenceChanged(position, b);

        }
    }

    public interface OnPresenceChangedListener{
        void onPresenceChanged(int position, boolean isPresence);
    }

}
