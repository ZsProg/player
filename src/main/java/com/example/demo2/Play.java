package com.example.demo2;

import org.json.JSONArray;

import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Play {
    public static JSONArray tracks = new JSONArray();
    public static void playTrack(String track) throws IOException, UnsupportedAudioFileException {

        URL url = new URL(tracks.getJSONObject(0).getString("url"));
        AudioInputStream stream = AudioSystem.getAudioInputStream(url);
        AudioFormat format = stream.getFormat();
        if ((format.getEncoding() == AudioFormat.Encoding.ULAW) || (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
            AudioFormat tmp = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
            stream = AudioSystem.getAudioInputStream(tmp, stream);
            format = tmp;
        }
        DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat(), ((int) stream.getFrameLength() * format.getFrameSize()));

    }
    public static void main(String[] args) throws IOException, InterruptedException, NoPlayerException, UnsupportedAudioFileException {
        Vk.token = "vk1.a.Qq1sA6hYepzU9Paq3X6cat8WasvhmQlm0V_lfZhv5ebqHo9XaBxNeybITU0raff_lgn5hoPOEdpt6sjJEWzQr-nyzB30f-V5INX11QueeEMV-UdU5UvIthaOrrcye7OoGiosklLuN8dOep37G-fCWw2Ff7XXtxBtRMCdxlsWA6E3mumvo0Nw6lPqJrGSDXwfZ6c1xNeu9QLjG5oLls5qXA";
        Play.tracks = Vk.getTracksJso();
        playTrack("1");
    }
}
