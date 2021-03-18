package ml.cheeseos.infinitelybetterinfinity;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InfinitelyBetterInfinity implements ModInitializer {
	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitialize() {
		LOGGER.info("Infinity has been improved infinitely.");
	}
}
