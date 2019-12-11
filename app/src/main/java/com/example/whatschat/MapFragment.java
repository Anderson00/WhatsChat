package com.example.whatschat;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import com.example.whatschat.model.GpsLocation;
import com.example.whatschat.model.Usuario;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap map;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.map_layout_fragment, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        callPermissions();

        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.getUiSettings().setAllGesturesEnabled(false);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        fetchUsers();
    }

    private void fetchUsers(){
        FirebaseFirestore.getInstance().collection("gps")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        List<DocumentChange> docs = queryDocumentSnapshots.getDocumentChanges();
                        for(DocumentChange doc : docs){
                            if(doc.getType() == DocumentChange.Type.ADDED ) {
                                GpsLocation gps = doc.getDocument().toObject(GpsLocation.class);
                                if(gps.getUuid() != FirebaseAuth.getInstance().getUid()) {

                                    LayoutInflater inflater = (LayoutInflater) getActivity()
                                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View v = inflater.inflate(R.layout.custom_marker_layout, null);

                                    LatLng coord = new LatLng(gps.getLatitude(), gps.getLongitude());

                                    CircleImageView imgView = v.findViewById(R.id.img);
                                    Picasso.get().load(gps.getProfileImgURL())
                                            .into(imgView, new Callback() {
                                                @Override
                                                public void onSuccess() {
                                                    map.addMarker(new MarkerOptions()
                                                            .position(coord)
                                                            .title(gps.getName())
                                                            .icon(BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(v))));
                                                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 15.0f));
                                                }

                                                @Override
                                                public void onError(Exception e) {

                                                }
                                            });
                                }
                            }
                        }
                    }
                });
    }

    public void requestLocationUpdates(){
        FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(getContext());
        LocationRequest request = new LocationRequest();

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(5000);
        request.setFastestInterval(2000);

        fusedLocationProviderClient.requestLocationUpdates(request, new LocationCallback(){

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // Toast.makeText(getContext(), "lat: "+ locationResult.getLastLocation().getLatitude(), Toast.LENGTH_SHORT).show();
                map.clear();

                Location loc = locationResult.getLastLocation();
                LatLng coord = new LatLng(loc.getLatitude(), loc.getLongitude());

                LayoutInflater inflater = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.custom_marker_layout, null);


                Usuario me = ApplicationSingleton.getInstance().getUsuario();
                if(me != null){

                    CircleImageView imgView = v.findViewById(R.id.img);
                    Picasso.get().load(me.getProfileIconURI())
                            .into(imgView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    map.addMarker(new MarkerOptions()
                                            .position(coord)
                                            .title("name")
                                            .icon(BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(v))));
                                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 15.0f));
                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });

                    GpsLocation gps = new GpsLocation();
                    gps.setLatitude(loc.getLatitude());
                    gps.setLongitude(loc.getLongitude());
                    gps.setName(me.getUsername());
                    gps.setUuid(me.getUuid());
                    gps.setProfileImgURL(me.getProfileIconURI());

                    FirebaseFirestore.getInstance().collection("/gps")
                            .document(me.getUuid())
                            .set(gps)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });
                }



            }
        }, Looper.getMainLooper());
    }

    public static Bitmap loadBitmapFromView(View v) {

        if (v.getMeasuredHeight() <= 0) {
            v.measure(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
            return b;
        }
        return null;
    }

    public static MarkerOptions createMarker(Context context, LatLng point) {
        MarkerOptions marker = new MarkerOptions();
        marker.position(point);
        int px = 50;
        View markerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        markerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        markerView.layout(0, 0, px, px);
        markerView.buildDrawingCache();
        CircleImageView imageView = markerView.findViewById(R.id.img);
        Bitmap mDotMarkerBitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mDotMarkerBitmap);
        markerView.draw(canvas);
        marker.icon(BitmapDescriptorFactory.fromBitmap(mDotMarkerBitmap));
        return marker;
    }

    public void callPermissions(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        String rationale = "Location permissions are required to get User Location";
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle("Info")
                .setSettingsDialogTitle("Warning");

        Permissions.check(getContext()/*context*/, permissions, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {
                requestLocationUpdates();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                callPermissions();
            }
        });
    }
}
