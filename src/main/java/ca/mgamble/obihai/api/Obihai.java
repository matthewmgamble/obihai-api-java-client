/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mgamble.obihai.api;

import ca.mgamble.obihai.api.classes.SessionRequest;
import ca.mgamble.obihai.api.classes.SessionResponseData;
import com.google.gson.Gson;

import java.io.Closeable;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.asynchttpclient.*;
import java.util.concurrent.Future;
import java.util.logging.Level;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author mgamble
 */
public class Obihai implements Closeable {

    private static final String JSON = "application/json; charset=UTF-8";
    private final boolean closeClient;
    private final AsyncHttpClient client;
    private final String url;
    private final String token;
    private final String org;
    //  private final ObjectMapper mapper;
    private final Logger logger;
    private boolean closed = false;

    public Obihai(String username, String password) throws Exception {
        this("https://api.obitalk.com/api/v1/", username, password);
    }

    public Obihai(String url, String username, String password) throws Exception {
        this.logger = Logger.getLogger(Obihai.class);
        Gson gson = new Gson();
        this.url = url ;

        // this.closeClient = client == null;
        //   this.token = null;
        this.client = new DefaultAsyncHttpClient();

        SessionRequest sessionRequest = new SessionRequest(username, password);
        System.out.println(gson.toJson(sessionRequest));
        RequestBuilder builder = new RequestBuilder("POST");
        Request request = builder.setUrl(this.url + "sessions")
                .addHeader("Accept", JSON)
                .addHeader("Content-Type", JSON)
                .setBody(gson.toJson(sessionRequest))
                .build();
        Future<Response> f = client.executeRequest(request);
        Response r = f.get();
        if (r.getStatusCode() != 201) {
            if (r.getStatusCode() == 401) {

                throw new Exception("Unauthorized");
            } else {
                System.out.println(r.getResponseBody());
                System.out.println(r.getStatusCode());
                throw new Exception("Could not login to obihai");
            }
        } else {
            SessionResponseData loginResponse = gson.fromJson(r.getResponseBody(), SessionResponseData.class);
            this.token = loginResponse.getData().getToken();
            this.org = loginResponse.getData().getOrg();
            logger.debug("Got token!" + this.token);
            System.out.println("got token: " + this.token);
        }
        closeClient = true;
    }

    //////////////////////////////////////////////////////////////////////
    // Closeable interface methods
    //////////////////////////////////////////////////////////////////////
    public boolean isClosed() {
        return closed || client.isClosed();
    }

    public void close() {
        if (closeClient && !client.isClosed()) {
            try {
                client.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Obihai.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        closed = true;
    }
    
    /* Device functions */
    public String getDeviceID(String macAddress) throws Exception {
        RequestBuilder builder = new RequestBuilder("GET");
        Request request = builder.setUrl(this.url + "devices?macAddress=" + macAddress.toUpperCase())
                .addHeader("Accept", JSON)
                .addHeader("Content-Type", JSON)
                .addHeader("Authorization", "Bearer " + this.token)
                
                .build();
        Future<Response> f = client.executeRequest(request);
        Response r = f.get();
        if (r.getStatusCode() != 200) {
            System.out.println(this.url + "devices?macAddress=" + macAddress.toUpperCase());
            System.out.println("Got response " + r.getStatusCode());
            throw new Exception("Could not get device ID");
        }  else {
                final JSONObject obj = new JSONObject(r.getResponseBody());
                final JSONArray dataobject = obj.getJSONArray("data");
                final JSONObject data = dataobject.getJSONObject(0);
                return data.getString("id");
        }
    }
}
