package lonelypiscis.props.storage;

import ninja.leaping.configurate.ConfigurationNode;

public interface IHoconStorable {
	/* A class implementing IHoconStorable can write itself to a hocon node. */
	
	public void saveToNode(ConfigurationNode node);
	
	/* A class implementing IStorable can read itself from a json object. */
	
	public void loadFromNode(ConfigurationNode node);
}
