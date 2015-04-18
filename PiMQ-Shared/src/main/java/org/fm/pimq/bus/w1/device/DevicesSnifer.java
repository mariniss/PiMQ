/**
 * Copyright 2014 Fabio Marini
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fm.pimq.bus.w1.device;


import org.fm.pimq.conf.Configuration;

import java.io.File;

/**
 * Singleton class that provides all directories where the W1-Therm devices write their data
 *
 * @author Fabio Marini
 */
public class DevicesSnifer {

    protected final static File[] NO_DEVICES = {};

    private DirectoryFileFilter fileFilter;

    private String dirPath;


    /************************************************************
     *          Singleton implementation - start
     ************************************************************/

    /**
     * Default instance
     */
    private static DevicesSnifer instance = null;

    /**
     * Default constructor
     */
    private DevicesSnifer(){

    }

    public static DevicesSnifer getInstance(Configuration configuration){
        if(instance == null){
            synchronized (DevicesSnifer.class){
                if(instance == null){
                    instance = new DevicesSnifer();
                    instance.fileFilter = new DirectoryFileFilter(configuration.getW1DevicesPrefix());
                    instance.dirPath = configuration.getW1DirPath();
                }
            }
        }

        return instance;
    }

    /************************************************************
     *          Singleton implementation - end
     ************************************************************/


    /**
     * Return the number of W1-Therm devices identified
     * @return the number of devices
     */
    public Integer count() {
        return getDevicesDir().length;
    }


    /**
     * Return an array with all directories where the W1-Therm devices write their data
     * @return array of file directories, or an empty array if there are no one
     */
    public File[] getDevicesDir() {
        File dir = new File(dirPath);

        if(dir.exists() && dir.isDirectory()) {
            return dir.listFiles(fileFilter);
        }

        return NO_DEVICES;
    }


    //TODO: integrate file watcher to identify new devices at runtime - https://docs.oracle.com/javase/tutorial/essential/io/notification.html
}
