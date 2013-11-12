package me.apla.cordova;

//import java.util.Iterator;
//import java.util.Map;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import android.content.ActivityNotFoundException;
//import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

// http://developer.android.com/guide/topics/ui/settings.html
// http://stackoverflow.com/questions/4990529/android-a-good-looking-standard-settings-menu
// http://androidpartaker.wordpress.com/2010/07/11/android-preferences/

/*

<?xml version="1.0" encoding="utf-8"?>
<PreferenceScreen xmlns:android="http://schemas.android.com/apk/res/android">
    <PreferenceCategory android:title="Main">
        <CheckBoxPreference android:title="Enable Preferences"
            android:key="EnablePreferences" android:summary="Check to enable Other Preferences" />
    </PreferenceCategory>
    <PreferenceCategory android:title="Other Prefernces">
        <ListPreference android:title="List Preference"
            android:key="DayOfWeek" android:dependency="EnablePreferences"
            android:summary="Selec Day of the Week" android:entries="@array/daysOfWeek"
            android:entryValues="@array/daysOfWeekValues" />
        <EditTextPreference android:title="Edit Text Preference"
            android:key="Name" android:dependency="EnablePreferences"
            android:summary="Enter Your Name" android:dialogTitle="Enter Your Name"
            android:defaultValue="Android Partaker"/>
        <RingtonePreference android:title="Ringtone Preference"
            android:key="Ringtone" android:dependency="EnablePreferences"
            android:summary="Select Ringtone" android:ringtoneType="all" />
    </PreferenceCategory>
 
    <PreferenceCategory android:title="Advance Preference">
        <PreferenceScreen android:title="Advance Preference">
 
            <EditTextPreference android:title="Enter Text"
                android:key="Text" />
        </PreferenceScreen>
    </PreferenceCategory>
</PreferenceScreen>

*/

public class AppPreferences extends CordovaPlugin {

//    private static final String LOG_TAG = "AppPreferences";
//    private static final int NO_PROPERTY = 0;
//    private static final int NO_PREFERENCE_ACTIVITY = 1;
    private static final int COMMIT_FAILED = 2;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
//        String result = "";

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.cordova.getActivity());

        if (action.equals("fetch")) {
                JSONObject options = args.getJSONObject (0);
                String key  = options.getString("key");
                String dict = options.getString("dict");
                if (dict != "")
                		key = dict + '.' + key;
            if (sharedPrefs.contains(key)) {
                String obj = (String) sharedPrefs.getAll().get(key);
                // JSONObject jsonValue = new JSONObject((Map) obj);
                callbackContext.success(obj.toString());
            } else {
            		callbackContext.error(0);
//                callbackContext.sendPluginResult(new PluginResult ());
            }
            return true;
        } else if (action.equals("store")) {
            JSONObject options = args.getJSONObject (0);
            String key    = options.getString("key");
            String value  = options.getString("value");
            String dict = options.getString("dict");
            if (dict != "")
            		key = dict + '.' + key;
            Editor editor = sharedPrefs.edit();
            editor.putString(key, value);
            if (editor.commit()) {
                callbackContext.success();
            } else {
                callbackContext.error(createErrorObj(COMMIT_FAILED, "Cannot commit change"));
            } 
            return true;
//            } else if (action.equals("load")) {
//                JSONObject obj = new JSONObject();
//                Map prefs = sharedPrefs.getAll();
//                Iterator it = prefs.entrySet().iterator();
//                while (it.hasNext()) {
//                    Map.Entry pairs = (Map.Entry)it.next();
//                    obj.put(pairs.getKey().toString(), pairs.getValue().toString());
//                }
//                callbackContext.sendPluginResult(new PluginResult(status, obj));
//            } else if (action.equals("show")) {
//                String activityName = args.getString(0);
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setClassName(this.cordova.getActivity(), activityName);
//                try {
//                    this.cordova.getActivity().startActivity(intent);
//                } catch (ActivityNotFoundException e) {
//                    callbackContext.sendPluginResult(createErrorObj(NO_PREFERENCE_ACTIVITY, "No preferences activity called " + activityName));
//                }
        } else if (action.equals("clearAll")) {
            Editor editor = sharedPrefs.edit();
            editor.clear();
            editor.commit();
            if (editor.commit()) {
                callbackContext.success();
            } else {
                callbackContext.error(createErrorObj(COMMIT_FAILED, "Cannot commit change"));
            } 
            return true;
        } else if (action.equals("remove")) {
            JSONObject options = args.getJSONObject (0);
            String key  = options.getString("key");
            String dict = options.getString("dict");
            if (dict != "")
            		key = dict + '.' + key;
            if (sharedPrefs.contains(key)) {
                Editor editor = sharedPrefs.edit();
                editor.remove(key);
                if (editor.commit()) {
                    callbackContext.success();
                } else {
                    callbackContext.error(createErrorObj(COMMIT_FAILED, "Cannot commit change"));
                } 
            } else {
                callbackContext.sendPluginResult(new PluginResult (PluginResult.Status.NO_RESULT));
            }
            return true;
        }
        // callbackContext.sendPluginResult(new PluginResult (PluginResult.Status.JSON_EXCEPTION));
        return false;
    }

    private JSONObject createErrorObj(int code, String message) throws JSONException {
        JSONObject errorObj = new JSONObject();
        errorObj.put("code", code);
        errorObj.put("message", message);
        return errorObj;
    }

}
