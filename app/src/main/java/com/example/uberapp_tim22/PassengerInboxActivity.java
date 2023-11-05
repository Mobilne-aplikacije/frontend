package com.example.uberapp_tim22;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.uberapp_tim22.DTO.ResponseChatDTO;
import com.example.uberapp_tim22.adapters.ChatBoxListAdapter;
import com.example.uberapp_tim22.fragments.InboxChatFragment;
import com.example.uberapp_tim22.service.ServiceUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerInboxActivity  extends AppCompatActivity implements ChatBoxListAdapter.ChatBoxItemClickListener{

    private RecyclerView chatBoxListRecyclerView;
    private ChatBoxListAdapter chatBoxListAdapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_inbox);

        Toolbar toolbar = findViewById(R.id.inboxToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FAB Car");

        chatBoxListRecyclerView = findViewById(R.id.listOfChats);
        chatBoxListAdapter = new ChatBoxListAdapter(this);;

        sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        Long myId = sharedPreferences.getLong("pref_id", 0);

        getChats(myId);
        chatBoxListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatBoxListRecyclerView.setAdapter(chatBoxListAdapter);
    }

    @Override
    public void onChatBoxItemClick(ResponseChatDTO chat) {
        InboxChatFragment fragment = new InboxChatFragment();

        Bundle args = new Bundle();
        args.putSerializable("responseChat", chat);
        args.putLong("myIdd", chat.getMyId());
        args.putLong("otherIdd", chat.getOtherId());
        fragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.chat, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menuOrder) {
            Intent intent = new Intent(this, PassengerMainActivity.class);
            startActivity(intent);
            return true;
        }
        if (itemId == R.id.menuAccount) {
            Intent intent = new Intent(this, PassengerAccountActivity.class);
            startActivity(intent);
            return true;
        }
        if (itemId == R.id.menuStatistic) {
            Intent intent = new Intent(this, PassengerReportsActivity.class);
            startActivity(intent);
            return true;
        }

        if (itemId == R.id.menuStatistic) {
            Intent intent = new Intent(this, PassengerReportsActivity.class);
            startActivity(intent);
            return true;
        }
        if (itemId == R.id.menuFav) {
            Intent intent = new Intent(this, PassengerFavoriteRides.class);
            startActivity(intent);
            return true;
        }
        if (itemId == R.id.menuHistory) {
            Intent intent = new Intent(this, PassangerRideHistory.class);
            startActivity(intent);
            return true;
        }
        if (itemId == R.id.menuLogOut) {
            deletePreferences();
            Intent intent = new Intent(this, UserLoginActivity.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void deletePreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.clear().commit();
    }

    private void getChats(Long myId) {
        Call<List<ResponseChatDTO>> call = ServiceUtils.chatService.getChatsOfUser(myId);
        call.enqueue(new Callback<List<ResponseChatDTO>>() {
            @Override
            public void onResponse(Call<List<ResponseChatDTO>> call, Response<List<ResponseChatDTO>> response){


                if (response.isSuccessful()) {
                    List<ResponseChatDTO> responseChats = response.body();
                    chatBoxListAdapter.setCheckBoxlist(responseChats);

                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<ResponseChatDTO>> call, Throwable t) {
                Toast.makeText(PassengerInboxActivity.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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