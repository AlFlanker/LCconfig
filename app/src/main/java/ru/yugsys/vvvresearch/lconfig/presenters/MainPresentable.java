package ru.yugsys.vvvresearch.lconfig.presenters;

import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;
import ru.yugsys.vvvresearch.lconfig.views.MainViewable;

public interface MainPresentable extends Presentable<MainViewable> {
    void fireUpdateDataForView();
    void loadDevByEUI(String eui);

}
