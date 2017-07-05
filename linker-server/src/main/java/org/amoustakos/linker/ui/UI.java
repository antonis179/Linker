package org.amoustakos.linker.ui;

import org.amoustakos.linker.JettyServer;
import org.amoustakos.linker.exceptions.ServerException;
import org.amoustakos.linker.utils.DesktopUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class UI {
    //Log4j
    private static final Logger logger = LogManager.getLogger(UI.class.getName());

    private final JettyServer server;

    // Create a popup menu components
    private final MenuItem startItem = new MenuItem("Start Server");
    private final MenuItem stopItem = new MenuItem("Stop Server");
//    private final MenuItem aboutItem = new MenuItem("About");
    private final MenuItem exitItem = new MenuItem("Exit");

    /*
     * Constructors
     */
    public UI(JettyServer server) {
        this.server = server;
        stopItem.setEnabled(false);
    }


    public void init(){
        //Set look and feel
        try {
            if(DesktopUtil.isWindows())
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            else
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException |
                    IllegalAccessException |
                    InstantiationException |
                    ClassNotFoundException ex) {
            logger.catching(ex);
        }

        //Turn off bold fonts
        UIManager.put("swing.boldMetal", Boolean.FALSE);

        //Check the SystemTray support
        if (!SystemTray.isSupported()) {
            logger.error("SystemTray is not supported. " +
                    "Please switch JVMs or start in CLIStarter mode.");
            return;
        }

        SwingUtilities.invokeLater(this::makeTaskbarMenu);
    }


    /*
     * Server functionality
     */

    private synchronized boolean startServer(){
        disableServerOperations();
        try {
            server.start();
            toggleServerStates(true);
            return true;
        } catch (ServerException e) {
            //TODO: Add error dialog
            toggleServerStates(false);
            return false;
        }
    }

    private synchronized boolean stopServer(){
        disableServerOperations();
        toggleServerStates(!server.stop());
        return true;
    }

    private void toggleServerStates(boolean serverState){
        startItem.setEnabled(!serverState);
        stopItem.setEnabled(serverState);
    }
    private void disableServerOperations(){
        startItem.setEnabled(false);
        stopItem.setEnabled(false);
    }


    /*
     * Helpers
     */

    /**
     * Makes the taskbar menu
     */
    private void makeTaskbarMenu(){
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon =
                                new TrayIcon(
                                        createImage(
                                                "icon.png",
                                                "tray icon"
                                        )
                                );
        final SystemTray tray = SystemTray.getSystemTray();

        //Add to popup
        popup.add(startItem);
        popup.add(stopItem);
        //TODO: Add about button
//        popup.addSeparator();
//        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(exitItem);

        //Add to tray
        trayIcon.setPopupMenu(popup);

        //Set icon
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("Could not add tray icon.");
        }

        //Add listeners
        exitItem.addActionListener(ev -> {
            server.shutdown();
            System.exit(0);
        });
        startItem.addActionListener(ev -> startServer());
        stopItem.addActionListener(ev -> stopServer());
    }


    /**
     * Obtains the icon URL
     */
    private static Image createImage(String path, String description) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        URL imageURL = classLoader.getResource(path);

        if (imageURL == null) {
            //TODO: Move to error string
            logger.error("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
}
