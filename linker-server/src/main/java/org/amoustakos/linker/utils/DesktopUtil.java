package org.amoustakos.linker.utils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class DesktopUtil {

    public static boolean browse(URI uri) throws IOException {
        return browseSystemSpecific(uri.toString()) || browseDESKTOP(uri);
    }


    public static boolean open(File file) throws IOException {
        return openSystemSpecific(file.getPath()) || openDESKTOP(file);
    }


    public static boolean edit(File file) throws IOException {
        return openSystemSpecific(file.getPath()) || editDESKTOP(file);
    }


    private static boolean openSystemSpecific(String what) throws IOException {
        String os = getOs();
        IOException exc = null;

        if (isLinux(os)) {
            try {
                if (runCommand("kde-open", "%s", what))
                    return true;
            }catch (IOException e){exc = e;}
            try{
                if (runCommand("gnome-open", "%s", what))
                    return true;
            }catch (IOException e){exc = e;}
            try {
                if (runCommand("xdg-open", "%s", what))
                    return true;
            }catch (IOException e){exc = e;}
        }

        if (isMac(os))
            try{
                if (runCommand("open", "%s", what))
                    return true;
            }catch (IOException e){exc = e;}

        if (isWindows(os))
            try{
                if (runCommand("explorer", "%s", what))
                    return true;
            }catch (IOException e){exc = e;}

        if(exc != null)
            throw exc;

        return false;
    }

    private static boolean browseSystemSpecific(String what) throws IOException {
        String os = getOs();
        IOException exc = null;

        if (isLinux(os)) {
            try {
                if (runCommand("kde-open", "%s", what))
                    return true;
            }catch (IOException e){exc = e;}
            try{
                if (runCommand("gnome-open", "%s", what))
                    return true;
            }catch (IOException e){exc = e;}
            try {
                if (runCommand("xdg-open", "%s", what))
                    return true;
            }catch (IOException e){exc = e;}
        }

        if (isMac(os))
            try{
                if (runCommand("open", "%s", what))
                    return true;
            }catch (IOException e){exc = e;}

        if(exc != null)
            throw exc;

        return false;
    }


    private static boolean browseDESKTOP(URI uri) throws IOException {
        if (!Desktop.isDesktopSupported())
            //Platform is not supported
            return false;

        if (!Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))
            //BROWSE is not supported
            return false;

        Desktop.getDesktop().browse(uri);
        return true;
    }


    private static boolean openDESKTOP(File file) throws IOException {
        if (!Desktop.isDesktopSupported())
            //Platform is not supported
            return false;

        if (!Desktop.getDesktop().isSupported(Desktop.Action.OPEN))
            //OPEN is not supported
            return false;

        Desktop.getDesktop().open(file);
        return true;
    }


    private static boolean editDESKTOP(File file) throws IOException {
        if (!Desktop.isDesktopSupported())
            //Platform is not supported
            return false;

        if (!Desktop.getDesktop().isSupported(Desktop.Action.EDIT))
            //EDIT is not supported
            return false;

        Desktop.getDesktop().edit(file);
        return true;
    }


    private static boolean runCommand(String command, String args, String file) throws IOException {
        String[] parts = prepareCommand(command, args, file);

        Process p = Runtime.getRuntime().exec(parts);
        if (p == null)
            return false;

        try {
            int retval = p.exitValue();
            if (retval == 0)
                return false;
            else
                return false;
        } catch (IllegalThreadStateException itse) {
            return true;
        }
    }


    private static String[] prepareCommand(String command, String args, String file) {
        List<String> parts = new ArrayList<>();
        parts.add(command);

        if (args != null) {
            for (String s : args.split(" ")) {
                s = String.format(s, file); // put in the filename thing

                parts.add(s.trim());
            }
        }

        return parts.toArray(new String[parts.size()]);
    }


    public interface OS {
        String linux = "linux",
                macos = "macos",
                solaris = "solaris",
                unknown = "uknown",
                windows = "windows";
    }
    private static boolean isLinux(String os){
        return OS.linux.equals(os) || OS.solaris.equals(os);
    }
    private static boolean isMac(String os){
        return OS.macos.equals(os);
    }
    private static boolean isWindows(String os){
        return OS.windows.equals(os);
    }


    public static String getOs() {
        String s = System.getProperty("os.name").toLowerCase();

        if (s.contains("win"))
            return OS.windows;

        if (s.contains("mac"))
            return OS.macos;

        if (s.contains("solaris"))
            return OS.solaris;

        if (s.contains("sunos"))
            return OS.solaris;

        if (s.contains("linux"))
            return OS.linux;

        if (s.contains("unix"))
            return OS.linux;

        return OS.unknown;
    }
}
