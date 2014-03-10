package com.dani.digitalbusride;

public class Fermata {
	
    long id;
    String nome;
    double latitude;
    double longitude;
    String linea;
 
    // constructors
    public Fermata() {
    }
 
    public Fermata(long id, String nome, double latitude, double longitude, String linea) {
        this.id = id;
        this.nome = nome;
        this.latitude = latitude;
        this.longitude = longitude;
        this.linea = linea;
    }
 
    // setters
    public void setId(long id) {
        this.id = id;
    }
 
    public void setNome(String nome) {
        this.nome = nome;
    }
 
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
     
    public void setLongitude(double longitude){
        this.longitude = longitude;
    }
    
    public void setLinea(String linea){
        this.linea = linea;
    }
 
    // getters
    public long getId() {
        return this.id;
    }
 
    public String getNome() {
    	return this.nome;
    }
 
    public double getLatitude() {
    	return this.latitude;
    }
     
    public double getLongitude(){
    	return this.longitude;
    }
    
    public String getLinea(){
    	return this.linea;
    }
    
    @Override
    public String toString() {
        return this.linea + " - " + this.nome + " [LAT: " + this.latitude + " - LON: "+ this.longitude + "]";
    }
}
