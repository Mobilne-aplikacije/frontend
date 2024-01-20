package project.roomeo.components;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import project.roomeo.R;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        TextView logInText = findViewById(R.id.logInText);
        SpannableString spannableString = new SpannableString("Already have an account? Log in now");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(Registration.this, Login.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setFakeBoldText(true);
            }
        };
        spannableString.setSpan(clickableSpan, spannableString.length() - 11, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        logInText.setText(spannableString);
        logInText.setMovementMethod(LinkMovementMethod.getInstance());

    }


}
