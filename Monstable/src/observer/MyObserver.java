package observer;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MyObserver implements PropertyChangeListener{
public ObsInter Do = new ObsInter(){
public void OnDirectionChange(String direction){}
public void OnBackgroundChange(BufferedImage beckgroundTile){}
};

public MyObserver(MySubject subject, ObsInter WhatToDo){
	subject.addChangeListener(this);
	Do = WhatToDo;
}
public void propertyChange(PropertyChangeEvent event){
	if (event.getPropertyName()
	== MySubject.properties.direction.toString()) Do.OnDirectionChange((String) event.getNewValue());
	else if (event.getPropertyName()
	== MySubject.properties.backgroundTile.toString()) Do.OnBackgroundChange((BufferedImage) event.getNewValue());
}
}