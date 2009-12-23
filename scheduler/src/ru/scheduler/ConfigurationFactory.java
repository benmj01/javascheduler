package ru.scheduler;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBContext;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: ALeX
 * Date: 27.11.2009
 * Time: 18:24:53
 */
public class ConfigurationFactory {
    private static final String CONFIG_NAME = "config.xml";
    private static final String CONFIG_PATH = System.getProperty("user.dir") + File.separator + CONFIG_NAME;
    private static ConfigurationFactory instance;
    private Configuration configuration;
    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;


    private ConfigurationFactory() {
        try {
            JAXBContext ctx = JAXBContext.newInstance("ru.scheduler");
            marshaller = ctx.createMarshaller();
            unmarshaller = ctx.createUnmarshaller();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        reload();
    }

    public static ConfigurationFactory getInstance() {
        if (instance == null) {
            instance = new ConfigurationFactory();
        }
        return instance;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void saveConfiguration(Configuration conf) {
        try {
            FileOutputStream fos = new FileOutputStream(CONFIG_PATH);
            try {
                marshaller.marshal(conf, fos);
            } finally {
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveConfiguration() {
        saveConfiguration(configuration);
    }

    public void reload() {
        try {
            FileInputStream fis = new FileInputStream(CONFIG_PATH);
            try {
                configuration = (Configuration) unmarshaller.unmarshal(fis);
            } finally {
                fis.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

