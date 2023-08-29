package com.example.demo2;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

public class Vk {
    public static String token = null;
    public static String get(String ur) throws IOException, InterruptedException {
        sleep(200);
        boolean captcha = false;
        StringBuilder sb = null;
        String resp = null;
        String baseUrl = "https://api.vk.com/method/";
        do {
            URL url = new URL(baseUrl+ur+"&v=5.95&access_token="+token);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.addRequestProperty("User-Agent", "KateMobileAndroid/56 lite-460 (Android 4.4.2; SDK 19; x86; unknown Android SDK built for x86; en)");
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            resp = sb.toString();
            if(sb.indexOf("Captcha")!=-1) {
                System.out.println("капча " + sb);
                JSONObject jso =  new JSONObject(sb.toString()).getJSONObject("error");
                //solveCaptcha(jso.getString("captcha_img"),jso.getString("captcha_sid"),ur);
                sleep(200);
                captcha = false;
                resp = solveCaptcha(jso.getString("captcha_img"),jso.getString("captcha_sid"),baseUrl+ur);
            }else if(sb.indexOf("error")!=-1){
                System.out.println("ssssss " + sb.indexOf("error"));
                System.out.println(sb.toString());
                sleep(100);
                captcha = true;
            }else{
                captcha = false;
            }


        }while (captcha);
        return resp;
    }
    public static void login(){

        //https://oauth.vk.com/authorize?client_id=2685278&scope=69640&redirect_uri=https://oauth.vk.com/blank.html&display=page&response_type=token&revoke=1
        System.setProperty("webdriver.chrome.driver","C:\\Users\\Admin\\Desktop\\123\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriver wb = new ChromeDriver(options);
        String tokenUrl;
        wb.get("https://oauth.vk.com/authorize?client_id=2685278&scope=69640&redirect_uri=https://oauth.vk.com/blank.html&display=page&response_type=token&revoke=1");

        while(true) {
            String s ;
            s = wb.getTitle();
            if(s.equals("OAuth Blank")){
               tokenUrl = wb.getCurrentUrl();

               wb.quit();
               break;
            }
        }
        String token = tokenUrl.substring(tokenUrl.indexOf("token=")+6,tokenUrl.indexOf("&"));
        System.out.println(token);
        Vk.token = token;
    }
    public static String getTrackId(String track) throws IOException, InterruptedException {
        String trackId = null;
        String url = "audio.search?q="+track.replace(" ","%20");


        JSONObject jso = new JSONObject( get(url)).getJSONObject("response");
        trackId = String.valueOf(jso.getJSONArray("items").getJSONObject(0).getInt("id"));
        return trackId;
    }
    public static String getOwnerId(String track) throws IOException, InterruptedException {
        String ownerId = null;
        String url = "audio.search?q="+track.replace(" ","%20");

        JSONObject jso = new JSONObject( get(url)).getJSONObject("response");
        ownerId = String.valueOf(jso.getJSONArray("items").getJSONObject(0).getInt("owner_id"));

        return ownerId;
    }
    public static  String addTrack( String track) throws IOException, InterruptedException {
        String url = "audio.add?audio_id=" + getTrackId(track) + "&owner_id=" + getOwnerId(track);
        return get(url);
    }
    public static String deleteTrack(String track) throws IOException, InterruptedException {
        ArrayList<String> trackList  = getTracksWithId();
        int listSize = trackList.size();

        String title, autor, trackId, ownerId, url="";

        int firstSplit, secondSplit, thirdSplit;

        for(int i =0; i<listSize;i++){
            firstSplit = trackList.get(i).indexOf("&");
            secondSplit = trackList.get(i).indexOf("&",firstSplit+1);
            thirdSplit = trackList.get(i).indexOf("&",secondSplit+1);

            title = trackList.get(i).substring(0, firstSplit);
            autor = trackList.get(i).substring(firstSplit+1, secondSplit);
            trackId = trackList.get(i).substring(secondSplit+1, thirdSplit);
            ownerId = trackList.get(i).substring(thirdSplit+1);

            if(track.toLowerCase().equals(title.toLowerCase()+" "+autor.toLowerCase())||track.toLowerCase().equals(autor.toLowerCase())){
                url = "audio.delete?audio_id=" + trackId + "&owner_id=" + ownerId;
                get(url);
                System.out.println("success!");
            }
        }
        //String url = "audio.delete?audio_id=" + getTrackId(track) + "&owner_id=" + getOwnerId(track);
        return "get(url)";
    }
    public static String solveCaptcha(String captchaUrl,String captchaSid,String url) throws IOException, InterruptedException {
        FileWriter writer = new FileWriter("captchaUrl.txt");
        writer.write(captchaUrl);
        writer.flush();
        Process p = Runtime.getRuntime().exec("main.exe");
        
        p.waitFor();
        Path path = Paths.get("captchaSolved.txt");
        String key =  Files.lines(Paths.get("captchaSolved.txt")).collect(Collectors.joining(System.lineSeparator()));
       URL ur = new URL(url+"&captcha_key="+key+"&captcha_sid="+captchaSid);
       HttpURLConnection httpConn = (HttpURLConnection) ur.openConnection();
       httpConn.addRequestProperty("User-Agent", "KateMobileAndroid/56 lite-460 (Android 4.4.2; SDK 19; x86; unknown Android SDK built for x86; en)");
       httpConn.setRequestMethod("GET");
       httpConn.connect();
       BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
       StringBuilder sb = new StringBuilder();
       String line;

       while ((line = br.readLine()) != null) {
           sb.append(line + "\n");
       }
       br.close();
       System.out.println("ответ от капчи     "+sb.toString());
       return sb.toString();
   }

    public static ArrayList<String> getTracks() throws IOException, InterruptedException {
        ArrayList<String> tracks = new ArrayList<String>();

        String get =  get("audio.get?offset=0");
        JSONArray jso = new JSONObject(get).getJSONObject("response").getJSONArray("items");
        String artist;
        String title;
        System.out.println(jso);
        for(int i=1;jso.length()!=0;i++){
            for(int j=0;j<jso.length();j++){
                artist = jso.getJSONObject(j).getString("title");
                title = jso.getJSONObject(j).getString("artist");
                tracks.add(artist+"&"+title);
                //System.out.println(tracks.get(i*j));
            }
            jso = new JSONObject(get("audio.get?offset="+  200*i)).getJSONObject("response").getJSONArray("items");
            System.out.println(jso);
        }

        return tracks;
    }
    public static JSONArray getTracksJso() throws IOException, InterruptedException {
        JSONArray tracks = new JSONArray();

        String get = get("audio.get?offset=0");
        JSONArray jso = new JSONObject(get).getJSONObject("response").getJSONArray("items");

        for(int i=1;jso.length()!=0;i++){
            for(int j=0;j<jso.length();j++){
                tracks.put(jso.getJSONObject(j).toString());
            }
            jso = new JSONObject(get("audio.get?offset="+  200*i)).getJSONObject("response").getJSONArray("items");
        }

        return tracks;
    }
    public static ArrayList <String> getTracksWithId() throws IOException, InterruptedException {
        ArrayList<String> tracks = new ArrayList<String>();

        String get =  get("audio.get?offset=0");
        JSONArray jso = new JSONObject(get).getJSONObject("response").getJSONArray("items");
        String artist;
        String title;
        //System.out.println(jso);
        for(int i=1;jso.length()!=0;i++){
            for(int j=0;j<jso.length();j++){
                artist = jso.getJSONObject(j).getString("title");
                title = jso.getJSONObject(j).getString("artist");
                tracks.add(artist+"&"+title+"&"+jso.getJSONObject(j).getInt("id")+"&"+jso.getJSONObject(j).getInt("owner_id"));
                //System.out.println(tracks.get(i*j));
            }
            jso = new JSONObject(get("audio.get?offset="+  200*i)).getJSONObject("response").getJSONArray("items");
            //System.out.println(jso);
        }

        //System.out.println(tracks.size());
        return tracks;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
       Vk.token = "vk1.a.Qq1sA6hYepzU9Paq3X6cat8WasvhmQlm0V_lfZhv5ebqHo9XaBxNeybITU0raff_lgn5hoPOEdpt6sjJEWzQr-nyzB30f-V5INX11QueeEMV-UdU5UvIthaOrrcye7OoGiosklLuN8dOep37G-fCWw2Ff7XXtxBtRMCdxlsWA6E3mumvo0Nw6lPqJrGSDXwfZ6c1xNeu9QLjG5oLls5qXA";

        getTracksJso();
    }
}
