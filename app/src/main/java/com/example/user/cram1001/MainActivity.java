package com.example.user.cram1001;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.user.cram1001.volleymgr.NetworkManager;

import java.security.MessageDigest;
//import java.net.URL;


public class MainActivity extends AppCompatActivity {
    private EditText AccountInput, PasswordInput;
    private ProgressDialog mProgressDialog, nProgressDialog, oProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AccountInput = (EditText) findViewById(R.id.editID);
        PasswordInput = (EditText) findViewById(R.id.editPWD);

/*
        Button CreateMemberButton = (Button) findViewById(R.id.test);
        CreateMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, AddScheduleActivity.class);
                startActivity(intent);
            }
        });
*/

        // 取得帳號密碼
        /*String strUserName = AccountInput.getText().toString();
        String strPassword = PasswordInput.getText().toString();*/


        Button button = (Button) findViewById(R.id.create);//取得按鈕
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, GcmActivity.class);
                MainActivity.this.startActivity(intent);

            }
        });//將這個Listener綑綁在這個Button


        Button LogInButton = (Button) findViewById(R.id.button);
        LogInButton.setOnClickListener(LogInListener);
        oProgressDialog = new ProgressDialog(this);
        oProgressDialog.setMessage("帳號錯誤");
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("確認登入中");
        nProgressDialog = new ProgressDialog(this);
        nProgressDialog.setMessage("密碼錯誤");

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    private View.OnClickListener LogInListener = new View.OnClickListener() {


        public void onClick(View v) {
            try {

                String strAccount = URLEncoder.encode(AccountInput.getEditableText().toString(), "UTF-8");


                mProgressDialog.show();

                String url = "https://cramschoollogin.herokuapp.com/api/checkaccount?user=" + strAccount;
                StringRequest request = new StringRequest(Request.Method.GET, url, AccountSuccessListener, AccountErrorListener);
                NetworkManager.getInstance(MainActivity.this).request(null, request);


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    };

    protected Response.Listener<String> AccountSuccessListener = new Response.Listener<String>() {
        private String user;

        @Override
        public void onResponse(String response) {
            try {

                JSONArray array = new JSONArray(response);

                String strAccount = URLEncoder.encode(AccountInput.getEditableText().toString(),"UTF-8");


                int length = array.length();
                for (int i = 0; i < length; i++) {
                    JSONObject obj = array.getJSONObject(i);
                    user = obj.getString("user");

                    if (user.equals("1")) {
                        String url = "https://cramschoollogin.herokuapp.com/api/query?user=" + strAccount ;
                        StringRequest request = new StringRequest(Request.Method.GET, url, LoginSuccessListener, LoginErrorListener);
                        NetworkManager.getInstance(MainActivity.this).request(null, request);
                    } else {
                        mProgressDialog.dismiss();
                        oProgressDialog.show();
                    }
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
    protected ErrorListener AccountErrorListener = new ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError err) {
            mProgressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Err " + err.toString(), Toast.LENGTH_LONG).show();
        }
    };
    protected Listener<String> LoginSuccessListener = new Listener<String>() {
        private String DataPassword, UID;

        @Override
        public void onResponse(String response) {

            try {
                JSONArray array = new JSONArray(response);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    DataPassword = obj.getString("password");
                    UID = obj.getString("_id");
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            } finally {
                mProgressDialog.dismiss();
            }

            //String strPassword = PasswordInput.getEditableText().toString();
            String strPassword = md5(PasswordInput.getEditableText().toString());


            //將抓下來的密碼與輸入密碼比較
            if (DataPassword.equals(strPassword)) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.setClass(MainActivity.this, HomeActivity.class);
                intent.putExtra("UID", UID);

                startActivity(intent);

            }

            else {
                nProgressDialog.show();
            }

        }
    };

    public static String md5(String str)
    {
        MessageDigest md5 = null;
        try
        {
            md5 = MessageDigest.getInstance("MD5");
        }catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for(int i = 0; i < charArray.length; i++)
        {
            byteArray[i] = (byte)charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();
        for( int i = 0; i < md5Bytes.length; i++)
        {
            int val = ((int)md5Bytes[i])&0xff;
            if(val < 16)
            {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }


    protected ErrorListener LoginErrorListener = new ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError err) {
            mProgressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Err " + err.toString(), Toast.LENGTH_LONG).show();
        }
    };
    protected ErrorListener LoginAccountErrorListener = new ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError err) {
            mProgressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Err " + err.toString(), Toast.LENGTH_LONG).show();
        }
    };



}