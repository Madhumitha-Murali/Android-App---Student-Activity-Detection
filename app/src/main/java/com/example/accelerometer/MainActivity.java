package com.example.accelerometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.widget.ZoomControls;

import java.util.Calendar;



import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.File;
//import java.io.FileOutputStream;
import java.io.FileWriter;


public class MainActivity extends AppCompatActivity implements SensorEventListener {


    private static final String TAG = "MainActivity";
    private SensorManager sensorManager;
    private Sensor accelerometer, mGyro, mMagno,mRot;
    public int num,t;
    PDFView pdfView;
    private Handler mHandler = new Handler();

    String xValue, yValue, zValue;
    String mydate,fileinit;
    String xGyroValue, yGyroValue, zGyroValue, xMagnoValue, yMagnoValue, zMagnoValue,xRotValue,yRotValue,zRotValue;
    float xval,yval,zval,xgval,ygval,zgval,xmval,ymval,zmval,xrval,yrval,zrval;
    float xval1,yval1,zval1,xgval1,ygval1,zgval1,xmval1,ymval1,zmval1,xrval1,yrval1,zrval1;
    TextView text1;
    ZoomControls zcl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //The sensor values file name is the timestamp at the time of starting the app
        //This is done so that it is possible to distinguish between sensor reports of different times
        fileinit = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        super.onCreate(savedInstanceState);
        t=0;
        xval=0;
        yval=0;
        zval=0;
        xgval=0;             //Initializing all the values to zero
        ygval=0;
        zgval=0;
        xmval=0;
        ymval=0;
        zmval=0;
        xrval=0;
        yrval=0;
        zrval=0;
        xval1=0;
        yval1=0;
        zval1=0;
        xgval1=0;
        ygval1=0;
        zgval1=0;
        xmval1=0;
        ymval1=0;
        zmval1=0;
        xrval1=0;
        yrval1=0;
        zrval1=0;
        setContentView(R.layout.activity_main);
        mHandler.postDelayed(mToastRunnable,0);

        LinearLayout myLayout = (LinearLayout) findViewById(R.id.Touch);

        //declaring the PDFView
        pdfView=(PDFView) findViewById(R.id.pdfView);
        num=0;

        extStore();

