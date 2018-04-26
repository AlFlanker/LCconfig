package ru.yugsys.vvvresearch.lconfig.Services;



import android.nfc.Tag;
import android.os.AsyncTask;
import android.util.Log;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class ReadTask extends AsyncTask<Void, Void, Void> {
    private Tag cTag;
    private byte[] systemInfo;
    private DataDevice currentDataDevice = new DataDevice();
    private byte[] addressStart = new byte[8];;
    private byte[] numberOfBlockToRead ;
    private long cpt;
    private AsyncTaskCallBack.ReadCallBack readCallBack;
    private byte[] readMultipleBlockAnswer;

    public ReadTask(byte[] systemInfo, byte[] addressStart,byte[] numberOfBlockToRead,Tag tag) {
        this.systemInfo = systemInfo;
         this.cTag = tag;
        if(addressStart==null)
        Arrays.fill(this.addressStart, (byte) 0x00);
        else this.addressStart = addressStart;
        if(numberOfBlockToRead == null) this.numberOfBlockToRead = new byte[]{(byte) 0x00, (byte) 0x20};
        else this.numberOfBlockToRead = numberOfBlockToRead;


    }



    @Override
    protected Void doInBackground(Void... params) {
        if ((currentDataDevice = Util.DecodeGetSystemInfoResponse(systemInfo, currentDataDevice)) != null) {
            readMultipleBlockAnswer = null;
            currentDataDevice.setCurrentTag(this.cTag);
            if (currentDataDevice.isMultipleReadSupported() && ByteBuffer.wrap(numberOfBlockToRead).getShort() > 1) {
                if (Util.Convert2bytesHexaFormatToInt(numberOfBlockToRead) < 32) {
                    while ((readMultipleBlockAnswer == null || readMultipleBlockAnswer[0] == 1) && cpt <= 10) {
                        readMultipleBlockAnswer = NFCCommand.SendReadMultipleBlockCommandCustom(currentDataDevice.getCurrentTag(), addressStart, numberOfBlockToRead[1], currentDataDevice);
                        cpt++;
                    }

                    cpt = 0;
                } else {
                    while ((readMultipleBlockAnswer == null || readMultipleBlockAnswer[0] == 1) && cpt <= 10) {
                        readMultipleBlockAnswer = NFCCommand.SendReadMultipleBlockCommandCustom2(currentDataDevice.getCurrentTag(), addressStart, numberOfBlockToRead, currentDataDevice);
                        cpt++;
                    }

                    cpt = 0;
                }

            } else {
                while ((readMultipleBlockAnswer == null || readMultipleBlockAnswer[0] == 1) && cpt <= 10) {
                    readMultipleBlockAnswer = NFCCommand.Send_several_ReadSingleBlockCommands_NbBlocks(currentDataDevice.getCurrentTag(), addressStart, numberOfBlockToRead, currentDataDevice);
                    cpt++;
                }

                cpt = 0;
            }

        }
    return null;
    }


    @Override
    protected void onPostExecute(Void param ) {
        try {
            OnRead(decode(readMultipleBlockAnswer));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


    public void subscribe(AsyncTaskCallBack.ReadCallBack readCallBack){
        this.readCallBack = readCallBack;
    }
    private void OnRead(DeviceEntry d){
        this.readCallBack.getDeviceEntry(d);
    }

    private  DeviceEntry decode(byte[] raw) throws IllegalAccessException, IOException, NoSuchFieldException {
        CRC16 crc16 = new CRC16();
        StringBuilder sb;
        byte[] crc = new byte[121];
        DeviceEntry currentDevice;
        System.arraycopy(raw, 1, crc, 0, 121);
        int res = crc16.CRC16ArrayGet(0, crc);
        sb = new StringBuilder();
        for (Byte b : Integer.toHexString(res).getBytes()) {
            sb.append(String.format("%02x ", b));
        }
        sb = new StringBuilder();
        for (Byte b : raw) {
            sb.append(String.format("%02x ", b));
        }
        int c16 = ByteBuffer.wrap(new byte[]{0x00, 0x00, raw[123], raw[122]}).getInt();
        if (c16 == res) {
            return currentDevice = Util.decodeByteArrayToDevice(crc);
            //            ((App) getApplication()).getModel().setCurrentDevice(currentDevice);
//            Intent addActivity = new Intent(this, AddEditActivity.class);
//            addActivity.putExtra(ADD_NEW_DEVICE_MODE, Boolean.FALSE);
//            currentDataDevice = null;
//            startActivity(addActivity);

        } else {
            Log.d("Er", sb.toString());
            return null;
        }
    }

}
