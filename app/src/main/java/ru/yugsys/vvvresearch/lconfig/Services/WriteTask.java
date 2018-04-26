package ru.yugsys.vvvresearch.lconfig.Services;

import android.os.AsyncTask;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class WriteTask extends AsyncTask<DeviceEntry,Void,Byte>{

    public DeviceEntry devTEST;
    private DataDevice dataDevice;
    private AsyncTaskCallBack.WriteCallback writeCallback;


    public WriteTask(DataDevice dataDevice) {
        this.dataDevice = dataDevice;
    }



    @Override
    protected Byte doInBackground(DeviceEntry...objects) {
        byte[] dataBuf;
        byte[] valueBlocksWrite ;
        int numberOfBlocks ;
        int countOfAttempt;
        byte[] block;
        byte[] writeResult;

        if(dataDevice !=null) {
            valueBlocksWrite=getBytes(objects[0]);

            if (valueBlocksWrite.length % 4 != 0) {
                int l = 4 - valueBlocksWrite.length % 4;
                dataBuf = new byte[valueBlocksWrite.length + l];
                System.arraycopy(valueBlocksWrite, 0, dataBuf, 0, valueBlocksWrite.length);
                Arrays.fill(dataBuf, valueBlocksWrite.length, valueBlocksWrite.length + 1, (byte) 0xFF);
                numberOfBlocks = dataBuf.length / 4;

            } else {
                dataBuf = new byte[valueBlocksWrite.length];
                System.arraycopy(valueBlocksWrite, 0, dataBuf, 0, valueBlocksWrite.length);
                numberOfBlocks = dataBuf.length / 4;
            }
            for (int startAddres = 0; startAddres < numberOfBlocks; startAddres++) {
                byte[] addressStart = Util.ConvertIntTo2bytesHexaFormat(startAddres);
                block = new byte[4];
                block[0] = dataBuf[startAddres * 4];
                block[1] = dataBuf[startAddres * 4 + 1];
                block[2] = dataBuf[startAddres * 4 + 2];
                block[3] = dataBuf[startAddres * 4 + 3];
                countOfAttempt = 0;
                writeResult = null;
                while ((writeResult == null || writeResult[0] == 1) && countOfAttempt <= 10) {
                    writeResult = NFCCommand.writeSingleBlockCommand(dataDevice.getCurrentTag(), addressStart, block, dataDevice);
                    countOfAttempt++;
                }
                if (writeResult != null) {
                    if (writeResult[0] != (byte) 0x00) {
                        writeResult[0] = (byte) 0xE1;
                        return writeResult[0];
                    }
                    return writeResult[0];
                }
            }
        }
        return 0x1F;
    }



    @Override
    protected void onPostExecute(Byte flag) {
        OnWrite(flag);
    }

    private byte[] getBytes(DeviceEntry object) {
        byte[] valueBlocksWrite = new byte[123] ;
        byte[] raw;
        try {
            raw = Util.Object2ByteArray(object);
            System.arraycopy(raw, 0, valueBlocksWrite, 0, raw.length);
            CRC16 crc16 = new CRC16();
            int res = crc16.CRC16ArrayGet(0, raw);
            byte[] crc = ByteBuffer.allocate(4).putInt(res).array();
            valueBlocksWrite[121] = crc[3];
            valueBlocksWrite[122] = crc[2];
                return valueBlocksWrite;
        }

        catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void subscribe(AsyncTaskCallBack.WriteCallback writeCallback){
        this.writeCallback = writeCallback;
    }
    private void OnWrite(Byte b){
        this.writeCallback.writeComplate(b);
    }



}
