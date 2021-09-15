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

public class PermanentResidenceActivity extends AppCompatActivity {

    
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    String username;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PermanentResidenceActivity.this, HomeActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permanent_residence);

        CardView cardApplyPRVisa = (CardView) findViewById(R.id.card_apply_pr_visa);
        CardView cardPlanPRAcc = (CardView) findViewById(R.id.card_plan_pr_acc);
        CardView cardApplyGovPortal = (CardView) findViewById(R.id.card_apply_gov_portal);

        TextView tvApplyPRVisaStatus = findViewById(R.id.status_pr_apply_visa);
        TextView tvPlanPRAccStatus = findViewById(R.id.status_plan_pr_acc);
        TextView tvGovPortalStatus = findViewById(R.id.status_gov_portals);

        SharedPreferences pref = getSharedPreferences("YourBuddy",
                Context.MODE_PRIVATE);
        username = pref.getString("username", null);
        final int taskId = pref.getInt("curr_task_id", -1);

        final int applyPRVisaTaskStatus = checkSubtaskStatusForUser(username, taskId, AppConstants.PERMANENT_RESIDENCE_SUBTASK_APPLY_VISA_ID);
        final int planPRAccTaskStatus = checkSubtaskStatusForUser(username, taskId, AppConstants.PERMANENT_RESIDENCE_SUBTASK_PLAN_ACC_ID);
        final int applyGovPortalTaskStatus = checkSubtaskStatusForUser(username, taskId, AppConstants.PERMANENT_RESIDENCE_SUBTASK_GOV_PORTALS_ID);

        cardApplyPRVisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity(taskId, AppConstants.PERMANENT_RESIDENCE_SUBTASK_APPLY_VISA_ID, applyPRVisaTaskStatus, PermanentResidenceStep1ApplyVisaActivity.class);
            }
        });
        statusUpdate(tvApplyPRVisaStatus, applyPRVisaTaskStatus);

        if (applyPRVisaTaskStatus == AppConstants.TASK_IN_PROGRESS || applyPRVisaTaskStatus == AppConstants.TASK_NOT_STARTED) {
            disableTask(cardPlanPRAcc);
            disableTask(cardApplyGovPortal);
            return;
        }
        statusUpdate(tvPlanPRAccStatus, planPRAccTaskStatus);
        cardPlanPRAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity(taskId, AppConstants.PERMANENT_RESIDENCE_SUBTASK_PLAN_ACC_ID, planPRAccTaskStatus, PermanentResidenceStep2PlanAccActivity.class);
            }
        });
        if (planPRAccTaskStatus == AppConstants.TASK_IN_PROGRESS || planPRAccTaskStatus == AppConstants.TASK_NOT_STARTED) {
            disableTask(cardApplyGovPortal);
            return;
        }
        statusUpdate(tvGovPortalStatus, applyGovPortalTaskStatus);
        cardApplyGovPortal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity(taskId, AppConstants.PERMANENT_RESIDENCE_SUBTASK_GOV_PORTALS_ID, applyGovPortalTaskStatus, PermanentResidenceStep3GovProtalsActivity.class);
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
                Toast.makeText(PermanentResidenceActivity.this, AppConstants.TASK_DISBALED_MSG, Toast.LENGTH_LONG).show();
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
            Toast.makeText(PermanentResidenceActivity.this, "Username missing", Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences pref = getSharedPreferences("YourBuddy",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("curr_subtask_id", subTaskId);
        editor.putInt("subtask_status", taskStatus);
        editor.commit();
        if (taskStatus == AppConstants.TASK_IN_PROGRESS) {
            Toast.makeText(PermanentResidenceActivity.this, "In Progress Task", Toast.LENGTH_LONG).show();
            startActivity(new Intent(PermanentResidenceActivity.this, nextActivity));
            return;
        } else if (taskStatus == AppConstants.TASK_COMPLETED) {
            Toast.makeText(PermanentResidenceActivity.this, "Completed Task!! Starting Read only Mode", Toast.LENGTH_LONG).show();
            startActivity(new Intent(PermanentResidenceActivity.this, nextActivity));
            return;
        }
        boolean isAdded = databaseHelper.addUserTaskSubTask(username, taskId, subTaskId, AppConstants.TASK_IN_PROGRESS);
        if (isAdded) {
            Toast.makeText(PermanentResidenceActivity.this, "Task Started", Toast.LENGTH_LONG).show();
            startActivity(new Intent(PermanentResidenceActivity.this, nextActivity));
        } else {
            Toast.makeText(PermanentResidenceActivity.this, "Error!!", Toast.LENGTH_LONG).show();
        }

    }
}
