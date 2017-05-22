package com.zyf.fwms.commonlibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SharedPreUtil {
	
	private static final String FILE_NAME = "config";
	
	/**
	 * 获取布局型变量的值
	 * @param context 
	 * @param key 
	 * @param defValue 获取不到时，给定的默认的值
	 * @return
	 */
	public static boolean getBoolean(Context context, String key, boolean defValue) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}
	
	/**
	 * 保存boolean变量
	 */
	public static void saveBoolean(Context context, String key, boolean value) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}
	
	
	/**
	 * 获取字符串型变量的值
	 * @param context 
	 * @param key 
	 * @param defValue 获取不到时，给定的默认的值
	 * @return
	 */
	public static String getString(Context context, String key, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}

	/**
	 * 保存字符串变量
	 */
	public static void saveString(Context context, String key, String value) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}
	/**
	 * 获取整型变量的值
	 * @param context
	 * @param key
	 * @param defValue 获取不到时，给定的默认的值
	 * @return
	 */
	public static int getInt(Context context, String key, int defValue) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return sp.getInt(key,defValue);
	}


	/**
	 * 保存整型变量
	 */
	public static void saveInt(Context context, String key, int value) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		sp.edit().putInt(key,value).commit();
	}

	/**
	 * 获取Long变量的值
	 * @param context
	 * @param key
	 * @param defValue 获取不到时，给定的默认的值
	 * @return
	 */
	public static long getLong(Context context, String key, long defValue) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return sp.getLong(key,defValue);
	}

	/**
	 * 保存Long变量
	 */
	public static void saveLong(Context context, String key, long value) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		sp.edit().putLong(key,value).commit();
	}

	/**
	 * 11.     * 保存List
	 * 12.     * @param tag
	 * 13.     * @param datalist
	 * 14.
	 */
	public static  <T> void setDataList(Context context, String tag, List<T> datalist) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();

		if (null == datalist || datalist.size() <= 0)
			return;

		Gson gson = new Gson();
		//转换成json数据，再保存
		String strJson = gson.toJson(datalist);
		editor.putString(tag, strJson);
		editor.commit();

	}

	/**
	 * 获取List
	 *
	 * @param tag
	 * @return
	 */
	public static <T> List<T> getDataList(Context context, String tag) {
		SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		List<T> datalist = new ArrayList<T>();
		String strJson = preferences.getString(tag, null);
		if (null == strJson) {
			return datalist;
		}
		Gson gson = new Gson();
		datalist = gson.fromJson(strJson, new TypeToken<List<T>>() {
		}.getType());
		return datalist;

	}
	/**
	 * 获取List
	 *
	 * @param tag
	 * @return
	 */
	public static <T> List<T> getDataList(Context context, String tag, Class<T> cls) {
		SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		String strJson = preferences.getString(tag, null);
		List<T> list = new ArrayList<T>();
		try {
			Gson gson = new Gson();
			JsonArray arry = new JsonParser().parse(strJson).getAsJsonArray();
			for (JsonElement jsonElement : arry) {
				list.add(gson.fromJson(jsonElement, cls));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;


	}



}
