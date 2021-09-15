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

public class ForeignStudyStep2PlanAccActivity extends AppCompatActivity {


    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ForeignStudyStep2PlanAccActivity.this, ForeignStepActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_study_step2_plan_acc);
        final CheckBox cbGotVisa = findViewById(R.id.cb_got_visa);
        final CheckBox cbSearchRoom = findViewById(R.id.cb_search_room);
        final CheckBox cbBookTicket = findViewById(R.id.cb_book_ticket);

        final TextView tvGotVisaStatus = findViewById(R.id.status_got_visa);
        final TextView tvSearchRoomStatus = findViewById(R.id.status_search_room);
        final TextView tvBookTicketStatus = findViewById(R.id.status_book_ticket);

        final Button btnDone = findViewById(R.id.btn_done_fs2);

        SharedPreferences pref = getSharedPreferences("YourBuddy",
                Context.MODE_PRIVATE);
        final String username = pref.getString("username", null);
        final int taskId = pref.getInt("curr_task_id", -1);
        final int subtaskId = pref.getInt("curr_subtask_id", -1);
        final int subtaskStatus = pref.getInt("subtask_status", -1);

        final int gotVisaTaskStatus = checkDetailedTaskStatusForUser(username, taskId, subtaskId, AppConstants.FOREIGN_STUDY_PLAN_ACC_DETAILED_TASK_GOT_VISA_ID);
        final int searchRoomTaskStatus = checkDetailedTaskStatusForUser(username, taskId, subtaskId, AppConstants.FOREIGN_STUDY_PLAN_ACC_DETAILED_TASK_SEARCH_ROOM_ID);
        final int bookTicketTaskStatus = checkDetailedTaskStatusForUser(username, taskId, subtaskId, AppConstants.FOREIGN_STUDY_PLAN_ACC_DETAILED_TASK_BOOK_TICKET_ID);

        updateStatus(cbGotVisa, tvGotVisaStatus, gotVisaTaskStatus);
        updateStatus(cbSearchRoom, tvSearchRoomStatus, searchRoomTaskStatus);
        updateStatus(cbBookTicket, tvBookTicketStatus, bookTicketTaskStatus);

        cbGotVisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAdded = UpdateTaskDetails(cbGotVisa, tvGotVisaStatus, username, taskId, subtaskId, AppConstants.FOREIGN_STUDY_PLAN_ACC_DETAILED_TASK_GOT_VISA_ID);
                if (isAdded) {
                    cbSearchRoom.setEnabled(true);
                    cbSearchRoom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean isAdded = UpdateTaskDetails(cbSearchRoom, tvSearchRoomStatus, username, taskId, subtaskId, AppConstants.FOREIGN_STUDY_PLAN_ACC_DETAILED_TASK_SEARCH_ROOM_ID);
                            if (isAdded) {
                                cbBookTicket.setEnabled(true);
                                cbBookTicket.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        UpdateTaskDetails(cbBookTicket, tvBookTicketStatus, username, taskId, subtaskId, AppConstants.FOREIGN_STUDY_PLAN_ACC_DETAILED_TASK_BOOK_TICKET_ID);
                                    }
                                });
                                btnDone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final int appearTaskCurrentStatus = checkDetailedTaskStatusForUser(username, taskId, subtaskId, AppConstants.FOREIGN_STUDY_PLAN_ACC_DETAILED_TASK_BOOK_TICKET_ID);
                                        if (appearTaskCurrentStatus == AppConstants.TASK_COMPLETED) {
                                            markSubtaskCompleted(username, taskId, subtaskId);
                                        } else {
                                            Toast.makeText(ForeignStudyStep2PlanAccActivity.this, "Task can't be marked completed until all the activities are completed", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
        if (gotVisaTaskStatus == AppConstants.TASK_NOT_STARTED) {
            disableTask(cbSearchRoom);
            disableTask(cbBookTicket);
            disableButton(btnDone);
            return;
        }
        cbSearchRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAdded = UpdateTaskDetails(cbSearchRoom, tvSearchRoomStatus, username, taskId, subtaskId, AppConstants.FOREIGN_STUDY_PLAN_ACC_DETAILED_TASK_SEARCH_ROOM_ID);
                if (isAdded) {
                    cbBookTicket.setEnabled(true);
                    cbBookTicket.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UpdateTaskDetails(cbBookTicket, tvBookTicketStatus, username, taskId, subtaskId, AppConstants.FOREIGN_STUDY_PLAN_ACC_DETAILED_TASK_BOOK_TICKET_ID);
                        }
                    });
                    btnDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final int appearTaskCurrentStatus = checkDetailedTaskStatusForUser(username, taskId, subtaskId, AppConstants.FOREIGN_STUDY_PLAN_ACC_DETAILED_TASK_BOOK_TICKET_ID);
                            if (appearTaskCurrentStatus == AppConstants.TASK_COMPLETED) {
                                markSubtaskCompleted(username, taskId, subtaskId);
                            } else {
                                Toast.makeText(ForeignStudyStep2PlanAccActivity.this, "Task can't be marked completed until all the activities are completed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
        if (searchRoomTaskStatus == AppConstants.TASK_NOT_STARTED) {
            disableTask(cbBookTicket);
            disableButton(btnDone);
            return;
        }
        cbBookTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateTaskDetails(cbBookTicket, tvBookTicketStatus, username, taskId, subtaskId, AppConstants.FOREIGN_STUDY_PLAN_ACC_DETAILED_TASK_BOOK_TICKET_ID);
            }
        });
        if (subtaskStatus == AppConstants.TASK_COMPLETED) {
            btnDone.setEnabled(false);
        } else {
            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int appearTaskCurrentStatus = checkDetailedTaskStatusForUser(username, taskId, subtaskId, AppConstants.FOREIGN_STUDY_PLAN_ACC_DETAILED_TASK_BOOK_TICKET_ID);
                    if (appearTaskCurrentStatus == AppConstants.TASK_COMPLETED) {
                        markSubtaskCompleted(username, taskId, subtaskId);
                    } else {
                        Toast.makeText(ForeignStudyStep2PlanAccActivity.this, "Task can't be marked completed until all the activities are completed", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void markSubtaskCompleted(String username, int taskId, int subtaskId) {
        boolean isUpdated = databaseHelper.updateSubTaskStatus(username, taskId, subtaskId, AppConstants.TASK_COMPLETED);
        if (isUpdated) {
            Toast.makeText(ForeignStudyStep2PlanAccActivity.this, "Task completed", Toast.LENGTH_LONG).show();
            startActivity(new Intent(ForeignStudyStep2PlanAccActivity.this, ForeignStepActivity.class));
        } else {
            Toast.makeText(ForeignStudyStep2PlanAccActivity.this, "Error!!", Toast.LENGTH_LONG).show();
        }
    }

    private void disableButton(Button btnDone) {
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ForeignStudyStep2PlanAccActivity.this, "Task can't be marked completed until all the activities are completed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean UpdateTaskDetails(CheckBox checkbox, TextView textView, String username, int taskId, int subTaskId, int detailedTaskId) {
        textView.setBackgroundColor(Color.parseColor("#757575"));
        textView.setText("Completed");
        checkbox.setEnabled(false);
        boolean isAdded = databaseHelper.addUserTaskSubTskDetailedTask(username, taskId, subTaskId, detailedTaskId, AppConstants.TASK_COMPLETED);
        if (isAdded) {
            Toast.makeText(ForeignStudyStep2PlanAccActivity.this, "Task Marked as completed", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ForeignStudyStep2PlanAccActivity.this, "Error!!", Toast.LENGTH_LONG).show();
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
                Toast.makeText(ForeignStudyStep2PlanAccActivity.this, AppConstants.TASK_DISBALED_MSG, Toast.LENGTH_LONG).show();
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
