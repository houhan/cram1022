package com.example.user.cram1001;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.cram1001.volleymgr.ContentCheck;
import com.example.user.cram1001.volleymgr.NetworkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.R.attr.format;


public class CheckActivity extends AppCompatActivity {


    private ArrayList<ContentCheck> contentCheck = new ArrayList<ContentCheck>();
    private ListView listView;
    private MyAdapterCheck myAdapter;
    private Button checkbutton;
    private TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);


       /* Button checkbutton = (Button) findViewById(R.id.buttoncheck);


        checkbutton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                tv1.setText("HI");
                /*
                //new一個intent物件，並指定Activity切換的class
                Intent intent = new Intent();
                intent.setClass(CheckActivity.this, CheckActivity.class);

                //new一個Bundle物件，並將要傳遞的資料傳入
                Bundle bundle = new Bundle();
                bundle.putDouble("抵達",height );
                bundle.putString("sex", sex);

                //將Bundle物件assign給intent
                intent.putExtras(bundle);

                //切換Activity
                startActivity(intent);

            }

        });*/

        StringRequest request = new StringRequest(Request.Method.GET, "https://cramschoollogin.herokuapp.com/api/querystudentname", mResponseListener, mErrorListener);
        NetworkManager.getInstance(this).request(null, request);

        setContentView(R.layout.list_check);
        listView = (ListView) findViewById(R.id.listviewcheck);
        myAdapter = new MyAdapterCheck(this, contentCheck);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(CheckActivity.this)
                        .setTitle("是否抵達")
                        .setMessage("確定抵達 " + position + " item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tv1 = (TextView) findViewById(R.id.textcheck);
                                tv1.setText("到");
                                //myAdapter.removeItem(position);
                                //myAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

                return false;
            }
        });
    }

    void fillData() {
    }

    private Response.Listener<String> mResponseListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String string) {
            Log.d("Response", string);
            // contentTest=new ArrayList<ContentTest>();
            try {

                JSONArray ary = new JSONArray(string);
                StringBuilder names = new StringBuilder();
                for (int i = 0; i < ary.length(); i++) {
                    JSONObject json = ary.getJSONObject(i);
                    String name = json.getString("name");
                    ContentCheck contentC = new ContentCheck(name);
                    contentCheck.add(contentC);
                }
                myAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private Response.ErrorListener mErrorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Error", error.toString());
        }
    };


}