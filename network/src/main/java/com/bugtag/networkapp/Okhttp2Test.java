package com.bugtag.networkapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by bugtags.com on 16/3/8.
 */
public class Okhttp2Test {

    private static final String HOST_PREFIX = "http://192.168.1.105:8080";
    private static final String TAG = "Okhttp2Test";
    static Handler handler = new Handler(Looper.getMainLooper());

    static OkHttpClient okHttpClient = new OkHttpClient();

    public static void getHtml() {
        String urlStr = HOST_PREFIX + "/get/html?xxx=bb";

        urlStr = "http://baidu.com";

        Request request = new Request.Builder().
                url(urlStr).
                addHeader("a", "b").
                addHeader("b", "c").
                build();
        Log.d(TAG, urlStr);
        try {
            Response response = okHttpClient.newCall(request).execute();

            Log.d(TAG, response.body().string());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getJson() {
        String urlStr = HOST_PREFIX + "/get/json?xxx=bb";

        urlStr = "http://baidu.com";

        Request request = new Request.Builder().
                addHeader("a", "b").
                addHeader("b", "c").
                url(urlStr).
                build();


        Log.d(TAG, urlStr);
        try {
            Response response = okHttpClient.newCall(request).execute();

            Log.d(TAG, response.body().string());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getFile(final View view) {
        String urlStr = HOST_PREFIX + "/get/file?xxx=bb";


        urlStr = "http://baidu.com";

        Request request = new Request.Builder().
                url(urlStr).
                addHeader("a", "b").
                addHeader("b", "c").
                build();
        Log.d(TAG, urlStr);
        try {
            Response response = okHttpClient.newCall(request).execute();

            final Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());

            if (bitmap != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ((Button) view).setBackgroundDrawable(new BitmapDrawable(bitmap));
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void postUrlencode() {
        String urlStr = HOST_PREFIX + "/post/encode?xxx=bb";


        urlStr = "http://baidu.com";

        RequestBody body = new FormEncodingBuilder().
                add("name", "hello").
                add("p1", "1").
                add("p2", "2").
                build();

        Request request = new Request.Builder().
                url(urlStr).post(body).
                addHeader("a", "b").
                addHeader("b", "c").
                build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            Log.d(TAG, response.body().string());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void postMultipart(Activity activity) {
        String urlStr = HOST_PREFIX + "/post/multer";


        urlStr = "http://baidu.com";

        //bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher);

        //bitmap to byte
        byte[] pixels = new byte[bitmap.getWidth() * bitmap.getHeight()];
        for (int i = 0; i < bitmap.getWidth(); ++i) {
            for (int j = 0; j < bitmap.getHeight(); ++j) {
                //we're interested only in the MSB of the first byte,
                //since the other 3 bytes are identical for B&W images
                pixels[i + j] = (byte) ((bitmap.getPixel(i, j) & 0x80) >> 7);
            }
        }

        //attachment
        String attachmentName = "bitmap";
        String attachmentFileName = "bitmap.png";

        RequestBody body = new MultipartBuilder().
                type(MultipartBuilder.FORM).
                addFormDataPart(attachmentName, attachmentFileName, RequestBody.create(MediaType.parse("image/png"), pixels)).
                build();

        Request request = new Request.Builder().
                url(urlStr).post(body).
                addHeader("a", "b").
                addHeader("b", "c").
                build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            Log.d(TAG, response.body().string());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}