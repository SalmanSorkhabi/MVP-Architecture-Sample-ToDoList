package com.sevenlearn.todo.main;

import com.sevenlearn.todo.base.BaseView;
import com.sevenlearn.todo.model.Task;
import com.sevenlearn.todo.model.TaskDao;

import java.util.List;

public class MainPresenter implements MainContract.Presenter {
    private TaskDao taskDao;
    private List<Task> tasks;
    private MainContract.View view;

    public MainPresenter(TaskDao taskDao) {
        this.taskDao = taskDao;
        this.tasks = taskDao.getAll();
    }

    @Override
    public void onDeleteAllBtnClick() {
        taskDao.deleteAll();
        view.clearTasks();
        view.setEmptyState(true);
    }

    @Override
    public void onSearch(String query) {
        if (!query.isEmpty()){
            List<Task> tasks =taskDao.search(query);
            view.showTasks(tasks);
        }else {
            List<Task> tasks =taskDao.getAll();
            view.showTasks(tasks);
        }

    }

    @Override
    public void onTaskItemClick(Task task) {
        task.setCompleted(!task.isCompleted());
        int result = taskDao.update(task);
        if (result>0)
            view.updateTask(task);
    }


    @Override
    public void onAttach(MainContract.View view) {
        this.view = view;
        if (!tasks.isEmpty()){
            view.showTasks(tasks);
            view.setEmptyState(false);
        }else
            view.setEmptyState(true);
    }

    @Override
    public void onDetach() {

    }
}
