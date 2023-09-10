package my.edu.utar.assignment20;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FloatingActionButton updateProfileButton;


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    CircleImageView profilePic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    FirebaseAuth auth;
    Button buttonLogOut;
    FirebaseUser user;
    TextView profileInfo;
    String email;
    DatabaseReference reference;
    FirebaseStorage storage;
    StorageReference mStoragref;
    String PhotoUrl;
    DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        updateProfileButton = rootView.findViewById(R.id.addPicture2);
        profileInfo = rootView.findViewById(R.id.profileInfo);

        profilePic = rootView.findViewById(R.id.imageView);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference();

        DatabaseReference getImage = databaseReference.child("your_database_node"); // Replace "your_database_node" with the actual node name

        getImage.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(
                            @NonNull DataSnapshot dataSnapshot) {
                        // Iterate through the children to find the correct image URL
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            // Get the image URL from the child node
                            String link = childSnapshot.child("imageUrl").getValue(String.class);

                            // Loading that data into the profilePic ImageView using Picasso
                            if (link != null && !link.isEmpty()) {
                                Picasso.get().load(link).into(profilePic);
                                break; // Stop iterating once you find the first URL (assuming you only want one URL)
                            }
                        }
                    }

                    // This will be called when any problem
                    // occurs in getting data
                    @Override
                    public void onCancelled(
                            @NonNull DatabaseError databaseError) {
                        // Show an error message in case of failure
                        Toast.makeText(getContext(), "Error Loading Image: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        reference = FirebaseDatabase.getInstance().getReference("Users");


        auth = FirebaseAuth.getInstance();
        buttonLogOut = rootView.findViewById(R.id.logOut);
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getActivity(), LoginPage.class);
            startActivity(intent);
        }

        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginPage.class);
                startActivity(intent);
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iterate through the dynamic child nodes under "match"
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // Get the name (dynamic) of the child node

                    String name = childSnapshot.child("name").getValue(String.class);
                    String dateOfBirth = childSnapshot.child("dateOfBirth").getValue(String.class);
                    String email = childSnapshot.child("email").getValue(String.class);

                    // Retrieve the data under the child node

                    profileInfo.setText(name + "\n" + dateOfBirth + "\n" + email);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database errors here
            }
        });

        storage=FirebaseStorage.getInstance();
        mStoragref = storage.getReference();

        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(ProfileFragment.this)
                        .crop(16f,16f)	    			//Crop image(Optional), Check Customization for more option
                        .compress(500)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1000, 1000)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(20);
            }
        });

        return rootView;
    }

    public static int calculateAge(String dob) {
        return 0;
    }
    private void uploadImageToFirebase(Uri uri) {
        if (uri != null) {
            final StorageReference myRef = mStoragref.child("image/" + uri.getLastPathSegment());

            myRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    myRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (uri != null) {
                                PhotoUrl = uri.toString();

                                // After successfully getting the download URL, store it in the Realtime Database
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("your_database_node"); // Replace "your_database_node" with the actual node name where you want to store the URL

                                // Create a map to store the URL
                                Map<String, Object> urlMap = new HashMap<>();
                                urlMap.put("imageUrl", PhotoUrl);

                                // Push the URL to the database
                                databaseReference.push().setValue(urlMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // URL successfully stored in the database
                                        Toast.makeText(getContext(), "Image uploaded and URL stored.", Toast.LENGTH_SHORT).show();
                                    }

                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle failure when storing URL in the database
                                        Toast.makeText(getContext(), "Failed to store URL in the database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure when getting download URL
                            Toast.makeText(getContext(), "Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace(); // Print the error stack trace for debugging
                }
            });
        }
    }
}