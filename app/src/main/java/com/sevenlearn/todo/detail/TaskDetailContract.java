package com.sevenlearn.todo.detail;

import com.sevenlearn.todo.base.BasePresenter;
import com.sevenlearn.todo.base.BaseView;
import com.sevenlearn.todo.model.Task;

public interface TaskDetailContract {

    interface View extends BaseView {
        void showTask(Task task);

        void setDeleteBtnVisibility(boolean visible);

        void showError(String error);

        void returnResult(int resultCode,Task task);
    }

    interface Presenter extends BasePresenter<View> {
        void deleteTask();

        void saveChanges(int importance, String title);




    }
}
