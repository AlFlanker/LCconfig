package ru.yugsys.vvvresearch.lconfig.Services;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.views.MainActivity;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class ReadTask extends AsyncTask<Byte, Void, byte[]> {

    private byte[] numberOfBlockToRead;
    private byte[] systemInfo;
    private byte[] addressStart;

    public ReadTask(byte[] systemInfo) {
        this.systemInfo = systemInfo;
    }

    @Override
    protected void onPreExecute() {
        int cpt = 0;
        if ((Util.DecodeGetSystemInfoResponse(systemInfo, new DataDevice())) != null) {
            addressStart = new byte[8];
            Arrays.fill(addressStart, (byte) 0x00);
            numberOfBlockToRead = new byte[]{(byte) 0x00, (byte) 0x20}; // 32 блока на 4 байта
        }
    }

    @Override
    protected byte[] doInBackground(Byte... bytes) {
        return new byte[0];
    }


    @Override
    protected void onPostExecute(byte[] bytes) {
        super.onPostExecute(bytes);
    }


}
