package ru.yugsys.vvvresearch.lconfig.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import ru.yugsys.vvvresearch.lconfig.R;

public class AddEditActivity extends AppCompatActivity implements AddEditViewable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
    }
}
