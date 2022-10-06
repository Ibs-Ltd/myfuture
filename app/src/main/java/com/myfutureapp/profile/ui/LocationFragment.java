package com.myfutureapp.profile.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.myfutureapp.R;
import com.myfutureapp.login.DataHolder;
import com.myfutureapp.profile.adapter.CityManuallyAdapter;
import com.myfutureapp.profile.model.CityListResponse;
import com.myfutureapp.profile.presenter.LocationFragmentPresenter;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.Helper;
import com.myfutureapp.util.LocationHelper;
import com.myfutureapp.util.OnItemClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.myfutureapp.util.AnimationSinglton.collapse;
import static com.myfutureapp.util.AnimationSinglton.expandView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationFragment extends Fragment implements LocationFragmentView, OnMapReadyCallback,
        GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener, LocationListener, OnItemClickEvent {

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private static HandleFragmentLoading handleFragmentLoading;
    RelativeLayout llForcityrecylerLocation;
    LocationFragment localcontext;
    boolean city_flag = false;
    Marker marker;
    Geocoder geocoder;
    String city = "", citySelected = "";
    String address = "", pincode = "", state = "";
    String region = "";
    Location location;
    Double Lat, Lng;
    double userLat = 0.0;
    double userLong = 0.0;
    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    List<CityListResponse.CityModel> updatedList = new ArrayList<>();
    //    private ProfileActivity myContext;
    MapFragment mapFrag;
    EditText searchET;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;
    private TextView cityManually;
    private LinearLayout manuallyLL;
    private RecyclerView cityListRV;
    private CityManuallyAdapter cityManuallyAdapter;
    private List<CityListResponse.CityModel> cityModelsResponse;
    private TextView citytv;
    private LinearLayout gpsLocation;
    private ProgressBar mapProgress;
    private View cityView;
    private boolean searchedCity = false;
    private boolean btnEnble = false;
    private GoogleMap googlemap = null;
    private View view;
    private ImageView serachClearIV;

    public LocationFragment() {
        // Required empty public constructor
    }


    public static LocationFragment newInstance(HandleFragmentLoading handleFragmentLoadings) {
        LocationFragment fragment = new LocationFragment();
        handleFragmentLoading = handleFragmentLoadings;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_location, container, false);
            searchET = (EditText) view.findViewById(R.id.et_select_cityLocation);

            inUi(view);

            return view;
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
//        myContext = (ProfileActivity) activity;
        super.onAttach(activity);
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void inUi(View view) {
        LocationFragmentPresenter locationFragmentPresenter = new LocationFragmentPresenter(getContext(), this);
        serachClearIV = view.findViewById(R.id.serachClearIV);
        cityManually = view.findViewById(R.id.cityManually);
        manuallyLL = view.findViewById(R.id.manuallyLL);
        LinearLayout mainLocationLL = view.findViewById(R.id.mainLocationLL);
        cityView = view.findViewById(R.id.cityView);
        gpsLocation = view.findViewById(R.id.gpsLocation);
        llForcityrecylerLocation = view.findViewById(R.id.ll_forcityrecylerLocation);
        TextView saveLocation = view.findViewById(R.id.saveLocation);
        mapProgress = view.findViewById(R.id.mapProgress);
        mapFrag = (MapFragment) ((Activity) getContext()).getFragmentManager().findFragmentById(R.id.map);
        localcontext = LocationFragment.this;
        String permission = Manifest.permission.ACCESS_COARSE_LOCATION;
        int res = getContext().checkCallingOrSelfPermission(permission);
        if (res != PackageManager.PERMISSION_GRANTED) {
            gpsLocation.setVisibility(View.VISIBLE);
        } else {
            if (checkGpsStatus()) {
                fetchLastLocation();
            } else {
                gpsLocation.setVisibility(View.VISIBLE);
            }
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        serachClearIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchET.setText("");
            }
        });
        gpsLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION, AppConstants.PERMISSION_REQ_ID_ACCESS_COARSE_LOCATION)) {
                    if (checkGpsStatus()) {
                        fetchLastLocation();
                    } else {
                        Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent1);
                    }
                }
            }
        });

        searchET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        saveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnEnble) {
                    /*if (city != null && city.length() > 0) {
                        DataHolder.getInstance().getUserDataresponse.address = address;
                        DataHolder.getInstance().getUserDataresponse.pincode = pincode;
                        DataHolder.getInstance().getUserDataresponse.state = state;
                        loadNextFragment(city);
                    } else {*/
                    DataHolder.getInstance().getUserDataresponse.address = null;
                    DataHolder.getInstance().getUserDataresponse.pincode = null;
                    DataHolder.getInstance().getUserDataresponse.state = null;
                    loadNextFragment(citySelected);

//                    }
                }
            }
        });
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    searchedCity = true;
                    updatedList.clear();
                    for (int i = 0; i < cityModelsResponse.size(); i++) {
                        if (cityModelsResponse.get(i).name.toLowerCase().contains(s.toString().toLowerCase())) {
                            updatedList.add(cityModelsResponse.get(i));
                        }
                    }
                    cityManuallyAdapter.clearAll();
                    cityManuallyAdapter.notifyItem(updatedList);
                    serachClearIV.setVisibility(View.VISIBLE);
                } else {
                    updatedList.clear();
                    searchedCity = false;
                    cityManuallyAdapter.clearAll();
                    cityManuallyAdapter.notifyItem(cityModelsResponse);
                    serachClearIV.setVisibility(View.GONE);
                }
            }
        });

        cityManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // llForcityrecylerLocation.setVisibility(View.VISIBLE);
                if (city_flag == false) {
                    expandView(llForcityrecylerLocation);
                    // ivCountryDrpDwnIcon.animate().rotation(180).setDuration(400);
                    city_flag = true;

                } else {
                    collapse(llForcityrecylerLocation);
                    //  ivCountryDrpDwnIcon.animate().rotation(0).setDuration(400);
                    city_flag = false;


                }
                citytv.setVisibility(View.GONE);
                cityView.setVisibility(View.GONE);
            }
        });


        cityListRV = (RecyclerView) view.findViewById(R.id.cityListRV);
        citytv = (TextView) view.findViewById(R.id.citytv);
        cityListRV.setHasFixedSize(true);
        LinearLayoutManager searchLinearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        cityListRV.setLayoutManager(searchLinearLayoutManager);
        cityListRV.setItemAnimator(new DefaultItemAnimator());
        if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.cityData != null && DataHolder.getInstance().getUserProfileDataresponse.data.cityData.name != null) {
            cityManually.setText(DataHolder.getInstance().getUserProfileDataresponse.data.cityData.name);
            citySelected = DataHolder.getInstance().getUserProfileDataresponse.data.cityData.name;
            saveLocation.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_dark_black_btn));
            btnEnble = true;
        }

        cityManuallyAdapter = new CityManuallyAdapter(getContext(), new ArrayList<>(), new CityManuallyAdapter.CitySelected() {
            @Override
            public void citySelect(CityListResponse.CityModel cityModel) {
                if (Helper.isContainValue(cityModel.name)) {
                    cityManually.setText(cityModel.name);
                    manuallyLL.setVisibility(View.GONE);
                }
                citySelected = cityModel.name;
                btnEnble = true;
                saveLocation.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_dark_black_btn));


            }
        });
        mainLocationLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (city_flag == true) {
                    collapse(llForcityrecylerLocation);
                    //  ivCountryDrpDwnIcon.animate().rotation(0).setDuration(400);
                    city_flag = false;
                    citytv.setVisibility(View.GONE);
                    cityView.setVisibility(View.GONE);
                }
            }
        });
        cityListRV.setAdapter(cityManuallyAdapter);
        cityManuallyAdapter.setOnItemClicked(this);
        locationFragmentPresenter.callCityListApi();
    }

    public boolean checkGpsStatus() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void loadNextFragment(String cityName) {
        if (cityName.length() != 0) {
            DataHolder.getInstance().getUserDataresponse.city = cityName;
            handleFragmentLoading.loadingQuizDetailFragment("jobLocationFragment");
        } else {
            showMessage("Please Select City");
        }
    }

    @Override
    public void setCityListResponse(CityListResponse cityListResponse) {
        if (cityListResponse != null && cityListResponse.success) {
            cityModelsResponse = cityListResponse.data;
            cityManuallyAdapter.notifyItem(cityListResponse.data);
        }
    }

    @Override
    public void showLoader() {

    }

    @Override
    public void hideLoader() {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case AppConstants.PERMISSION_REQ_ID_ACCESS_COARSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION, AppConstants.PERMISSION_REQ_ID_ACCESS_FINE_LOCATION);
                } else {
                    showAlertDialogForDenyPermission("Without this permission the app is unable to invite friends, so please allow the permission");
                }
                break;
            }
            case AppConstants.PERMISSION_REQ_ID_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkGpsStatus()) {
                        fetchLastLocation();
                        gpsLocation.setVisibility(View.GONE);
                    } else {
                        Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent1);
                    }
