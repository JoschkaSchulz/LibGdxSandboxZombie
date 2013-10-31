package com.zombies.helper;

import java.util.HashMap;

import com.zombies.game.inventory.*;

public class ItemHelper {
	
private static HashMap<Integer , Consumable> Consumables;

public static void initialize_all_Items(){
	//TODO
	//reads every item from the xml and initializes it in the specific list
}

public static Consumable createConsumable(int ID) throws InvalidIDException
{
	if (Consumables.containsKey(ID))
	{
		return (Consumable) Consumables.get(ID).clone();
	}
	else
	throw new InvalidIDException("ID is not found in Consumables");
			
}

}

class InvalidIDException extends Exception
{
	public InvalidIDException()
	{
	
	}
	public InvalidIDException(String message)
	{
		super(message);
	}
}
