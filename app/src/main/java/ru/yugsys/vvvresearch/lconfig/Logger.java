package ru.yugsys.vvvresearch.lconfig;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger implements Thread.UncaughtExceptionHandler {
    private static Logger logger;
    private boolean isLogOn = true;
    private boolean isLogToDisk = true;
    private File fileLog;

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (isLogOn) {
            Log.d(thread.getName(), throwable.getMessage());
            if (isLogToDisk)
                writeToDisk(thread.getName(), throwable.getMessage());
        }
    }

    private Logger() {
        String sdState = android.os.Environment.getExternalStorageState();
        if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
            File sdDir = android.os.Environment.getExternalStorageDirectory();
            fileLog = new File(sdDir, "LConfig/log.txt");
            if (!fileLog.exists())
                fileLog.mkdirs();
        }
    }

    public static Logger getInstance() {
        if (logger == null) {
            synchronized (Logger.class) {
                logger = new Logger();
                return logger;
            }
        } else return logger;
    }

    public void setLogOn(boolean logOn) {
        this.isLogOn = logOn;
    }

    public void d(String tag, String message) {
        if (isLogOn) {
            Log.d(tag, message);
            if (isLogToDisk)
                writeToDisk(tag, message);
        }

    }

    private void writeToDisk(String tag, String message) {

        FileWriter f = null;
        try {
            f = new FileWriter(fileLog);
            f.write(tag + " : " + message);
            f.flush();
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
