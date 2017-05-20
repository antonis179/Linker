package org.amoustakos.linker;

public class Version {

	//Change version in build.gradle also
	private static final double VERSION_CODE = 1500;
	private static final String VERSION = "0.1.5";
	private static final String RELEASE = "ALPHA";
	
	
	public static String getFormattedVersion(){
		return VERSION + '-' + RELEASE;
	}
	
	public static double getVersionCode(){return VERSION_CODE;}
	public static String getReleaseType(){return RELEASE;}

}
