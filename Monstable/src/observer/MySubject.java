package observer;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class MySubject{
private List<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();

public static enum properties{
direction,
backgroundTile
}

public MySubject(){}

//
private String direction;

public void setDirection(String dir){ notifyListeners(properties.direction.toString(), direction, direction = dir); }
public void setBackgroundTile(BufferedImage backgroundTile){
	notifyListeners(properties.backgroundTile.toString(), backgroundTile);
}
//
public void addChangeListener(PropertyChangeListener newListener){ listener.add(newListener); }
protected void notifyListeners(String propertie, String oldValue, String newValue){
	int size = listener.size();
	for (int i = 0; i
	< size; i++) listener.get(i).propertyChange(new PropertyChangeEvent(this, propertie, oldValue, newValue));
}
protected void notifyListeners(String propertie, BufferedImage newValue){
	int size = listener.size();
	for (int i = 0; i
	< size; i++) listener.get(i).propertyChange(new PropertyChangeEvent(this, propertie, null, newValue));
}
protected void notifyListeners(String propertie, Float newValue){
	int size = listener.size();
	for (int i = 0; i
	< size; i++) listener.get(i).propertyChange(new PropertyChangeEvent(this, propertie, null, newValue));
}
}