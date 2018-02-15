package ru.yugsys.vvvresearch.lconfig.views.Interfaces;

import ru.yugsys.vvvresearch.lconfig.model.Dev_Data;
import ru.yugsys.vvvresearch.lconfig.presenters.Presentable.DataActivityPresenter;

import java.util.List;

public interface IEditView {
    void update(List<Dev_Data> list);

    void addButtonClick();

    void bindPresenter(DataActivityPresenter presenter);

    void unbindPresenter();

    void ShowError();




}
