package com.example.grocerylist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<String> shoppingList = null;
    ArrayAdapter<String> adapter = null;
    ListView lv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addElement();
            }
        });

        shoppingList = getArrayVal(getApplicationContext());
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, shoppingList);
        lv = findViewById(R.id.listView);
        lv.setAdapter(adapter);
        //this will make the list view display the elements inside of the arraylist.
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem  = ((TextView) view).getText().toString();
                if(selectedItem.trim().equals(shoppingList.get(position).trim())) {
                    //use position next in the intent to save the list
                    Intent intent1 = new Intent(MainActivity.this, ListOfItems.class);
                    intent1.putExtra("message", selectedItem);
                    startActivity(intent1);
                }
                else
                    Toast.makeText(getApplicationContext(), "Error going into list", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        //this if statements all point to diferent button clicks and what actions they do.
        if(id == R.id.action_map) //open map action
        {
            Intent intent2 = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent2);
            return true;
        }

        if(id == R.id.action_addEvent)
        {
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.samsung.android.calendar");
            if (launchIntent != null) {
                startActivity(launchIntent);//null pointer check in case package name was not found
            }
            else
            {
                launchIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.calendar");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Look at this dialog!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        }

        if(id == R.id.action_clear) //clear whole list
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Clear all lists");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    shoppingList.clear();
                    storeArrayVal(shoppingList, getApplicationContext());
                    lv.setAdapter(adapter);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static String preferredCase(String original) { //Sets first character to upper case and the rest to lower case.
        if(original.isEmpty())
        {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }

    public static void storeArrayVal(ArrayList inArrayList, Context context) {
        Set WhatToWrite = new HashSet(inArrayList);
        SharedPreferences WordSearchPutPrefs = context.getSharedPreferences("dbArrayValues", Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = WordSearchPutPrefs.edit();
        prefEditor.putStringSet("listArray", WhatToWrite);
        prefEditor.commit();
    }

    public static ArrayList getArrayVal(Context context) {
        SharedPreferences WordSearchPutPrefs = context.getSharedPreferences("dbArrayValues", Activity.MODE_PRIVATE);
        Set tempSet = new HashSet();
        tempSet = WordSearchPutPrefs.getStringSet("listArray", tempSet);
        return new ArrayList<String>(tempSet);
    }

    public void addElement() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New List");
        EditText input = new EditText(this);
        builder.setView(input);

        final EditText finalInput = input;
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shoppingList.add(preferredCase(finalInput.getText().toString()));
                storeArrayVal(shoppingList, getApplicationContext());
                lv.setAdapter(adapter);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
