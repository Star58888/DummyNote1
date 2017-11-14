package com.star.dummynote1;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DummyNote extends AppCompatActivity implements AdapterView.OnItemClickListener {
    EditText editText1 ,editText2;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list);
        listView.setEmptyView(findViewById(R.id.empty));
        listView.setOnItemClickListener(this);
        setAdapter();
    }

//    private String[] note_array = {
//            "Activities",
//            "Services",
//            "Content Providers",
//            "Broadcast Receiver"
//    };
//
//    private DB mDbHelper;
//
//    private void setAdapter() {
//
//        mDbHelper = new DB(this);
//        mDbHelper.open();
//
//        ArrayAdapter adapter = new ArrayAdapter<String>(this ,
//        android.R.layout.simple_list_item_1,
//        note_array);
//        listView.setAdapter(adapter);
//    }



    private void setAdapter() {
        mDbHelper = new DB(this).open();

        fillData();
    }

    private DB mDbHelper;
    private Cursor mNotesCursor;
    private void fillData() {
        mNotesCursor = mDbHelper.getAll();
//        if(mNotesCursor != null)
//            mNotesCursor.moveToFirst();
        startManagingCursor(mNotesCursor);
        String[] from = new String[]{"note" , "created"};
        int[] to = new int[]{R.id.text1 ,R.id.text2 };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.simple_list_item_1, mNotesCursor, from, to ,SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0 , 1 , 0 ,"新增").setIcon(android.R.drawable.ic_menu_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0 , 2 , 0 ,"刪除").setIcon(android.R.drawable.ic_menu_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0 , 3 , 0 ,"修改").setIcon(android.R.drawable.ic_menu_edit).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

        int count;
        long rowId ;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case 1:
                count ++;
                mDbHelper.create(count + "Note");
                fillData();
                break;
            case 2:
                mDbHelper.delete(rowId);
                fillData();
                setAdapter();
                break;
            case 3:
                editText1 = new EditText(this);
                new AlertDialog.Builder(this).setTitle("修改項目名稱").setMessage("請輸入你要修改的資料").setView(editText1).setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                   mDbHelper.update(rowId,editText1.getText().toString());
                        fillData();
                    }
                }).show();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position , long id) {
        rowId = id;
        Toast.makeText(this , "第" + (position + 1)+ "項" ,Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AlertDialog : " + (position + 1));
        builder.setMessage("這裡可以用來顯示Alert訊息，要按[回上頁]鍵或是「確認」鈕才會關閉");
        builder.setPositiveButton("確認" , null);
        builder.show();

    }
}

