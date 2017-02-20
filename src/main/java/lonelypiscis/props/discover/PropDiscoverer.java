package lonelypiscis.props.discover;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.spongepowered.api.Sponge;

import lonelypiscis.props.utils.Debug;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class PropDiscoverer {
	public static Path discoveryFilePath = Paths.get(Sponge.getAssetManager().getAsset("discovery.conf").get().getUrl().toString());
	ConfigurationLoader<CommentedConfigurationNode> loader;
	
	public PropDiscoverer() {
		
	}
	
	public void discover() {
		Debug.getLogger().info("DiscoveryFilePath = " + discoveryFilePath);
		
		loader = HoconConfigurationLoader.builder().setPath(discoveryFilePath).build();
		ConfigurationNode rootNode = loader.createEmptyNode(ConfigurationOptions.defaults());
		
		try {
		    rootNode = loader.load();
		} catch(IOException e) {
		    e.printStackTrace();
		}
		
		boolean shouldDiscover = rootNode.getNode("discover", "autodiscover").getBoolean();
		
		System.out.println(shouldDiscover);
	}
}
