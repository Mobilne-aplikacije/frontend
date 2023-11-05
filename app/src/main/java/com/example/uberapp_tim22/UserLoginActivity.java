package com.example.uberapp_tim22;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.example.uberapp_tim22.DTO.HopInMessageDTO;
import com.example.uberapp_tim22.DTO.HopInMessageReturnedDTO;
import com.example.uberapp_tim22.DTO.RequestLoginDTO;
import com.example.uberapp_tim22.DTO.ResponseChatDTO;
import com.example.uberapp_tim22.DTO.ResponseLoginDTO;
import com.example.uberapp_tim22.DTO.TokenDTO;
import com.example.uberapp_tim22.DTO.UserDTO;
import com.example.uberapp_tim22.service.ServiceUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLoginActivity extends AppCompatActivity {



    private SharedPreferences sharedPreferences;
    private Intent intent;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private EditText email, password;
    private Button loginBtn, signupBtn, forgotBtn;

    private TextView popUpNotification;
    private EditText popUpEmail;
    private Button popUpCancelBtn, popUpSendBtn;

    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FAB Car");

        email = findViewById(R.id.emailLogIn);
        password = findViewById(R.id.passwordLogIn);
        loginBtn = findViewById(R.id.login_button);
        signupBtn = findViewById(R.id.signup_btn);
        forgotBtn = findViewById(R.id.forgot_button);

        forgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewPopUpDialog();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getEmail= email.getText().toString();
                String getPassword = password.getText().toString();
//                String getEmail = "marko.markovic@gmail.com";
//                String getPassword = "marko123";
//                String getEmail = "andrea.katzenberger@gmail.com";
//                String getPassword = "andrea123";
                login(getEmail, getPassword);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLoginActivity.this, PassengerRegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void login(String email, String password){
        RequestLoginDTO loginDTO = new RequestLoginDTO(email, password);
        Call<ResponseLoginDTO> call = ServiceUtils.userService.login(loginDTO);
        call.enqueue(new Callback<ResponseLoginDTO>() {

            @Override
            public void onResponse(@NonNull Call<ResponseLoginDTO> call, @NonNull Response<ResponseLoginDTO> response) {

                if(!response.isSuccessful()) return;
                if(response.code() == 204){
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
                if(role.equalsIgnoreCase("PASSENGER")){
                    setPreferences(id, email, role, loginResponse);
                    setTokenPreference(loginResponse.getAccessToken(), loginResponse.getRefreshToken());
//                    getMessages(2L, 5L);
                    startActivity(new Intent(UserLoginActivity.this, PassengerMainActivity.class));

                }
                else if(role.equalsIgnoreCase("DRIVER")) {
                    setPreferences(id, email, role, loginResponse);
                    setTokenPreference(loginResponse.getAccessToken(), loginResponse.getRefreshToken());
                    startActivity(new Intent(UserLoginActivity.this, DriverMainActivity.class));

//                    startActivity(new Intent(UserLoginActivity.this, DriverMainActivity.class));
//                    startActivity(new Intent(UserLoginActivity.this, WebSocketActivity.class));

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

    private void createNewPopUpDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View popUpView = getLayoutInflater().inflate(R.layout.fragment_forgot_password_pop_up, null);

        popUpEmail = (EditText) popUpView.findViewById(R.id.popUpEditText);
        popUpCancelBtn = (Button) popUpView.findViewById(R.id.popUpCancelBtn);
        popUpSendBtn = (Button) popUpView.findViewById(R.id.popUpSendBtn);
        popUpNotification = (TextView) popUpView.findViewById(R.id.popUpNotification);

        dialogBuilder.setView(popUpView);
        dialog = dialogBuilder.create();
        dialog.show();

        popUpCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        popUpSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail(popUpEmail.getText().toString());
            }
        });
    }

    public void sendEmail(String email){
        Call<UserDTO> call = ServiceUtils.userService.findByEmail(email);
        call.enqueue(new Callback<UserDTO>() {

            @Override
            public void onResponse(@NonNull Call<UserDTO> call, @NonNull Response<UserDTO> response) {
                if (!response.isSuccessful()) {
                    popUpNotification.setTextColor(Color.RED);
                    popUpNotification.setText("Email does not exist");
                    return;

                }
                if (response.code() == 204) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Log.d("Email sending failed", t.getMessage());
            }
        });
    }

    private void getMessages(Long myId, Long otherId) {
        Call<List<ResponseChatDTO>> call = ServiceUtils.chatService.getChatsOfUser(myId);
        call.enqueue(new Callback<List<ResponseChatDTO>>() {

            @Override
            public void onResponse(Call<List<ResponseChatDTO>> call, Response<List<ResponseChatDTO>> response){
                if (response.body() != null) {
                    List<ResponseChatDTO> responseChats = response.body();
                    Log.i("List size", String.valueOf(responseChats.size()));

                    ResponseChatDTO chat = new ResponseChatDTO();
                    for(ResponseChatDTO responseChat: responseChats){
                        if(otherId == responseChat.getOtherId()){
                            chat = responseChat;
                            Log.i("TRUE", "TRUE");
                        }
                    }

                    Log.i("CHAT", chat.getOtherName());

                    intent = new Intent(getApplicationContext(), PassengerMapActivity.class);
                    intent.putExtra("responseChat", chat);
                    intent.putExtra("myIdd", myId);
                    intent.putExtra("otherIdd", otherId);
                    intent.putExtra("rideIdd", 5L);
                    startActivity(intent);
                }
                else {
                    Log.d("MESS", "SENDING ERROR");
                }
            }

            @Override
            public void onFailure(Call<List<ResponseChatDTO>> call, Throwable t) {
                Log.d("EMAIL_REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }

    private void getMessagess(Long myId, Long otherId) {
        Call<List<ResponseChatDTO>> call = ServiceUtils.chatService.getChatsOfUser(myId);
        call.enqueue(new Callback<List<ResponseChatDTO>>() {

            @Override
            public void onResponse(Call<List<ResponseChatDTO>> call, Response<List<ResponseChatDTO>> response){
                if (response.body() != null) {
                    List<ResponseChatDTO> responseChats = response.body();
                    Log.i("List size", String.valueOf(responseChats.size()));

                    ResponseChatDTO chat = new ResponseChatDTO();
                    for(ResponseChatDTO responseChat: responseChats){
                        if(otherId == responseChat.getOtherId()){
                            chat = responseChat;
                            Log.i("TRUE", "TRUE");
                        }
                    }

                    Log.i("CHAT", chat.getOtherName());

                    intent = new Intent(getApplicationContext(), ChatActivity.class);
                    intent.putExtra("responseChat", chat);
                    intent.putExtra("myIdd", myId);
                    intent.putExtra("otherIdd", otherId);
                    intent.putExtra("rideIdd", 5L);
                    startActivity(intent);
                }
                else {
                    Log.d("MESS", "SENDING ERROR");
                }
            }

            @Override
            public void onFailure(Call<List<ResponseChatDTO>> call, Throwable t) {
                Log.d("EMAIL_REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
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