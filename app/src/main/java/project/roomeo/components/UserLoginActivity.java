package project.roomeo.components;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.widget.EditText;
import android.widget.Toast;
import com.auth0.android.jwt.JWT;

import androidx.annotation.NonNull;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import project.roomeo.DTO.RequestLoginDTO;
import project.roomeo.DTO.ResponseLoginDTO;
import project.roomeo.DTO.TokenDTO;
import project.roomeo.DTO.UserDTO;
import project.roomeo.components.admin.AdminMainActivity;
import project.roomeo.components.guest.GuestMainActivity;
import project.roomeo.components.host.HostMainActivity;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import project.roomeo.R;

public class UserLoginActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private EditText email, password;
    private Button forgotBtn;
    private TextView signupBtn, loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        email = findViewById(R.id.emailLogIn);
        password = findViewById(R.id.passwordLogIn);
        loginBtn = findViewById(R.id.login_button);
        signupBtn = findViewById(R.id.signup_btn);
        forgotBtn = findViewById(R.id.forgot_button);

        SpannableString spannableString = new SpannableString("Don't have an account? Register now");

        // Make "Register now" bold and underlined
        spannableString.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), spannableString.length() - 12, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new android.text.style.UnderlineSpan(), spannableString.length() - 12, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(UserLoginActivity.this, UserRegisterActivity.class));
            }
        };
        spannableString.setSpan(clickableSpan, spannableString.length() - 12, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signupBtn.setText(spannableString);

        // Enable the TextView to handle link clicks
        signupBtn.setMovementMethod(LinkMovementMethod.getInstance());

//
//        forgotBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                createNewPopUpDialog();
//            }
//        });
//
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String getEmail= email.getText().toString();
//                String getPassword = password.getText().toString();
//                String getEmail = "marko.markovic@gmail.com";
//                String getPassword = "marko123";
//                String getEmail = "andrea.katzenberger@gmail.com";
//                String getPassword = "andrea123";
                String getEmail = "admin@gmail.com";
                String getPassword = "admin123";
                login(getEmail, getPassword);
            }
        });



        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLoginActivity.this, UserRegisterActivity.class);
                startActivity(intent);
            }
        });

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.login_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    public void login(String email, String password){
        RequestLoginDTO loginDTO = new RequestLoginDTO(email, password);
        Call<ResponseLoginDTO> call = ServiceUtils.userService.login(loginDTO);
        call.enqueue(new Callback<ResponseLoginDTO>() {

            @Override
            public void onResponse(@NonNull Call<ResponseLoginDTO> call, @NonNull Response<ResponseLoginDTO> response) {

                if(!response.isSuccessful()) return;
                if(response.code() == 204) {
                    Toast.makeText(UserLoginActivity.this, "Email not confirmed!", Toast.LENGTH_SHORT).show();
                    return;
                }

                ResponseLoginDTO loginResponse = response.body();
                JWT jwt = new JWT(loginResponse.getAccessToken());

                Long id = jwt.getClaim("id").asLong();
                String email = jwt.getClaim("sub").asString();
                String role = jwt.getClaim("role").asString();

                setToken(loginResponse);
                Log.e(role, role);
                if(role.equalsIgnoreCase("GUEST")){
                    setPreferences(id, email, role, loginResponse);
                    setTokenPreference(loginResponse.getAccessToken(), loginResponse.getRefreshToken());
                    startActivity(new Intent(UserLoginActivity.this, GuestMainActivity.class));
                }
                else if(role.equalsIgnoreCase("HOST")) {
                    setPreferences(id, email, role, loginResponse);
                    setTokenPreference(loginResponse.getAccessToken(), loginResponse.getRefreshToken());
                    startActivity(new Intent(UserLoginActivity.this, HostMainActivity.class));
                    }
                else if(role.equalsIgnoreCase("ADMIN")) {
                    setPreferences(id, email, role, loginResponse);
                    setTokenPreference(loginResponse.getAccessToken(), loginResponse.getRefreshToken());
                    startActivity(new Intent(UserLoginActivity.this, AdminMainActivity.class));
                }
            }

            @Override
            public void onFailure(Call<ResponseLoginDTO> call, Throwable t) {
                Log.d("Login Failed", t.getMessage());

            }
        });
    }

    private void setToken(ResponseLoginDTO loginResponse) {
        TokenDTO tokenDTO = TokenDTO.getInstance();
        tokenDTO.setAccessToken(loginResponse.getAccessToken());
        tokenDTO.setRefreshToken(loginResponse.getRefreshToken());
    }

    private void deleteTokenPreferences() {
        TokenDTO tokenDTO = TokenDTO.getInstance();
        tokenDTO.setAccessToken(null);
        tokenDTO.setRefreshToken(null);
        this.sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = this.sharedPreferences.edit();
        spEditor.clear().commit();
    }

    private void setTokenPreference(String token, String refreshToken) {
        this.sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = this.sharedPreferences.edit();
        spEditor.putString("pref_accessToken", token);
        spEditor.putString("pref_refreshToken", refreshToken);
    }

    private void setSharedPreferences(Long id, String email, String role){
        this.sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = this.sharedPreferences.edit();
        spEditor.putLong("pref_id", id);
        spEditor.putString("pref_email", email);
        spEditor.putString("pref_role", role);
        spEditor.apply();
    }

    private void setPreferences(Long id, String email, String role, ResponseLoginDTO loginResponse){
        setSharedPreferences(id, email, role);
        setTokenPreference(loginResponse.getAccessToken(), loginResponse.getRefreshToken());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "onStart()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume()",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause()",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "onStop()",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy()",Toast.LENGTH_SHORT).show();
    }
}