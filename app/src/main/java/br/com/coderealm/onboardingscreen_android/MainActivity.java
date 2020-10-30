package br.com.coderealm.onboardingscreen_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private OnboardingAdapter onboardingAdapter;
    private LinearLayout layoutOnboardingIndicators;
    private MaterialButton buttonOnboardingAction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean("appAlreadyOpened", false)){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("appAlreadyOpened", true);
        editor.apply();

        layoutOnboardingIndicators = findViewById(R.id.layoutOnboardingIndicators);
        buttonOnboardingAction = findViewById(R.id.buttonOnboardingAction);

        setupOnboardingItems();
        final ViewPager2 onboardingViewPager = findViewById(R.id.onboardingViewPager);
        onboardingViewPager.setAdapter(onboardingAdapter);

        setupOnboardIndicators();
        setCurrentOnboardingIndicator(0);

        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicator(position);
            }
        });

        buttonOnboardingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onboardingViewPager.getCurrentItem()+1 < onboardingAdapter.getItemCount()){
                    onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem()+1);
                }else {
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    finish();
                }

            }
        });
    }


    private void setupOnboardingItems(){
        List<OnboardingItem> onboardingItems = new ArrayList<>();

        OnboardingItem itemFirst = new OnboardingItem();
        itemFirst.setTitle("First screen");
        itemFirst.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. ");
        itemFirst.setImage(R.drawable.img_first);

        OnboardingItem itemSecond = new OnboardingItem();
        itemSecond.setTitle("Second screen");
        itemSecond.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. ");
        itemSecond.setImage(R.drawable.img_second);

        OnboardingItem itemThird = new OnboardingItem();
        itemThird.setTitle("Third screen");
        itemThird.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. ");
        itemThird.setImage(R.drawable.img_third);

        onboardingItems.add(itemFirst);
        onboardingItems.add(itemSecond);
        onboardingItems.add(itemThird);

        onboardingAdapter= new OnboardingAdapter(onboardingItems);
    }

    private void setupOnboardIndicators(){
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,0,0);
        for (int i = 0; i< indicators.length; i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.onboarding_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicators.addView(indicators[i]);
        }

    }

    private void setCurrentOnboardingIndicator(int index){
        int childCount = layoutOnboardingIndicators.getChildCount();
        for (int i=0; i<childCount; i++){
            ImageView imageView = (ImageView) layoutOnboardingIndicators.getChildAt(i);
            if (i == index){
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_active)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_inactive)
                );
            }
        }
        if(index ==onboardingAdapter.getItemCount() -1){
            buttonOnboardingAction.setText("Get Started");
        } else {
            buttonOnboardingAction.setText("Next");
        }
    }
}