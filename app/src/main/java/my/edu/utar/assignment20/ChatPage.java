package my.edu.utar.assignment20;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatPage extends AppCompatActivity {
    private String topics[] = {
            "Are you a dog or cat person?",
            "What would you do if money was not a problem?",
            "What will you do if you are bored?",
            "Are you a dog or cat person?",
            "What is your love language?",

            "How would you describe yourself in 3 words?",
            "What do you value most in a person?",
            "What is a deal breaker for you in a relationship?",
            "Where do you see yourself in 5 or 10 years?",
            "What attracts you to people?",

            "What is your favorite position?",
            "Why did the chicken crossed the road?",
            "Do you believe in God or aliens?",
            "What is the best food around your area?",
            "What does love mean to you?"
    };
    private int counter = 0;
    private RecyclerView recyclerView;
    CircleImageView personImage;
    private EditText tvInput;
    private ImageButton btnSend;
    private ImageButton btnBack;
    private ImageButton btnAdd;
    private Button btnAddCamera;
    private Button btnAddMedia;
    private Button btnAddTopic;
    private Button btnAddDelivery;
    private Button btnAddDate;
    private Button btnAddPlugIn;
    private LinearLayout btnContainer;
    private List<ChatMessage> chatMessageList;
    private ChatAdapter chatAdapter;
    FirebaseDatabase database;
    DatabaseReference reference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        recyclerView = findViewById(R.id.chatrecycle);
        tvInput = findViewById(R.id.tvInput);
        btnSend = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);
        btnAdd = findViewById(R.id.btnAdd);
        btnContainer = findViewById(R.id.btnContainer);
        btnAddCamera = findViewById(R.id.btnAddCamera);
        btnAddMedia = findViewById(R.id.btnAddMedia);
        btnAddTopic = findViewById(R.id.btnAddTopic);
        btnAddDelivery = findViewById(R.id.btnAddDelivery);
        btnAddDate = findViewById(R.id.btnAddDate);
        btnAddPlugIn = findViewById(R.id.btnAddPlugIn);
        personImage=findViewById(R.id.circleImageView);

        Intent intent=getIntent();
        String imageURL=intent.getStringExtra("imageURL");

        Picasso.get()
                .load(imageURL)
                .placeholder(R.drawable.liam) // Placeholder image while loading
                .error(R.drawable.ic_baseline_person_24) // Error image if loading fails
                .into(personImage);

        // Initialize the chat message list
        chatMessageList = new ArrayList<>();

        // Initialize the RecyclerView and its adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // Set this to true to display items from the end
        recyclerView.setLayoutManager(layoutManager);
        chatAdapter = new ChatAdapter(chatMessageList);
        recyclerView.setAdapter(chatAdapter);

        // Handle back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onBackPressed(); }
        });

        // Handle add button for additional buttons container
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;

                if (counter % 2 == 0)
                    btnContainer.setVisibility(View.GONE);

                if (counter % 2 == 1)
                    btnContainer.setVisibility(View.VISIBLE);
            }
        });

        // Additional button: camera button
        btnAddCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatPage.this, "Coming Soon!", Toast.LENGTH_SHORT).show();

                counter++;
                btnContainer.setVisibility(View.GONE);
            }
        });

        // Additional button: media library button
        btnAddMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatPage.this, "Coming Soon!", Toast.LENGTH_SHORT).show();

                counter++;
                btnContainer.setVisibility(View.GONE);
            }
        });

        // Additional button: topic suggestion button
        btnAddTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int value = random.nextInt(topics.length - 1);
                Toast.makeText(ChatPage.this, topics[value], Toast.LENGTH_LONG).show();

                counter++;
                btnContainer.setVisibility(View.GONE);
            }
        });

        // Additional button: delivery button
        btnAddDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatPage.this, "Coming Soon!", Toast.LENGTH_SHORT).show();

                counter++;
                btnContainer.setVisibility(View.GONE);
            }
        });

        // Additional button: date setup button
        btnAddDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatPage.this, "Coming Soon!", Toast.LENGTH_SHORT).show();

                counter++;
                btnContainer.setVisibility(View.GONE);
            }
        });

        // Additional button: entertainment plug-ins button
        btnAddPlugIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatPage.this, "Coming Soon!", Toast.LENGTH_SHORT).show();

                counter++;
                btnContainer.setVisibility(View.GONE);
            }
        });

        // Handle sending messages
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String messageText = tvInput.getText().toString().trim();
        if (!messageText.isEmpty()) {
            // Create a new ChatMessage object and add it to the list
            ChatMessage message = new ChatMessage(messageText, true); // Assuming this message is sent by the user
            chatMessageList.add(message);

            // Notify the adapter that the dataset has changed
            chatAdapter.notifyDataSetChanged();

            // Clear the input field
            tvInput.setText("");

            // Scroll to the last item in the RecyclerView
            recyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);

            // You can also send the message to the recipient or store it in your database here
        }
    }

}
