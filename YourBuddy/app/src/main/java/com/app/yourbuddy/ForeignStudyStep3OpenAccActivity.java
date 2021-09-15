package com.app.yourbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import database.DatabaseHelper;

public class ForeignStudyStep3OpenAccActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ForeignStudyStep3OpenAccActivity.this, ForeignStepActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_study_step3_open_acc);
        final CheckBox cbOpenAccount = findViewById(R.id.cb_open_account);
        final CheckBox cbDocument = findViewById(R.id.cb_document);
        final CheckBox cbCheckAccOpt = findViewById(R.id.cb_check_acc_opt);

        final TextView tvOpenAccountStatus = findViewById(R.id.status_open_account);
        final TextView tvDocumentStatus = findViewById(R.id.status_document);
        final TextView tvCheckAccOptStatus = findViewById(R.id.status_check_acc_opt);

        final Button btnDone = findViewById(R.id.btn_done_fs3);

        SharedPreferences pref = getSharedPreferences("YourBuddy",
                Context.MODE_PRIVATE);
        final String username = pref.getString("username", null);
        final int taskId = pref.getInt("curr_task_id", -1);
        final int subtaskId = pref.getInt("curr_subtask_id", -1);
        final int subtaskStatus = pref.getInt("subtask_status", -1);

        final int openAccountTaskStatus = checkDetailedTaskStatusForUser(username, taskId, subtaskId, AppConstants.FOREIGN_STUDY_OPEN_BANK_ACC_DETAILED_TASK_OPEN_ACC_ID);
        final int documentTaskStatus = checkDetailedTaskStatusForUser(username, taskId, subtaskId, AppConstants.FOREIGN_STUDY_OPEN_BANK_ACC_DETAILED_TASK_DOCUMENT_ID);
        final int checkAcctOptTaskStatus = checkDetailedTaskStatusForUser(username, taskId, subtaskId, AppConstants.FOREIGN_STUDY_OPEN_BANK_ACC_DETAILED_TASK_CHECK_ACC_OPT_ID);

        updateStatus(cbOpenAccount, tvOpenAccountStatus, openAccountTaskStatus);
        updateStatus(cbDocument, tvDocumentStatus, documentTaskStatus);
        updateStatus(cbCheckAccOpt, tvCheckAccOptStatus, checkAcctOptTaskStatus);

        cbOpenAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAdded = UpdateTaskDetails(cbOpenAccount, tvOpenAccountStatus, username, taskId, subtaskId, AppConstants.FOREIGN_STUDY_OPEN_BANK_ACC_DETAILED_TASK_OPEN_ACC_ID);
                if (isAdded) {
                    cbDocument.setEnabled(true);
                    cbDocument.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean isAdded = UpdateTaskDetails(cbDocument, tvDocumentStatus, username, taskId, subtaskId, AppConstants.FOREIGN_STUDY_OPEN_BANK_ACC_DETAILED_TASK_DOCUMENT_ID);
                            if (isAdded) {
                                cbCheckAccOpt.setEnabled(true);
                                cbCheckAccOpt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        UpdateTaskDetails(cbCheckAccOpt, tvCheckAccOptStatus, username, taskId, subtaskId, AppConstants.FOREIGN_STUDY_OPEN_BANK_ACC_DETAILED_TASK_CHECK_ACC_OPT_ID);
                                    }
                                });
                                btnDone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final int appearTaskCurrentStatus = checkDetailedTaskStatusForUser(username, taskId, subtaskId, AppConstants.FOREIGN_STUDY_OPEN_BANK_ACC_DETAILED_TASK_CHECK_ACC_OPT_ID);
                                        if (appearTaskCurrentStatus == AppConstants.TASK_COMPLETED) {
                                            markSubtaskCompleted(username, taskId, subtaskId);
                                            markTaskCompleted(username, taskId);
                                        } else {
                                            Toast.makeText(ForeignStudyStep3OpenAccActivity.this, "Task can't be marked completed until all the activities are completed", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }

            }
        });
        if (openAccountTaskStatus == AppConstants.TASK_NOT_STARTED) {
            disableTask(cbDocument);
            disableTask(cbCheckAccOpt);
            disableButton(btnDone);
            return;
        }
        cbDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAdded = UpdateTaskDetails(cbDocument, tvDocumentStatus, username, taskId, subtaskId, AppConstants.FOREIGN_STUDY_OPEN_BANK_ACC_DETAILED_TASK_DOCUMENT_ID);
                if (isAdded) {
                    cbCheckAccOpt.setEnabled(true);
                    cbCheckAccOpt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UpdateTaskDetails(cbCheckAccOpt, tvCheckAccOptStatus, username, taskId, subtaskId, AppConstants.FOREIGN_STUDY_OPEN_BANK_ACC_DETAILED_TASK_CHECK_ACC_OPT_ID);
                        }
                    });
                    btnDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final int appearTaskCurrentStatus = checkDetailedTaskStatusForUser(username, taskId, subtaskId, AppConstants.FOREIGN_STUDY_OPEN_BANK_ACC_DETAILED_TASK_CHECK_ACC_OPT_ID);
                            if (appearTaskCurrentStatus == AppConstants.TASK_COMPLETED) {
                                markSubtaskCompleted(username, taskId, subtaskId);
                                markTaskCompleted(username, taskId);
                            } else {
                                Toast.makeText(ForeignStudyStep3OpenAccActivity.this, "Task can't be marked completed until all the activities are completed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
        if (documentTaskStatus == AppConstants.TASK_NOT_STARTED) {
            disableTask(cbCheckAccOpt);
            disableButton(btnDone);
            return;
        }
        cbCheckAccOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateTaskDetails(cbCheckAccOpt, tvCheckAccOptStatus, username, taskId, subtaskId, AppConstants.FOREIGN_STUDY_OPEN_BANK_ACC_DETAILED_TASK_CHECK_ACC_OPT_ID);
            }
        });
        if (subtaskStatus == AppConstants.TASK_COMPLETED) {
            btnDone.setEnabled(false);
        } else {
            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int appearTaskCurrentStatus = checkDetailedTaskStatusForUser(username, taskId, subtaskId, AppConstants.FOREIGN_STUDY_OPEN_BANK_ACC_DETAILED_TASK_CHECK_ACC_OPT_ID);
                    if (appearTaskCurrentStatus == AppConstants.TASK_COMPLETED) {
                        markSubtaskCompleted(username, taskId, subtaskId);
                        markTaskCompleted(username, taskId);
                    } else {
                        Toast.makeText(ForeignStudyStep3OpenAccActivity.this, "Task can't be marked completed until all the activities are completed", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void markTaskCompleted(String username, int taskId) {
        boolean isUpdated = databaseHelper.updateTaskStatus(username, taskId, AppConstants.TASK_COMPLETED);
        if (isUpdated) {
            Toast.makeText(ForeignStudyStep3OpenAccActivity.this, "Main Task completed", Toast.LENGTH_LONG).show();
            startActivity(new Intent(ForeignStudyStep3OpenAccActivity.this, ForeignStepActivity.class));
        } else {
            Toast.makeText(ForeignStudyStep3OpenAccActivity.this, "Error!!", Toast.LENGTH_LONG).show();
        }
    }

    private void markSubtaskCompleted(String username, int taskId, int subtaskId) {
        boolean isUpdated = databaseHelper.updateSubTaskStatus(username, taskId, subtaskId, AppConstants.TASK_COMPLETED);
        if (isUpdated) {
            Toast.makeText(ForeignStudyStep3OpenAccActivity.this, "Task completed", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ForeignStudyStep3OpenAccActivity.this, "Error!!", Toast.LENGTH_LONG).show();
        }
    }

    private void disableButton(Button btnDone) {
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ForeignStudyStep3OpenAccActivity.this, "Task can't be marked completed until all the activities are completed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean UpdateTaskDetails(CheckBox checkbox, TextView textView, String username, int taskId, int subTaskId, int detailedTaskId) {
        textView.setBackgroundColor(Color.parseColor("#757575"));
        textView.setText("Completed");
        checkbox.setEnabled(false);
        boolean isAdded = databaseHelper.addUserTaskSubTskDetailedTask(username, taskId, subTaskId, detailedTaskId, AppConstants.TASK_COMPLETED);
        if (isAdded) {
            Toast.makeText(ForeignStudyStep3OpenAccActivity.this, "Task Marked as completed", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ForeignStudyStep3OpenAccActivity.this, "Error!!", Toast.LENGTH_LONG).show();
        }
        return isAdded;
    }

    private int checkDetailedTaskStatusForUser(String username, int taskId, int subtaskId, int detailedTaskId) {
        return databaseHelper.checkDetailedTaskStatusForUser(username, taskId, subtaskId, detailedTaskId);
    }

    private void disableTask(CheckBox checkBox) {
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ForeignStudyStep3OpenAccActivity.this, AppConstants.TASK_DISBALED_MSG, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateStatus(CheckBox checkBox, TextView textView, int status) {
        if (status == AppConstants.TASK_COMPLETED) {
            textView.setBackgroundColor(Color.parseColor("#757575"));
            textView.setText("Completed");
            checkBox.setEnabled(false);
            checkBox.setChecked(true);
        }
    }
}
