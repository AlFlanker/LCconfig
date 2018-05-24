package ru.yugsys.vvvresearch.lconfig.Services.RequestsManager;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.Assert;
import ru.yugsys.vvvresearch.lconfig.Services.RequestsManager.Strategy.GET;
import ru.yugsys.vvvresearch.lconfig.Services.RequestsManager.Strategy.POST;
import ru.yugsys.vvvresearch.lconfig.Services.RequestsManager.Strategy.REST;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;


public class RequestMaster {
    public REST getRequest() {
        return request;
    }

    public enum REST_FUNCTION {
        CreateDevice, DeleteDevice, AppData, CommandTypesOfDevice, SendCommand
    }

    protected final static EnumMap<REST_FUNCTION, String> select;
    private static String hostAPI;

    static {
        select = new EnumMap<REST_FUNCTION, String>(REST_FUNCTION.class);
        select.put(REST_FUNCTION.AppData, "appdata?");
        select.put(REST_FUNCTION.CreateDevice, "createdevice?");
    }

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
    private TimeZone timeZone = TimeZone.getTimeZone("UTC");
    private REST request;
    private JSONObject object;
    private final static EnumMap<REST.REST_PRM, String> parameters = new EnumMap<>(REST.REST_PRM.class);

    private REST_FUNCTION func;

    /**
     * Default constructor. Private to prevent direct instantiation.
     *
     * @see #newInstance()
     */
    private RequestMaster() {

    }

    /**
     * Returns a new, empty builder.
     *
     * @return the new {@code RequestMaster}
     */
    public static RequestMaster newInstance() {
        return new RequestMaster();
    }

    public RequestMaster Build(String hostAPI, REST_FUNCTION function) {
        this.func = function;
        this.hostAPI = (hostAPI + select.get(function));
        return this;
    }

    public RequestMaster addQueryParamets(REST.REST_PRM prm, String value) {
        Assert.notNull(value, "'value' must not be null");
        if (!(REST.REST_PRM.startDate.equals(prm) || REST.REST_PRM.endDate.equals(prm))) {
            parameters.put(prm, value);
        }
        return this;
    }

    public RequestMaster addQueryParamets(REST.REST_PRM prm, Date value) {
        Assert.notNull(value, "'Date' must not be null");
        simpleDateFormat.setTimeZone(timeZone);
        parameters.put(prm, simpleDateFormat.format(value));
        return this;
    }

    public RequestMaster addDeviceEntry(DeviceEntry deviceEntry) {
        Assert.notNull(deviceEntry, "'deviceEntry' must not be null");
        this.object = convert2JSON(deviceEntry);
        return this;
    }

    // Here different strategies are applied
    public void execute() {
        if (checkParameter()) {
            switch (func) {
                case AppData:
                    this.request = new GET(hostAPI, parameters);
                    break;
                case CreateDevice:
                    this.request = new POST(hostAPI, parameters, object);
                    break;
            }
            this.request.send();

        }


    }

