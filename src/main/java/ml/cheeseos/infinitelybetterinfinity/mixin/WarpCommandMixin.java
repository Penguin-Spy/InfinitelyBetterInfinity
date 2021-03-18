package ml.cheeseos.infinitelybetterinfinity.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.WarpCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WarpCommand.class)
public class WarpCommandMixin {
    @Shadow
    private static int execute(ServerCommandSource source, String taret) throws CommandSyntaxException {
        return -1;
    }

    /**
     * @author Penguin_Spy
     * Overwrite the registration of the method to apply a requirement for OP level 2 (same as /tp).
     * @reason I honestly have no idea how I would even begin to start creating an @Injection for the mess below, so this is an @Overwrite
     * @param dispatcher the dispatcher, idk
     */
    @Overwrite
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("warp")
            .requires(source -> source.hasPermissionLevel(2))
            .then(CommandManager.argument("target", StringArgumentType.greedyString())
                .executes((commandContext) -> execute(commandContext.getSource(), StringArgumentType.getString(commandContext, "target")))
            )
        );
    }
}
