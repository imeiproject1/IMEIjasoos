package com.example.android.imeijasoos;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import static java.sql.Types.NULL;


public class HomeActivity extends AppCompatActivity {
    EditText IMEIEntered;
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        IMEIEntered = (EditText)findViewById(R.id.etIMEI);
    }
    public void OnButtonClick(View view) throws ExecutionException, InterruptedException {
        String username = IMEIEntered.getText().toString();
        username = username.substring(0,8);
        String password = "";
        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this,HomeActivity.this);
        backgroundWorker.execute(type,username,password);
    }
    public class BackgroundWorker extends AsyncTask<String,Void,String> {
        Context context;
        AlertDialog alertDialog;
        private Activity activity;
        BackgroundWorker (Context ctx,Activity activity) {
            context = ctx;
            this.activity = activity;
        }
        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String login_url = "http://192.168.110.60/IMEIjasoos/login.php";
            if(type.equals("login")) {
                try {

                    String user_name = params[1];
                    //String password = params[2];
                    URL url = new URL(login_url);
                    Log.d("mydebug","Working till here");
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    //Log.d("mydebug",Integer.toString(httpURLConnection.getResponseCode()));
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("imei_n","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result="";
                    String line="";
                    while((line = bufferedReader.readLine())!= null) {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String retval = "";
            return retval;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("IMEI Status Info");
        }

        @Override
        protected void onPostExecute(String result) {

            alertDialog.setMessage(result);
            alertDialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}
