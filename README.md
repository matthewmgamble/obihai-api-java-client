# obihai-api-java-client
This is a simple Java wrapper client for the OBiTALK service API. This is a work in progress and the API converage is only about 65-70% complete at this point.  

The OBi API offers organizations cloud-based management functionality over their deployments of Obihai equipment.  If you don't know what any of that means, this code likely isn't for you.

Example usage:


            Obihai obihai = new Obihai("https://api.obitalk.com/api/v1/", "USERNAME", "PASSWORD");
            
            String deviceID = obihai.getDeviceID("MACADDRESS");
            DeviceQuickValues = obihai.getDeviceQuickValues(deviceID);
            /* Reboot the device */
            obihai.rebootDevice(deviceID)

