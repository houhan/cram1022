package com.example.user.cram1001;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class BeaconActivity extends AppCompatActivity {
    private BluetoothAdapter mBluetoothAdapter; //宣告一個要放入藍芽資訊的堆疊

    private TextView beaconn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);

        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE); //宣告藍芽資訊接收管理
        mBluetoothAdapter = bluetoothManager.getAdapter(); //將接收的資訊放入堆疊
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {  //堆疊是空的 or 堆疊disabled
            Intent enableBluetooth = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 1);
        }
        mBluetoothAdapter.startLeScan(mLeScanCallback); //開始接收藍芽資訊

    }

    private Handler mHandler = new Handler() {

    };

    private Runnable mDetectRunnable = new Runnable() {  //監聽器

        @SuppressLint("NewApi")
        @Override
        public void run() {
            // TODO detect logic
            mHandler.postDelayed(this, 1000);
        }

    };

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {   //接收到藍芽資訊然後針對接收到的資料進行解析
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi,
                             final byte[] scanRecord) {
            int startByte = 2;
            boolean patternFound = false;
            //ibeacon
            while (startByte <= 5) {
                if (((int) scanRecord[startByte + 2] & 0xff) == 0x02 && // Identifies
                        // an
                        // iBeacon
                        ((int) scanRecord[startByte + 3] & 0xff) == 0x15) { // Identifies
                    // correct
                    // data
                    // length
                    patternFound = true;
                    break;
                }
                startByte++;
            }
            // 收到beacon資料
            if (patternFound) {
                // uuid 16位元
                byte[] uuidBytes = new byte[16];
                System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16);
                String hexString = bytesToHex(uuidBytes);

                // ibeaconUUID
                String uuid = hexString.substring(0, 8) + "-"
                        + hexString.substring(8, 12) + "-"
                        + hexString.substring(12, 16) + "-"
                        + hexString.substring(16, 20) + "-"
                        + hexString.substring(20, 32);

                // ibeaconmajor id
                int major = (scanRecord[startByte + 20] & 0xff) * 0x100
                        + (scanRecord[startByte + 21] & 0xff);

                // ibeaconminor id
                int minor = (scanRecord[startByte + 22] & 0xff) * 0x100
                        + (scanRecord[startByte + 23] & 0xff);

                String stringValue = Integer.toString(minor);

                String ibeaconName = device.getName();
                String mac = device.getAddress();

                int txPower = (scanRecord[startByte + 24]);
                Log.d("BLE",bytesToHex(scanRecord));
                Log.d("BLE", "Name:" + ibeaconName + "\nMac:" + mac
                        + " \nUUID:" + uuid + "\nMajor:" + major + "\nMinor:"
                        + minor + "\nTxPower:" + txPower + "\nrssi:" + rssi);

                Log.d("BLE","distance:"+calculateAccuracy(txPower,rssi));
                mHandler.postDelayed(mDetectRunnable, 1000);

                beaconn = (TextView) findViewById(R.id.beacon);
                beaconn.setText(stringValue);
            }
        }
    };
    static final char[] hexArray = "0123456789ABCDEF".toCharArray();

/*
    //按鈕事件
    public void button1_Click(View view) {

        //顯示掃描結果
        Toast.makeText(getApplicationContext(), "MAC：" + mac, Toast.LENGTH_SHORT).show();
        //清除記憶值

        mac ="沒有找到Beacon";
    }
*/
    private static String bytesToHex(byte[] bytes) {  //位元轉換
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    protected static double calculateAccuracy(int txPower, double rssi) {   //計算距離 (沒記錯的話)
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }

        double ratio = rssi * 1.0 / txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio, 10);
        } else {
            double accuracy = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
            return accuracy;
        }
    }
}
