package ru.yugsys.vvvresearch.lconfig.Services;

import android.text.InputFilter;
import android.text.Spanned;

public class LengthFilter implements InputFilter {
    private short length;

    public LengthFilter(short length) {

        this.length = length;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (dest.length() < length) {
            return null;
        } else if (source.equals("")) {
            return dest.subSequence(dstart, dend);
        } else {
            return "";
        }

    }
}
