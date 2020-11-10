package org.techtown.sampleapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "휴대폰 정보 가져오기";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




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
        textViewAccounts.setText(accountsStringBuilder.toString());

        /*
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);


        Log.d(TAG, "The number of Accounts: " + numberOfAccounts);
        Log.d(TAG, accountsStringBuilder.toString());
        Log.d(TAG, "음성통화 상태 : [ getCallState ] >>> " + tm.getCallState());
        Log.d(TAG, "데이터통신 상태 : [ getDataState ] >>> " + tm.getDataState());
        Log.d(TAG, "IMEI : [ getDeviceId ] >>>" + tm.getDeviceId());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.d(TAG, "전화번호 : [ getLine1Number ] >>> " + tm.getLine1Number());
        Log.d(TAG, "통신사 ISO 국가코드 : [ getNetworkCountryIso ] >>> "+tm.getNetworkCountryIso());
        Log.d(TAG, "통신사 ISO 국가코드 : [ getSimCountryIso ] >>> "+tm.getSimCountryIso());
        Log.d(TAG, "망사업자 MCC+MNC : [ getNetworkOperator ] >>> "+tm.getNetworkOperator());
        Log.d(TAG, "망사업자 MCC+MNC : [ getSimOperator ] >>> "+tm.getSimOperator());
        Log.d(TAG, "망사업자명 : [ getNetworkOperatorName ] >>> "+tm.getNetworkOperatorName());
        Log.d(TAG, "망사업자명 : [ getSimOperatorName ] >>> "+tm.getSimOperatorName());
        Log.d(TAG, "SIM 카드 시리얼넘버 : [ getSimSerialNumber ] >>> "+tm.getSimSerialNumber());
        Log.d(TAG, "SIM 카드 상태 : [ getSimState ] >>> "+tm.getSimState());
        Log.d(TAG, "소프트웨어 버전넘버 : [ getDeviceSoftwareVersion ] >>> "+tm.getDeviceSoftwareVersion());
*/


        /*Context mAppContext = null;
        TelephonyManager tMgr = (TelephonyManager) mAppContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String mPhoneNumber = tMgr.getLine1Number();
        Log.d("PHONE", accountsStringBuilder.toString());
         */


    }
}