package com.example.abhishek.clg_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.VectorDrawable;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class ImageviewTouch extends AppCompatActivity {

    private ImageView imageView;

    private int x1, x2, y1, y2;
    private int count = 0;

    public String TAG = "TAG";


    private int top_x, top_y, bottom_x, bottom_y, left_shoulder_x, left_shoulder_y, right_shoulder_x, right_shoulder_y, waist_left_x, waist_left_y, chest_left_x, chest_left_y, chest_right_x, chest_right_y, waist_right_x, waist_right_y;
    private double given_height = 1.8288;
    private double top_down_distance;
    private double formula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageview_touch);

        imageView = findViewById(R.id.im_view);

        String imgPath = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("imagePath", "");
        Log.d("TAG", "onCreate: path : " + imgPath);
        File imgFile = new File(imgPath);
        if (imgFile.exists()) {
            final Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            final Bitmap newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            imageView.setImageBitmap(bitmap);
            final Canvas canvas = new Canvas(newBitmap);
            final Paint paint = new Paint();
            final Paint paint2 = new Paint();
            paint.setColor(ContextCompat.getColor(this, R.color.selectDark));
            paint2.setColor(ContextCompat.getColor(this, R.color.selectLight));
            int[] co = new int[2];
            int cx = imageView.getTop();
            int cy = imageView.getLeft();
            Log.d("TAG", "onCreate: co : " + cx + "," + cy);

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Log.i("TAG", "touched down");
                            Log.i("TAG", "onTouch: (" + x + ", " + y + ")");
                            break;
                        case MotionEvent.ACTION_MOVE:
                            Log.i("TAG", "moving: (" + x + ", " + y + ")");
                            break;
                        case MotionEvent.ACTION_UP:
                            Log.i("TAG", "touched up at : " + x + ":" + y);

                            switch (count) {
                                case 0:
                                    top_x = x;
                                    top_y = y;
                                    count++;
                                    break;
                                case 1:
                                    bottom_x = x;
                                    bottom_y = y;
                                    count++;
                                    break;
                                case 2:
                                    left_shoulder_x = x;
                                    left_shoulder_y = y;
                                    count++;
                                    break;
                                case 3:
                                    right_shoulder_x = x;
                                    right_shoulder_y = y;
                                    count++;
                                    break;
                                case 4:
                                    chest_left_x = x;
                                    chest_left_y = y;
                                    count++;
                                    break;
                                case 5:
                                    chest_right_x = x;
                                    chest_right_y = y;
                                    count++;
                                    break;
                                case 6:
                                    waist_left_x = x;
                                    waist_left_y = y;
                                    count++;
                                    break;
                                case 7:
                                    waist_right_x = x;
                                    waist_right_y = y;
                                    count++;
                                    break;
                            }

                            break;
                    }
//                    float xx = x * (float) 3.16;
//                    float yy = y * (float) 3.456;
                    canvas.drawCircle(x, y, 40, paint2);
                    canvas.drawCircle(x, y, 15, paint);    // for circle dot
                    imageView.setImageBitmap(newBitmap);
                    //imageView.invalidate();

                    return true;
                }
            });
        }

//        final Bitmap bitmap = null;
    }

    private Bitmap rotateImage(Bitmap img) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public void onCalculateClick(View view) {

        double shoulder_distance = getDistance(left_shoulder_x, left_shoulder_y, right_shoulder_x, right_shoulder_y);
        double chest_distance = getDistance(chest_left_x, chest_left_y, chest_right_x, chest_right_y);
        double waist_distance = getDistance(waist_left_x, waist_left_y, waist_right_x, waist_right_y);
        top_down_distance = getDistance(top_x, top_y, bottom_x, bottom_y);

        formula = given_height / top_down_distance;

        Log.d(TAG, "shoulder length : " + getActualDistance(shoulder_distance));
        Log.d(TAG, "chest length : " + getActualDistance(chest_distance));
        Log.d(TAG, "waist length : " + getActualDistance(waist_distance));

//        int temp1 = x2 - x1;
//        double temp2 = Math.pow(temp1, 2);
//        int temp3 = y2 - y1;
//        double temp4 = Math.pow(temp3, 2);
//
//        double temp5 = temp2 + temp4;
//
//        double ans = Math.sqrt(temp5);
//        Log.d("TAG", "onCalculateClick: DISTANCE : " + ans * one_pixel_length);


    }

    private double getActualDistance(double distance) {

        double length = formula * distance;

        return length;
    }

    private double getDistance(int x1, int y1, int x2, int y2) {

        int tmp1 = x2 - x1;
        int tmp2 = y2 - y1;

        double tmp3 = Math.pow(tmp1, 2);
        double tmp4 = Math.pow(tmp2, 2);

        double distance = Math.sqrt(tmp3 + tmp4);


        return distance;
    }
}
