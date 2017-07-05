package org.amoustakos.linker;

import org.amoustakos.linker.exceptions.ServerException;
import org.amoustakos.linker.exceptions.SettingsException;
import org.amoustakos.linker.resources.Constants;
import org.amoustakos.linker.resources.Settings;
import org.amoustakos.linker.resources.SettingsLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    private static String settingsPath = Constants.SETTINGS_LOCATION;


    private Main(){}


    public static void main(String[] args) throws IOException, SettingsException, ServerException {
        if (args != null)
            for (int i = 0; i < args.length; i++) {
                if ("-c".equals(args[i])) {
                    settingsPath = args[i + 1];
                    break;
                }
            }

        //Settings
        loadSettings();

        //Init UI or CLIStarter
        if(Settings.getInstance().initServerGUI())
            UIStarter.init();
        else
            CLIStarter.init();
    }



    /*
     * Helpers
     */
    private static void loadSettings() throws IOException, SettingsException {
        Properties config = new Properties();
        try {
            config.load(new FileInputStream(new File(settingsPath)));
        } catch (IOException | NullPointerException ignored) {
            System.err.println("Settings could not be loaded.");
            System.out.println("Settings path specified: " + settingsPath);
        }

        /*
		 * Log4J configuration must be done before any log is started
		 */
        System.setProperty("log.path", config.getProperty("log.path"));


        Settings.SETTINGS_PATH = settingsPath;

        // Load settings
        SettingsLoader.loadSettings();
    }




}
