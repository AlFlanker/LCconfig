package ru.yugsys.vvvresearch.lconfig.model;



import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import org.greenrobot.greendao.query.Query;
import ru.yugsys.vvvresearch.lconfig.Services.GPScallback;
import ru.yugsys.vvvresearch.lconfig.Services.Helper;
import ru.yugsys.vvvresearch.lconfig.Services.NFCCommand;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DeviceDao;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataRead;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.Model;
import ru.yugsys.vvvresearch.lconfig.model.Manager.EventManager;


import java.util.*;


public class DataModel implements Model, GPScallback<Location> {


    /*-----------------------------------------------------------------------*/
    public DaoSession daoSession;
    public EventManager eventManager = new EventManager();
    private Location mCurrentLocation;
    private DataDevice currentDataDevice;
    Device dev;

    /*-----------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------*/
    /*                            NFC varible                                */
    byte[] getSystemInfoAnswer = null;
    byte[] readMultipleBlockAnswer = null;
    byte[] numberOfBlockToRead = null;
    private long cpt = 0L;
    List<DataRead> listOfData = null;
    String[] catBlocks = null;
    String[] catValueBlocks = null;
    private DataModel dataModel;
    String startAddressString;
    byte[] addressStart = null;
    String sNbOfBlock = null;
    int nbblocks = 0;
    StringBuilder sb = new StringBuilder();
    DataDevice getCurrentDataDevice;

    /*------------------------------------------------------------------------*/
    /* Methods and Classes block*/
    @Override
    public void readNfcDev() {
        new StartReadTask().execute(new Void[0]);

    }

    @Override
    public void saveDevice(Device device) {
        Log.d("BD","datamodel -> saveDevice ->" + device.type);
        DeviceDao dataDao = this.daoSession.getDeviceDao();
        Log.d("BD","device.type = "+device.type);
        try {
            dataDao.insert(device);
        }
        catch (Exception e){
           Log.d("BD","exception: ",e);
        }
        Log.d("BD","Save into");
        eventManager.notify(EventManager.TypeEvent.OnDevDataChecked, null, true, Collections.emptyList(), null);
    }


    public DataModel(DaoSession daoSession) {
        this.daoSession = daoSession;

    }


    @Override
    public void setCurrentDataDevice(DataDevice d) {
        this.currentDataDevice = d;
    }




    @Override
    public void setSession(DaoSession s) {
        this.daoSession = s;
        Log.d("GPS","inside");
    }

    @Override
    public void load() {
        DeviceDao dataDao = this.daoSession.getDeviceDao();
        Query<Device> queue = dataDao.queryBuilder().orderAsc(DeviceDao.Properties.Id).build();
        eventManager.notify(EventManager.TypeEvent.OnDataReceive, null, false, queue.list(), null);


    }


    @Override
    public void OnGPSdata(Location location) {
        this.mCurrentLocation = location;
        Log.d("GPS", "In Model: " + mCurrentLocation.toString());
        eventManager.notify(EventManager.TypeEvent.OnGPSdata, null, false, Collections.emptyList(), mCurrentLocation);
    }

    private class StartReadTask extends AsyncTask<Void, Void, Void> {


        private StartReadTask() {

        }

        protected void onPreExecute() {
            DataDevice dataDevice = currentDataDevice;
            getSystemInfoAnswer = NFCCommand.SendGetSystemInfoCommandCustom(dataDevice.getCurrentTag(), dataDevice);
            if ((currentDataDevice = Helper.DecodeGetSystemInfoResponse(getSystemInfoAnswer, dataDevice)) != null) {
                startAddressString = "0";
                startAddressString = Helper.castHexKeyboard(startAddressString);
                startAddressString = Helper.FormatStringAddressStart(startAddressString, dataDevice);
                addressStart = Helper.ConvertStringToHexBytes(startAddressString);
                sNbOfBlock = "128";
                sNbOfBlock = Helper.FormatStringNbBlockInteger(sNbOfBlock, startAddressString, dataDevice);
                numberOfBlockToRead = Helper.ConvertIntTo2bytesHexaFormat(Integer.parseInt(sNbOfBlock));
            }
        }

