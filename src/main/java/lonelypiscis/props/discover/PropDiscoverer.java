package lonelypiscis.props.discover;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javax.annotation.Resource;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.property.item.HarvestingProperty;
import org.spongepowered.api.data.property.item.UseLimitProperty;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

import lonelypiscis.props.Props;
import lonelypiscis.props.prop.ModelData;
import lonelypiscis.props.prop.Prop;
import lonelypiscis.props.utils.Debug;
import lonelypiscis.props.utils.ResourceUtils;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

/*
 * The PropDiscoverer scans the prop configurations directory. Found files are checked and then loaded into
 * the Re
 */

public class PropDiscoverer {
	private File propConfigurationsDir;
	
	public PropDiscoverer() {
		propConfigurationsDir = ResourceUtils.getAssetFile("propConfigurations/");
	}
	
	/**
	 * Invokes the discovery process. Discovered files are handled by the {@link #handleDiscovery(Prop) handleDiscovery} method.
	 */
	
	public void discover() {
		for (File fileToScan : scanDirectory(propConfigurationsDir)) {
			Prop discoveredProp = readPropFile(fileToScan);
			handleDiscovery(discoveredProp);
		}
	}
	
	private void handleDiscovery(Prop prop) {
		ModelData loadedModelData = prop.getModelData();
		
		if (loadedModelData.getOverrideTarget() == null) {
			Debug.getLogger().info("Requesting model data...");
			
			// Apply for resource pack
			prop.setModelData(Props.getProps().getResourcePackFactory().requestModelData(loadedModelData.getModelName()));
			// Notify PropRegistry
			Props.getProps().getRegistry().registerDiscoveredProp(prop, false);
		}
	}
	
	/**
	 * Reads and parses a prop configuration file into a Prop object.
	 */
	
	public Prop readPropFile(File fileToScan) {
		Prop propOut = new Prop();
		ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(fileToScan).build();
		
		try {
			ConfigurationNode rootNode = loader.load();
			propOut.loadFromNode(rootNode);
			Debug.getLogger().info("Scanned " + propOut.getId());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return propOut;
	}
	
	/** 
	 * Scans the given directory for suitable prop configuration files. 
	 */
	
	public ArrayList<File> scanDirectory(File scanDirectory) {
		ArrayList<File> filesOut = new ArrayList<>();
		
		for (File foundFile : scanDirectory.listFiles()) {
			// Check the found item's criteria. Only files with an conf extension are allowed
			
			String fileName = foundFile.getName();
			String fileExtension = fileName.substring(fileName.length()-4, fileName.length());
			
			// Is directory?
			if (!foundFile.isDirectory()) {
				// Has propery extension?
				if (fileExtension.equals("conf")) {
					filesOut.add(foundFile);
				}
			}
		}
		
		return filesOut;
	}
}
