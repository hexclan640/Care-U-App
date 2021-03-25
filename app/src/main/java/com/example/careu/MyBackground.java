package com.example.careu;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class MyBackground extends BroadcastReceiver {

    private static String requestId[];
    @Override
    public void onReceive(Context context, Intent intent) {

        get_request_id_array(context);
    }

    public void get_request_id_array(final Context c)
    {
        sessionManagement sessionManagement = new sessionManagement(c);
        final String username = sessionManagement.getSession();
        final String apiurl="http://10.0.2.2/careu-php/requestStatus.php?userName="+username;
        class  dbManager extends AsyncTask<String,Void,String>
        {
            Runnable runnable;
            int a = 0;
            protected void onPostExecute(String data)
            {
                try {
                    final JSONArray ja = new JSONArray(data);
                    JSONObject jo = null;

                    requestId = new String[ja.length()];


                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                        requestId[i] = jo.getString("requestId");
                    }

//                    for(int j=0;j<ja.length();j++){
//                        Toast.makeText(c, requestId[j], Toast.LENGTH_SHORT).show();
//                    }


                    final Handler handler = new Handler();
                    runnable = new Runnable() {

                        @Override
                        public void run() {
                            try{

                                for(int j=0;j<ja.length();j++){
                                    BackgroundWorkerRequest rqStatus = new BackgroundWorkerRequest(c);
                                    String st = rqStatus.execute("status",requestId[j]).get();


                                    if(st.equals("accepted")){
                                        Ringtone ringtone = RingtoneManager.getRingtone(c,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
                                        ringtone.play();
                                        SystemClock.sleep(2000);
                                        ringtone.stop();
                                        Toast.makeText(c, "accepted", Toast.LENGTH_SHORT).show();
                                        String message = "Your request "+ requestId[j] + " has been accepted";
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(c).setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                                                .setContentTitle("New notification")
                                                .setContentText(message)
                                                .setAutoCancel(true);
                                        Intent intent = new Intent(c,policeRequestList.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        //intent.putExtra("message",message);

                                        PendingIntent pendingIntent = PendingIntent.getActivity(c,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                        builder.setContentIntent(pendingIntent);
                                        NotificationManager notificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
                                        notificationManager.notify(0,builder.build());
                                        requestId[j] = "ignore";

                                    }else if (st.equals("rejected")){
                                        Ringtone ringtone = RingtoneManager.getRingtone(c,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
                                        ringtone.play();
                                        SystemClock.sleep(2000);
                                        ringtone.stop();
                                        Toast.makeText(c, "rejected", Toast.LENGTH_SHORT).show();
                                        String message = "Your request "+ requestId[j] + " has been rejected";
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(c).setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                                                .setContentTitle("New notification")
                                                .setContentText(message)
                                                .setAutoCancel(true);
                                        Intent intent = new Intent(c,myrequests.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        //intent.putExtra("message",message);

                                        PendingIntent pendingIntent = PendingIntent.getActivity(c,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                        builder.setContentIntent(pendingIntent);
                                        NotificationManager notificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
                                        notificationManager.notify(0,builder.build());
                                        requestId[j] = "ignore";
                                    }
                                }
                            }
                            catch (Exception e) {
                                //
                            }
                            finally{
                                //also call the same runnable to call it at regular interval
                                a = a + 1;
                                //Toast.makeText(c, a, Toast.LENGTH_SHORT).show();
                                if(a < 18){
                                    handler.postDelayed(this, 3000);
                                }else{
                                    handler.removeCallbacks(runnable);
                                }

                            }
                        }
                    };
                    handler.post(runnable);

                } catch (Exception ex) {
//                    Toast.makeText(c, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... strings)
            {
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuffer data = new StringBuffer();
                    String line;

                    while ((line = br.readLine()) != null) {
                        data.append(line + "\n");
                    }
                    br.close();

                    return data.toString();

                } catch (Exception ex) {
                    return ex.getMessage();
                }

            }

        }
        dbManager obj=new dbManager();
        obj.execute(apiurl);

    }

}
