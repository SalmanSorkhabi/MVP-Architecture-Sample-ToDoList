package com.sevenlearn.todo.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.sevenlearn.todo.R;
import com.sevenlearn.todo.main.MainActivity;
import com.sevenlearn.todo.model.AppDatabase;
import com.sevenlearn.todo.model.Task;
import com.sevenlearn.todo.model.TaskDao;

public class TaskDetailActivity extends AppCompatActivity implements TaskDetailContract.View {
    private int selectedImportance = Task.IMPORTANCE_NORMAL;
    private ImageView lastSelectedImportanceIv;
    private TaskDetailContract.Presenter presenter;
    private EditText editText;
    private View deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        presenter = new TaskDetailPresenter(AppDatabase.getAppDatabase(this).getTaskDao(),(Task) getIntent().getParcelableExtra(MainActivity.EXTRA_KEY_TASK));

        View backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deleteBtn = findViewById(R.id.deleteTaskBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.deleteTask();
            }
        });


        editText = findViewById(R.id.taskEt);
        MaterialButton saveChangeBtn = findViewById(R.id.saveChangesBtn);
        saveChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.saveChanges(selectedImportance,editText.getText().toString());
            }
        });

        View normalImportanceBtn = findViewById(R.id.normalImportanceBtn);
        lastSelectedImportanceIv = normalImportanceBtn.findViewById(R.id.normalImportanceCheckIv);

        View highImportanceBtn = findViewById(R.id.highImportanceBtn);
        highImportanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImportance != Task.IMPORTANCE_HIGH) {
                    lastSelectedImportanceIv.setImageResource(0);
                    ImageView imageView = v.findViewById(R.id.highImportanceCheckIv);
                    imageView.setImageResource(R.drawable.ic_check_white_24dp);
                    selectedImportance = Task.IMPORTANCE_HIGH;

                    lastSelectedImportanceIv = imageView;
                }
            }
        });
        View lowImportanceBtn = findViewById(R.id.lowImportanceBtn);
        lowImportanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImportance != Task.IMPORTANCE_LOW) {
                    lastSelectedImportanceIv.setImageResource(0);
                    ImageView imageView = v.findViewById(R.id.lowImportanceCheckIv);
                    imageView.setImageResource(R.drawable.ic_check_white_24dp);
                    selectedImportance = Task.IMPORTANCE_LOW;

                    lastSelectedImportanceIv = imageView;
                }
            }
        });

        normalImportanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImportance != Task.IMPORTANCE_NORMAL) {
                    lastSelectedImportanceIv.setImageResource(0);
                    ImageView imageView = v.findViewById(R.id.normalImportanceCheckIv);
                    imageView.setImageResource(R.drawable.ic_check_white_24dp);
                    selectedImportance = Task.IMPORTANCE_NORMAL;

                    lastSelectedImportanceIv = imageView;
                }
            }
        });

        presenter.onAttach(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }


    @Override
    public void showTask(Task task) {
        editText.setText(task.getTitle());
        switch (task.getImportance()){
            case Task.IMPORTANCE_HIGH:
                findViewById(R.id.highImportanceBtn).performClick();
                break;
            case Task.IMPORTANCE_NORMAL:
                findViewById(R.id.normalImportanceBtn).performClick();
                break;
            case Task.IMPORTANCE_LOW:
                findViewById(R.id.lowImportanceBtn).performClick();
                break;
        }
    }

    @Override
    public void setDeleteBtnVisibility(boolean visible) {
        deleteBtn.setVisibility(visible ? View.VISIBLE:View.GONE);
    }

    @Override
    public void showError(String error) {
        Snackbar.make(findViewById(R.id.rootTaskDetail),error,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void returnResult(int resultCode, Task task) {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.EXTRA_KEY_TASK,task);
        setResult(resultCode,intent);
        finish();
    }

}
