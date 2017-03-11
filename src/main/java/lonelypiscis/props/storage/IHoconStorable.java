package lonelypiscis.props.storage;

import java.io.File;
import java.io.IOException;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public interface IHoconStorable {
	/* A class implementing IHoconStorable can write itself to a hocon node. */
	
	public void saveToNode(ConfigurationNode node);
	
	/* A class implementing IStorable can read itself from a json object. */
	
	public void loadFromNode(ConfigurationNode node);
	
	/* */
	
	public default void loadFromFile(File dataIn) {
		ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(dataIn).build();
		try {
			this.loadFromNode(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public default void saveToFile(File dataOut) {
		ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(dataOut).build();
		ConfigurationNode nodeOut = loader.createEmptyNode();
		
		this.saveToNode(nodeOut);
		
		try {
			loader.save(nodeOut);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