        //declaring the zoom-control for the pdf
        zcl=findViewById(R.id.zcl);
        zcl.setOnZoomOutClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                float x=pdfView.getScaleX();  //Inorder to change the scale (Zooming Out)
                float y=pdfView.getScaleY();
                pdfView.setScaleY(y-0.5f);
                pdfView.setScaleX(x-0.5f);
                pdfView.documentFitsView();
            }

           });

        zcl.setOnZoomInClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                float x=pdfView.getScaleX();   //Inorder to change the scale (Zooming In)
                float y=pdfView.getScaleY();
                pdfView.setScaleY(y+0.5f);
                pdfView.setScaleX(x+0.5f);
                pdfView.documentFitsView();
            }

        });

        /**mToastRunnable.run();
         Runnable mToastRunnable = new Runnable() {
        @Override
        public void run() {
        mHandler.postDelayed(this, 5000);

        saveLog(this);
        }
        };*/
        //startRepeating();
        //saveLog();

        pdfView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {    //This calculates the touch frequency and is called every time the user touches
                t=t+1;
                handleTouch();
              /*  if(t==1) {
                    startRepeating(v);
                }*/
                return false;
            }

        });

        text1=(TextView)findViewById(R.id.touchText);    //The Text view stores the no. of times the user has touched the screen
        /**xValue = (TextView) findViewById(R.id.xValue);
         yValue = (TextView) findViewById(R.id.yValue);
         zValue = (TextView) findViewById(R.id.zValue);

         xGyroValue = (TextView) findViewById(R.id.xGyroValue);
         yGyroValue = (TextView) findViewById(R.id.yGyroValue);
         zGyroValue = (TextView) findViewById(R.id.zGyroValue);

         xMagnoValue = (TextView) findViewById(R.id.xMagnoValue);
         yMagnoValue = (TextView) findViewById(R.id.yMagnoValue);
         zMagnoValue = (TextView) findViewById(R.id.zMagnoValue);


         xRotValue = (TextView) findViewById(R.id.xRotValue);
         yRotValue = (TextView) findViewById(R.id.yRotValue);
         zRotValue = (TextView) findViewById(R.id.zRotValue);

         */

        Log.d(TAG, "onCreate: Initializing Sensor Services");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);   //getting accelerometer sensor values
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Accelerometer Listener");

        } else {
            //xValue.setText("Accelerometer is not supported");
        }


        mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);              //getting gyroscope sensor values
        if (mGyro != null) {
            sensorManager.registerListener(this, mGyro, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Gyroscope Listener");

        } else {
            //xGyroValue.setText("Gyroscope is not supported");
        }


        mMagno = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);         //getting magnetic field values
        if (mMagno != null) {
            sensorManager.registerListener(this, mMagno, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Magnetometer Listener");

        } else {
            //xMagnoValue.setText("Magnetometer is not supported");
        }

        mRot = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);         //getting rotation vector sensor values
        if (mRot != null) {
            sensorManager.registerListener(this, mRot, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Rotation Vector Listener");

        } else {
            //xRotValue.setText("Rotation Vector is not supported");
        }


        pdfView = (PDFView)findViewById(R.id.pdfView);



      /*  pdfView.fromAsset("book.pdf")
                //.defaultPage(pageNumber)
                //.onPageChange(this)
                .enableAnnotationRendering(true)
                //.onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
                .pageFitPolicy()
                .load();
                */
        /**pdfView.fromAsset("book.pdf").pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
         .enableSwipe(true) // allows to block changing pages using swipe
         .swipeHorizontal(false)
         .enableDoubletap(true)
         .defaultPage(0)
         // allows to draw something on the current page, usually visible in the middle of the screen

         .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
         .password(null)
         .scrollHandle(null)
         .enableAntialiasing(true) // improve rendering a little bit on low-res screens
         // spacing between pages in dp. To define spacing color, set view background
         .spacing(0)
         .load();

         */

        pdfView.fromAsset("book1.pdf")
                //.pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)   //for zooming
                .defaultPage(0)
                .scrollHandle(new DefaultScrollHandle(this))

                // allows to draw something on the current page, usually visible in the middle of the screen
                //.onDraw(onDrawListener)
                // allows to draw something on all pages, separately for every page. Called only for visible pages
                //.onDrawAll(onDrawListener)
                //.onLoad(onLoadCompleteListener) // called after document is loaded and starts to be rendered
                //.onPageChange(onPageChangeListener)
                //.onPageScroll(onPageScrollListener)
                //.onError(onErrorListener)
                //.onPageError(onPageErrorListener)
                //.onRender(onRenderListener) // called after document is rendered for the first time
                // called on single tap, return true if handled, false to toggle scroll handle visibility
                //.onTap(onTapListener)
                //.onLongPress(onLongPressListener)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .autoSpacing(false)
                .scrollHandle(new DefaultScrollHandle(this))
                // add dynamic spacing to fit each page on its own on the screen
                //.linkHandler(DefaultLinkHandler)
                .pageFitPolicy(FitPolicy.WIDTH)
                .pageSnap(true) // snap pages to screen boundaries
                .pageFling(false) // make a fling change only a single page like ViewPager
                .nightMode(false) // toggle night mode
                .load();



    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Sensor sensor = sensorEvent.sensor;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.d(TAG, "onSensorChanged: X: " + sensorEvent.values[0] + " Y: " + sensorEvent.values[1] + " Z: " + sensorEvent.values[2]);

            /** xval=xval1;
             yval=yval1;
             zval=zval1;
             xval1=sensorEvent.values[0];
             yval1=sensorEvent.values[1];
             zval1=sensorEvent.values[2];
             xValue="x:"+(xval1-xval);
             yValue="y:"+(yval1-yval);
             zValue="z:"+(zval1-zval);

             xValue.setText("x: " + sensorEvent.values[0]);
             yValue.setText("y: " + sensorEvent.values[1]);
             zValue.setText("z: " + sensorEvent.values[2]);
             */
            xValue="x: " + sensorEvent.values[0];   //Assigning the current sensor values
            yValue="y: " + sensorEvent.values[1];
            zValue="z: " + sensorEvent.values[2];


        } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {


            //The difference in subsequent sensor values is used (We are not interested in the Absolute value of the magnetic field, rather the change
            //is of interest to us
            xgval=xgval1;  //storing previous value
            ygval=ygval1;
            zgval=zgval1;
            xgval1=sensorEvent.values[0];   //assigning the present value
            ygval1=sensorEvent.values[1];
            zgval1=sensorEvent.values[2];
            xGyroValue="x:"+(xgval1-xgval);  //Taking the difference
            yGyroValue="y:"+(ygval1-ygval);
            zGyroValue="z:"+(zgval1-zgval);
            //xGyroValue.setText("x: " + sensorEvent.values[0]);
            //yGyroValue.setText("y: " + sensorEvent.values[1]);
            //zGyroValue.setText("z: " + sensorEvent.values[2]);
        } else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

            //The difference in subsequent sensor values is used (We are not interested in the Absolute value of the magnetic field, rather the change
            //is of interest to us

            xmval=xmval1;       //storing previous value
            ymval=ymval1;
            zmval=zmval1;
            xmval1=sensorEvent.values[0];  //assigning the present value
            ymval1=sensorEvent.values[1];
            zmval1=sensorEvent.values[2];
            xMagnoValue="x:"+(xmval1-xmval);   //Taking the difference
            yMagnoValue="y:"+(ymval1-ymval);
            zMagnoValue="z:"+(zmval1-zmval);
            //xMagnoValue.setText("x: " + sensorEvent.values[0]);
            //yMagnoValue.setText("y: " + sensorEvent.values[1]);
            //zMagnoValue.setText("z: " + sensorEvent.values[2]);
        } else if (sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {

            xrval=xrval1; //storing previous value
            yrval=yrval1;
            zrval=zrval1;
            xrval1=sensorEvent.values[0];  //assigning the present value
            yrval1=sensorEvent.values[1];
            zrval1=sensorEvent.values[2];
            xRotValue="x:"+(xrval1-xrval);;   //Taking the difference
            yRotValue="y:"+(yrval1-yrval);
            zRotValue="z:"+(zrval1-zrval);
            //xRotValue.setText("x: " + sensorEvent.values[0]);
            //yRotValue.setText("y: " + sensorEvent.values[1]);
            //zRotValue.setText("z: " + sensorEvent.values[2]);
        }

        //The (For Title of the CSV file) is obtained in this way
        mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

    }


    //num gets incremented each time handleTouch method is invoked by the OnTouchListener()
    public void handleTouch() {
        num = num + 1;

        text1.setText(Integer.toString(num));
        /**Toast.makeText(this,"", Toast.LENGTH_SHORT).show();*/

    }

    //This method checks whether external storage is possible (whether permissions are given) on the device

    private boolean extStore(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            Log.i("State","Yes,it is Writable!");
            return true;
        }else
        {
            Log.i("State","No,it is not Writable!");
            return false;
        }
    }

    //This method automatically calls the runnable to start gathering sensor values to save it to the log

    public void startRepeating(View view) {
        //mHandler.postDelayed(mToastRunnable, 5000);
        mToastRunnable.run();
    }

    //This method can be used with a button to forcibly prevent the app from saving sensor values into the csv file. In the current
    //implementation, this button is not used.

    public void stopRepeating(View v) {
        mHandler.removeCallbacks(mToastRunnable);
    }

    //This toast is just for testing to confirm that the content is being written into the csv file

    private Runnable mToastRunnable = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(MainActivity.this, "This is a delayed toast", Toast.LENGTH_SHORT).show();
            mHandler.postDelayed(this, 20000);

            saveLog(this);
        }
    };


    //The sensor values are collected in the form of a string and written into the memory with saveLog() method
    public void saveLog(Runnable view)
    {
        // String FILENAME="androidLog.csv";
        num=0;

        String entry=xValue+ ","+
                yValue+ ","+
                zValue+ ","+
                xGyroValue +","+
                yGyroValue +","+
                zGyroValue +","+
                xMagnoValue +","+
                yMagnoValue +","+
                zMagnoValue +","+
                xRotValue +","+
                yRotValue +","+
                zRotValue +","+
                text1.getText()+","+
                mydate +","+"\n";

        /**String entry= xValue.getText().toString() + "," +
         yValue.getText().toString() + "," +
         zValue.getText().toString() + "," +
         xGyroValue.getText().toString() + "," +
         yGyroValue.getText().toString() + "," +
         zGyroValue.getText().toString() + "," +
         xMagnoValue.getText().toString() + "," +
         yMagnoValue.getText().toString() + "," +
         zMagnoValue.getText().toString() + "," +
         xRotValue.getText().toString() + "," +
         yRotValue.getText().toString() + "," +
         zRotValue.getText().toString() + "," +
         text1.getText()+ "\n";
         */

        try{

            //To write the File into the Internal storage of the device

            File folder= new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            //String filename= folder.toString()+"/androidTest.csv";
            String filename= folder.toString()+"/"+fileinit+".csv";
            FileWriter fw = new FileWriter(filename,true);
            fw.append(entry);
            Toast.makeText(this,"Entry Made",Toast.LENGTH_SHORT).show();
            fw.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}

