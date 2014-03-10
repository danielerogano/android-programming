package com.dani.digitalbusride;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
 
public class Start extends Activity {

    private LocationManager lManager;
    private LocationListener lListener;
    private Location location;
    
    public boolean iftrack;
    public String provider;
    public Double utctime;
    public Long gpstime;
    public Double longitude;
    public Double latitude;
    public Double altitudine;
    public Double accuracy;
    public Double speed;
    ImageView imgred,imggreen, startbutton, stopbutton, pausebutton;
    TextView textcarico, textcaricovalue;
    
	private StringBuilder logrow = new StringBuilder(100);
	private String rideID;
	private int trackid;
	Integer entrate,uscite, carico;
	String linea;
    final Context context = this;
    DatabaseHelper db;
    Trackpoint tp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        db = new DatabaseHelper(getApplicationContext());
        
        iftrack = false;
        
        imgred=(ImageView) findViewById(R.id.imagered);
        imggreen=(ImageView) findViewById(R.id.imagegreen);
		startbutton = (ImageView) findViewById(R.id.imageStart);
		stopbutton = (ImageView) findViewById(R.id.imageStop);
		pausebutton = (ImageView) findViewById(R.id.imagePause);
		
		textcarico = (TextView) findViewById(R.id.textcarico);
	    textcaricovalue = (TextView) findViewById(R.id.textcaricovalore);
		
		final Button fermata = (Button) findViewById(R.id.button1);
		final Button ripresa = (Button) findViewById(R.id.button2);
		
        imgred.setVisibility(View.VISIBLE);
        imggreen.setVisibility(View.INVISIBLE);
        startbutton.setVisibility(View.VISIBLE);
        stopbutton.setVisibility(View.INVISIBLE);
        pausebutton.setVisibility(View.INVISIBLE);
        fermata.setVisibility(View.INVISIBLE);
        ripresa.setVisibility(View.INVISIBLE);
        
        File directory = new File(Environment.getExternalStorageDirectory().getPath()+"/DigitalBusRide/");
    	// Verifica presenza directory, altrimenti crea nuova.
    	if(!directory.exists()){
    		directory.mkdirs();
        }
    		
    	NumberPicker npEntrate = (NumberPicker) findViewById(R.id.NumberPicker1);
		npEntrate.setMaxValue(100);
		npEntrate.setMinValue(0);
		entrate = npEntrate.getValue();
		
		NumberPicker npUscite = (NumberPicker) findViewById(R.id.NumberPicker2);
		npUscite.setMaxValue(100);
		npUscite.setMinValue(0);
		uscite = npUscite.getValue();
		
//		Listener del bottone START
		
		startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //     startActivity(new Intent(FirstActivity.this,SecondActivity.class));
             	buttonLoadClick();
            	
            	AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Info nuova corsa"); //Set Alert dialog title here
                // alert.setMessage("Inserisci il numero di passeggeri:"); //Message here
                alert.setInverseBackgroundForced(true);
     
                LinearLayout lalert= new LinearLayout(context);
                final TextView nomelinea = new TextView(context);
                nomelinea.setText("Nome linea");
                final TextView numeropasseggeri = new TextView(context);
                numeropasseggeri.setText("Numero passeggeri a bordo");
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                final EditText input1 = new EditText(context);
                lalert.setOrientation(LinearLayout.VERTICAL);
                lalert.addView(nomelinea);
                lalert.addView(input1);
                lalert.addView(numeropasseggeri);
                lalert.addView(input);
                alert.setView(lalert);
     
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	linea = input1.getEditableText().toString();
                	
                	// Creazione fermata
                	Fermata fermata_capolinea = new Fermata();
                	Trackpoint startTp = new Trackpoint();
                	
                	// check if GPS enabled     

