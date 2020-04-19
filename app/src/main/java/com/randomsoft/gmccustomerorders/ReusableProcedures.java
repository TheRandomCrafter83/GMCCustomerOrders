package com.randomsoft.gmccustomerorders;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.IntRange;
import androidx.annotation.Nullable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public final class ReusableProcedures {
    //Variables for Encrypt/Decrypt
    private static final String DIGEST_ALGORITHM = "SHA-256";
    private static final String TRANSFORMATION = "AES";
    private static final String CIPHER_ENCODING= "UTF-8";

    //Functions to show a message box/alert box
    static void showMessage(Context context,String title, String message, String dismisstext){
        String btnText = !dismisstext.isEmpty() ? dismisstext : context.getString(R.string.okbuttontext);
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setCancelable(false);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlg.setContentView(R.layout.message_dialog);

        TextView tvTitle = dlg.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        TextView tvMessage = dlg.findViewById(R.id.tv_message);
        tvMessage.setText(message);
        Button okButton = dlg.findViewById(R.id.button_ok);
        okButton.setText(btnText);
        okButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dlg.dismiss();
            }
        });
        dlg.show();
    }

    static void showMessage(Context context,String title, String message){
            //String btnText = !dismisstext.isEmpty() ? dismisstext : "Ok";
            String titleText = !title.isEmpty() ? title : context.getResources().getString(R.string.app_name);
            final Dialog dlg = new Dialog(context);
            dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dlg.setCancelable(false);
            dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dlg.setContentView(R.layout.message_dialog);
            TextView tvTitle = dlg.findViewById(R.id.tv_title);
            tvTitle.setText(titleText);
            TextView tvMessage = dlg.findViewById(R.id.tv_message);
            tvMessage.setText(message);
            Button okButton = dlg.findViewById(R.id.button_ok);
            okButton.setText(context.getString(R.string.okbuttontext));
            okButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    dlg.dismiss();
                }
            });
            dlg.show();
        }


    //Encryption/Decryption
    static String encryptData(String data, String password) throws Exception{
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(TRANSFORMATION);
        c.init(Cipher.ENCRYPT_MODE ,key);
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.encodeToString(encVal,Base64.DEFAULT);
    }

    static String decryptData(String data, String password) throws Exception{
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(TRANSFORMATION);
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decVal = Base64.decode(data,Base64.DEFAULT);
        byte[] decValue = c.doFinal(decVal);
        return new String(decValue);
    }

    private static SecretKeySpec generateKey(String password) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance(DIGEST_ALGORITHM);
        byte[] bytes = password.getBytes(CIPHER_ENCODING);
        digest.update(bytes,0,bytes.length);
        byte[] key = digest.digest();
        return new SecretKeySpec(key,TRANSFORMATION);
    }

    static Bitmap getQRImage(Context context,String data){
        return QRCodeHelper.newInstance(context).setContent(data).setErrorCorrectionLevel(ErrorCorrectionLevel.Q).setMargin(2).getQRCOde();
    }
}

