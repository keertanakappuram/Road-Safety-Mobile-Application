package com.example.roadtosafety; 
import androidx.core.app.ActivityCompat; 
import androidx.core.app.NotificationCompat; 
import androidx.fragment.app.FragmentActivity; 
import android.Manifest; 
import android.app.Notification; 
import android.app.NotificationChannel; 
import android.app.NotificationManager; 
import android.content.Context; 
import android.content.pm.PackageManager; 
import android.graphics.BitmapFactory; 
import android.graphics.Color; 
import android.os.Build; 
import android.os.Bundle; 
import android.os.Looper; 
import android.widget.Toast; 
import com.firebase.geofire.GeoFire; 
import com.firebase.geofire.GeoLocation; 
import com.firebase.geofire.GeoQuery; 
import com.firebase.geofire.GeoQueryEventListener; import com.google.android.gms.location.FusedLocationProviderClient; import com.google.android.gms.location.LocationCallback; import com.google.android.gms.location.LocationRequest; import com.google.android.gms.location.LocationResult; import com.google.android.gms.location.LocationServices; import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.GoogleMap; 
import com.google.android.gms.maps.OnMapReadyCallback; import com.google.android.gms.maps.SupportMapFragment; import com.google.android.gms.maps.model.CircleOptions; import com.google.android.gms.maps.model.LatLng; import com.google.android.gms.maps.model.Marker; import com.google.android.gms.maps.model.MarkerOptions; import com.example.roadtosafety.databinding.ActivityMapsBinding; import com.google.firebase.database.DatabaseError; import com.google.firebase.database.DatabaseReference; import com.google.firebase.database.FirebaseDatabase; import com.karumi.dexter.Dexter; 
import com.karumi.dexter.PermissionToken; 
import com.karumi.dexter.listener.PermissionDeniedResponse; import com.karumi.dexter.listener.PermissionGrantedResponse; import com.karumi.dexter.listener.PermissionRequest; import com.karumi.dexter.listener.single.PermissionListener; 
import java.util.ArrayList; 
import java.util.List; 
import java.util.Random; 
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GeoQueryEventListener { 
private GoogleMap mMap; 
//private ActivityMapsBinding binding; 
private LocationRequest locationRequest; 
private LocationCallback locationCallback; 
private FusedLocationProviderClient fusedLocationProviderClient; private Marker currentUser;

private DatabaseReference myLocationRef; 
private GeoFire geoFire; 
private List<LatLng> dangerousArea; 
@Override 
protected void onCreate(Bundle savedInstanceState) { 
super.onCreate(savedInstanceState); 
setContentView(R.layout.activity_maps); 
Dexter.withActivity(this) 
.withPermission(Manifest.permission.ACCESS_FINE_LOCATION) .withListener(new PermissionListener() { 
@Override 
public void onPermissionGranted(PermissionGrantedResponse
response) { 
// Obtain the SupportMapFragment and get notified whenthemapis ready to be used. 
buildLocationRequest(); 
buildLocationCallback(); 
fusedLocationProviderClient = 
LocationServices.getFusedLocationProviderClient(MapsActivity.this); 
SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager() 
.findFragmentById(R.id.map); 
mapFragment.getMapAsync(MapsActivity.this); 
initArea(); 
settingGeoFire(); 
} 
@Override

public void onPermissionDenied(PermissionDeniedResponse
response) { 
Toast.makeText(MapsActivity.this, "You must enable permission", Toast.LENGTH_SHORT).show(); 
} 
@Override 
public void 
onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) { 
} 
}).check(); 
//binding = ActivityMapsBinding.inflate(getLayoutInflater()); //setContentView(binding.getRoot()); 
} 
private void initArea() { 
dangerousArea=new ArrayList<>(); 
dangerousArea.add(new LatLng(19.0468,72.8934)); 
dangerousArea.add(new LatLng(19.0614,72.8984)); 
//dangerousArea.add(new LatLng(19.0507,72.8935)); 
} 
private void settingGeoFire() { 
myLocationRef= 
FirebaseDatabase.getInstance().getReference("MyLocation"); geoFire=new GeoFire(myLocationRef);

} 
private void buildLocationCallback() { 
locationCallback = new LocationCallback() { 
@Override 
public void onLocationResult(LocationResult locationResult) { if (mMap != null) { 
geoFire.setLocation("You", new 
GeoLocation(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude()), new
GeoFire.CompletionListener() { 
@Override 
public void onComplete(String key, DatabaseError error) { if (currentUser != null) currentUser.remove(); 
currentUser = mMap.addMarker(new MarkerOptions() .position(new 
LatLng(locationResult.getLastLocation().getLatitude(), 
locationResult.getLastLocation().getLongitude())) 
.title("You")); 
//After add marker , move camera 
mMap.animateCamera(CameraUpdateFactory 
.newLatLngZoom(currentUser.getPosition(), 12.0f)); 
} 
}); 
} 
} 
}; 
}

