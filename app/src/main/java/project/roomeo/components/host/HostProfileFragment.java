package project.roomeo.components.host;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import project.roomeo.R;

public class HostProfileFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_host_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String deleteText = getString(R.string.delete_account);
        SpannableString spannableString = new SpannableString(deleteText);
        spannableString.setSpan(new UnderlineSpan(), 0, deleteText.length(), 0);
        TextView deleteTextView = view.findViewById(R.id.deleteText);
        deleteTextView.setText(spannableString);
    }
}