package com.example.amazinglu.async_task_run_parallel_demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView text;
    private ImageView image;
    private Button loadTextButton, loadImageButton;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView) findViewById(R.id.main_text);
        image = (ImageView) findViewById(R.id.main_image);
        client = new OkHttpClient();

        loadTextButton =(Button) findViewById(R.id.load_text_button);
        loadTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadTextTask loadTextTask = new LoadTextTask("http://www.jiuzhang.com/api/course/?format=json");
                loadTextTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        loadImageButton = (Button) findViewById(R.id.load_image_button);
        loadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadImageTask loadImageTask = new LoadImageTask("http://www.jiuzhang.com/media/avatars/guojing2.png");
                loadImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
    }

    class LoadTextTask extends AsyncTask<Void, Void, String> {

        private String url;

        public LoadTextTask(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(Void... voids) {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            text.setText(s);
        }
    }

    class LoadImageTask extends AsyncTask<Void, Void, byte[]> {

        private String url;

        public LoadImageTask(String url) {
            this.url = url;
        }

        @Override
        protected byte[] doInBackground(Void... voids) {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().bytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            /**
             * return null, not working
             * */
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            image.setImageBitmap(bitmap);
        }
    }
}
