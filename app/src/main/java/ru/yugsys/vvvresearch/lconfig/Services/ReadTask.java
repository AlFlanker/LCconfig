package ru.yugsys.vvvresearch.lconfig.Services;




import android.os.AsyncTask;
import android.util.Log;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
        if (numberOfBlockToRead == null) this.numberOfBlockToRead = new byte[]{(byte) 0x00, (byte) 0x80};
        else this.numberOfBlockToRead = numberOfBlockToRead;


    }



    @Override
    protected DeviceEntry doInBackground(DataDevice... params) {
        Log.d("NFC", "start doInBackground");
            currentDataDevice = params[0];
        if (currentDataDevice == null) return null;
            int cpt=0;
            byte[] readMultipleBlockAnswer = null;
            if (currentDataDevice.isMultipleReadSupported() && ByteBuffer.wrap(numberOfBlockToRead).getShort() > 1) {
                if (Util.Convert2bytesHexaFormatToInt(numberOfBlockToRead) < 32) {
                    while ((readMultipleBlockAnswer == null || readMultipleBlockAnswer[0] == 1) && cpt <= 10) {
                        if (isCancelled()) return null;
                        Log.d("NFC", "start SendReadMultipleBlockCommandCustom");
                        readMultipleBlockAnswer = NFCCommand.SendReadMultipleBlockCommandCustom(currentDataDevice.getCurrentTag(), addressStart, numberOfBlockToRead[1], currentDataDevice);
                        cpt++;
                    }

                    cpt = 0;
                } else {
                    while ((readMultipleBlockAnswer == null || readMultipleBlockAnswer[0] == 1) && cpt <= 10) {
                        if (isCancelled()) return null;
                        Log.d("NFC", "start SendReadMultipleBlockCommandCustom2");
                        readMultipleBlockAnswer = NFCCommand.SendReadMultipleBlockCommandCustom2(currentDataDevice.getCurrentTag(), addressStart, numberOfBlockToRead, currentDataDevice);
                        cpt++;
                    }

                    cpt = 0;
                }

            } else {
                while ((readMultipleBlockAnswer == null || readMultipleBlockAnswer[0] == 1) && cpt <= 10) {
                    if (isCancelled()) return null;
                    Log.d("NFC", "start Send_several_ReadSingleBlockCommands_NbBlocks");
                    readMultipleBlockAnswer = NFCCommand.Send_several_ReadSingleBlockCommands_NbBlocks(currentDataDevice.getCurrentTag(), addressStart, numberOfBlockToRead, currentDataDevice);
                    cpt++;
                }

                cpt = 0;
            }

        Log.d("NFC", "all readedNFC");
        try {
                StringBuilder sb = new StringBuilder();
                for(byte ch: readMultipleBlockAnswer){
                    sb.append(String.format("%02x ",ch));
                }
                Log.d("NFC_READ",sb.toString());
            /*!!!!*/
            if (sb.length() < 121) return null;
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
        Log.d("NFC", "onPostExecute");
        OnRead(dev);
    }


    public void subscribe(AsyncTaskCallBack.ReadCallBack readCallBack){
        this.readCallBack = readCallBack;
    }
    private void OnRead(DeviceEntry d){
        this.readCallBack.OnDeviceEntry(d);
    }

    private  DeviceEntry decode(byte[] raw) throws IllegalAccessException, IOException, NoSuchFieldException {
        if (raw == null) return null;
        StringBuilder sb;
        byte[] crc = new byte[121];
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        System.arraycopy(raw, 1, crc, 0, 121);
        sb = new StringBuilder();
        for (Byte b : raw) {
            sb.append(String.format("%02x ", b));
        }
        int res = DeviceEntry.CRC16.CRC16ArrayGet(0, crc);
        int c16 = ByteBuffer.wrap(new byte[]{0x00, 0x00, raw[123], raw[122]}).getInt();

        if (c16 == res) {
            data.write(crc);
            int amount = ByteBuffer.wrap(Arrays.copyOf(new byte[]{raw[124], raw[125]}, 4)).order(ByteOrder.LITTLE_ENDIAN).getInt();
            if (amount > (512 - (crc.length + 4))) {
                return DeviceEntry.decodeByteArrayToDevice(data.toByteArray());
            }
            int crc_added = ByteBuffer
                    .wrap(Arrays.copyOf(new byte[]{raw[124 + amount], raw[124 + amount + 1]}, 4))
                    .order(ByteOrder.LITTLE_ENDIAN).getInt();
            if (crc_added == DeviceEntry.CRC16.CRC16ArrayGet(0, Arrays.copyOfRange(raw, 124, 124 + amount))) {
                data.write(Arrays.copyOfRange(raw, 124, 124 + amount));
                return DeviceEntry.decodeByteArrayToDevice(data.toByteArray());
            } else
                return DeviceEntry.decodeByteArrayToDevice(data.toByteArray());
        } else {
            Log.d("Er", sb.toString());
            return null;
        }

    }

}