//
//                    mapFrag.getMapAsync(localcontext);
//                    mapFrag.setRetainInstance(true);
//                    gpsLocation.setVisibility(View.GONE);
                } else {
                    showAlertDialogForDenyPermission("Without this permission the app is unable to invite friends, so please allow the permission");
                }
                break;
            }
        }
    }

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{permission}, requestCode);
            return false;
        }
        return true;
    }

    private void showAlertDialogForDenyPermission(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = dialog.create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Permission Denied");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.orange));
    }


    @Override
    public void onCameraIdle() {

    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                checkwhethergpsenableornot();
                // Helper.setToast(MapActivity.this, "no network provider is enabled");
            } else {
                googlemap = map;
                /*if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    Log.d("GPS Enabled", "GPS Enabled");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            userLat = location.getLatitude();
                            userLong = location.getLongitude();
                        }
                    }
                } else if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.e("NetworkEnabled", "NetworkEnabled");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            userLat = location.getLatitude();
                            userLong = location.getLongitude();
                        }
                    }
                } else {
                    showMessage("no network provider is enabled");
                }

*/
                geocoder = new Geocoder(getContext(), Locale.getDefault());


                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(userLat,
                        userLong));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

                googlemap.moveCamera(center);
                googlemap.animateCamera(zoom);
