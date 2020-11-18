package org.techtown.sampleapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings.Secure;

import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;


public class MainActivity extends AppCompatActivity {

    TextView textView3;
    TextView textView4;
    private String android_id;
    private FusedLocationProviderClient mfusedLocationClient;

    private static String TAG = "휴대폰 정보 가져오기";

    private static final int MULTIPLE_PERMISSION = 10235;

    private String[] PERMISSIONS = {
            android.Manifest.permission.GET_ACCOUNTS,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.READ_SMS,
            android.Manifest.permission.READ_PHONE_NUMBERS,
            android.Manifest.permission.ACCESS_WIFI_STATE
};
    private String SerialNumber;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);

        mfusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click", "Start collecting location info");
                getCurrentLocation();
                startLocationService();
            }
        });

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, MULTIPLE_PERMISSION);
        } else {
            showInfo();
        }

    }

    private void getCurrentLocation() {
        OnCompleteListener<Location> mCompleteListener = new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    Location mCurrentLocation = task.getResult();
                    String message = "Lat:" + mCurrentLocation.getLatitude() + " / Alt:" + mCurrentLocation.getAltitude();
                    textView4.setText("Location(Google API)-> "+message);
                    Log.d("Location(Google API)", "Location(Google API)-> "+message);
                } else {
                    Log.d("Location(Google API)", "getCurrentLocation"+ task.getException());
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mfusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, mCompleteListener);
    }

    public void startLocationService() {
        LocationManager mLM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            String message = "Location info: No permission";
            textView3.setText(message);
            Log.d("Location(LM)", message);
            //return;
        }

        Location location = mLM.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Log.d("Location(LM)", String.valueOf(location));
        textView3.setText("Location(LM): " +location);

        if (location != null){
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            String message = "Location(LM) -> Latitude:" + latitude + " / Longitude:" + longitude;

            textView3.setText(message);
            Log.d(TAG, message);

        }

        GPSListener gpsListener = new GPSListener();
        long minTime = 10000;
        float minDistance = 0;

        mLM.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
        Toast.makeText(getApplicationContext(), "내 위치확인 요청함", Toast.LENGTH_SHORT).show();

    }

    class GPSListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            String message = "Location(LM)-> Latitude:" + latitude + " / Longtitude:" + longitude;
            textView3.setText(message);
            Log.d(TAG, message);

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showInfo() {
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
        textViewNumberOfAccounts.setText("● The number of Accounts: " + numberOfAccounts);

        //-----------------------------------------------------------
        // 계정들을 텍스트 뷰로 출력합니다.

        StringBuilder accountsStringBuilder = new StringBuilder();
        accountsStringBuilder.append("● Accounts (Types): \n");
        for (int i = 0; i < numberOfAccounts; i++) {
            accountsStringBuilder.append(accounts[i].name);
            accountsStringBuilder.append(" (");
            accountsStringBuilder.append(accounts[i].type);
            accountsStringBuilder.append(")\n");
        }

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        accountsStringBuilder.append("● Telephone information: ");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            accountsStringBuilder.append("\n전화번호 : [getLine1Number] >>> No permission");
            return;
        } else {
            accountsStringBuilder.append("\n전화번호 : [getLine1Number] >>> "  + tm.getLine1Number());
        }

        android_id = Secure.getString(getContentResolver(),Secure.ANDROID_ID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            SerialNumber = Build.getSerial();
        }
        else
        {
            SerialNumber = Build.SERIAL;
        }
        accountsStringBuilder.append("\nHardware Serial >>> " + SerialNumber);
        accountsStringBuilder.append("\nAndroid ID >>> " + android_id);
        accountsStringBuilder.append("\nIMEI : [getDeviceId] >>>" + tm.getDeviceId()); //READ_PRIVILEGED_PHONE_STATE
        accountsStringBuilder.append("\nIMEI : [getImei] >>>" + tm.getImei()); //READ_PRIVILEGED_PHONE_STATE
        accountsStringBuilder.append("\nIMSI : [getSubscriberId] >>>" + tm.getSubscriberId()); //READ_PRIVILEGED_PHONE_STATE
        //gsf,hardware serial

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        WifiInfo wInfo = wifiManager.getConnectionInfo();
        //String macAddress = wInfo.getMacAddress();
        accountsStringBuilder.append("\nMAC Address >>> " + wInfo.getMacAddress());

        //accountsStringBuilder.append("\nMAC Address >>> " + macAddress);
        //android.net.wifi.WifiInfo.getMacAddress
        //bluetooth
        accountsStringBuilder.append("\n[getNetworkOperator] >>> " + tm.getNetworkOperator()); //망사업자 MCC+MNC
        accountsStringBuilder.append("\n[getNetworkOperatorName] >>> " + tm.getNetworkOperatorName()); //망사업자명
        accountsStringBuilder.append("\n[getSimOperator] >>> " + tm.getSimOperator()); //망사업자 MCC+MNC
        accountsStringBuilder.append("\n[getSimOperatorName] >>> " + tm.getSimOperatorName());
        accountsStringBuilder.append("\n[getSimSerialNumber] >>> " + tm.getSimSerialNumber()); //READ_PRIVILEGED_PHONE_STATE, SIM 카드 S/N :
        //Too long to show in text
        accountsStringBuilder.append("\nNetConfig >>> Too Long (Log)");
        //accountsStringBuilder.append("\nNetConfig >>> " + wifiManager.getConfiguredNetworks());
        Log.d(TAG, "NetConfig >>> " + wifiManager.getConfiguredNetworks());

        accountsStringBuilder.append("\nBSSID >>> " + wInfo.getBSSID());
        accountsStringBuilder.append("\nSSID >>> " + wInfo.getSSID());

        int cid = 0;
        int lac = 0;
        try {
            if (tm != null) {
                GsmCellLocation gc = (GsmCellLocation) tm.getCellLocation();
                if (null != gc) {
                    cid = gc.getCid();
                    lac = gc.getLac();
                }
            }
        } catch (Exception e) {
            if (tm != null) {
                CdmaCellLocation location = (CdmaCellLocation) tm.getCellLocation();
                if (null != location) {
                }
                lac = location.getNetworkId();
                cid = location.getBaseStationId();
                cid /= 16;
            }
        }
        accountsStringBuilder.append("\n[getCid, getLac] >>> " + lac + "," + cid);

        /*FusedLocationProviderClient.getLastLocation
        android.location.LocationManager.requestLocationUpdates
        android.location.LocationManager.requestSingleUpdate
        android.location.LocationManager.getLastKnownLocation
        android.location.LocationManager.addProximityAlert
        android.telephony.gsm.GsmCellLocation.getCid
        android.telephony.gsm.GsmCellLocation.getLac
        */
        accountsStringBuilder.append("\n[getCellLocation] >>> " + tm.getCellLocation());
        accountsStringBuilder.append("\n[getAllCellInfo] >>> Too Long (Log)");
        //accountsStringBuilder.append("\n[getAllCellInfo] >>> " + tm.getAllCellInfo());
        //Too long to show in text
        Log.d(TAG, "[getAllCellInfo] >>> " + tm.getAllCellInfo());
        accountsStringBuilder.append("\n[getNeighboringCellInfo] >>> "+tm.getNeighboringCellInfo());

        //accountsStringBuilder.append("\n통신사 ISO 국가코드[getNetworkCountryIso] >>> " + tm.getNetworkCountryIso());
        //accountsStringBuilder.append("\n통신사 ISO 국가코드[getSimCountryIso] >>> " + tm.getSimCountryIso());
        //accountsStringBuilder.append("\nSIM 카드 상태[getSimState] >>> " + tm.getSimState());
        //Log.d(TAG, "소프트웨어 버전넘버 : [getDeviceSoftwareVersion] >>> "+tm.getDeviceSoftwareVersion());

        //LocationManager mLM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        GsmCellLocation cellLocation = (GsmCellLocation) tm.getCellLocation();
        //accountsStringBuilder.append("\nCell ID >>> " + cellLocation.getCid());
        //accountsStringBuilder.append("\nLocation area code >>> " + cellLocation.getLac());

        textViewAccounts.setText(accountsStringBuilder.toString());

    }


    /*
        LocationManager mLM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        private void registerLocationUpdates() {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLM.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000, 1, mLocationListener);

            mLM.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 1, mLocationListener);
    //1000은 1초마다, 1은 1미터마다 해당 값을 갱신한다는 뜻으로, 딜레이마다 호출하기도 하지만

    //위치값을 판별하여 일정 미터단위 움직임이 발생 했을 때에도 리스너를 호출 할 수 있다.
        }

        private final LocationListener mLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
    //여기서 위치값이 갱신되면 이벤트가 발생한다.
    //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다
                if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
    //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
                    double longitude = location.getLongitude();    //경도
                    double latitude = location.getLatitude();         //위도
                    float accuracy = location.getAccuracy();        //신뢰도
                } else {
    //Network 위치제공자에 의한 위치변화
    //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.
                }
            }
            public void onProviderDisabled(String provider) {
            }
            public void onProviderEnabled(String provider) {
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        };
    */
    @RequiresApi(api = Build.VERSION_CODES.O)
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
