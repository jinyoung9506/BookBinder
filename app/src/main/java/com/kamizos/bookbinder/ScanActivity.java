package com.kamizos.bookbinder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanActivity extends AppCompatActivity {

    private IntentIntegrator scanner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scanner = new IntentIntegrator(this);
        scanner.initiateScan();
    }
    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        Intent intent = new Intent();

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this,"No contents", Toast.LENGTH_LONG);
                scanner.initiateScan();
            } else { //QR코드, 내용 존재
                try {
                    /* QR 코드 내용*/
                    String temp = result.getContents();

                    /* 로직
                     *
                     * 로직 끝 */

                    Toast.makeText(getApplicationContext(), result.getContents(), Toast.LENGTH_LONG).show();
                    intent.putExtra("scanresult",result.getContents());
                    setResult(RESULT_OK, intent);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this,"Exception Occured", Toast.LENGTH_LONG);
                    scanner.initiateScan();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        finish();
    }

}
