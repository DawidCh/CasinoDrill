package in.chojnacki.casinodrill;

import android.speech.tts.TextToSpeech;

import java.util.*;

public class Reader implements Runnable {

    private final TextToSpeech tts;
    private final Map<Shape, Set<Color>> configuration;
    private Boolean keepReading;
    private int delay = 3000;

    Reader(TextToSpeech tts, Map<Shape, Set<Color>> configuration) {
        this.tts = tts;
        this.configuration = configuration;
    }

    @Override
    public void run() {
        keepReading = true;
        while (keepReading) {
            String text = getTextToRead();
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "0");
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String getTextToRead() {
        List<Shape> shapes = new ArrayList<>(configuration.keySet());
        Collections.shuffle(shapes);
        Shape shape = shapes.get(0);
        String result = shape.name();
        ArrayList<Color> colors = new ArrayList<>(configuration.get(shape));
        if (!colors.isEmpty()) {
            Collections.shuffle(colors);
            Color color = colors.get(0);
            result += " " + color.name();
        }
        return result;
    }

    void stopReading() {
        keepReading = false;
    }

    void setDelay(int delay) {
        if (delay == 0) {
            this.delay = 3000;
        } else {
            this.delay = delay;
        }
    }
}
