package com.dani.digitalbusride;

public class Trackpoint {
	 
    int idtrack;
    long istante;
    double latitude;
    double longitude;
    double speed;
    String linea;
 
    // constructors
    public Trackpoint() {
    }
 
    public Trackpoint(int idtrack, long istante, double latitude, double longitude, double speed, String linea) {
        this.idtrack = idtrack;
        this.istante = istante;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.linea = linea;
    }
 
    // setters
    public void setId(int id) {
        this.idtrack = id;
    }
 
    public void setIstante(long istante) {
        this.istante = istante;
    }
 
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
     
    public void setLongitude(double longitude){
        this.longitude = longitude;
    }
    
    public void setSpeed(double speed){
        this.speed = speed;
    }
    
    public void setLinea(String linea){
        this.linea = linea;
    }
 
    // getters
    public int getIdtrack() {
        return this.idtrack;
    }
 
    public long getIstante() {
    	return this.istante;
    }
 
    public double getLatitude() {
    	return this.latitude;
    }
     
    public double getLongitude(){
    	return this.longitude;
    }
    
    public double getSpeed(){
    	return this.speed;
    }
    
    public String getLinea(){
    	return this.linea;
    }
}
