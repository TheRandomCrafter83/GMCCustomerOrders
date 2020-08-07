package com.randomsoft.gmccustomerorders;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;



//region Scan QR Stuff
import android.content.Intent;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;

import static com.randomsoft.gmccustomerorders.Globals.ENC_PASSWORD;
//endregion

public class MainActivity extends AppCompatActivity {

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";
    View container;
    View decorView;

    ImageView btnScanQR;
    Button btn;
    EditText editUsername;
    EditText editPassword;


    String scannedQR_Code="";
    String errorMessage="";
    ViewTreeObserver.OnGlobalLayoutListener gll = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect measureRect = new Rect();
            container.getWindowVisibleDisplayFrame(measureRect);
            final NavigationBar nav = new NavigationBar(container.getRootView());
            int keypadHeight;
            keypadHeight = container.getRootView().getHeight() - measureRect.bottom;
            if (keypadHeight > 0) {
                // keyboard is opened
                //Toast.makeText(getApplicationContext(),"Hello World",Toast.LENGTH_LONG).show();
            } else if (keypadHeight > getNavBarHeight()) {
                //recyclerView.setVisibility(View.VISIBLE);
                //Toast.makeText(getApplicationContext(),"Hello World Hide",Toast.LENGTH_LONG).show();
            }else{
                hideSystemUI();
            }
        }
    };

    public static String getResourceString(Context context, int ResID){
         return context.getResources().getString(ResID);
    }

    public void initViews(){
        ImageView btnScanQR = findViewById(R.id.image_ScanQR);
        Button btn= findViewById(R.id.btnLogin);
        EditText editUsername= findViewById(R.id.edit_Username);
        EditText editPassword= findViewById(R.id.edit_Password);
        btnScanQR.setOnClickListener(this.imageScanQRClick);
        btn.setOnClickListener(this.btnLoginClick);
        editUsername.setOnTouchListener(this.editTouch);
        editPassword.setOnTouchListener(this.editTouch);
    }

    //Event Handlers-------------------------------------------------------------

    final View.OnClickListener btnLoginClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getApplicationContext(),"Hello World",Toast.LENGTH_LONG).show();
        }
    };

    final View.OnClickListener imageScanQRClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
            intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        }
    };

    final View.OnTouchListener editTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                container = view.getRootView();
                container.getViewTreeObserver().removeOnGlobalLayoutListener(gll);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        container.getViewTreeObserver().addOnGlobalLayoutListener(gll);
                    }
                }, 100);
                hideSystemUI();
            }else if(event.getAction()==MotionEvent.ACTION_UP){
                hideSystemUI();
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        decorView = getWindow().getDecorView();
        hideSystemUI();
        setContentView(R.layout.activity_main);
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        // Note that system bars will only be "visible" if none of the
                        // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            //showSystemUI();
                        } else if((visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {
                            hideSystemUI();
                        }
                    }
                });

        // try to show the keyboard and capture the result
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        IMMResult result = new IMMResult();
        imm.showSoftInput(editUsername, 0, result);

// if keyboard doesn't change, handle the keypress
        int res = result.getResult();
        if (res == InputMethodManager.RESULT_UNCHANGED_SHOWN ||
                res == InputMethodManager.RESULT_UNCHANGED_HIDDEN) {
            Toast.makeText(getApplicationContext(),Integer.toString(getNavBarHeight()),Toast.LENGTH_LONG).show();
        }



    }

    @Override
    protected void onResume(){
        super.onResume();
        hideSystemUI();
    }

    @Override
    public void onUserInteraction(){
        super.onUserInteraction();
        hideSystemUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == CommonStatusCodes.CANCELED){
            return;
        }
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    errorMessage =getResources().getString(R.string.barcode_success);
                    scannedQR_Code = barcode.displayValue;
                    Log.println(1,scannedQR_Code,scannedQR_Code);
                    try {
                        scannedQR_Code = ReusableProcedures.decryptData(scannedQR_Code,ENC_PASSWORD);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    errorMessage = getResources().getString(R.string.barcode_failure);
                }
            } else {
                errorMessage = String.format(getString( R.string.barcode_error) , CommonStatusCodes.getStatusCodeString(resultCode));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        LoginQR scanned  ;
        Gson gson = new Gson();

        try {
            scanned = gson.fromJson(scannedQR_Code, LoginQR.class);
            editUsername.setText(scanned.username);
            editPassword.setText(scanned.password);
        }catch(Exception e){
            ReusableProcedures.showMessage(this,"Error","Invalid QR code scanned");
        }
    }

    @Override
    protected void onSaveInstanceState (Bundle outState){
        super.onSaveInstanceState (outState);
        //outState.putBoolean("fullscreen_land",isFullscreen_land);
        //outState.putBoolean("fullscreen_port",isFullscreen_port);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }



    private void hideSystemUI() {
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE|
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private int getNavBarHeight(){
        Resources resources = getApplicationContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private boolean hasNavBar(){
        boolean temporaryHidden = ((this.getWindow().getDecorView().getVisibility() & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) !=0);
        if (!temporaryHidden){
            View d = this.getWindow().getDecorView();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                return (d.getRootWindowInsets().getStableInsetBottom() != 0);
            }
        }
        return true;
    }
}
