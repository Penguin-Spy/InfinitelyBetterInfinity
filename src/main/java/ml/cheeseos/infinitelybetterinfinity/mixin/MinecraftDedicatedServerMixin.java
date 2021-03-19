package ml.cheeseos.infinitelybetterinfinity.mixin;

import ml.cheeseos.infinitelybetterinfinity.MinecraftDedicatedServerInterface;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.dedicated.ServerPropertiesHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftDedicatedServer.class)
public class MinecraftDedicatedServerMixin implements MinecraftDedicatedServerInterface {
    @Shadow
    public ServerPropertiesHandler getProperties() {
        return null;
    }

    @Inject(method="isNetherAllowed()Z", at=@At("HEAD"), cancellable = true)
    public void isNetherAllowed(CallbackInfoReturnable<Boolean> ci) {
        ci.setReturnValue(true);
    }

    public boolean isNetherActuallyAllowed() {
        return this.getProperties().allowNether;
    }
}
