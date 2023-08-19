package hn.uth.examen201930010221;

import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import hn.uth.examen201930010221.databinding.ActivityMain2Binding;

public class MainActivity extends AppCompatActivity {

    private ActivityMain2Binding binding;

    BottomNavigationView bottomNav;

    MaterialToolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        bottomNav = binding.navView;
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);


        NavigationUI.setupWithNavController(bottomNav, navController);
        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> toolbar.setTitle(navDestination.getLabel()));
       /* BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);*/
    }

}