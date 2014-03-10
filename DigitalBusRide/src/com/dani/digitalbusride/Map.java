package com.dani.digitalbusride;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class Map extends Activity {

	Fermata fermata = new Fermata();
	String linea,nome, time_format;
	double latitude, longitude, lat, lon, speed;
	long time;
	
	DatabaseHelper db;
	List<Fermata> fermate;
	List<Trackpoint> punti;
	List<LatLng> polipoints;
	String[] values;
	
	 MapFragment myMapFragment;
	 GoogleMap myMap;
	 View mapView;
	 LatLng posfermata, punto;
	 
 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.map);
  
  FragmentManager myFragmentManager = getFragmentManager();
  MapFragment myMapFragment = 
    (MapFragment)myFragmentManager.findFragmentById(R.id.map);
	  myMap = myMapFragment.getMap();
	  myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
	  myMap.setMyLocationEnabled(true);
	  
	  if (true) {
		  Intent sender=getIntent();
	linea = sender.getExtras().getString("linea");
		  
	db = new DatabaseHelper(getApplicationContext());
    fermate = db.getAllFermateByLinea(linea);
    punti = db.getAllTrackpointsByLinea(linea);
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    
    PolylineOptions line = new PolylineOptions();
    line.width(10);
    line.color(Color.CYAN);
    
    Iterator<Fermata> it = fermate.iterator();
    while(it.hasNext())
    {
        Fermata f = it.next();
        latitude = f.getLatitude();
        longitude = f.getLongitude();
        time = f.getId();
        SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss");
        time_format = s.format(time);
        nome = f.getNome();
    	posfermata= new LatLng(latitude, longitude);
    	//builder.include(posfermata);
    	// Toast.makeText(this,"test "+latitude+" - "+longitude+" - "+nome,Toast.LENGTH_LONG).show();
    	myMap.addMarker(new MarkerOptions().position(posfermata).title(linea).snippet(time_format));
    	
    }
   
    Iterator<Trackpoint> pt = punti.iterator();
    while(pt.hasNext())
    {
        Trackpoint t = pt.next();
        lat = t.getLatitude();
        lon = t.getLongitude();
        punto = new LatLng(lat, lon);
        line.add(punto);
        builder.include(punto);
    }
   
    LatLngBounds bounds = builder.build();
    myMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 
            this.getResources().getDisplayMetrics().widthPixels, 
            this.getResources().getDisplayMetrics().heightPixels, 
            60));
    
    myMap.addPolyline(line);
    
   //  myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posfermata, 10));
	  }

     //   mapView = getFragmentManager().findFragmentById(R.id.map).getView();
 
 }
 

}