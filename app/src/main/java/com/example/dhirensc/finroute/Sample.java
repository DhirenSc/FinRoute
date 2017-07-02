package com.example.dhirensc.finroute;

/**
 * Created by dhirensc on 30/6/17.
 */


        import android.app.AlertDialog;
        import android.os.AsyncTask;

        import android.app.AlertDialog;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.util.Log;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.maps.model.LatLng;

        import org.apache.http.HttpResponse;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.json.JSONArray;

        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.io.OutputStreamWriter;
        import java.net.URI;
        import java.net.URL;
        import java.net.URLConnection;
        import java.net.URLEncoder;
        import java.util.ArrayList;


public class Sample  extends AsyncTask<String,Void,String> {
    ProgressDialog am;
    private Context context;
    //flag 0 means get and 1 means post.(By default it is get.)
    public Sample(Context context) {
        this.context = context;


    }

    protected void onPreExecute(){
        am = new ProgressDialog(this.context, AlertDialog.THEME_HOLO_DARK);
        am.setTitle("Building Route");
        am.setMessage("Building ... ");
        am.show();
        Log.e("onPreExecutive", "called" + am);
    }

    @Override
    protected String doInBackground(String... arg0) {


        try{




            String link=arg0[0];

            Log.e("Link",link+"");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
            return sb.toString();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result){
        am.dismiss();
        Log.e("Result",result+"");


        ParserTask parserTask = new ParserTask();

        // Invokes the thread for parsing the JSON data
        parserTask.execute(result);






    }
}

