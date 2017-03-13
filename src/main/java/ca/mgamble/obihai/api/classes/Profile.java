/*
 * The MIT License
 *
 * Copyright 2017 mgamble.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ca.mgamble.obihai.api.classes;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mgamble
 */
public class Profile {
    private String id;
    private String displayName;
    private ProfileType type;
    private List<String> includes;
    private List<Setting> settings;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the type
     */
    public ProfileType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(ProfileType type) {
        this.type = type;
    }

    public void addInclude(String include) {
        if (this.includes == null) {
            this.includes = new ArrayList<>();
        }
        this.includes.add(include);
    }
    /**
     * @return the includes
     */
    public List<String> getIncludes() {
        return includes;
    }

    /**
     * @param includes the includes to set
     */
    public void setIncludes(List<String> includes) {
        this.includes = includes;
    }

    public void addSetting(Setting setting) {
        if (this.settings == null) {
            this.settings = new ArrayList<>();
        }
        this.settings.add(setting);
    }
    /**
     * @return the settings
     */
    public List<Setting> getSettings() {
        return settings;
    }

    /**
     * @param settings the settings to set
     */
    public void setSettings(List<Setting> settings) {
        this.settings = settings;
    }
    
}
