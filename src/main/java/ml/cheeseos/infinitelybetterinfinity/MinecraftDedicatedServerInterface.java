package ml.cheeseos.infinitelybetterinfinity;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface MinecraftDedicatedServerInterface {
    // fantastic names, i know
    void isNetherAllowed(CallbackInfoReturnable<Boolean> ci); // Vanilla method, mixed to always return true, used all over the code for if a dimension ticks
    boolean isNetherActuallyAllowed();  // Mixed in method, returns actual server.properties setting, used only to determine if an entity can travel to the nether
}
