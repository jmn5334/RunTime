package runtime.clueless.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Config class for storing system variables
 *
 */
public class Config {

    private static Config instance = null;

    // singleton
    private Config() {/** Exists only to defeat instantiation.**/}
    public static Config getInstance() {
        if(instance == null) {
            instance = new Config();
        }
        return instance;
    }
    public static Config ptr(){return getInstance();}

    /**
     *  Member variables
     *
     */
    private Properties prop = new Properties();
    public static final String APPNAME="CLUELESS";
    private boolean CONFIGLOADED = false;

    public void init(String configpath){

        File configfid = new File(configpath);
        System.out.println("Properties file location:" +configfid);

        // read the main config file
        try {
            FileInputStream in = new FileInputStream(configpath);
            prop.load(in);
            in.close();
        }
        catch(IOException e) {
            String err = "property file '" + configpath + "' not found in the classpath";

        }

        CONFIGLOADED = true;
    }

    private void checkready(){
        if(!CONFIGLOADED)
            System.out.println("Config Object was called before being initialized");
    }

    public String getStr(String param){
        checkready();

        if(prop.containsKey(param))
            return prop.getProperty(param).replace("\"","");

        return "";

    }

    public Integer getInt(String param){
        checkready();

        if(prop.containsKey(param))
            return Integer.valueOf(prop.getProperty(param));
        else
            System.out.println(" The requested property does not exist param="+param);
        return 0;

    }

    public static String str(String param){

        return Config.ptr().getStr(param);

    }
    public static Integer num(String param){

        return Config.ptr().getInt(param);

    }
}
