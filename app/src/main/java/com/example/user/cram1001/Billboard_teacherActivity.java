package com.example.user.cram1001;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.cram1001.Adapter.ContentTest;
import com.example.user.cram1001.Adapter.MyAdapter;
import com.example.user.cram1001.volleymgr.NetworkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Billboard_teacherActivity extends AppCompatActivity {
    private ArrayList<ContentTest> contentTest = new ArrayList<ContentTest>();
    private ListView listView;
    private MyAdapter myAdapter;
    private String date,title,content,DelGood ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billboard_teacher);
        //設定此Activity使用的res layout
        //setContentView(R.layout.list);
        listView = (ListView) findViewById(R.id.list1105);
        myAdapter = new MyAdapter(this, contentTest);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//回前頁

        StringRequest request = new StringRequest(Request.Method.GET, "https://cramschoollogin.herokuapp.com/api/querybillboard", mResponseListener, mErrorListener);
        NetworkManager.getInstance(this).request(null, request);

        Button buttonaddbill = (Button) findViewById(R.id.buttonaddbill);//取得按鈕
        buttonaddbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Billboard_teacherActivity.this, AddBillboardActivity.class);
                Billboard_teacherActivity.this.startActivity(intent);
            }
        });


 /*       Button DeleteButton = (Button) findViewById(R.id.deletebb);
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest request = new StringRequest(Request.Method.GET, "https://cramschoollogin.herokuapp.com/api/delete2" , mOnDeleteSuccessListener, mOnErrorListener);
                NetworkManager.getInstance(BillboardActivity.this).request(null, request);
            }
        });*/

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final ContentTest contenttest = (ContentTest) parent.getAdapter().getItem(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(Billboard_teacherActivity.this);

                new AlertDialog.Builder(Billboard_teacherActivity.this)
                        .setTitle("want to delele?")
                        .setMessage("Want to delete " + position + " item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                StringRequest request = new StringRequest(Request.Method.GET, "https://cramschoollogin.herokuapp.com/api/deletebill?id=" + contenttest._id, mOnDeleteSuccessListener, mOnErrorListener);
                                NetworkManager.getInstance(Billboard_teacherActivity.this).request(null, request);

                                }
                            }
                        )
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

                return false;
            }
        });
        //  queryTodoList();
    }


    ///Del步驟1 - 傳指定刪除的名稱及UID上去，回傳要刪除的ID
    private Response.Listener<String> mOnDeleteSuccessListener = new Response.Listener<String>() {
        private String delID;
        @Override
        public void onResponse(String response) {
            Toast.makeText(Billboard_teacherActivity.this, "刪除", Toast.LENGTH_LONG).show();

            //結束頁面
            Billboard_teacherActivity.this.finish();
        }
    };
    private Response.ErrorListener mOnErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError err) {
            Toast.makeText(Billboard_teacherActivity.this, err.toString(), Toast.LENGTH_LONG).show();
        }
    };

    void fillData() {
    }


    private Response.Listener<String> mResponseListener = new Response.Listener<String>() {
        private String delID;
        @Override
        public void onResponse(String string) {
            Log.d("Response", string);
            // contentTest=new ArrayList<ContentTest>();
            try {

                JSONArray ary = new JSONArray(string);
                StringBuilder ids = new StringBuilder();
                StringBuilder dates = new StringBuilder();
                StringBuilder titles = new StringBuilder();
                StringBuilder contents = new StringBuilder();
                for (int i = 0; i < ary.length(); i++) {
                    JSONObject json = ary.getJSONObject(i);
                    String id = json.getString("_id");
                    String date = json.getString("date");
                    String title = json.getString("title");
                    String content = json.getString("content");
                   // delID = json.getString("_id");
                    ContentTest contentS = new ContentTest(date, title, content);
                    contentTest.add(contentS);
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

/*
    private Response.Listener<String> mOnDeleteSuccessListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
        }
    };
    private ErrorListener mOnErrorListener = new ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError err) {
            Toast.makeText(BillboardActivity.this, err.toString(), Toast.LENGTH_LONG).show();
        }
    };*/


    //回前頁
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        setResult(RESULT_OK, intent);
        finish();
    }
}