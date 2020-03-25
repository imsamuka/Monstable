package observer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MyObserver implements PropertyChangeListener{
public ObserverInterface Do = new ObserverInterface(){
public void OnDirectionChange(String direction){}
};

public MyObserver(MySubject subject, ObserverInterface WhatToDo){
	subject.addChangeListener(this);
	Do = WhatToDo;
}
public void propertyChange(PropertyChangeEvent event){
	if (event.getPropertyName() == MySubject.properties.direction.toString()) Do.OnDirectionChange((String) event.getNewValue());
	
}
}