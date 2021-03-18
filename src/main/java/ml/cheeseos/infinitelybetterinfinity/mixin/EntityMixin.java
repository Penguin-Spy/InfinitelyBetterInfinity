package ml.cheeseos.infinitelybetterinfinity.mixin;

import ml.cheeseos.infinitelybetterinfinity.EntityInterface;
import net.minecraft.entity.Entity;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityInterface {
    @Shadow
    DimensionType dimension;

    DimensionType previousDimension;

    /**
     * Store our current dimension before it gets overwritten when we teleport.
     * This is used by the PortalForcerMixin to make the portals 2-way.
     * This does NOT use field_24306 of Entity because that is the ID of the dimension we're going to (which isn't useful)
     * @param ci mixins make me put this otherwise they get upset.
     */
    //@Inject(method="tickNetherPortal()V", at= @At(value="INVOKE", target="Lnet/minecraft/entity/Entity;changeDimension(Lnet/minecraft/world/dimension/DimensionType;)Lnet/minecraft/entity/Entity;"))
    @Inject(method="setInNetherPortal(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V", at = @At(value="INVOKE", target = "Lnet/minecraft/block/entity/NetherPortalBlockEntity;getDimension()I"))
    private void mixin(CallbackInfo ci) {
        this.previousDimension = this.dimension;
    }

    /**
     * This is what the PortalForcerMixin actually calls to get the previous dimension we were in.
     * @return The ID of the last dimension this entity was in.
     */
    @Override
    public int getPreviousDimensionID() {
        return this.previousDimension.getRawId() + 1;
    }
}