                    	if(location != null){
                    		imgred.setVisibility(View.INVISIBLE);
        					imggreen.setVisibility(View.VISIBLE);
        					
	                        
	                        trackid = (int) System.currentTimeMillis()/1000;

		                        latitude = location.getLatitude();
		                        longitude = location.getLongitude();
		                        gpstime = (long) location.getTime();
		                        accuracy = (double) location.getAccuracy();
		                        speed = (double) location.getSpeed();      
		                        
		                        fermata_capolinea.setId(System.currentTimeMillis()/1000);
		                    	fermata_capolinea.setNome("Capolinea");
		                    	fermata_capolinea.setLatitude(latitude);
		                    	fermata_capolinea.setLongitude(longitude);
		                    	fermata_capolinea.setLinea(linea);
		                    	
		                    	startTp.setId(trackid);
		                    	startTp.setIstante(System.currentTimeMillis()/1000);
		                    	startTp.setLatitude(latitude);
		                    	startTp.setLongitude(longitude);
		                    	startTp.setSpeed(speed);
		                    	startTp.setLinea(linea);
		                    	
		                    	// Aggiunta della fermata al DB
		                    	
		                    	long capolinea_id = db.createFermata(fermata_capolinea);
		                    	Toast.makeText(context,"DB id"+capolinea_id,Toast.LENGTH_LONG).show();
		                    	
		                    	carico = Integer.parseInt(input.getEditableText().toString());
		                        SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy-hh.mm.ss");
		                        rideID = s.format(new Date());
		                        logrow.append("time;latitude;longitude;accuracy;name;up;down;carico\n");
		                        logrow.append(System.currentTimeMillis()/1000+ ";" +latitude+ ";" +longitude+ ";" +accuracy+ ";Capolinea;0;0;" +carico+"\n");
		                        
		                        startbutton.setVisibility(View.INVISIBLE);
		                        stopbutton.setVisibility(View.VISIBLE);
		                    	fermata.setVisibility(View.VISIBLE);
		
		                        textcaricovalue.setText(input.getEditableText().toString());
		                        String mess = "Nuova corsa con "+carico+" passeggeri";
		                        Toast.makeText(context,mess,Toast.LENGTH_LONG).show();
		                        
		                        iftrack = true;
		                        
                    	}else{
                    		Toast.makeText(context,"GPS is fixing, please wait...",Toast.LENGTH_LONG).show();
                        }
                     
                } // End of onClick(DialogInterface dialog, int whichButton)
            }); //End of alert.setPositiveButton
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                      dialog.cancel();
                  }
            }); //End of alert.setNegativeButton
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
           /* Alert Dialog Code End*/ 
                
            //	Toast.makeText(getApplicationContext(), "Start", Toast.LENGTH_LONG).show();
            }
     });
		
//		Listener del bottone FERMATA
		
        fermata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           	 
           	 if (latitude!=null && longitude!=null) {
           		// Creazione fermata
                	Fermata fermatax = new Fermata();
                	fermatax.setId(System.currentTimeMillis()/1000);
                	fermatax.setNome("Fermata");
                	fermatax.setLatitude(latitude);
                	fermatax.setLongitude(longitude);
                	fermatax.setLinea(linea);
                	
                	// Aggiunta della fermata al DB
                	
                	long capolinea_id = db.createFermata(fermatax);
                	
           	 logrow.append(System.currentTimeMillis()/1000+ ";" +latitude+ ";" +longitude+ ";" +accuracy+ ";fermataX;");
                pausebutton.setVisibility(View.VISIBLE);
                startbutton.setVisibility(View.INVISIBLE);
                ripresa.setVisibility(View.VISIBLE);
                fermata.setVisibility(View.INVISIBLE);
           	 Toast.makeText(getApplicationContext(), "Bus Stop", Toast.LENGTH_SHORT).show();
           	 } else {
                	String mess2 = "GPS non disponibile, attendere e riprovare...";
                	Toast.makeText(context,mess2,Toast.LENGTH_LONG).show();
                }
            }
            
     });
		
//		Listener del bottone RIPRESA
        
        ripresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           	 entrate = ((NumberPicker) findViewById(R.id.NumberPicker1)).getValue();
           	 uscite = ((NumberPicker) findViewById(R.id.NumberPicker2)).getValue();
           	 
           	 carico = carico+entrate-uscite;
           	 textcaricovalue.setText(String.valueOf(carico));
           	 logrow.append(entrate+ ";" +uscite+ ";" +carico+ ";");
           	 logrow.append("\n");
           	pausebutton.setVisibility(View.INVISIBLE);
           	startbutton.setVisibility(View.VISIBLE);
           	ripresa.setVisibility(View.INVISIBLE);
            fermata.setVisibility(View.VISIBLE);
            
            	resetPickers();                 
           	 	Toast.makeText(getApplicationContext(), "Bus Stop Saved", Toast.LENGTH_SHORT).show();            
            }
            
     });
		