private void buildLocationRequest() { 
locationRequest = new LocationRequest(); 
locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); locationRequest.setInterval(10000); 
locationRequest.setFastestInterval(5000); 
locationRequest.setSmallestDisplacement(10f); 
} 
/** 
* Manipulates the map once available. 
* This callback is triggered when the map is ready to be used. * This is where we can add markers or lines, add listeners or move thecamera. In this case, 
* we just add a marker near Sydney, Australia. 
* If Google Play services is not installed on the device, the user will beprompted to install 
* it inside the SupportMapFragment. This method will only be triggeredoncethe user has 
* installed Google Play services and returned to the app. */ 
@Override 
public void onMapReady(GoogleMap googleMap) { 
mMap = googleMap; 
mMap.getUiSettings().setZoomControlsEnabled(true); 
if (fusedLocationProviderClient != null) 
if (ActivityCompat.checkSelfPermission(this, 
Manifest.permission.ACCESS_FINE_LOCATION) !=

PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, 
Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { 
// TODO: Consider calling 
// ActivityCompat#requestPermissions 
// here to request the missing permissions, and then overriding// public void onRequestPermissionsResult(int requestCode, String[] permissions, 
// int[] grantResults) 
// to handle the case where the user grants the permission. Seethedocumentation 
// for ActivityCompat#requestPermissions for more details. return; 
} 
fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper()); 
//Add Circle for dangerous areas 
for(LatLng latLng:dangerousArea) 
{ 
mMap.addCircle(new CircleOptions().center(latLng) 
.radius(500) //500m radius 
.strokeColor(Color.BLUE) 
.fillColor(0x220000FF) //22 is transparent 
.strokeWidth(5.0f) 
); 
//Create GepQuery when user in dangerous location GeoQuery geoQuery=geoFire.queryAtLocation(new GeoLocation(latLng.latitude,latLng.longitude),0.5f); //500m geoQuery.addGeoQueryEventListener(MapsActivity.this);

} 
} 
@Override 
protected void onStop() { 
fusedLocationProviderClient.removeLocationUpdates(locationCallback); super.onStop(); 
} 
@Override 
public void onKeyEntered(String key, GeoLocation location) { sendNotification("You",String.format("%s entered a dangerous area",key)); } 
@Override 
public void onKeyExited(String key) { 
sendNotification("You",String.format("%s exited a dangerous area",key)); } 
@Override 
public void onKeyMoved(String key, GeoLocation location) { sendNotification("You",String.format("%s are moving within a dangerousarea",key)); 
} 
@Override 
public void onGeoQueryReady() { 
}
@Override 
public void onGeoQueryError(DatabaseError error) { 
Toast.makeText(this, ""+error.getMessage(), 
Toast.LENGTH_SHORT).show(); 
} 
private void sendNotification(String title, String content) { Toast.makeText(this,""+content, Toast.LENGTH_SHORT).show(); String NOTIFICATION_CHANNEL_ID = "you_multiple_location"; NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); 
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { 
NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,"My Notification", NotificationManager.IMPORTANCE_DEFAULT); 
//Config 
notificationChannel.setDescription("Channel description"); notificationChannel.enableLights(true); 
notificationChannel.setLightColor(Color.RED); 
notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000}); notificationChannel.enableVibration(true); 
notificationManager.createNotificationChannel(notificationChannel); } 
NotificationCompat.Builder builder=new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID); builder.setContentTitle(title)
.setContentText(content) 
.setAutoCancel(false) 
.setSmallIcon(R.mipmap.ic_launcher) 
.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher)); 
Notification notification=builder.build(); 
notificationManager.notify(new Random().nextInt(),notification);}}
