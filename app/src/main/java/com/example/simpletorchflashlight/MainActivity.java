package com.example.simpletorchflashlight;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private Button buttonEnable;
    private ImageView imageFlashLight;
    private static final int cameraReq = 50;
    private boolean FlashLightStat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageFlashLight = (ImageView) findViewById(R.id.imageFlashLight);
        buttonEnable = (Button) findViewById(R.id.buttonEnable);

        final boolean hasCamFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        boolean isEnable = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        buttonEnable.setEnabled(!isEnable);
        imageFlashLight.setEnabled(isEnable);
        buttonEnable.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, cameraReq);
            }
        });

        imageFlashLight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(hasCamFlash)
                {
                    if(FlashLightStat)
                    {
                        flashLightOff();
                    }
                    else
                    {
                        flashLightOn();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "No Flash Available on Your Device", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void flashLightOn()
    {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try
        {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId,true);
            FlashLightStat = true;
            imageFlashLight.setImageResource(R.drawable.btn_swtich_on);
        }
        catch(CameraAccessException e)
        {

        }
    }

    private void flashLightOff()
    {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try
        {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId,false);
            FlashLightStat = false;
            imageFlashLight.setImageResource(R.drawable.btn_switch_off);
        }
        catch(CameraAccessException e)
        {

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch(requestCode)
        {
            case cameraReq :
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    buttonEnable.setEnabled(false);
                    buttonEnable.setText("Camera Enabled");
                    imageFlashLight.setEnabled(true);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Permission Denied for the Camera", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
