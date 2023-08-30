package com.example.demo2;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriver;

import javax.media.NoPlayerException;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

import static java.lang.Thread.sleep;


public class Play {
    public static JSONArray tracks = new JSONArray();
    public static void playTrack(String track) throws IOException, InterruptedException {
        //URL url = new URL(tracks.getJSONObject(0).getString("url"));
        URL url = new URL("https://cs121-1v4.vkuseraudio.net/s/v1/acmp/O0N1F_tQ_hGW1di6AyQT3_-GxJCWULHEWhSSuNwPYmgGcxHnBCn9QjNGJ27p-TXjcpx4TZEIOSfcGpmhSLVLoQaYpU8D3pJchkaU18helzsBUXxu1CxIgK6ZFInSwwx8aTn31iYh7QQSH7Gkpx_jGQosicm5_WVlablIcg3VE8WZT2OIcQ.mp3");
        System.setProperty("webdriver.chrome.driver","C:\\Users\\Admin\\Desktop\\123\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        //options.setHeadless(true);
        options.addArguments("--remote-allow-origins=*");
        WebDriver wb = new ChromeDriver(options);
        wb.get(String.valueOf(url));
        System.out.println("start wait");
        sleep(9000);
        WebElement we = wb.findElement(By.name("media"));


    }
    public static void pause(WebDriver wb, WebElement we){
        JavascriptExecutor jsExecutor = (JavascriptExecutor) wb;
        jsExecutor.executeScript("arguments[0].pause()", we);
    }
    public static void play(WebDriver wb, WebElement we){
        JavascriptExecutor jsExecutor = (JavascriptExecutor) wb;
        jsExecutor.executeScript("return arguments[0].play()", we);
    }
    public static void main(String[] args) throws IOException, InterruptedException, NoPlayerException, UnsupportedAudioFileException {
        Vk.token = "vk1.a.Qq1sA6hYepzU9Paq3X6cat8WasvhmQlm0V_lfZhv5ebqHo9XaBxNeybITU0raff_lgn5hoPOEdpt6sjJEWzQr-nyzB30f-V5INX11QueeEMV-UdU5UvIthaOrrcye7OoGiosklLuN8dOep37G-fCWw2Ff7XXtxBtRMCdxlsWA6E3mumvo0Nw6lPqJrGSDXwfZ6c1xNeu9QLjG5oLls5qXA";
        //tracks = Vk.getTracksJso().getJSONArray("tracklist").getJSONArray(0);
        playTrack("1");
    }
}
