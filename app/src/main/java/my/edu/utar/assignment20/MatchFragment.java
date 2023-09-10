package my.edu.utar.assignment20;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MatchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MatchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MatchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MatchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MatchFragment newInstance(String param1, String param2) {
        MatchFragment fragment = new MatchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ImageButton btLike,btUnlike,btInfo,btnBack;
    TextView name_Age,state,lifeStyle,hobby,personality,name_Age2,state2;
    ImageView imgView,imgInfo;
    int currentImage;
    int image[]=new int[20];
    LinearLayout infoLayout;
    boolean btnClicked;
    FirebaseDatabase database;
    DatabaseReference reference;
    private Uri selectedImageUri=null;
    private FirebaseStorage storage;
    StorageReference storageReference;
    String name;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview=inflater.inflate(R.layout.fragment_match, container, false);

        image[1]=R.drawable.emily;image[3]=R.drawable.ava;image[5]=R.drawable.mia;image[7]=R.drawable.liam;image[9]=R.drawable.henry;image[11]=R.drawable.benjamin;
        image[2]=R.drawable.sophia;image[4]=R.drawable.olivia;image[6]=R.drawable.harper;image[8]=R.drawable.noah;image[10]=R.drawable.mason;image[0]=R.drawable.lisa;

        String []nameAge={"Lisa 18","Emily 23","Sophia 22","Ava 21","Olivia 20","Mia 19","Harper 24",
                            "Liam 24","Noah 23","Henry 22","Mason 20","Benjamin 21"};

        String []State={"Penang","Perak","Kedah","Selangor","Selangor","Johor","Pahang","Melaka","Sarawak",
                        "Sabah","Johor","Perak"};

        String []life={"Minimalist,Active","Minimalist,Sustainable","Minimalist,Nomadic","Minimalist,Socialite",
                            "Minimalist,Frugal","Active,Sustainable","Active,Nomadic","Active,Socialite",
                            "Active,Frugal","Sustainable,Nomadic","Sustainable,Socialite","Sustainable,Frugal"};

        String[]hobbyArray={"Running,Reading","Running,Cooking","Running,Gaming","Running,Gardening",
                            "Running,Photography","Reading,Cooking","Reading,Gardening","Reading,Gaming",
                            "Reading,Photography","Cooking,Gaming","Cooking,Gardening","Cooking,Photography"};

        String[]personArray={"Extroverted,Introverted","Extroverted,Optimistic","Extroverted,Analytical","Extroverted,Ambitious",
                            "Extroverted,Empathetic","Introverted,Optimistic","Introverted,Analytical","Introverted,Empathetic",
                            "Optimistic,Analytical","Introverted,Ambitious","Optimistic,Empathetic","Optimistic,Ambitious"};

        currentImage=0;

        imgView=rootview.findViewById(R.id.imageView2);
        imgInfo=rootview.findViewById(R.id.imageView3);
        name_Age=rootview.findViewById(R.id.textView7);
        state=rootview.findViewById(R.id.state);

        infoLayout=rootview.findViewById(R.id.infoLayout);

        btLike=rootview.findViewById(R.id.btnLike);
        btUnlike=rootview.findViewById(R.id.btnDislike);
        btInfo=rootview.findViewById(R.id.btnInfo);
        btnBack=rootview.findViewById(R.id.btnBack2);

        database=FirebaseDatabase.getInstance();
        reference=database.getReference("match");

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference().child("matching");

        imgView.setImageResource(image[currentImage]);
        name_Age.setText(nameAge[currentImage]);
        state.setText(State[currentImage]);

        btLike.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View view) {
                int index=nameAge[currentImage].indexOf(" ");
                name=nameAge[currentImage].substring(0,(index+1));

                reference.child(name).child("Name").setValue(name);

                // Get the drawable from the ImageView
                Drawable drawable = imgView.getDrawable();

                if (drawable != null) {
                    // Convert the drawable to a Bitmap
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

                    // Upload the Bitmap to Firebase Storage
                    uploadImage(bitmap);
                } else {
                    // Handle the case where the ImageView doesn't have an image
                }

                btnClicked=true;
                currentImage++;
                if(currentImage==12){
                    currentImage=0;
                }

                imgView.setImageResource(image[currentImage]);
                name_Age.setText(nameAge[currentImage]);
                state.setText(State[currentImage]);

            }
        });

        btUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnClicked=true;
                currentImage++;
                if(currentImage==12){
                    currentImage=0;
                }

                imgView.setImageResource(image[currentImage]);
                name_Age.setText(nameAge[currentImage]);
                state.setText(State[currentImage]);

            }
        });

        btInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name_Age2=rootview.findViewById(R.id.name_Age);
                state2=rootview.findViewById(R.id.state2);
                lifeStyle=rootview.findViewById(R.id.lifeStyle);
                hobby=rootview.findViewById(R.id.hobby);
                personality=rootview.findViewById(R.id.personality);

                infoLayout.setVisibility(View.VISIBLE);
                imgInfo.setImageResource(image[currentImage]);

                name_Age2.setText(nameAge[currentImage]);
                state2.setText(State[currentImage]);
                lifeStyle.setText(life[currentImage]);
                hobby.setText(hobbyArray[currentImage]);
                personality.setText(personArray[currentImage]);

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoLayout.setVisibility(View.GONE);
            }
        });

        return rootview;
    }

    private void uploadImage(Bitmap bitmap) {
        if (bitmap != null) {
            // Use a unique identifier, such as a timestamp, to avoid duplicates
            String imageName =name + ".jpg";

            StorageReference imageRef = storageReference.child(imageName);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnSuccessListener(requireActivity(), taskSnapshot -> {
                // Image uploaded successfully
                // You can get the download URL if needed:
                imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                    String imageUrl = downloadUri.toString();
                    reference.child(name).child("imageLink").setValue(imageUrl);
                    // Do something with the image URL (e.g., save it to a database)
                });
            }).addOnFailureListener(e -> {
                // Handle upload failure
            });
        } else {
            // Handle the case where the Bitmap is null
        }
    }
}