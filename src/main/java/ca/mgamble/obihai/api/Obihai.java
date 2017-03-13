/*
The MIT License (MIT)

Copyright (c) 2017 Matthew M. Gamble https://www.mgamble.ca

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package ca.mgamble.obihai.api;

import ca.mgamble.obihai.api.classes.BaseProfiles;
import ca.mgamble.obihai.api.classes.Configuration;
import ca.mgamble.obihai.api.classes.ConfigurationType;
import ca.mgamble.obihai.api.classes.ConfigurationTypes;
import ca.mgamble.obihai.api.classes.Configurations;
import ca.mgamble.obihai.api.classes.DeviceList;
import ca.mgamble.obihai.api.classes.DeviceQuickValues;
import ca.mgamble.obihai.api.classes.DeviceStats;
import ca.mgamble.obihai.api.classes.DeviceTypes;
import ca.mgamble.obihai.api.classes.Organization;
import ca.mgamble.obihai.api.classes.Organizations;
import ca.mgamble.obihai.api.classes.Profile;
import ca.mgamble.obihai.api.classes.RebootStatus;
import ca.mgamble.obihai.api.classes.SessionRequest;
import ca.mgamble.obihai.api.classes.SessionResponse;

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
    Gson gson = new Gson();

    public Obihai(String username, String password) throws Exception {
        this("https://api.obitalk.com/api/v1/", username, password);
    }

    public Obihai(String url, String username, String password) throws Exception {
        this.logger = Logger.getLogger(Obihai.class);
        this.url = url;
        this.client = new DefaultAsyncHttpClient();

        SessionRequest sessionRequest = new SessionRequest(username, password);
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
                throw new Exception("Could not login to obihai");
            }
        } else {
            JSONObject obj = new JSONObject(r.getResponseBody());
            JSONObject dataobject = obj.getJSONObject("data");
            SessionResponse loginResponse = gson.fromJson(dataobject.toString(), SessionResponse.class);
            /// JSONObject data = dataobject.getJSONObject(0);
            this.token = loginResponse.getToken();
            this.org = loginResponse.getOrg();
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

    /* Profile functions */
    
    public BaseProfiles getBaseProfiles() throws Exception {
        Future<Response> f = client.executeRequest(buildRequest("GET", "base-profiles"));
        Response r = f.get();
        if (r.getStatusCode() != 200) {
            
            throw new Exception("Could not get device ID");
        } else {
            return gson.fromJson(r.getResponseBody(), BaseProfiles.class);
            
        }
    }
    /*Replace a basic base profile resource instance. The baseprofile path parameter is the profile’s resource id.*/
    public Profile replaceBaseProfile(Profile baseProfile)throws Exception {
        
        Future<Response> f = client.executeRequest(buildRequest("PUT", "base-profiles/" + baseProfile.getId(), gson.toJson(baseProfile)));
        Response r = f.get();
        if (r.getStatusCode() != 200) {
            
            throw new Exception("Could not replace base profile");
        } else {
            return gson.fromJson(r.getResponseBody(), Profile.class);
            
        }
    }
    public Profile updateBaseProfile(Profile baseProfile)throws Exception {
        
        Future<Response> f = client.executeRequest(buildRequest("PATCH", "base-profiles/" + baseProfile.getId(), gson.toJson(baseProfile)));
        Response r = f.get();
        if (r.getStatusCode() != 200) {
            
            throw new Exception("Could not update base profile");
        } else {
            return gson.fromJson(r.getResponseBody(), Profile.class);
            
        }
    }
    
    /* Configuration functions */
    
    public Configurations getConfigurations() throws Exception {
      Future<Response> f = client.executeRequest(buildRequest("GET", "cfgs"));
        Response r = f.get();
        if (r.getStatusCode() != 200) {
            
            throw new Exception("Could not get configurations");
        } else {
            return gson.fromJson(r.getResponseBody(), Configurations.class);
            
        }
    }
    
        public Configuration getConfiguration(String configID) throws Exception {
      Future<Response> f = client.executeRequest(buildRequest("GET", "cfgs/" + configID));
        Response r = f.get();
        if (r.getStatusCode() != 200) {
            
            throw new Exception("Could not get configuration");
        } else {
                        JSONObject obj = new JSONObject(r.getResponseBody());
            JSONObject dataobject = obj.getJSONObject("data");
            Configuration configuration = gson.fromJson(dataobject.toString(), Configuration.class);
            return configuration;
        }
    }
        
     public Configuration updateConfiguration(Configuration config) throws Exception {
      Future<Response> f = client.executeRequest(buildRequest("PATCH", "cfgs/" + config.getId(), gson.toJson(config)));
        Response r = f.get();
        if (r.getStatusCode() != 200) {
            throw new Exception("Could not update configuration");
        } else {
            JSONObject obj = new JSONObject(r.getResponseBody());
            JSONObject dataobject = obj.getJSONObject("data");
            Configuration configuration = gson.fromJson(dataobject.toString(), Configuration.class);
            return configuration;
        }
    }
     
    public Configuration replaceConfiguration(Configuration config) throws Exception {
      Future<Response> f = client.executeRequest(buildRequest("PUT", "cfgs/" + config.getId(), gson.toJson(config)));
        Response r = f.get();
        if (r.getStatusCode() != 200) {
            throw new Exception("Could not replace configuration");
        } else {
            JSONObject obj = new JSONObject(r.getResponseBody());
            JSONObject dataobject = obj.getJSONObject("data");
            Configuration configuration = gson.fromJson(dataobject.toString(), Configuration.class);
            return configuration;
        }
    }
        
    /* Configuration Types */
    
    public ConfigurationTypes getConfigurationTypes() throws Exception {
      Future<Response> f = client.executeRequest(buildRequest("GET", "cfg-types"));
        Response r = f.get();
        if (r.getStatusCode() != 200) {
            
            throw new Exception("Could not get configuration types");
        } else {
            return gson.fromJson(r.getResponseBody(), ConfigurationTypes.class);
            
        }
    }
    
    public ConfigurationType getConfigurationType(String configID) throws Exception {
      Future<Response> f = client.executeRequest(buildRequest("GET", "cfg-types/" + configID));
        Response r = f.get();
        if (r.getStatusCode() != 200) {
            
            throw new Exception("Could not get configuration type");
        } else {
                        JSONObject obj = new JSONObject(r.getResponseBody());
            JSONObject dataobject = obj.getJSONObject("data");
            ConfigurationType configurationType = gson.fromJson(dataobject.toString(), ConfigurationType.class);
            return configurationType;
        }
    }
        
     public ConfigurationType updateConfigurationType(ConfigurationType cfgType) throws Exception {
      Future<Response> f = client.executeRequest(buildRequest("PATCH", "cfg-type/" + cfgType.getId(), gson.toJson(cfgType)));
        Response r = f.get();
        if (r.getStatusCode() != 200) {
            throw new Exception("Could not update configuration type");
        } else {
            JSONObject obj = new JSONObject(r.getResponseBody());
            JSONObject dataobject = obj.getJSONObject("data");
            ConfigurationType configurationType = gson.fromJson(dataobject.toString(), ConfigurationType.class);
            return configurationType;
        }
    }
     
    public ConfigurationType replaceConfigurationType(ConfigurationType cfgType) throws Exception {
      Future<Response> f = client.executeRequest(buildRequest("PUT", "cfg-type/" + cfgType.getId(), gson.toJson(cfgType)));
        Response r = f.get();
        if (r.getStatusCode() != 200) {
            throw new Exception("Could not replace configuration type");
        } else {
            JSONObject obj = new JSONObject(r.getResponseBody());
            JSONObject dataobject = obj.getJSONObject("data");
            ConfigurationType configurationType = gson.fromJson(dataobject.toString(), ConfigurationType.class);
            return configurationType;
        }
    }
    /* Device functions */
    
    public DeviceList getDevices() throws Exception {
        Future<Response> f = client.executeRequest(buildRequest("GET", "devices"));
        Response r = f.get();
        if (r.getStatusCode() != 200) {
            
            throw new Exception("Could not get device ID");
        } else {
            return gson.fromJson(r.getResponseBody(), DeviceList.class);
            
        }
    }
    
    public DeviceTypes getDeviceTypes() throws Exception  {
        Future<Response> f = client.executeRequest(buildRequest("GET", "device-types"));
        Response r = f.get();
        if (r.getStatusCode() != 200) {
            
            throw new Exception("Could not get device types");
        } else {
            return gson.fromJson(r.getResponseBody(), DeviceTypes.class);
        }
    }
        
    public String getDeviceID(String macAddress) throws Exception  {
        Future<Response> f = client.executeRequest(buildRequest("GET", "devices?macAddress=" + macAddress.toUpperCase()));
        Response r = f.get();
        if (r.getStatusCode() != 200) {
            throw new Exception("Could not get device with ID: " + macAddress);
        } else {
            final JSONObject obj = new JSONObject(r.getResponseBody());
            final JSONArray dataobject = obj.getJSONArray("data");
            final JSONObject data = dataobject.getJSONObject(0);
            return data.getString("id");
        }
    }

    public DeviceQuickValues getDeviceQuickValues(String deviceID) throws Exception {
        Future<Response> f = client.executeRequest(buildRequest("GET", "devices/" + deviceID + "/quick-values"));
        Response r = f.get();
        if (r.getStatusCode() != 200) {
            
            throw new Exception("Could not get device quick values");
        } else {

            JSONObject obj = new JSONObject(r.getResponseBody());
            JSONObject dataobject = obj.getJSONObject("data");
            DeviceQuickValues quickValues = gson.fromJson(dataobject.toString(), DeviceQuickValues.class);
            /// JSONObject data = dataobject.getJSONObject(0);
            return quickValues;
        }
    }
    
    public DeviceStats getDeviceStats(String deviceID) throws Exception {
        Future<Response> f = client.executeRequest(buildRequest("GET", "devices/" + deviceID + "/stats"));
        Response r = f.get();
        if (r.getStatusCode() != 200) {
            
            throw new Exception("Could not get device stats");
        } else {
            return gson.fromJson(r.getResponseBody(), DeviceStats.class);
            
        }
    }
        
    
    public RebootStatus rebootDevice(String deviceID) throws Exception {
        Future<Response> f = client.executeRequest(buildRequest("GET", "devices/" + deviceID + "/reboots"));
        RebootStatus rebootStatus = new RebootStatus();
        Response r = f.get();
        if (r.getStatusCode() != 200) {
            if (r.getStatusCode() == 405) {
                rebootStatus.setSuccess(false);
            } else {            
                throw new Exception("Exception trying to reboot device");
            }
        } else {
            JSONObject obj = new JSONObject(r.getResponseBody());
            JSONObject dataobject = obj.getJSONObject("data");
            rebootStatus.setSuccess(true);
           // rebootStatus.setAckTime("asdf");

        }
        return rebootStatus;
    }

    /* Org functions */
    
    public Organizations getOrganizations() throws Exception {
        Future<Response> f = client.executeRequest(buildRequest("GET", "orgs"));
        Response r = f.get();
        if (r.getStatusCode() != 200) {
            throw new Exception("Could not get Organizations");
        } else {
            return gson.fromJson(r.getResponseBody(), Organizations.class);
            
        }
    }
    
    public Organization getOrganization(String orgID) throws Exception {
        Future<Response> f = client.executeRequest(buildRequest("GET", "orgs/" + orgID));
        Response r = f.get();
        if (r.getStatusCode() != 200) {
             throw new Exception("Could not get Organizations");
        } else {
            JSONObject obj = new JSONObject(r.getResponseBody());
            JSONObject dataobject = obj.getJSONObject("data");
            Organization orgValues = gson.fromJson(dataobject.toString(), Organization.class);
            /// JSONObject data = dataobject.getJSONObject(0);
            return orgValues;
            
        }
    }
    //Organization
    private Request buildRequest(String type, String subUrl) {
        RequestBuilder builder = new RequestBuilder(type);
        Request request = builder.setUrl(this.url + subUrl)
                .addHeader("Accept", JSON)
                .addHeader("Content-Type", JSON)
                .addHeader("Authorization", "Bearer " + this.token)
                .build();
        return request;
    }
    
        private Request buildRequest(String type, String subUrl, String requestBody) {
        RequestBuilder builder = new RequestBuilder(type);
        Request request = builder.setUrl(this.url + subUrl)
                .addHeader("Accept", JSON)
                .addHeader("Content-Type", JSON)
                .addHeader("Authorization", "Bearer " + this.token)
                .setBody(requestBody)
                .build();
        return request;
    }
    
}