//R.string.rc
                addMarker(googlemap, userLat, userLong,
                        " R.string.validate",
                        "R.string.validate");
                getAddress(userLat, userLong);

                googlemap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                    @Override
                    public void onCameraMove() {
                        marker.remove();
                        LatLng midLatLng = googlemap.getCameraPosition().target;
                        marker = googlemap.addMarker(new MarkerOptions().title("I am here ").
                                position(midLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pin_up)));
                    }
                });

                googlemap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        marker.remove();
                        LatLng position = marker.getPosition();
                        marker = googlemap.addMarker(new MarkerOptions().title("I am here ").
                                position(position).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pin_up)));
                        Log.e("LATLONG", "LAT:" + position.latitude + "Long: " + position.longitude);
                        getAddress(position.latitude, position.longitude);

                    }
                });
                mapProgress.setVisibility(View.GONE);

            }
        }

    }

    public void checkwhethergpsenableornot() {
        final int REQUEST_CHECK_SETTINGS = 199;
        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!gps_enabled) {


            //   Log.e("Google client","call");
            GoogleApiClient googleApiClient = new GoogleApiClient.Builder(getContext())
                    .addApi(LocationServices.API).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(10000 / 2);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            Log.i("", "All location settings are satisfied.");
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.i("", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the result
                                // in onActivityResult().
                                status.startResolutionForResult((Activity) getContext(), REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                Log.i("", "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Log.i("", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                            break;
                    }
                }
            });


        }
    }

    public void getAddress(double userLat, double userLong) {
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(userLat, userLong, 1);
            address = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getLocality();
            region = addresses.get(0).getSubLocality();
            pincode = addresses.get(0).getPostalCode();
            state = addresses.get(0).getAdminArea();
            Log.e("ADdress", address + "," + city);

            citytv.setText(city + " " + state + " " + pincode + " ");
            address = city + " " + state + " " + pincode + " ";
            citytv.setVisibility(View.VISIBLE);
            cityView.setVisibility(View.VISIBLE);
            manuallyLL.setVisibility(View.GONE);
            cityManually.setText("Select City Manually");

            Lat = userLat;
            Lng = userLong;
        } catch (Exception e) {

        }
    }

    private void fetchLastLocation() {
        gpsLocation.setVisibility(View.GONE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AppConstants.PERMISSION_REQ_ID_ACCESS_FINE_LOCATION);

            return;
        }
        mapProgress.setVisibility(View.VISIBLE);
        int result = new LocationHelper(getContext(), new LocationCallback() {


            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                userLat = locationResult.getLastLocation().getLatitude();
                userLong = locationResult.getLastLocation().getLongitude();
                mapFrag.getMapAsync(localcontext);
                mapFrag.setRetainInstance(true);

            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
                if (!locationAvailability.isLocationAvailable()) {

                }
            }
        }, null, 0).getCurrentLocation();
      /*  Task<Location> task = fusedLocationProviderClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                     }

            }
        });*/
    }

    /*private void initLocationLibraries() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        mSettingsClient = LocationServices.getSettingsClient(getContext());

       LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }
   */
    private Marker addMarker(GoogleMap map, double lat, double lon,
                             String title, String snippet) {
        marker = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                .title(title)
                .snippet(snippet)
                .draggable(true));
        return marker;


    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("onLocationChanged", location.getLatitude() + "  c   " + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onItemClicked(View view, int position) {
        if (view.getId() == R.id.cityMainItemLL) {
            String strCity;
            if (searchedCity) {
                strCity = updatedList.get(position).name;
                searchET.setText("");
            } else {
                strCity = cityModelsResponse.get(position).name;
            }
            collapse(llForcityrecylerLocation);
            cityManually.setText(strCity);
            city_flag = false;

        }
    }
}