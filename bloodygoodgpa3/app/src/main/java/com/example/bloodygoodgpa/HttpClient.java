package com.example.bloodygoodgpa;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.proxy.NullProxySelector;

import android.os.Handler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class HttpClient {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    static OkHttpClient client = new OkHttpClient();
    static String url = "http://192.168.56.1:8080/data";
    ExecutorService executor;

    public HttpClient(ExecutorService executor) {
        this.executor = executor;
    }

    private Object post(String url, String input, ArrayList<String> headers) {
        RequestBody body = RequestBody.create(input, JSON);
        Request.Builder builder = new Request.Builder();

        for(String a: headers) {
            builder.addHeader(a.split("=")[0], a.split("=")[1]);
        }
        Request request = builder
                            .url(url)
                            .post(body)
                            .build();

        return execute(request);
    }

    private Object put(String url, String input, ArrayList<String> headers)  {
        RequestBody body = RequestBody.create(input, JSON);
        Request.Builder builder = new Request.Builder();

        for (String a : headers) {
            builder.addHeader(a.split("=")[0], a.split("=")[1]);
        }
        Request request = builder
                            .url(url)
                            .put(body)
                            .build();

        return execute(request);
    }

    private Object get(String url,ArrayList<String> headers) {
        Request.Builder builder = new Request.Builder();

        for(String a: headers) {
            builder.addHeader(a.split("=")[0], a.split("=")[1]);
        }

        Request request = builder
                            .url(url)
                            .get()
                            .build();

        return execute(request);
    }

    private Object delete(String url, String input, ArrayList<String> headers) {
        RequestBody body = RequestBody.create(input, JSON);
        Request.Builder builder = new Request.Builder();

        for(String a: headers) {
            builder.addHeader(a.split("=")[0], a.split("=")[1]);
        }

        Request request = builder
                            .url(url)
                            .delete(body)
                            .build();

        return execute(request);
    }

    private Object execute(Request request) {
        Future obj = executor.submit(() -> {
            try {
                return getBody(client.newCall(request).execute());
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
        try {
            return obj.get();
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String queryBuilder(String method, int numArgs) {
        return url + "?method=" + method + "&numArgs=" + numArgs;
    }

    private Object getBody(Response response) throws IOException {
        try {
            ByteArrayInputStream byteResponse = new ByteArrayInputStream(response.body().bytes());
            ObjectInputStream objResponse = new ObjectInputStream(byteResponse);
            Object retVal = objResponse.readObject();
            objResponse.close();
            return retVal;
        }
        catch(ClassNotFoundException e) {
            return null;
        }
        catch(NullPointerException e) {
            return null;
        }
    }

    public Boolean removeCourse(String netid, String inputCode) {
        String query = queryBuilder("removeCourse", 2);
        ArrayList<String> headers = new ArrayList<>();
        headers.add("args1="+netid);
        headers.add("args2="+inputCode);
        try {
            return (Boolean)(delete(query, "", headers));
        }
        catch(NullPointerException e) {
            return null;
        }
    }

    public Boolean deleteUser(String netid) {
        String query = queryBuilder("deleteUser", 1);
        ArrayList<String> headers = new ArrayList<>();
        headers.add("args1="+netid);
        try {
            return (Boolean)(delete(query, "", headers));
        }
        catch(NullPointerException e) {
            return null;
        }
    }


    @SuppressWarnings("unchecked")
    public ArrayList<String> getCourseArray(String netid) {
        String query = queryBuilder("getCourseArray", 1);
        ArrayList<String> headers = new ArrayList<>();
        headers.add("args1="+netid);
        return (ArrayList<String>)get(query,headers);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<String> getRedeemableCourses(String netid) {
        String query = queryBuilder("getRedeemableCourses", 1);
        ArrayList<String> headers = new ArrayList<>();
        headers.add("args1="+netid);
        return (ArrayList<String>)get(query,headers);
    }

    public Integer getLifetimeDonationNum(String netid) {
        String query = queryBuilder("getLifetimeDonationNum", 1);
        ArrayList<String> headers = new ArrayList<>();
        headers.add("args1="+netid);
        return (Integer)get(query,headers);
    }

    public Integer getNumTokens(String netid)  {
        String query = queryBuilder("getNumTokens", 1);
        ArrayList<String> headers = new ArrayList<>();
        headers.add("args1="+netid);
        Integer retVal = (Integer)get(query,headers);
        if(retVal == null)
            return 0;
        return retVal;
    }

    public void donateBlood(String netid)  {
        String query = queryBuilder("donateBlood", 1);
        ArrayList<String> headers = new ArrayList<>();
        headers.add("args1="+netid);
        put(query, "", headers);
    }

    public void newUser(String netid, String password) {
        String query = queryBuilder("newUser", 2);
        ArrayList<String> headers = new ArrayList<>();
        headers.add("args1="+netid);
        headers.add("args2="+password);
        post(query, "", headers);
    }

    public Boolean addCourse(String netid, String inputCode, String inputProfessor)  {
        ArrayList<String> headers = new ArrayList<>();
        String query = "";
        if(!netid.equals("admin")) {
            query = queryBuilder("addCourse", 3);
            headers.add("args3="+netid);
        }
        else {
            query = queryBuilder("adminAddCourse", 2);
        }
        headers.add("args1="+inputCode);
        headers.add("args2="+inputProfessor);

        return (Boolean)post(query,"",headers);
    }

    public Boolean redeem(String netid, String courseCode)  {
        String query = queryBuilder("redeem", 2);
        ArrayList<String> headers = new ArrayList<>();
        headers.add("args1="+netid);
        headers.add("args2="+courseCode);
        Boolean retVal = (Boolean)put(query,"",headers);
        if(retVal == null) return false;
        return retVal;
    }

    public Boolean login(String netid, String password) {
        String query = queryBuilder("login", 2);
        ArrayList<String> headers = new ArrayList<>();
        headers.add("args1=" + netid);
        headers.add("args2=" + password);
        return (Boolean)put(query, "", headers);
    }

    public Boolean isAdmin(String netid) {
        String query = queryBuilder("isAdmin", 1);
        ArrayList<String> headers = new ArrayList<>();
        headers.add("args1=" + netid);
        Boolean retVal;
        if((retVal = (Boolean)put(query, "", headers)) != null)
            return retVal;
        return false;
    }

    public void sendCode(String netid) {
        String query = queryBuilder("sendCode", 1);
        ArrayList<String> headers = new ArrayList<>();
        headers.add("args1=" + netid);
        put(query, "", headers);
    }

    public Boolean isCorrectCode(String code) {
        String query = queryBuilder("isCorrectCode", 1);
        ArrayList<String> headers = new ArrayList<>();
        headers.add("args1=" + code);
        Boolean retVal;
        if((retVal = (Boolean)put(query, "", headers)) != null)
            return retVal;
        return false;
    }



}