        protected Void doInBackground(Void... params) {

            readMultipleBlockAnswer = null;
            cpt = 0L;
            if ((currentDataDevice = Helper.DecodeGetSystemInfoResponse(getSystemInfoAnswer, currentDataDevice)) != null) {
                if (currentDataDevice.isMultipleReadSupported() && Helper.Convert2bytesHexaFormatToInt(numberOfBlockToRead) > 1) {
                    if (Helper.Convert2bytesHexaFormatToInt(numberOfBlockToRead) < 32) {
                        while ((readMultipleBlockAnswer == null || readMultipleBlockAnswer[0] == 1) && cpt <= 10L) {
                            readMultipleBlockAnswer = NFCCommand.SendReadMultipleBlockCommandCustom(currentDataDevice.getCurrentTag(), addressStart, numberOfBlockToRead[1], currentDataDevice);
                            cpt = cpt + 1L;
                        }

                        cpt = 0L;
                    } else {
                        while ((readMultipleBlockAnswer == null || readMultipleBlockAnswer[0] == 1) && cpt <= 10L) {
                            readMultipleBlockAnswer = NFCCommand.SendReadMultipleBlockCommandCustom2(currentDataDevice.getCurrentTag(), addressStart, numberOfBlockToRead, currentDataDevice);
                            cpt = cpt + 1L;
                        }

                        cpt = 0L;
                    }
                } else {
                    while ((readMultipleBlockAnswer == null || readMultipleBlockAnswer[0] == 1) && cpt <= 10L) {
                        readMultipleBlockAnswer = NFCCommand.Send_several_ReadSingleBlockCommands_NbBlocks(currentDataDevice.getCurrentTag(), addressStart, numberOfBlockToRead, currentDataDevice);
                        cpt = cpt + 1L;
                    }

                    cpt = 0L;
                }
            }

            return null;
        }

        protected void onPostExecute(Void unused) {
            Log.d("NFC", "Button Read CLICKED **** On Post Execute ");

            if ((currentDataDevice = Helper.DecodeGetSystemInfoResponse(getSystemInfoAnswer, currentDataDevice)) != null) {
                nbblocks = Integer.parseInt(sNbOfBlock);
                if (readMultipleBlockAnswer != null && readMultipleBlockAnswer.length - 1 > 0) {
                    if (readMultipleBlockAnswer[0] == 0) {
                        catBlocks = Helper.buildArrayBlocks(addressStart, nbblocks);
                        catValueBlocks = Helper.buildArrayValueBlocks(readMultipleBlockAnswer, nbblocks);
                        listOfData = new ArrayList();

                        for (int i = 0; i < nbblocks; ++i) {
                            DataRead dataRead = new DataRead(catBlocks[i], catValueBlocks[i]);
                            sb.append(catValueBlocks[i]);
                            listOfData.add(dataRead);

                        }
                        Log.d("NFC", sb.toString());
                        dev = decodeByteList(sb.toString());
                        eventManager.notify(EventManager.TypeEvent.OnNFCconnected, dev, false, Collections.emptyList(), null);

                    }
                }
            }

        }
    }

    public static long parseUnsignedHex(String text) {
        if (text.length() == 16) {
            return (parseUnsignedHex(text.substring(0, 1)) << 60)
                    | parseUnsignedHex(text.substring(1));
        }
        return Long.parseLong(text, 16);
    }

    String getStringAchi(String s) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < s.length(); i += 2) {
            String str = s.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();

    }

    Device decodeByteList(String s) {
        String srt = s.replace(" ", "");
        Device d = new Device();
        d.type = getStringAchi(srt.substring(0, 10));
        Log.d("NFC", String.valueOf(s.length()));
        d.eui = srt.substring(12, 28);
        d.appeui = srt.substring(28, 44);
        d.appkey = srt.substring(44, 75);
        d.nwkid = srt.substring(75, 83);
        d.devadr = srt.substring(84, 92);
        d.nwkskey = srt.substring(92, 124);
        d.appskey = srt.substring(124, 156);
        Log.d("NFC", "TEST:" + srt.substring(156, 164));
        long lo = Long.parseLong(srt.substring(156, 164), 16);
        Float f = Float.intBitsToFloat((int) lo);
        Log.d("NFC", "TEST:" + f.toString() + " ");
        d.setLatitude(f);
        d.setLongitude(Float.intBitsToFloat((int) Long.parseLong(srt.substring(164, 172), 16)));
        // d.outType = srt.substring(86,90);
        //   d.kV = srt.substring(91,118);
        //  d.kI = srt.substring(119,120);
        return d;
    }
}
