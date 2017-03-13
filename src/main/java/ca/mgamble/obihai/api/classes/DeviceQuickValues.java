/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mgamble.obihai.api.classes;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mgamble
 */
public class DeviceQuickValues {
    private String device;
    private boolean online;
    private String firmware;
    private List<RegState> spRegStates;

    /**
     * @return the device
     */
    public String getDevice() {
        return device;
    }

    /**
     * @param device the device to set
     */
    public void setDevice(String device) {
        this.device = device;
    }

    /**
     * @return the online
     */
    public boolean isOnline() {
        return online;
    }

    /**
     * @param online the online to set
     */
    public void setOnline(boolean online) {
        this.online = online;
    }

    /**
     * @return the firmware
     */
    public String getFirmware() {
        return firmware;
    }

    /**
     * @param firmware the firmware to set
     */
    public void setFirmware(String firmware) {
        this.firmware = firmware;
    }

    public void addSpRegState(RegState regState) {
        if (this.spRegStates == null) {
            this.spRegStates = new ArrayList<>();
        }
        this.spRegStates.add(regState);
    }
    /**
     * @return the spRegStates
     */
    public List<RegState> getSpRegStates() {
        return spRegStates;
    }

    /**
     * @param spRegStates the spRegStates to set
     */
    public void setSpRegStates(List<RegState> spRegStates) {
        this.spRegStates = spRegStates;
    }
    
}
