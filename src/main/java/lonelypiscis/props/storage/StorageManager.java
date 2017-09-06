package lonelypiscis.props.storage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.World;

import com.google.common.collect.Sets;

import lonelypiscis.props.Props;
import lonelypiscis.props.utils.Debug;
import lonelypiscis.props.utils.ResourceUtils;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class StorageManager {

	/*
	 * Elements of {@link #wakeables} are notified when the world is fully
	 * loaded.
	 */

	private ArrayList<IWakeable> wakeables;

	public StorageManager() {
		wakeables = new ArrayList<>();
	}

	public void registerWorldLoadedWakeable(IWakeable wakeable) {
		wakeables.add(wakeable);
	}

	public void checkWorldFiles() {

	}

	public void loadWorldFiles() {
		File file_propEntityManager = ResourceUtils.getAssetFile("worlddata/propEntityManager.conf");

		ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder()
				.setFile(file_propEntityManager).build();

		try {
			ConfigurationNode rootNode = loader.load();
			Props.getProps().getPropEntityManager().loadFromNode(rootNode);
			loader.save(rootNode);

			System.out.println("Loaded from " + file_propEntityManager.getAbsolutePath());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveWorldFiles() {
		saveWorldFile(getWorldDirectories().get(0));

	}

	public void saveWorldFile(File worldDir) {
		File file_propEntityManager = ResourceUtils.getAssetFile("worlddata/propEntityManager.conf");

		ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder()
				.setFile(file_propEntityManager).build();

		try {
			// ConfigurationNode rootNode = loader.load();

			ConfigurationNode rootNode = loader.createEmptyNode();

			Props.getProps().getPropEntityManager().saveToNode(rootNode);
			loader.save(rootNode);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Determines all world directories (including other dimensions) and puts
	 * them in a list.
	 * 
	 * @return the list of world directories
	 */

	private List<File> getWorldDirectories() {
		ArrayList<File> dirs = new ArrayList<>();
		Path savesDir = Sponge.getGame().getGameDirectory();

		for (World world : Sponge.getServer().getWorlds()) {
			File worldDir = savesDir.resolve(world.getName()).toFile();
			dirs.add(worldDir);
		}

		return dirs;
	}
}
