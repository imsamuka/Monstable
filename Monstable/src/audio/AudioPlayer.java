package audio;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class AudioPlayer{
private static Clip[]   clips    = new Clip[16];
private static String[] pathList = new String[16];
private static int      current  = 0;
private Clip            thisClip = null;
private FloatControl gainControl;


public AudioPlayer(String path){
	if (path == null) return;
	for (int i = 0; i < current; i++) if (path.equals(pathList[i])){
		thisClip = clips[i];
		gainControl = (FloatControl) thisClip.getControl(FloatControl.Type.MASTER_GAIN);
		return;
	}
	
	try{
		AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(path));
		AudioFormat baseFormat = ais.getFormat();
		AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
		AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
		thisClip = AudioSystem.getClip();
		thisClip.open(dais);
	}
	catch(Exception e){
		e.printStackTrace();
	}
	gainControl = (FloatControl) thisClip.getControl(FloatControl.Type.MASTER_GAIN);
	clips[current]    = thisClip;
	pathList[current] = path;
	current++;
}
public void play(){
	if (thisClip == null) return;
	stop();
	thisClip.setFramePosition(0);
	thisClip.start();
}
public void stop(){ if (thisClip.isRunning()) thisClip.stop(); }
public void loop(){ thisClip.loop(Clip.LOOP_CONTINUOUSLY); }
public void setVolume(float f) {
	gainControl.setValue(f); // -10.0f Reduce volume by 10 decibels.
}
}
