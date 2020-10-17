package myfirebase.com;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView iv = findViewById(R.id.logo);
        TextView tv = findViewById(R.id.welcomeText3);
        TextView tv2 = findViewById(R.id.welcomeText2);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splashtransition);
        iv.startAnimation(animation);
        tv.startAnimation(animation);
        tv2.startAnimation(animation);

        final Intent i = new Intent(this, LoginActivity.class);

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();

    }
}
