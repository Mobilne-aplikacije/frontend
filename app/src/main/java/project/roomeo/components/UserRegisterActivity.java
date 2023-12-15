package project.roomeo.components;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import project.roomeo.DTO.GuestDTO;
import project.roomeo.DTO.RequestHostDTO;
import project.roomeo.DTO.RequestGuestDTO;
import project.roomeo.R;
import project.roomeo.DTO.HostDTO;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegisterActivity extends AppCompatActivity {

    Button loginBtn;
    Button registerBtn;
    EditText firstNameTxt;
    EditText lastNameTxt;
    EditText emailTxt;
    EditText phoneTxt;
    EditText addressTxt;
    EditText passwordTxt;
    EditText confirmPasswordTxt;
    String firstName;
    String lastName;
    String email;
    String phone;
    String address;
    String password;
    String confirmPassword;
    String profilePicture = "Avatar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        registerBtn = findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.login_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    public void register(){

        findViewsById();
        getDataByViews();

        if(password != confirmPassword){
            Log.i("Registration error", "Confirm password does not match with a password");
        }

        RadioGroup radioGroup = findViewById(R.id.radiogroup);
        int checkedId = radioGroup.getCheckedRadioButtonId();

        if (checkedId == R.id.guest) {
            RequestGuestDTO request = new RequestGuestDTO(firstName, lastName, profilePicture, phone, email, address, password);
            putPassenger(request);
        } else if (checkedId == R.id.host) {
            RequestHostDTO request = new RequestHostDTO(firstName, lastName, profilePicture, email, phone, address, password);
            putDriver(request);
        }
    }

    public void putPassenger(RequestGuestDTO request){


        Log.i("ADS","AAAAAAAAAAAAAAa");
        Call<GuestDTO> call = ServiceUtils.guestService.createGuest(request);
        call.enqueue(new Callback<GuestDTO>() {
            @Override
            public void onResponse(Call<GuestDTO> call, Response<GuestDTO> response) {
                if(!response.isSuccessful()) return;
                Log.d("Success" ,"Successfully registered guest");
                Intent intent = new Intent(UserRegisterActivity.this, UserLoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<GuestDTO> call, Throwable t) {
                Log.d("FAIL", t.getMessage());
            }
        });
    }

    public void putDriver(RequestHostDTO request){

        Log.i("ADS","AAAAAAAAa");
        Call<HostDTO> call = ServiceUtils.hostService.createNewHost(request);
        call.enqueue(new Callback<HostDTO>() {
            @Override
            public void onResponse(Call<HostDTO> call, Response<HostDTO> response) {
                if(!response.isSuccessful()) return;
                Log.d("Success" ,"Successfully registered host");
                Intent intent = new Intent(UserRegisterActivity.this, UserLoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<HostDTO> call, Throwable t) {
                Log.d("FAIL", t.getMessage());
            }
        });
    }

    public void findViewsById(){
        firstNameTxt = findViewById(R.id.registrationFirstName);
        lastNameTxt = findViewById(R.id.registrationLastName);
        emailTxt = findViewById(R.id.registrationEmail);
        phoneTxt = findViewById(R.id.registrationPhone);
        addressTxt = findViewById(R.id.registrationAddress);
        passwordTxt = findViewById(R.id.registrationPassword);
        confirmPasswordTxt = findViewById(R.id.registrationConfirmPassword);
    }

    public void getDataByViews(){
        firstName = firstNameTxt.getText().toString();
        lastName = lastNameTxt.getText().toString();
        email = emailTxt.getText().toString();
        phone = phoneTxt.getText().toString();
        address = addressTxt.getText().toString();
        password = passwordTxt.getText().toString();
        confirmPassword = confirmPasswordTxt.getText().toString();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume()",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "onStart()", Toast.LENGTH_SHORT).show();
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