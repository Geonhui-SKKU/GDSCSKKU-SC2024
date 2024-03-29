package gdscskku.teletect;

import android.Manifest;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;

@NativePlugin(
        permissions = {
                Manifest.permission.READ_SMS
        }
)
@CapacitorPlugin(name = "SmsInboxReaderController")
public class SmsInboxReaderPlugin extends Plugin {

    @PluginMethod
    public void getCount(PluginCall call) {
        Cursor cursor = getContext().getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        JSObject countWrapper= new JSObject();
        countWrapper.put("count", cursor.getCount());
        call.success(countWrapper);
    }

    @PluginMethod
    public void getAllMessages(PluginCall call) throws JSONException {
        Cursor cursor = getContext().getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

        ArrayList smsArray = new ArrayList<JSObject>();



        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                JSObject currentSmsObject = new JSObject();
                currentSmsObject.put("from",  cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS)));
                currentSmsObject.put("message", cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY)));
                Date date = new Date(Long.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE))));
                currentSmsObject.put("createdAt", date.toString());
                currentSmsObject.put("id", cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms._ID)));
                smsArray.add(currentSmsObject);
                cursor.moveToNext();
            }
        }

        JSObject smsArrayContainer = new JSObject();

        smsArrayContainer.put("sms", new JSArray(smsArray.toArray()));
        smsArrayContainer.put("count", cursor.getCount());

        call.success(smsArrayContainer);
    }

    @PluginMethod
    public void getMessagesByPhoneNumber(PluginCall call) throws JSONException {
        String phoneNumber = call.getString("phoneNumber");
        Cursor cursor = getContext().getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

        ArrayList smsArray = new ArrayList<JSObject>();

        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                if (cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS)).equals(phoneNumber)) {
                    JSObject currentSmsObject = new JSObject();
                    currentSmsObject.put("from",  cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS)));
                    currentSmsObject.put("message", cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY)));
                    Date date = new Date(Long.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE))));
                    currentSmsObject.put("createdAt", date.toString());
                    currentSmsObject.put("id", cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms._ID)));
                    smsArray.add(currentSmsObject);
                }
                cursor.moveToNext();
            }
        }

        JSObject smsArrayContainer = new JSObject();

        smsArrayContainer.put("sms", new JSArray(smsArray.toArray()));
        smsArrayContainer.put("count", smsArray.size());

        call.success(smsArrayContainer);
    }
}