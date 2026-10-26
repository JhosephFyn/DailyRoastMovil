
package com.example.tallerandroidsena;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnCatalog = findViewById(R.id.btnCatalog);

        btnCatalog.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeActivity.this, CatalogActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(
                    HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
