package com.dani.digitalbusride;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

public class Main extends Activity {
       
       // private Button digitizebutton, listFermate, listLinee;
       private ImageView digitizebutton, listFermate, listLinee;
       final Context context = this;
       String linea;
       List<String> linee;
       DatabaseHelper db;
       
       @Override
       protected void onCreate(Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
             setContentView(R.layout.main);
             digitizebutton = (ImageView) findViewById(R.id.newride);
             listFermate = (ImageView) findViewById(R.id.list);
             listLinee = (ImageView) findViewById(R.id.map);

             
             digitizebutton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
			startActivity(new Intent(Main.this,Start.class));

                 }
          });

             listFermate.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                	 
                	 db = new DatabaseHelper(getApplicationContext());
                     linee = db.getAllLinee();
                     db.closeDB();
                	 AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                             Main.this);
                     builderSingle.setIcon(R.drawable.ic_launcher);
                     builderSingle.setTitle("Seleziona una linea:-");
                     final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    		 Main.this,
                             android.R.layout.select_dialog_singlechoice);
                     Iterator<String> iterator = linee.iterator();
                 	while (iterator.hasNext()) {
                 		arrayAdapter.add(iterator.next());
                 	}
                    
                     builderSingle.setNegativeButton("cancel",
                             new DialogInterface.OnClickListener() {

                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {
                                     dialog.dismiss();
                                 }
                             });

                     builderSingle.setAdapter(arrayAdapter,
                             new DialogInterface.OnClickListener() {

                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {
                                     String strName = arrayAdapter.getItem(which);
                                     Intent intent = new Intent(Main.this, ListFermate.class);        
                             	 	intent.putExtra("linea", strName);
                             	 	startActivity(intent);       
                                 }
                             });
                     builderSingle.show();
                 }
          });
             
             listLinee.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                	 db = new DatabaseHelper(getApplicationContext());
                     linee = db.getAllLinee();
                     db.closeDB();
                	 AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                             Main.this);
                     builderSingle.setIcon(R.drawable.ic_launcher);
                     builderSingle.setTitle("Seleziona una linea");
                     final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    		 Main.this,
                             android.R.layout.select_dialog_singlechoice);
                     Iterator<String> iterator = linee.iterator();
                 	while (iterator.hasNext()) {
                 		arrayAdapter.add(iterator.next());
                 	}
                    
                     builderSingle.setNegativeButton("cancel",
                             new DialogInterface.OnClickListener() {

                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {
                                     dialog.dismiss();
                                 }
                             });

                     builderSingle.setAdapter(arrayAdapter,
                             new DialogInterface.OnClickListener() {

                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {
                                     String strName = arrayAdapter.getItem(which);
                                     Intent intent = new Intent(Main.this, Map.class);        
                             	 	intent.putExtra("linea", strName);
                             	 	startActivity(intent);       
                                 }
                             });
                     builderSingle.show();
                 }
          });
             
       }
}