package observer;

import java.awt.image.BufferedImage;

public interface ObsInter{

	public void OnDirectionChange(String direction);
	public void OnBackgroundChange(BufferedImage newbackgroundTile);
}