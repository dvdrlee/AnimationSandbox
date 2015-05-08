package com.animationsandbox;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {

    private final List<String> animProperties = Arrays.asList(
            "translationX", "translationY",
            "rotation", "rotationX", "rotationY",
            "scaleX", "scaleY",
            "pivotX", "pivotY",
            "x", "y",
            "alpha"
    );

    private final int SIZE = animProperties.size();
    private final float TEXT_SIZE = 18f;
    private final int MAX_PROGRESS = 99;
    private final int DEFAULT_PROGRESS = 13;
    private final int MAX_DURATION_MS = 10000;

    private View imageView;
    private List<EditText> startFields = new ArrayList<EditText>();
    private List<EditText> endFields = new ArrayList<EditText>();
    private List<CheckBox> checkBoxes = new ArrayList<CheckBox>();
    private SeekBar durationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
            }
        });
        durationBar = (SeekBar)findViewById(R.id.seekbar);
        durationBar.setMax(MAX_PROGRESS);
        final TextView durationText = (TextView)findViewById(R.id.duration);
        durationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                durationText.setText(Integer.toString(getDuration(progress))+"ms");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });
        durationBar.setProgress(DEFAULT_PROGRESS);
        initFields();
    }

    private int getDuration(int progress){
        return (int) ((1 - Math.log(MAX_PROGRESS - progress+1) / Math.log(MAX_PROGRESS+1) ) * MAX_DURATION_MS);
    }

    private void initFields(){
        GridLayout grid = (GridLayout)findViewById(R.id.grid);
        for (int i=0; i< SIZE; i++){
            TextView title = getTitle(animProperties.get(i));
            grid.addView(title);

            EditText startText = getEditText("start");
            grid.addView(startText);
            startFields.add(startText);

            EditText endText = getEditText("end");
            grid.addView(endText);
            endFields.add(endText);

            CheckBox box = new CheckBox(this);
            grid.addView(box);
            checkBoxes.add(box);
        }
    }

    private TextView getTitle(String tag){
        TextView title = new TextView(this);
        title.setText(tag);
        title.setTextSize(TEXT_SIZE);
        return title;
    }

    private EditText getEditText(String tag){
        EditText editText = new EditText(this);
        editText.setHint(tag);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setTextSize(TEXT_SIZE);
        return editText;
    }

    private void startAnimation(){
        for (int i=0; i< SIZE; i++){
            List<PropertyValuesHolder> properties = new ArrayList<PropertyValuesHolder>();
            if(checkBoxes.get(i).isChecked()){
                float start = parseFloat(startFields.get(i).getText().toString());
                float end = parseFloat(endFields.get(i).getText().toString());
                String property = animProperties.get(i);
                properties.add(PropertyValuesHolder.ofFloat(property, start, end));
            }
            PropertyValuesHolder[] propertiesArray = properties.toArray(new PropertyValuesHolder[properties.size()]);
            long duration = (long) getDuration(durationBar.getProgress());
            ObjectAnimator.ofPropertyValuesHolder(imageView, propertiesArray).setDuration(duration).start();
        }
    }

    private float parseFloat(String str){
        if (str.isEmpty())
            return 0f;
        else
            return Float.parseFloat(str);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
