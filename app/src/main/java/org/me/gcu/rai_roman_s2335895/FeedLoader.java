package org.me.gcu.rai_roman_s2335895;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;

public class FeedLoader {
    private static final String BASE = "https://datex2.trafficscotland.org/rest/2.3/publications/";
    private static final String ROADWORKS_URL = BASE + "CurrentRoadworks/Content.xml";
    private static final String INCIDENTS_URL = BASE + "UnplannedEvents/Content.xml";

    private final Context context;
    private String clientId = "";
    private String clientKey = "";

    public FeedLoader(Context context) {
        this.context = context.getApplicationContext();
        try (InputStream in = this.context.getAssets().open("config.properties")) {
            Properties p = new Properties();
            p.load(in);
            clientId = p.getProperty("clientId", "");
            clientKey = p.getProperty("clientKey", "");
        } catch (IOException e) {
            Log.w("FeedLoader", "No config.properties, using saved or example data");
        }
    }

    public String roadworks() {
        return load(ROADWORKS_URL, "roadworks.xml", "roadworksContentExample.xml");
    }

    public String incidents() {
        return load(INCIDENTS_URL, "incidents.xml", "unplannedContentExample.xml");
    }

    private String load(String url, String cacheName, String exampleAsset) {
        try {
            String xml = download(url);
            saveCache(cacheName, xml);
            Log.d("FeedLoader", "Live data loaded: " + cacheName);
            return xml;
        } catch (Exception e) {
            Log.w("FeedLoader", "Live download failed (" + e.getMessage() + "), using saved copy");
        }
        String cached = readCache(cacheName);
        if (!cached.isEmpty()) return cached;
        return readAsset(exampleAsset);
    }

    private String download(String address) throws IOException {
        if (clientId.isEmpty() || clientKey.isEmpty()) {
            throw new IOException("no credentials");
        }
        HttpURLConnection conn = (HttpURLConnection) new URL(address).openConnection();
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(15000);
        String token = Base64.getEncoder().encodeToString(
                (clientId + ":" + clientKey).getBytes(StandardCharsets.UTF_8));
        conn.setRequestProperty("Authorization", token);
        try {
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP " + conn.getResponseCode());
            }
            return readAll(conn.getInputStream());
        } finally {
            conn.disconnect();
        }
    }

    private void saveCache(String name, String xml) {
        try (java.io.FileOutputStream out = context.openFileOutput(name, Context.MODE_PRIVATE)) {
            out.write(xml.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            Log.w("FeedLoader", "Could not save copy of " + name);
        }
    }

    private String readCache(String name) {
        try {
            return readAll(context.openFileInput(name));
        } catch (IOException e) {
            return "";
        }
    }

    private String readAsset(String name) {
        try {
            return readAll(context.getAssets().open(name));
        } catch (IOException e) {
            return "";
        }
    }

    private String readAll(InputStream stream) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }
}