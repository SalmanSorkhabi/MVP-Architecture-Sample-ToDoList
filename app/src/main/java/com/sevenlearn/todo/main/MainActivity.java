package com.sevenlearn.todo.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sevenlearn.todo.R;
import com.sevenlearn.todo.detail.TaskDetailActivity;
import com.sevenlearn.todo.model.AppDatabase;
import com.sevenlearn.todo.model.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.View, TaskAdapter.TaskItemEventListener {

    private static final int REQUEST_CODE = 209;
    public static final int RESULT_CODE_ADD_TASK = 1001;
    public static final int RESULT_CODE_DELETE_TASK = 1002;
    public static final int RESULT_CODE_UPDATE_TASK = 1003;
    public static final String EXTRA_KEY_TASK = "task";
    private MainContract.Presenter presenter;
    private TaskAdapter taskAdapter;
    private View emptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter(AppDatabase.getAppDatabase(this).getTaskDao());
        taskAdapter = new TaskAdapter(this, this);
        emptyState = findViewById(R.id.emptyState);

        View addNewTaskBtn = findViewById(R.id.addNewTaskBtn);
        addNewTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.taskListRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(taskAdapter);

        View deleteAllBtn = findViewById(R.id.deleteAllBtn);
        deleteAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onDeleteAllBtnClick();
            }
        });

        EditText searchEt = findViewById(R.id.searchEt);
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.onSearch(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        presenter.onAttach(this);
    }

    @Override
    public void showTasks(List<Task> tasks) {
        taskAdapter.setTasks(tasks);
    }

    @Override
    public void clearTasks() {
        taskAdapter.clearItems();
    }

    @Override
    public void updateTask(Task task) {
        taskAdapter.updateItem(task);
    }

    @Override
    public void addTask(Task task) {

    }

    @Override
    public void deleteTask(Task task) {

    }

    @Override
    public void setEmptyState(boolean visible) {
        emptyState.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if ((resultCode == RESULT_CODE_ADD_TASK || resultCode == RESULT_CODE_UPDATE_TASK || resultCode == RESULT_CODE_DELETE_TASK) && data != null) {
                Task task = data.getParcelableExtra(EXTRA_KEY_TASK);
                if (task != null) {
                    if (resultCode == RESULT_CODE_ADD_TASK) {
                        taskAdapter.addItem(task);
                    }else if (resultCode == RESULT_CODE_UPDATE_TASK) {
                        taskAdapter.updateItem(task);
                    }else {
                        taskAdapter.deleteItem(task);
                    }
                    setEmptyState(taskAdapter.getItemCount() == 0);
                }
            }
        }
    }

    @Override
    public void onClick(Task task) {
        presenter.onTaskItemClick(task);
    }

    @Override
    public void onLongClick(Task task) {
        Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
        intent.putExtra(EXTRA_KEY_TASK, task);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
