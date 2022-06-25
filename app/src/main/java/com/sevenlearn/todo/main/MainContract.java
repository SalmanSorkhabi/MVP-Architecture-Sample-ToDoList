package com.sevenlearn.todo.main;

import com.sevenlearn.todo.base.BasePresenter;
import com.sevenlearn.todo.base.BaseView;
import com.sevenlearn.todo.model.Task;

import java.util.List;

public interface MainContract {

    interface View extends BaseView {
        void showTasks(List<Task> tasks);
        void clearTasks();
        void updateTask(Task task);
        void addTask(Task task);
        void deleteTask(Task task);
        void setEmptyState(boolean visible);
    }

    interface Presenter extends BasePresenter<View> {
        void onDeleteAllBtnClick();
        void onSearch(String query);
        void onTaskItemClick(Task task);
    }
}
