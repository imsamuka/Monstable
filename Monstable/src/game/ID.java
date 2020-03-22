package game;

public enum ID{
	Player(null), 
	Enemy(null), 
		Common(Enemy),
	Tile(null), 
		Floor(Tile), 
		Wall(Tile), 
	Attack(null), 
		Goop(Attack);

private ID parent = null;

ID(ID parent){ this.parent = parent; }
public boolean is(ID other){
	
	if (other == null){ return false; }
	
	for (ID t = this; t != null; t = t.parent){
		
		if (other == t){ return true; }
	}
	return false;
}
	
}