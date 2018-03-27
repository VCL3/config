/**
 * Created by wliu on 12/1/17.
 */
package com.intrence.config.tasks;

import com.intrence.config.ConfigProvider;
import com.intrence.config.collection.ConfigMap;

public class ConfigJob {

    public static void main(String[] args) throws Exception {
        ConfigMap configMap = ConfigProvider.getConfig();
        System.out.println(configMap);
    }
}
