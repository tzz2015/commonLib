package com.zyf.fwms.commonlibrary.pickerview;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.zyf.fwms.commonlibrary.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 公司：杭州融科网络科技
 * 刘宇飞 创建 on 2017/3/7.
 * 描述：pickerview工具类
 */

public class PickCityUtil {

    public static List<String> options1Items = new ArrayList<>();//一级
    public static List<List<String>> options2Items = new ArrayList<>();//二级
    public static List<List<List<String>>> options3Items = new ArrayList<>();//三级

    public static List<String> areaId1 = new ArrayList<>();//一级
    public static List<List<String>> areaId2 = new ArrayList<>();//二级
    public static List<List<List<String>>> areaId3 = new ArrayList<>();//三级

    private static Gson gson;

    public static void initData(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    setProvince(context);
                    setCity(context);
                    setArea(context);
                }catch (Exception e){
                    LogUtil.getInstance().e(e.getMessage());
                }




            }
        }).start();

    }

    /**
     * 生成省份列表
     */
    public static void setProvince(Context context) {
        //读取省份字符串
        String province = getAssetsJson(context, "json/province.json");
        if (province != null && province.length() > 0) {
            List<CityModel> list = getCityList(province);
            if (!list.isEmpty()) {
                options1Items.clear();
                areaId1.clear();
                for (CityModel model : list) {
                    options1Items.add(model.name);
                    areaId1.add(model.id);
                }
            }
        }
    }


    /**
     * 生成城市列表
     */
    public static void setCity(Context context) {
        //读取城市字符串
        String city = getAssetsJson(context, "json/city.json");

        try {
            JSONObject jsonObject = new JSONObject(city);
            for (String id : areaId1) {
                List<String> newCity = new ArrayList<>();
                List<String> newCityId = new ArrayList<>();
                String cityJson = jsonObject.getString(id);
                List<CityModel> cityList = getCityList(cityJson);
                if (!cityList.isEmpty()) {
                    for (CityModel model : cityList) {
                        newCity.add(model.name);
                        newCityId.add(model.id);
                    }
                }
                options2Items.add(newCity);
                areaId2.add(newCityId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成地区列表
     */
    public static void setArea(Context context) {
        //读取地区字符串
        String area = getAssetsJson(context, "json/area.json");
        try {
            JSONObject jsonObject = new JSONObject(area);
            for(List<String> list1:areaId2){
                List<List<String>> newArea=new ArrayList<>();
                List<List<String>> newAreaId=new ArrayList<>();
                for(String id:list1){
                    List<String> newArea2 = new ArrayList<>();
                    List<String> newArea2Id = new ArrayList<>();
                    String cityJson = jsonObject.getString(id);
                    List<CityModel> cityList = getCityList(cityJson);
                    if (!cityList.isEmpty()) {
                        for (CityModel model : cityList) {
                            newArea2.add(model.name);
                            newArea2Id.add(model.id);
                        }
                    }
                    newArea.add(newArea2);
                    newAreaId.add(newArea2Id);
                }
                options3Items.add(newArea);
                areaId3.add(newAreaId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    /**
     * 获取list 集合
     */
    public static List<CityModel> getCityList(String json) {
        List<CityModel> list = new ArrayList<>();
        JsonArray arry = new JsonParser().parse(json).getAsJsonArray();
        for (JsonElement jsonElement : arry) {
            list.add(getGson().fromJson(jsonElement, CityModel.class));
        }
        return list;

    }


    /**
     * 获取资源文件字符串
     *
     * @param context
     * @return
     */
    public static String getAssetsJson(Context context, String fileName) {
        /*try {
            StringBuffer sb = new StringBuffer();
            // InputStream is = context.getAssets().open("json/city.json");
            InputStream is = context.getAssets().open(fileName);
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "utf-8"));
            }
            is.close();

            return sb.toString();
        } catch (Exception e) {
            return null;
        }*/
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    /**
     * 获取gson
     */
    public static Gson getGson() {
        if (gson == null)
            gson = new Gson();
        return gson;
    }

    /**
     * 城市选择
     *
     * @param
     * @param context
     */
    public static void showCityPickView(Context context, final ChooseCityListener listener) {

        if (options1Items.isEmpty() || options2Items.isEmpty() || options3Items.isEmpty()) {
            return;
        }

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String p = options1Items.get(options1);
                String c = options2Items.get(options1).get(options2);
                String a = "";
                if (!options3Items.get(options1).get(options2).isEmpty())
                    a = options3Items.get(options1).get(options2).get(options3);
                else
                    a = "--";
                if (p.equals(c)) {
                    listener.chooseCity(c + "_" + a);
                } else {
                    listener.chooseCity(p + "_" + c + "_" + a);
                }
            }
        })
                .setTitleText("选择城市")
                .build();
        pvOptions.setPicker(options1Items, options2Items, options3Items);
        pvOptions.show();


    }

    /**
     * 单列表
     *
     * @param
     * @param context
     */
    public static void showSinglePickView(Context context, final List<String> list, String title, final ChoosePositionListener listener) {

        if (list.isEmpty()) {
            return;
        }

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                listener.choosePosition(options1, list.get(options1));
            }
        })
                .setTitleText(title)
                .build();
        pvOptions.setPicker(list);
        pvOptions.show();


    }

    /**
     * 双列表
     *
     * @param
     * @param context
     */
    public static void showDoublePickView(Context context, final List<String> list1, final List<List<String>> list2, String title, final ChooseDPositionListener listener) {

        if (list1.isEmpty() || list2.isEmpty()) {
            return;
        }
        try {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                try {
                    listener.choosePosition(options1, options2, list1.get(options1) + "_" + list2.get(options1).get(options2));
                } catch (Exception e) {
                    LogUtil.getInstance().e(e.getMessage());
                }
            }
        })
                .setTitleText(title)
                .build();
        pvOptions.setPicker(list1, list2);
        pvOptions.show();
        }catch (Exception e){
            LogUtil.getInstance().e(e.getMessage());
        }


    }

    /**
     * @param
     * @param context
     */
    public static void showCityPickView(Context context, final ChooseCityListener listener, final ChooseCityAreaIdListener areaIdListener) {

        if (options1Items.isEmpty() || options2Items.isEmpty() || options3Items.isEmpty()) {
            return;
        }
        try {
            OptionsPickerView pvOptions = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    //返回的分别是三个级别的选中位置
                    String p = options1Items.get(options1);
                    String c = options2Items.get(options1).get(options2);
                    String a = "";
                    if (!options3Items.get(options1).get(options2).isEmpty())
                        a = options3Items.get(options1).get(options2).get(options3);
                    else
                        a = "--";
                    if (p.equals(c)) {
                        listener.chooseCity(c + "_" + a);
                    } else {
                        listener.chooseCity(p + "_" + c + "_" + a);
                    }
                    String ap = areaId1.get(options1);
                    String ac = areaId2.get(options1).get(options2);
                    String aa = "";
                    if (!areaId3.get(options1).get(options2).isEmpty())
                        aa = areaId3.get(options1).get(options2).get(options3);
                    else aa = "--";
                    areaIdListener.chooseAreaId(ap + "_" + ac + "_" + aa);
                }
            })
                    .setTitleText("选择城市")
                    .build();
            pvOptions.setPicker(options1Items, options2Items, options3Items);
            pvOptions.show();
        }catch (Exception e){
            LogUtil.getInstance().e(e.getMessage());
        }

    }

    /**
     * 显示时间
     */
    public static void showTimeDialog(Context context, final ChooseTimeListener listener) {
        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                listener.chooseTime(date);
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .build();
        pvTime.show();
    }

    public interface ChooseCityListener {
        void chooseCity(String s);
    }

    public interface ChooseTimeListener {
        void chooseTime(Date date);
    }

    public interface ChoosePositionListener {
        void choosePosition(int position, String s);
    }

    public interface ChooseDPositionListener {
        void choosePosition(int position1, int position2, String s);
    }

    public interface ChooseCityAreaIdListener {
        void chooseAreaId(String s);
    }


}
