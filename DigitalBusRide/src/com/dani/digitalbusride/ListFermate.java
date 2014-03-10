package com.dani.digitalbusride;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListFermate extends ListActivity  {

	TextView content;
	DatabaseHelper db;
	List<Fermata> fermate;
	String linea;
	Cursor cur;
	String[] values;
	Button deletebutton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listfermate);
        
        deletebutton = (Button) findViewById(R.id.button);
        deletebutton.setVisibility(1);        
        deletebutton.setVisibility(View.GONE);
        
        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	AlertDialog.Builder alert = new AlertDialog.Builder(ListFermate.this);
            	alert.setTitle("Elimina linea"); //Set Alert dialog title here
            	alert.setMessage("Sei sicuro di voler eliminare la linea?"); //Message here
            	alert.setInverseBackgroundForced(true);
     
            	alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	db.deleteFermate(linea);
                	Toast.makeText(getApplicationContext(), "Tutte le fermate della linea "+linea+" sono state eliminate", Toast.LENGTH_SHORT).show();
                	ListFermate.this.finish();
                } // End of onClick(DialogInterface dialog, int whichButton)
            }); //End of alert.setPositiveButton
            	alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int whichButton) {
                      dialog.cancel();
                  }
            }); //End of alert.setNegativeButton
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            
            }
        });
        
        
        Intent sender=getIntent();
    	linea = sender.getExtras().getString("linea");

        if(linea!=null)
        {
            linea =sender.getExtras().getString("linea");
        } else {
        	linea="Circolare";
        }
        
        content = (TextView)findViewById(R.id.output);

        db = new DatabaseHelper(getApplicationContext());
        fermate = db.getAllFermateByLinea(linea);
        db.closeDB();
        
        ArrayAdapter<Fermata> adapter = new ArrayAdapter<Fermata>(this,
                android.R.layout.simple_list_item_1, fermate);

        // Assign adapter to List
        setListAdapter(adapter); 
   }

    
  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
        
        super.onListItemClick(l, v, position, id);
        Object o = this.getListAdapter().getItem(position);
        content.setText("Click: \n Linea: "+((Fermata) o).getLinea()+"\n Fermata: "+((Fermata) o).getNome()+"\n Orario: "+getDate(((Fermata) o).getId()*1000));
        deletebutton.setVisibility(View.VISIBLE);
           
  }
  private String getDate(long time) {
	    Calendar cal = Calendar.getInstance(Locale.ITALIAN);
	    cal.setTimeInMillis(time);
	    String date = DateFormat.format("dd-MM-yyyy - HH:mm:ss", cal).toString();
	    return date;
	}
}