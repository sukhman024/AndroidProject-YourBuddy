package com.app.yourbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import database.DatabaseHelper;

public class ForeignStepActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    String username;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ForeignStepActivity.this, HomeActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_step);

        CardView cardApplyVisa = (CardView) findViewById(R.id.card_apply_visa);
        CardView cardPlanAcc = (CardView) findViewById(R.id.card_plan_acc);
        CardView cardOpenAcc = (CardView) findViewById(R.id.card_open_acc);

        TextView tvApplyVisaStatus = findViewById(R.id.status_apply_visa);
        TextView tvPlanAccStatus = findViewById(R.id.status_plan_acc);
        TextView tvOpenAccStatus = findViewById(R.id.status_open_acc);

        SharedPreferences pref = getSharedPreferences("YourBuddy",
                Context.MODE_PRIVATE);
        username = pref.getString("username", null);
        final int taskId = pref.getInt("curr_task_id", -1);

        final int applyVisaTaskStatus = checkSubtaskStatusForUser(username, taskId, AppConstants.FOREIGN_STUDY_SUBTASK_APPLY_VISA_ID);
        final int planAccTaskStatus = checkSubtaskStatusForUser(username, taskId, AppConstants.FOREIGN_STUDY_SUBTASK_PLAN_ACC_ID);
        final int openAccTaskStatus = checkSubtaskStatusForUser(username, taskId, AppConstants.FOREIGN_STUDY_SUBTASK_OPEN_BANK_ACC_ID);

        cardApplyVisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity(taskId, AppConstants.FOREIGN_STUDY_SUBTASK_APPLY_VISA_ID, applyVisaTaskStatus, ForeignStudyStep1ApplyVisaActivity.class);
            }
        });
        statusUpdate(tvApplyVisaStatus, applyVisaTaskStatus);

        if (applyVisaTaskStatus == AppConstants.TASK_IN_PROGRESS || applyVisaTaskStatus == AppConstants.TASK_NOT_STARTED) {
            disableTask(cardPlanAcc);
            disableTask(cardOpenAcc);
            return;
        }
        statusUpdate(tvPlanAccStatus, planAccTaskStatus);
        cardPlanAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity(taskId, AppConstants.FOREIGN_STUDY_SUBTASK_PLAN_ACC_ID, planAccTaskStatus, ForeignStudyStep2PlanAccActivity.class);
            }
        });
        if (planAccTaskStatus == AppConstants.TASK_IN_PROGRESS || planAccTaskStatus == AppConstants.TASK_NOT_STARTED) {
            disableTask(cardOpenAcc);
            return;
        }
        statusUpdate(tvOpenAccStatus, openAccTaskStatus);
        cardOpenAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity(taskId, AppConstants.FOREIGN_STUDY_SUBTASK_OPEN_BANK_ACC_ID, openAccTaskStatus, ForeignStudyStep3OpenAccActivity.class);
            }
        });
    }

    private int checkSubtaskStatusForUser(String username, int taskId, int subtaskId) {
        return databaseHelper.checkSubTaskStatusForUser(username, taskId, subtaskId);
    }

    private void disableTask(CardView cardView) {
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ForeignStepActivity.this, AppConstants.TASK_DISBALED_MSG, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void statusUpdate(TextView textView, int status) {
        if (status == AppConstants.TASK_IN_PROGRESS) {
            textView.setBackgroundColor(Color.parseColor("#1976D2"));
            textView.setText("In Progress");
        } else if (status == AppConstants.TASK_COMPLETED) {
            textView.setBackgroundColor(Color.parseColor("#757575"));
            textView.setText("Completed");
        }
    }

    private void startNextActivity(int taskId, int subTaskId, int taskStatus, Class nextActivity) {
        if (username == null || username == "") {
            Toast.makeText(ForeignStepActivity.this, "Username missing", Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences pref = getSharedPreferences("YourBuddy",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("curr_subtask_id", subTaskId);
        editor.putInt("subtask_status", taskStatus);
        editor.commit();
        if (taskStatus == AppConstants.TASK_IN_PROGRESS) {
            Toast.makeText(ForeignStepActivity.this, "In Progress Task", Toast.LENGTH_LONG).show();
            startActivity(new Intent(ForeignStepActivity.this, nextActivity));
            return;
        } else if (taskStatus == AppConstants.TASK_COMPLETED) {
            Toast.makeText(ForeignStepActivity.this, "Completed Task!! Starting Read only Mode", Toast.LENGTH_LONG).show();
            startActivity(new Intent(ForeignStepActivity.this, nextActivity));
            return;
        }
        boolean isAdded = databaseHelper.addUserTaskSubTask(username, taskId, subTaskId, AppConstants.TASK_IN_PROGRESS);
        if (isAdded) {
            Toast.makeText(ForeignStepActivity.this, "Task Started", Toast.LENGTH_LONG).show();
            startActivity(new Intent(ForeignStepActivity.this, nextActivity));
        } else {
            Toast.makeText(ForeignStepActivity.this, "Error!!", Toast.LENGTH_LONG).show();
        }

    }
}
