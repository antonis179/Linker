package org.amoustakos.linker;

public class Version {

	//Change version in build.gradle also
	private static final double VERSION_CODE = 5000;
	private static final String VERSION = "0.5.0";
	private static final String RELEASE = "BETA";
	
	
	public static String getFormattedVersion(){
		return VERSION + '-' + RELEASE;
	}
	
	public static double getVersionCode(){return VERSION_CODE;}
	public static String getReleaseType(){return RELEASE;}

}
