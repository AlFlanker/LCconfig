package ru.yugsys.vvvresearch.lconfig.Services;




import android.os.AsyncTask;
import android.util.Log;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ReadTask extends AsyncTask<DataDevice, Void, DeviceEntry> {
    private DataDevice currentDataDevice = new DataDevice();
    private byte[] addressStart = new byte[8];;
    private byte[] numberOfBlockToRead ;
    private AsyncTaskCallBack.ReadCallBack readCallBack;


    public ReadTask( byte[] addressStart,byte[] numberOfBlockToRead){
        if(addressStart==null)
        Arrays.fill(this.addressStart, (byte) 0x00);
        else this.addressStart = addressStart;
        if (numberOfBlockToRead == null) this.numberOfBlockToRead = new byte[]{(byte) 0x00, (byte) 0x30};
        else this.numberOfBlockToRead = numberOfBlockToRead;


    }



    @Override
    protected DeviceEntry doInBackground(DataDevice... params) {
            currentDataDevice = params[0];
            int cpt=0;
            byte[] readMultipleBlockAnswer = null;
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


        try {
                StringBuilder sb = new StringBuilder();
                for(byte ch: readMultipleBlockAnswer){
                    sb.append(String.format("%02x ",ch));
                }
                Log.d("NFC_READ",sb.toString());
            return decode(readMultipleBlockAnswer);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return  null;
    }


    @Override
    protected void onPostExecute(DeviceEntry dev ) {
        OnRead(dev);
    }


    public void subscribe(AsyncTaskCallBack.ReadCallBack readCallBack){
        this.readCallBack = readCallBack;
    }
    private void OnRead(DeviceEntry d){
        this.readCallBack.OnDeviceEntry(d);
    }

    private  DeviceEntry decode(byte[] raw) throws IllegalAccessException, IOException, NoSuchFieldException {

        StringBuilder sb;
        DeviceEntry dev;
        byte[] crc = new byte[121];
        byte[] added = new byte[60];
        byte[] crc4added = new byte[58];
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        System.arraycopy(raw, 1, crc, 0, 121);
        System.arraycopy(raw, 124, added, 0, 60);
        System.arraycopy(raw, 124, crc4added, 0, 58);
        sb = new StringBuilder();
        for (Byte b : raw) {
            sb.append(String.format("%02x ", b));
        }
        int res = DeviceEntry.CRC16.CRC16ArrayGet(0, crc);
        int res1 = DeviceEntry.CRC16.CRC16ArrayGet(0, crc4added);
        int c16_added = ByteBuffer.wrap(new byte[]{0x00, 0x00, added[59], added[58]}).getInt();
        int c16 = ByteBuffer.wrap(new byte[]{0x00, 0x00, raw[123], raw[122]}).getInt();

        if (c16 == res && res1 == c16_added) {
            data.write(crc);
            data.write(crc4added);
            return DeviceEntry.decodeByteArrayToDevice(data.toByteArray());
        } else if (c16 == res) {
            return DeviceEntry.decodeByteArrayToDevice(crc);
        } else {
            Log.d("Er", sb.toString());
            return null;
        }
    }

}
