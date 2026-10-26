
package com.example.tallerandroidsena;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        if (etEmail == null || etPassword == null
                || btnLogin == null || btnRegister == null) {
            Toast.makeText(this,
                    "Error: revisa los IDs de activity_main.xml",
                    Toast.LENGTH_LONG).show();
            return;
        }

        try {
            FirebaseApp.initializeApp(this);
            auth = FirebaseAuth.getInstance();
        } catch (Exception e) {
            Toast.makeText(this,
                    "Error al iniciar Firebase: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            return;
        }

        btnRegister.setOnClickListener(v -> registerUser());
        btnLogin.setOnClickListener(v -> loginUser());
    }

    private boolean validateFields() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        if (email.isEmpty()
                || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Escribe un correo válido");
            etEmail.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            etPassword.setError("La contraseña debe tener mínimo 6 caracteres");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void registerUser() {
        if (!validateFields()) {
            return;
        }

        setButtonsEnabled(false);

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    setButtonsEnabled(true);

                    if (task.isSuccessful()) {
                        Toast.makeText(this,
                                "¡Cuenta creada correctamente!",
                                Toast.LENGTH_LONG).show();
                    } else {
                        showFirebaseError("Registro", task.getException());
                    }
                });
    }

    private void loginUser() {
        if (!validateFields()) {
            return;
        }

        setButtonsEnabled(false);

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    setButtonsEnabled(true);

                    if (task.isSuccessful()) {
                        Intent intent = new Intent(MainActivity.this,
                                HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        showFirebaseError("Inicio de sesión",
                                task.getException());
                    }
                });
    }

    private void setButtonsEnabled(boolean enabled) {
        btnLogin.setEnabled(enabled);
        btnRegister.setEnabled(enabled);
    }

    private void showFirebaseError(String action, Exception exception) {
        String message = exception == null
                ? "Error desconocido"
                : exception.getMessage();

        if (exception instanceof FirebaseAuthException) {
            message = ((FirebaseAuthException) exception).getErrorCode();
        }

        Toast.makeText(this,
                action + ": " + message,
                Toast.LENGTH_LONG).show();
    }
}
