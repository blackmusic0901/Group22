package my.edu.utar.assignment20;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Verification extends AppCompatActivity {

    Button done;
    ImageButton Image1,Image2,Image3,Image4;
    CircleImageView profile;
    FloatingActionButton setProfile;
    FirebaseStorage storage;
    FirebaseFirestore firstore;
    StorageReference mStoragref;
    String PhotoUrl;
    FirebaseAuth firebaseAuth;
    FirebaseUser CurrentUserId;
    DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        done = findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Verification.this,LoginPage.class);
                startActivity(intent);
                finish();
            }
        });

        profile = findViewById(R.id.Profilepicture);
        setProfile = findViewById(R.id.changeProfile);
        Image1 = findViewById(R.id.image1);
        Image2 = findViewById(R.id.image2);
        Image3 = findViewById(R.id.image3);
        Image4 = findViewById(R.id.image4);

        //create instances
        firstore=FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();
        mStoragref = storage.getReference();

        //get current user id
        firebaseAuth=FirebaseAuth.getInstance();
        CurrentUserId=firebaseAuth.getCurrentUser();

        // Get current user id
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();


        Image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(Verification.this)
                        .crop(16f,16f)	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(1);
            }
        });

        Image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(Verification.this)
                        .crop(16f,16f)	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(2);
            }
        });

        Image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(Verification.this)
                        .crop(16f,16f)	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(3);
            }
        });

        Image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(Verification.this)
                        .crop(16f,16f)	    			//Crop image(Optional), Check Customization for more option
                        .compress(500)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1000, 1000)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(4);
            }
        });

        setProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(Verification.this)
                        .crop(16f,16f)	    			//Crop image(Optional), Check Customization for more option
                        .compress(500)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1000, 1000)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(20);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            Uri uri = data.getData();
            Image1.setImageURI(uri);
        } else if (requestCode == 2) {
            Uri uri = data.getData();
            Image2.setImageURI(uri);
        } else if (requestCode == 3) {
            Uri uri = data.getData();
            Image3.setImageURI(uri);
        } else if (requestCode == 4) {
            Uri uri = data.getData();
            Image4.setImageURI(uri);
        } else {
            Uri uri = data.getData();
            profile.setImageURI(uri);
            uploadImageToFirebase(uri);
        }
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
                                        Toast.makeText(getBaseContext(), "Image uploaded and URL stored.", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle failure when storing URL in the database
                                        Toast.makeText(getBaseContext(), "Failed to store URL in the database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure when getting download URL
                            Toast.makeText(getBaseContext(), "Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getBaseContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace(); // Print the error stack trace for debugging
                }
            });
        }
    }
}