    private boolean checkParameter() {
        boolean check = false;
        if (func.equals(REST_FUNCTION.AppData)) {
            if (parameters.get(REST.REST_PRM.token) != null) {
                Pattern pattern = Pattern.compile("((\\d|[a-f]|[A-F]){32})");
                check = pattern.matcher(parameters.get(REST.REST_PRM.token)).matches();
            }
            if (parameters.get(REST.REST_PRM.count) != null) {
                check &= Integer.parseInt(parameters.get(REST.REST_PRM.count)) > 0;
            }
            if (parameters.get(REST.REST_PRM.offset) != null) {
                check &= Integer.parseInt(parameters.get(REST.REST_PRM.offset)) >= 0;
            }
            if (parameters.get(REST.REST_PRM.order) != null) {
                check &= parameters.get(REST.REST_PRM.order).equals("desc");
            }

            if ((parameters.get(REST.REST_PRM.startDate) != null) && (parameters.get(REST.REST_PRM.endDate) != null)) {
                try {
                    check &= simpleDateFormat.parse(parameters.get(REST.REST_PRM.startDate)).before(simpleDateFormat.parse(parameters.get(REST.REST_PRM.endDate)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (parameters.get(REST.REST_PRM.deviceEui) != null) {
                Pattern pattern = Pattern.compile("((\\d|[a-f]|[A-F])(\\d|[a-f]|[A-F])-){7}((\\d|[a-f]|[A-F])(\\d|[a-f]|[A-F])){1}");
                check &= pattern.matcher(parameters.get(REST.REST_PRM.deviceEui)).matches();
            }
            return check;
        } else if (func.equals(REST_FUNCTION.CreateDevice)) {
            if (parameters.get(REST.REST_PRM.token) != null) {
                Pattern pattern = Pattern.compile("((\\d|[a-f]|[A-F]){32})");
                return pattern.matcher(parameters.get(REST.REST_PRM.token)).matches();
            }
            return false;
        }
        return check;
    }

    private JSONObject convert2JSON(DeviceEntry dev) {
        JSONObject jsonObject = new JSONObject();
        JSONObject device = new JSONObject();
        JSONObject model = new JSONObject();
        JSONObject geo;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < dev.getAppeui().length() - 2; i += 2) {
            stringBuilder.append(dev.getAppeui().substring(i, i + 2)).append("-");
        }
        stringBuilder.append(dev.getAppeui().substring(dev.getAppeui().length() - 2, dev.getAppeui().length()));
        dev.setAppeui(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        for (int i = 0; i < dev.getEui().length() - 2; i += 2) {
            stringBuilder.append(dev.getEui().substring(i, i + 2)).append("-");
        }
        stringBuilder.append(dev.getEui().substring(dev.getEui().length() - 2, dev.getEui().length()));
        dev.setEui(stringBuilder.toString());
        stringBuilder = new StringBuilder();
//        dev.setDevadr(dev.getDevadrMSBtoLSB());
        for (int i = 0; i < dev.getDevadr().length() - 2; i += 2) {
            stringBuilder.append(dev.getDevadr().substring(i, i + 2)).append("-");
        }
        stringBuilder.append(dev.getDevadr().substring(dev.getDevadr().length() - 2, dev.getDevadr().length()));
        dev.setDevadr(stringBuilder.toString());
        try {
            if (!dev.getIsOTTA()) {
                geo = new JSONObject();
                //Add method to convert Location -> addres (Google API)
                geo.put("countryddress", "Россия");
                geo.put("region", "КраснодарскийКрай");
                geo.put("city", "Краснодар");
                geo.put("street", "Московская");

                model.put("name", "LC5xx");
                model.put("version", "1.0");

                device.put("activationType", "ABP");
                device.put("alias", "".equals(dev.getComment()) ? "LC5xx" : dev.getComment());
                device.put("eui", dev.getEui().toLowerCase());
                device.put("applicationEui", dev.getAppeui());
                device.put("devAddr", dev.getDevadr());
                device.put("networkSessionKey", dev.getNwkskey());
                device.put("applicationSessionKey", dev.getAppskey());
                device.put("access", "Private");
                device.put("loraClass", "a");
                device.put("model", model);
                device.put("address", geo);

                jsonObject.put("token", parameters.get(REST.REST_PRM.token));
                jsonObject.put("device", device);
            } else {

                model.put("name", "LC5xx");
                model.put("version", "1.0");

                device.put("activationType", "OTAA");
                device.put("alias", "".equals(dev.getComment()) ? "LC5xx" : dev.getComment());
                device.put("eui", dev.getEui().toLowerCase());
                device.put("applicationEui", dev.getAppeui().toLowerCase());
                device.put("appKey", dev.getAppkey());
                device.put("access", "Private");
                device.put("model", model);

                jsonObject.put("token", parameters.get(REST.REST_PRM.token));
                jsonObject.put("device", device);
            }

        } catch (JSONException e) {
            Log.e("json", e.getMessage());
        }
        return jsonObject;
    }
}






