package in.chojnacki.casinodrill;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static in.chojnacki.casinodrill.Color.*;
import static in.chojnacki.casinodrill.Shape.*;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private Map<Shape, Set<Color>> elements = new HashMap<>();
    private Reader reader;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private int delay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Arrays.stream(Shape.values()).forEach(shape -> elements.put(shape, new HashSet<Color>()));
        TextToSpeech tts = new TextToSpeech(this, this);
        tts.setLanguage(getResources().getConfiguration().getLocales().get(0));

        this.reader = new Reader(tts, elements, getResources(), getPackageName());

        FloatingActionButton start = findViewById(R.id.start);
        start.setOnClickListener(view -> executor.submit(reader));

        FloatingActionButton stop = findViewById(R.id.stop);
        stop.setOnClickListener(view -> reader.stopReading());

        SeekBar seekBar = findViewById(R.id.delayBar);
        seekBar.setProgress(30);

        TextView delayLabel = findViewById(R.id.delayValue);
        delayLabel.setText(String.format(getResources().getString(R.string.delay), 3.0));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                delay = progress * 100;
                delayLabel.setText(String.format(getResources().getString(R.string.delay), progress / 10.0));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                reader.setDelay(delay);
            }
        });
    }

    public void onCheckboxClicked(View view) {
        CheckBox checkBox = (CheckBox)view;
        switch (checkBox.getId()) {
            case R.id.checkbox_cirle_blue:
                updateConfiguration(CIRCLE, BLUE, checkBox);
                break;
            case R.id.checkbox_cirle_red:
                updateConfiguration(CIRCLE, RED, checkBox);
                break;
            case R.id.checkbox_cirle_green:
                updateConfiguration(CIRCLE, GREEN, checkBox);
                break;
            case R.id.checkbox_cirle_yellow:
                updateConfiguration(CIRCLE, YELLOW, checkBox);
                break;
            case R.id.checkbox_cirle_white:
                updateConfiguration(CIRCLE, WHITE, checkBox);
                break;
            case R.id.checkbox_square_blue:
                updateConfiguration(SQUARE, BLUE, checkBox);
                break;
            case R.id.checkbox_square_green:
                updateConfiguration(SQUARE, GREEN, checkBox);
                break;
            case R.id.checkbox_square_yellow:
                updateConfiguration(SQUARE, YELLOW, checkBox);
                break;
            case R.id.checkbox_square_red:
                updateConfiguration(SQUARE, RED, checkBox);
                break;
            case R.id.checkbox_square_white:
                updateConfiguration(SQUARE, WHITE, checkBox);
                break;
            case R.id.checkbox_triangle_blue:
                updateConfiguration(TRIANGLE, BLUE, checkBox);
                break;
            case R.id.checkbox_triangle_green:
                updateConfiguration(TRIANGLE, GREEN, checkBox);
                break;
            case R.id.checkbox_triangle_yellow:
                updateConfiguration(TRIANGLE, YELLOW, checkBox);
                break;
            case R.id.checkbox_triangle_white:
                updateConfiguration(TRIANGLE, WHITE, checkBox);
                break;
            case R.id.checkbox_triangle_red:
                updateConfiguration(TRIANGLE, RED, checkBox);
                break;
        }
    }

    private void updateConfiguration(Shape shape, Color color, CheckBox checkBox) {
        Set<Color> colors = elements.get(shape);
        if (checkBox.isChecked()) {
            colors.add(color);
        } else {
            colors.remove(color);
        }
    }

    @Override
    public void onInit(int status) {

    }
}
