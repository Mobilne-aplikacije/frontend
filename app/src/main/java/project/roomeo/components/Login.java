package project.roomeo.components;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import project.roomeo.R;
import project.roomeo.components.admin.AdminMainActivity;
import project.roomeo.components.guest.GuestMainActivity;
import project.roomeo.components.host.HostMainActivity;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        TextView registerNowText = findViewById(R.id.registerNowText);
        SpannableString spannableString = new SpannableString("Don't have an account? Register now");

        // Make "Register now" bold and underlined
        spannableString.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), spannableString.length() - 12, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new android.text.style.UnderlineSpan(), spannableString.length() - 12, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(Login.this, Registration.class));
            }
        };
        spannableString.setSpan(clickableSpan, spannableString.length() - 12, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        registerNowText.setText(spannableString);

        // Enable the TextView to handle link clicks
        registerNowText.setMovementMethod(LinkMovementMethod.getInstance());


        TextView submitButton = findViewById(R.id.buttonSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userRole = "Guest";

                switch (userRole) {
                    case "Guest":
                        startActivity(new Intent(Login.this, GuestMainActivity.class));
                        break;
                    case "Admin":
                        startActivity(new Intent(Login.this, AdminMainActivity.class));
                        break;
                    case "Host":
                        startActivity(new Intent(Login.this, HostMainActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
