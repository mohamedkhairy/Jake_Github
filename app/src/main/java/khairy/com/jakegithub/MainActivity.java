package khairy.com.jakegithub;

import android.os.Bundle;

import dagger.android.support.DaggerAppCompatActivity;
import khairy.com.jakegithub.fragment.JakeGithubFragment;

public class MainActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startFragment();
    }

    private void startFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame, new JakeGithubFragment())
                .commit();
    }
}
