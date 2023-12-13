package com.example.roadtosafety; 
import android.content.Intent; 
import android.os.Bundle; 
import android.view.View; 
import android.widget.Button; 
import androidx.appcompat.app.AppCompatActivity; 
import com.example.roadtosafety.LoginActivity; 
import com.google.firebase.auth.FirebaseAuth; 
import com.google.firebase.auth.FirebaseUser; 
public class MainActivity extends AppCompatActivity { public Button button; 
Button btnLogOut; 
FirebaseAuth mAuth; 
@Override 
protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); 
setContentView(R.layout.activity_main); 
button = (Button) findViewById(R.id.startjrny); 
button.setOnClickListener(new View.OnClickListener() { @Override 
public void onClick(View view) { 
Intent intent = new Intent(MainActivity.this, MapsActivity.class); startActivity(intent); 
}
}); 
btnLogOut = findViewById(R.id.btnLogout); 
mAuth = FirebaseAuth.getInstance(); 
btnLogOut.setOnClickListener(view ->{ mAuth.signOut(); 
startActivity(new Intent(MainActivity.this, LoginActivity.class)); }); 
} 
@Override 
protected void onStart() { 
super.onStart(); 
FirebaseUser user = mAuth.getCurrentUser(); 
if (user == null){ 
startActivity(new Intent(MainActivity.this, LoginActivity.class)); } 
} 
}
