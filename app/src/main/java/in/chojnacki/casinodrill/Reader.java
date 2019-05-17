package in.chojnacki.casinodrill;

import android.content.res.Resources;
import android.speech.tts.TextToSpeech;

import java.util.*;

public class Reader implements Runnable {

    private final TextToSpeech tts;
    private final Map<Shape, Set<Color>> configuration;
    private final Resources resources;
    private final String packageName;
    private Boolean keepReading;
    private int delay = 3000;

    Reader(TextToSpeech tts, Map<Shape, Set<Color>> configuration, Resources resources, String packageName) {
        this.tts = tts;
        this.configuration = configuration;
        this.resources = resources;
        this.packageName = packageName;
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
        int shapeStringId = resources.getIdentifier(shape.name().toLowerCase(), "string", packageName);
        String result = resources.getString(shapeStringId);

        ArrayList<Color> colors = new ArrayList<>(configuration.get(shape));
        if (!colors.isEmpty()) {
            Collections.shuffle(colors);
            Color color = colors.get(0);

            int colorStringId = resources.getIdentifier(color.name().toLowerCase(), "string", packageName);
            result += " " + resources.getString(colorStringId);
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
