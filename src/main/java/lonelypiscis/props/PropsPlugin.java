package lonelypiscis.props;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import com.google.inject.Inject;

import lonelypiscis.props.command.CommandSpawnProp;
import lonelypiscis.props.event.PropEntityListener;

@Plugin(id = "props", name = "Props Plugin", version = "0.1")
public class PropsPlugin {
	
	@Inject
	private static Logger propsLogger;
	
	@Listener
    public void onServerStart(GameStartedServerEvent event) {
		Props.instatiate();
		
		registerListeners();
		registerCommands();
		
		getLogger().debug("Test");
    }
	
	public void registerListeners() {
		Sponge.getEventManager().registerListeners(this, new PropEntityListener());
	}
	
	public void registerCommands() {
		CommandManager cmdManager = Sponge.getCommandManager();
		cmdManager.register(this, new CommandSpawnProp(), "spawnProp");
	}
	
	@Inject
	private void setLogger(Logger logger) {
		propsLogger = logger;
	}
	
	public static Logger getLogger() {
		return propsLogger;
	}
}