//		Listener del bottone STOP
    
        stopbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	AlertDialog.Builder alert2 = new AlertDialog.Builder(context);
                alert2.setTitle("Termina corsa"); //Set Alert dialog title here
                alert2.setMessage("Sei sicuro di voler terminare la corsa?"); //Message here
                alert2.setInverseBackgroundForced(true);
     
                alert2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	
                	stopUsingGPS();
                	logrow.append(System.currentTimeMillis()/1000+ ";" +latitude+ ";" +longitude+ ";" +accuracy+ ";Finecorsa;0;"+carico+";0;\n");
                   	logrow.append("Terminated at :"+ System.currentTimeMillis()/1000);
                   	
                   	iftrack = false;
                   	
                   	File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/DigitalBusRide/"+rideID+".txt");
            		if(!myFile.exists()){
            	    }
                   	writeToFile(logrow,myFile);
                   	stopbutton.setVisibility(View.INVISIBLE);
                   	startbutton.setVisibility(View.VISIBLE);
                   	ripresa.setVisibility(View.INVISIBLE);
                   	imgred.setVisibility(View.VISIBLE);
                    imggreen.setVisibility(View.INVISIBLE);
                   	Toast.makeText(getApplicationContext(), "Stop", Toast.LENGTH_LONG).show();
                   	carico = 0;
                   	textcaricovalue.setText(String.valueOf(carico));
                   	
                   	Fermata fermata_finale = new Fermata();
                    if (latitude!=null && longitude!=null) {
                    	fermata_finale.setId(System.currentTimeMillis()/1000);
                    	fermata_finale.setNome("Fermata_finale");
                    	fermata_finale.setLatitude(latitude);
                    	fermata_finale.setLongitude(longitude);
                    	fermata_finale.setLinea(linea);
                    	
                    	// Aggiunta della fermata al DB
                    	
                    	long capolinea_id = db.createFermata(fermata_finale);
                    }
                   	
                } // End of onClick(DialogInterface dialog, int whichButton)
            }); //End of alert.setPositiveButton
                alert2.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                      dialog.cancel();
                  }
            }); //End of alert.setNegativeButton
                AlertDialog alertDialog = alert2.create();
                alertDialog.show();
           /* Alert Dialog Code End*/
            }           
     });        
        
     // Close database connection
        db.closeDB();
    }

    private void writeToFile(StringBuilder logrow, File myFile) {
	    try {
	    	
	  //  	myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append(logrow);
			myOutWriter.close();
			fOut.close();
	    }
	    catch (IOException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
	    } 
	}

    private void getCurrentLocation() {
        
        lManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        lListener = new LocationListener() {
             
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub
            }
             
            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
            }
             
            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
            }
             
            @Override
            public void onLocationChanged(Location loc) {
                // TODO Auto-generated method stub
                location = loc;
                if  ( location != null ) {
                    
                   // lManager.removeUpdates(lListener);
            
                    provider   = location.getProvider();
                    gpstime = location.getTime();
                    longitude  = location.getLongitude();
                    latitude     =location.getLatitude();
                    altitudine  = location.getAltitude();
                    accuracy   = (double)location.getAccuracy();
                    speed   = (double)location.getSpeed();
					imgred.setVisibility(View.INVISIBLE);
					imggreen.setVisibility(View.VISIBLE);
					                	
                	if (iftrack){
                	tp = new Trackpoint(trackid, location.getTime(), location.getLatitude(), location.getLongitude(), (double) location.getSpeed(), linea);
                	long trackp = db.createTrackpoint(tp);
                	}
                	
                    } else {
                    	imgred.setVisibility(View.VISIBLE);
    					imggreen.setVisibility(View.INVISIBLE);
                }
            }
        };
        lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, lListener);

    }
    
    private void buttonLoadClick() {
        
        this.getCurrentLocation();
        Toast.makeText(context,"GPS is fixing, please wait...",Toast.LENGTH_SHORT).show(); 

    }
 

    public void stopUsingGPS(){
        	if(lManager != null){       		 
        	    lManager.removeUpdates(lListener);	 
        	    lManager = null;
        	    Toast.makeText(context,"GPS is now off",Toast.LENGTH_SHORT).show(); 
        	}     
    }

    @Override
    public void onBackPressed() {
    	AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Close Application"); //Set Alert dialog title here
        alert.setMessage("Sei sicuro di voler uscire?"); //Message here

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
        	stopUsingGPS();
        	finish();
        } // End of onClick(DialogInterface dialog, int whichButton)
    }); //End of alert.setPositiveButton
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            // Canceled.
              dialog.cancel();
          }
    }); //End of alert.setNegativeButton
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
   /* Alert Dialog Code End*/ 

    }
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
    	 menu.add("Main menu").setOnMenuItemClickListener(new OnMenuItemClickListener() {
             public boolean onMenuItemClick(MenuItem item) {
                     Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                     return true;
             }
     });;
     menu.add("Settings").setOnMenuItemClickListener(new OnMenuItemClickListener() {
             public boolean onMenuItemClick(MenuItem item) {
                     Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                     return true;
             }
     });;
     menu.add("Help").setOnMenuItemClickListener(new OnMenuItemClickListener() {
             public boolean onMenuItemClick(MenuItem item) {
                     Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                     return true;
             }
     });;
		return true;
	}
    public void resetPickers(){
    	NumberPicker npEntrate = (NumberPicker) findViewById(R.id.NumberPicker1);
		npEntrate.setValue(0);
		npEntrate.invalidate();
		
		NumberPicker npUscite = (NumberPicker) findViewById(R.id.NumberPicker2);
		npUscite.setValue(0);
		npUscite.invalidate();
		
    }
}