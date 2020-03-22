package inputs;

public class KeyObj{
public static enum types{
movement,
other;
}

private Enum<types> type    = types.other;
private boolean     pressed = false, singlePressed = false;
private int[]       KeyID;
private String      name    = null, oppositeKey = null;
private InputInt    inputs;

protected KeyObj(int[] KeyID, String name){
	this.KeyID = KeyID;
	this.name  = name;
}
protected KeyObj(int[] KeyID, String name, InputInt inputs){
	this.KeyID  = KeyID;
	this.name  = name;
	this.inputs = inputs;
}
protected KeyObj(int[] KeyID, String name, String oppositeKey){
	this.KeyID       = KeyID;
	this.name        = name;
	this.oppositeKey = oppositeKey;
	type             = types.movement;
}
// Getters
public boolean isPressed(){ return pressed; }
public boolean isSinglePressed(){ return singlePressed; }
public int[] getKeyID(){ return KeyID; }
public String getName(){ return name; }
public String getOppositeKey(){ return oppositeKey; }
public Enum<types> getType(){ return type; }
public InputInt getInputs(){ return inputs; }
// Setters
protected void setPressed(boolean pressed){ this.pressed = pressed; }
protected void setSinglePressed(boolean singlePressed){ this.singlePressed = singlePressed; }
protected void setInputs(InputInt inputs){ this.inputs = inputs; }
}