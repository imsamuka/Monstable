package main;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class SavingData{
public static File file = new File("res/config.properties");
public static Properties  prop = new Properties();

public static void setSaveFiles(){
	
	if (Game.planB) return;
	
	try{
		OutputStream output = new FileOutputStream(file);
		
		prop.setProperty("Scale", String.valueOf(Windows.SCALE));
		prop.store(output, null);
		System.out.println("Saved Settings.");
	}
	catch(IOException e){
		e.printStackTrace();
	}
}
public static void getSaveFiles(){
	
	if (Game.planB) {
		prop.setProperty("Scale", "2");
		prop.setProperty("Record", "0");
		return;
	}
	
	
	
	
	if (!file.exists()) {
		System.out.println("Settings Dont exist.");
		return;
	}
	try{
		InputStream input = new FileInputStream(file);
		prop.load(input);
		System.out.println("Settings Recovered.");
	}
	catch(IOException e){
		e.printStackTrace();
	}
	
}


}