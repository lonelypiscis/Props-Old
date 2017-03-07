package lonelypiscis.props.storage;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/* The declared methods are used by the StorageManager, which is responsible for loading and saving. 
 * Letting the classes read and write themselves allows better maintaining.
 * */

public interface IJsonStorable {
	
	/* A class implementing IStorable can write itself to a json object. */
	
	public void saveToJson(JsonObject jsonObject);
	
	/* A class implementing IStorable can read itself from a json object. */
	
	public void loadFromJson(JsonObject jsonObject);
	
	public default JsonObject getSavedInstance() {
		JsonObject writtenJsonObject = new JsonObject();
		saveToJson(writtenJsonObject);
		return writtenJsonObject;
	}
	
	public default void saveToJson(JsonElement jsonElement) {
		saveToJson(jsonElement.getAsJsonObject());
	}
	
	public default void loadFromJson(JsonElement jsonElement) {
		loadFromJson(jsonElement.getAsJsonObject());
	}
}
