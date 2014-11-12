/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sounddisplay;

import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.util.Vector;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JFrame;

/**
 *
 * @author Joseph
 */
public class SoundDisplay {

    static int lineGet = 6;
    static SoundGUI mainGui;

    public static void main(String[] args) {
        mainGui = new SoundGUI();

        TargetDataLine line = StartSound();
        
        System.out.println("Opened");

        // Assume that the TargetDataLine, line, has already
        // been obtained and opened.
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int numBytesRead;
        byte[] data = new byte[line.getBufferSize() / 50];

        // Begin audio capture.
        line.start();

        // Here, stopped is a global boolean set by another thread.
        for (int i = 0; true; i++) {
            // Read the next chunk of data from the TargetDataLine.
            numBytesRead = line.read(data, 0, data.length);
            // Save this chunk of data.
            out.write(data, 0, numBytesRead);
            //System.out.println(data[0] + " " + data[1]);
            
            mainGui.updateDisplay(data, numBytesRead);
        }

        line.close();
        mainGui.dispose();
    }
    
    static public TargetDataLine StartSound() {
        Vector<AudioFormat> formats = getSupportedFormats(TargetDataLine.class);
        System.out.println(formats.size());
        TargetDataLine line;
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, formats.get(lineGet));

        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Not Supported");
            return null;
        }

        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(formats.get(lineGet));
        } catch (LineUnavailableException ex) {
            // Handle the error ... 
            System.out.println("Unavailable");
            return null;
        }
        
        return line;
    }

    static public Vector<AudioFormat> getSupportedFormats(Class<?> dataLineClass) {
        /*
         * These define our criteria when searching for formats supported
         * by Mixers on the system.
         */
        float sampleRates[] = {(float) 8000.0, (float) 16000.0, (float) 44100.0};
        int channels[] = {1, 2};
        int bytesPerSample[] = {2};

        AudioFormat format;
        DataLine.Info lineInfo;

        //SystemAudioProfile profile = new SystemAudioProfile(); // Used for allocating MixerDetails below.
        Vector<AudioFormat> formats = new Vector<AudioFormat>();

        for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
            for (int a = 0; a < sampleRates.length; a++) {
                for (int b = 0; b < channels.length; b++) {
                    for (int c = 0; c < bytesPerSample.length; c++) {
                        format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                                sampleRates[a], 8 * bytesPerSample[c], channels[b], bytesPerSample[c],
                                sampleRates[a], false);
                        lineInfo = new DataLine.Info(dataLineClass, format);
                        if (AudioSystem.isLineSupported(lineInfo)) {
                            /*
                             * TODO: To perform an exhaustive search on supported lines, we should open
                             * TODO: each Mixer and get the supported lines. Do this if this approach
                             * TODO: doesn't give decent results. For the moment, we just work with whatever
                             * TODO: the unopened mixers tell us.
                             */
                            if (AudioSystem.getMixer(mixerInfo).isLineSupported(lineInfo)) {
                                formats.add(format);
                                System.out.println(mixerInfo.toString() + " : " + format.toString());
                            }
                        }
                    }
                }
            }
        }
        return formats;
    }
}
