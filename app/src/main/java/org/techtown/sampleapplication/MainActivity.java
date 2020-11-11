package org.techtown.sampleapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "휴대폰 정보 가져오기";

    private static final int MULTIPLE_PERMISSION = 10235;

    private String[] PERMISSIONS = {
            android.Manifest.permission.GET_ACCOUNTS,
            android.Manifest.permission.READ_PHONE_STATE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, MULTIPLE_PERMISSION);
        } else {
            showInfo();
        }


    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void showInfo(){
        //-----------------------------------------------------------
        TextView textViewNumberOfAccounts
                = (TextView) findViewById(R.id.textView1);
        TextView textViewAccounts
                = (TextView) findViewById(R.id.textView2);

        //-----------------------------------------------------------
        // 계정 관리자에 접근하고, 그것으로부터 계정들을 배열로 구합니다.

        AccountManager accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccounts();

        //-----------------------------------------------------------
        // 계정의 개수를 텍스트 뷰로 출력합니다.

        int numberOfAccounts = accounts.length;
        textViewNumberOfAccounts.setText("The number of Accounts: " + numberOfAccounts);

        //-----------------------------------------------------------
        // 계정들을 텍스트 뷰로 출력합니다.

        StringBuilder accountsStringBuilder = new StringBuilder();
        accountsStringBuilder.append("Accounts (Types): \n");
        for (int i = 0; i < numberOfAccounts; i++) {
            accountsStringBuilder.append(accounts[i].name);
            accountsStringBuilder.append(" (");
            accountsStringBuilder.append(accounts[i].type);
            accountsStringBuilder.append(")\n");
        }


        accountsStringBuilder.append("\nTelephone information: \n");

        textViewAccounts.setText(accountsStringBuilder.toString());



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showInfo();
                } else {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("앱 권한");
                    alertDialog.setMessage("해당 앱의 원할한 기능을 이용하시려면 애플리케이션 정보>권한> 에서 모든 권한을 허용해 주십시오");

                    alertDialog.setPositiveButton("권한설정",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                                    startActivity(intent);
                                    dialog.cancel();
                                }
                            });

                    alertDialog.setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.show();
                }
                return;
        }
    }
}
