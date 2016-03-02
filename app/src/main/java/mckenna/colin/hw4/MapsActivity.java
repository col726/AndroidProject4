package mckenna.colin.hw4;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.


    private BitmapDescriptor redUfo;
    private ArrayList<Marker> ufoMarkers;
    private LatLng savedLocation;
    private LatLngBounds bounds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ufoMarkers = new ArrayList<>();
        setUpMapIfNeeded();
    }

    @Override
    protected void onStop() {
        Log.d("Main", "onStop");
        try {
            remoteUFOService.remove(reporter);
        } catch (RemoteException e) {
            Log.e("MainActivity4", "addReporter", e);
        }
        unbindService(serviceConnection);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    protected void onStart() {
        Log.d("Main", "onStart");

        // pass in the application id and fully-qualified service implementation class name
        Intent intent = new Intent();
        intent.setClassName("mckenna.colin.hw4", "mckenna.colin.hw4.UFOServiceImpl");
        if (!bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE))
            Toast.makeText(getBaseContext(), "Could not bind to service", Toast.LENGTH_LONG).show();

        super.onStart();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void updateSavedLocation(List<UFOPosition> pos) {

        for (UFOPosition u: pos)
        {
            savedLocation = new LatLng(u.getLat(), u.getLon());
            //add if doesnt exist

            if(u.getShipNum() >= ufoMarkers.size())
                ufoMarkers.add(u.getShipNum(), mMap.addMarker(new MarkerOptions()
                        .position(savedLocation)
                        .anchor(0.5f, 0.5f)
                        .icon(redUfo)));
            else {
                //draw polyline between old latlong and new
                //ufoMarkers.get(u.getShipNum()).setVisible(false);
                LatLng old  = ufoMarkers.get(u.getShipNum()).getPosition();

                mMap.addPolyline(new PolylineOptions()
                        .add(old, savedLocation)
                        .width(5)
                        .color(Color.RED));
                //update position after drawling polyline
                ufoMarkers.get(u.getShipNum()).setPosition(savedLocation);
            }
            //bounds.including(savedLocation);
            //
            //mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));

        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        redUfo = BitmapDescriptorFactory.fromResource(R.mipmap.red_ufo);
        //Thanks to http://stackoverflow.com/questions/13941253/google-maps-api-v2-latlngbounds-from-cameraposition for helping me set initial bounds
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(38.9073,-77.0365) , 14.0f));
        //bounds = new LatLngBounds(new LatLng(38.90,-77.03), new LatLng(38.98,-77.10));
    }

    private RemoteUFOService remoteUFOService;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override public void onServiceDisconnected(ComponentName name) {
            remoteUFOService = null;
        }
        @Override public void onServiceConnected(ComponentName name, IBinder service) {
            remoteUFOService = RemoteUFOService.Stub.asInterface(service);
            try {
                remoteUFOService.add(reporter);
            } catch (RemoteException e) {
                Log.e("MainActivity4", "addReporter", e);
            }
            Log.d("ServiceConnection", "onServiceConnected");
        }
    };

    private RemoteUFOServiceReporter reporter = new RemoteUFOServiceReporter.Stub() {
        @Override public void report(final List<UFOPosition> p) throws RemoteException {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("Ship Num:", Integer.toString(p.get(0).getShipNum()));
                    Log.i("Lat:", Double.toString(p.get(0).getLat()));
                    Log.i("Long:", Double.toString(p.get(0).getLon()));
                    updateSavedLocation(p);
                }
            });
        }};
}
