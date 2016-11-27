package com.example.user.cram1001;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.cram1001.Adapter.ContentTest;
import com.example.user.cram1001.volleymgr.NetworkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class CheckParentsActivity extends AppCompatActivity {
    private String UClass,UNAME,UUSER,UStatus;
    private TextView UIDtest,CLASS,UUNAME,UUStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_parents);
        Intent intent = this.getIntent();

        UClass = intent.getStringExtra("UClass");
        UNAME = intent.getStringExtra("UNAME");
        UUSER = intent.getStringExtra("UUSER");
        UStatus = intent.getStringExtra("UStatus");

        UUNAME = (TextView) findViewById(R.id.CID);
        UUNAME.setText(UNAME);


        UUStatus = (TextView) findViewById(R.id.status);

//        UUStatus = (TextView) findViewById(R.id.status);
//        UUStatus.setText(Statuss);
        String strName = URLEncoder.encode(UNAME);
        String url = "https://cramschoollogin.herokuapp.com/api/querystudentstatus?name=" + strName;
        StringRequest request = new StringRequest(Request.Method.GET,url, QuerySuccessListener, QueryErrorListener);
        NetworkManager.getInstance(CheckParentsActivity.this).request(null, request);
    }
//    public void queryStatus() {
//        String strName = URLEncoder.encode(UNAME);
//        String url = "https://cramschoollogin.herokuapp.com/api/querystudentstatus?name=" + strName;
//        StringRequest request = new StringRequest(Request.Method.GET,url, QuerySuccessListener, QueryErrorListener);
//        NetworkManager.getInstance(CheckParentsActivity.this).request(null, request);
//    }
    private Response.Listener<String> QuerySuccessListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String string) {
            Log.d("Response", string);
            // contentTest=new ArrayList<ContentTest>();
            try {

                JSONArray ary = new JSONArray(string);
                StringBuilder ssstatuss = new StringBuilder();

                for (int i = 0; i < ary.length(); i++) {
                    JSONObject json = ary.getJSONObject(i);
                    String ssstatus = json.getString("sstatus");
                    UUStatus.setText(ssstatus);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private Response.ErrorListener QueryErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError err) {
            Toast.makeText(CheckParentsActivity.this, "Err " + err.toString(), Toast.LENGTH_LONG).show();
        }
    };

    public class ContentStatus {
        public String status="";

        public  ContentStatus(String nstatus){
            this.status=nstatus;
        }
    }
}