package com.example.downloadwebcontent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private String mailRu = "https://ndscalc.ru";
    private String result;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        DownloadTask task = new DownloadTask();
        try {
            result = task.execute(mailRu).get();
            //Log.i("!@#", result);
            textView.setText(result);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    //создаем свой класс для загрузки данных (в другом потоке)
    private static class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            Log.i("!@#", strings[0]); //вывели в лог полученный аргумент url

            StringBuilder resultDownload = new StringBuilder(); // ресурс для формирования загруженного контента
            URL url = null;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);  // создаем объект url и преобразуем адрес из строки
                urlConnection = (HttpURLConnection) url.openConnection(); // создаем соединение
                InputStream in = urlConnection.getInputStream(); //открываем входящий поток
                InputStreamReader reader = new InputStreamReader(in);  //читать данные из входящего потока in
                BufferedReader bufferedReader = new BufferedReader(reader); //создаем буфер для чтения строками
                String line = bufferedReader.readLine();  //читаем
                while (line != null) {
                    resultDownload.append(line);
                    line = bufferedReader.readLine();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null){
                    urlConnection.disconnect();     //всегда закрываем соединение
                }
            }

            return resultDownload.toString();
        }
    }
}