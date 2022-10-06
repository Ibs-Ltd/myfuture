package com.myfutureapp.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.lang.ref.SoftReference;

public class LocationHelper extends LocationCallback{
	private SoftReference<Context> context;
	private FusedLocationProviderClient mFusedLocationClient;
	private SoftReference<LocationCallback> callback;
	private Location siteLocation;
	private LocationRequest locationRequest;
	private double radius;

//	public CommonHelper() {
//	}

	public LocationHelper(Context context, LocationCallback callback, Location location, double radius) {
		this.context = new SoftReference<>(context);
		mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.context.get());
		this.callback = new SoftReference<>(callback);
		this.siteLocation = location;
		this.radius = radius;
	}

	public static boolean checkPermission(Context context, Activity activity, boolean fromApi) {
		int permissionAFL = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
		return (permissionAFL == PackageManager.PERMISSION_GRANTED);
	}

	public static boolean checkGPSStatus(Context context) {
		LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		try {
			int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
			if (mode != 3) {
				statusOfGPS = false;
			}

		} catch (Settings.SettingNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return statusOfGPS;
	}

	private LocationRequest setLocationRequest(int accuracy) {
		LocationRequest locationRequest = LocationRequest.create();
		locationRequest.setInterval(1000);
		locationRequest.setNumUpdates(1);
		locationRequest.setPriority(accuracy);
		locationRequest.setExpirationDuration(2 * 60 * 1000);
		return locationRequest;
	}


	public void removeLocationUpdate(){
		mFusedLocationClient.removeLocationUpdates(this);
	}

	public int getCurrentLocation(){
		return getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
	}

	private int getCurrentLocation(int priority){
		if (context.get() == null){
			return -3;
		}
		if (!checkGPSStatus(context.get())){
			return -1;
		}
		if (ActivityCompat.checkSelfPermission(context.get(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
			return -2;
		}

		locationRequest = setLocationRequest(priority);
		mFusedLocationClient.requestLocationUpdates(locationRequest,this,null);

		return 1;
	}

	@Override
	public void onLocationResult(LocationResult locationResult) {
		super.onLocationResult(locationResult);
		mFusedLocationClient.removeLocationUpdates(this);
		if (callback.get() != null){
			boolean fetchAgain = false;
			if (locationResult.getLastLocation() != null){
				if (siteLocation != null && siteLocation.distanceTo(locationResult.getLastLocation()) > radius && locationRequest.getPriority() == LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY){
					fetchAgain = true;
				}
			}else{
			 	if(locationRequest.getPriority() == LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY){
					fetchAgain = true;
				}
			}
			if (fetchAgain){
				if (getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY) != 1){
					callback.get().onLocationResult(locationResult);
				}
			}else{
				callback.get().onLocationResult(locationResult);
			}
		}
	}

	@Override
	public void onLocationAvailability(LocationAvailability locationAvailability) {
		super.onLocationAvailability(locationAvailability);

//		mFusedLocationClient.removeLocationUpdates(this);
//		if (callback.get() != null){
//			callback.get().onLocationAvailability(locationAvailability);
//		}
	}
}
