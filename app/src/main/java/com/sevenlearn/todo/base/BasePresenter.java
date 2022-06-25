package com.sevenlearn.todo.base;

public interface BasePresenter<T extends BaseView> {
    void onAttach(T view);
    void onDetach();
}
