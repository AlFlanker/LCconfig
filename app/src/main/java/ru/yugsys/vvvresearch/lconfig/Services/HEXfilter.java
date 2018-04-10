package ru.yugsys.vvvresearch.lconfig.Services;

import android.text.InputFilter;
import android.text.Spanned;

public class HEXfilter implements InputFilter {
    public static final String DECIMAL_DELIMITER = ".";

    private int digsBeforeDot;
    private int digsAfterDot;

    public HEXfilter(int digsBeforeDot, int digsAfterDot) {
        this.digsBeforeDot = digsBeforeDot;
        this.digsAfterDot = digsAfterDot;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        boolean isCorrect = true;
        StringBuilder newText = new StringBuilder(dest).replace(dstart, dend, source.toString());
        int size = newText.length();
        int decInd = -1;
        decInd = decInd < 0 ? newText.indexOf(DECIMAL_DELIMITER) : -1;

        if (decInd < 0) {
            if (size > digsBeforeDot) isCorrect = false;
        } else if (decInd > digsBeforeDot) {// проверяем длину целой части
            isCorrect = false;
        } else if (size - decInd - 1 > digsAfterDot) { // проверяем длину дробной части
            isCorrect = false;
        }

        if (isCorrect) {
            return null;
        } else if (source.equals("")) {
            return dest.subSequence(dstart, dend);
        } else {
            return "";
        }
    }
}
