package lonelypiscis.props.command;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import lonelypiscis.props.Props;

public class CommandSpawnProp implements CommandCallable {
	
	private final Optional<Text> desc = Optional.of(Text.of("Spawns a prop."));
    private final Optional<Text> help = Optional.of(Text.of("Spawns a prop where the player is looking at."));
    private final Text usage = Text.of("<prop_name>");

	@Override
	public CommandResult process(CommandSource source, String arguments) throws CommandException {
		CommandResult result = CommandResult.success();
		
		if (source instanceof Player) {
			Player player = (Player) source;
			
			Location<World> loc = player.getLocation();
			Props.getProps().getPropEntityManager().spawnPropEntity(loc);
		}
		
		return result;
	}

	@Override
	public List<String> getSuggestions(CommandSource source, String arguments, Location<World> targetPosition)
			throws CommandException {
		return Collections.emptyList();
	}

	@Override
	public boolean testPermission(CommandSource source) {
		return true;
	}

	@Override
	public Optional<Text> getShortDescription(CommandSource source) {
		return desc;
	}

	@Override
	public Optional<Text> getHelp(CommandSource source) {
		return help;
	}

	@Override
	public Text getUsage(CommandSource source) {
		// TODO Auto-generated method stub
		return usage;
	}

}
