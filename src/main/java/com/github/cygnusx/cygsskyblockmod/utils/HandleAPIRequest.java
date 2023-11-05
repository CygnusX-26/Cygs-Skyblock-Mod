package com.github.cygnusx.cygsskyblockmod.utils;


import com.github.cygnusx.cygsskyblockmod.CygsSkyblockMod;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.utils.URIBuilder;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.zip.GZIPInputStream;

public class HandleAPIRequest {
    public String cURL;
    public String method;
    public HashMap<String, String> arguments;
    public HashMap<String, String> headers;
    public boolean gunzip = false;
    public String path;
    public boolean asJson = false;

    public HandleAPIRequest() {
        this.cURL = "";
        this.method = "";
        this.arguments =  new HashMap<String, String>();
        this.headers = new HashMap<String, String>();
        this.path = "";
    }

    public HandleAPIRequest host(String url) {
        this.cURL = url;
        return this;
    }

    public HandleAPIRequest method(String method) {
        this.method = method;
        return this;
    }

    public HandleAPIRequest addArgument(String key, String value) {
        this.arguments.put(key, value);
        return this;
    }

    public HandleAPIRequest addHeader(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    public HandleAPIRequest gunzip() {
        this.gunzip = true;
        return this;
    }

    public static HandleAPIRequest request() {
        return new HandleAPIRequest();
    }

    public HandleAPIRequest path(String path) {
        this.path = path;
        return this;
    }

    public HandleAPIRequest asJson() {
        this.asJson = true;
        return this;
    }

    private CompletableFuture<URL> builder() {
        CompletableFuture<URL> fut = new CompletableFuture<>();
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https").setHost(this.cURL).setPath(this.path);
        for (Map.Entry<String, String> set : arguments.entrySet()) {
            builder.addParameter(set.getKey(), set.getValue());
        }
        try {
            fut.complete(builder.build().toURL());
        } catch (MalformedURLException | URISyntaxException | NullPointerException e) {
            fut.completeExceptionally(e);
        }
        return fut;
    }

    private CompletableFuture<String> asyncGet() {
        return builder().thenApplyAsync(url -> {
            try {
                InputStream inputStream = null;
                URLConnection conn = null;
                try {
                    conn = url.openConnection();
                    if (conn instanceof HttpURLConnection) {
                        ((HttpURLConnection) conn).setRequestMethod(method);
                    }
                    conn.setConnectTimeout(10000);
                    conn.setReadTimeout(10000);
                    for (Map.Entry<String, String> set : headers.entrySet()) {
                        conn.setRequestProperty(set.getKey(), set.getValue());
                    }
                    inputStream = conn.getInputStream();
                    if (gunzip) {
                        inputStream = new GZIPInputStream(inputStream);
                    }
                    return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } finally {
                        if (conn instanceof HttpURLConnection) {
                            ((HttpURLConnection) conn).disconnect();
                        }
                    }
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e); // We can rethrow, since supplyAsync catches exceptions.
            }
        }, CygsSkyblockMod.fixedPool);
    }

    public CompletableFuture<JsonObject> requestJson() {
        return requestJson(JsonObject.class);
    }

    public <T> CompletableFuture<T> requestJson(Class<? extends T> clazz) {
        return asyncGet().thenApply(str -> CygsSkyblockMod.gson.fromJson(str, clazz));
    }
}
