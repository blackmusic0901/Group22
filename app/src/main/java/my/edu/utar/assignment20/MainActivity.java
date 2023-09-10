package my.edu.utar.assignment20;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import my.edu.utar.assignment20.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new MatchFragment());

        BottomNavigationView bnv;
        bnv = (BottomNavigationView) findViewById(R.id.bottomNavBar);
        bnv.setSelectedItemId(R.id.match);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.bottomNavBar.setOnItemSelectedListener(
                    item-> {
                        switch(item.getItemId()) {
                            case R.id.match:
                                replaceFragment(new MatchFragment());
                                break;

                            case R.id.chat:
                                replaceFragment(new ChatFragment());
                                break;

                            case R.id.profile:
                                replaceFragment(new ProfileFragment());
                                break;
                        }
                        return true;
                    });
//        }
    }

    //Switches to specified fragment when pressed
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}