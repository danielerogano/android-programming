ITSLabTracker
=========

![Splash Screen](screenshots/overview2.jpg)

For more info, see <http://160.97.53.150/home>

# Features

- Logs gps of your Android Device
- Saves gps coordinates to sqlite database on internal storage
- Sends gps coordinates to remote server


# Prerequisites
- Eclipse + ADT
- Android Platform 15

# Instructions

- Get a google maps API Key.
- Copy `api_key.xml` to `res/values`

	```
	<?xml version="1.0" encoding="utf-8"?>
	<resources>
    	<string name="google_maps_key">Your-GoogleMaps-API-Key</string>
	</resources>	
	```
- Build	and Deploy
