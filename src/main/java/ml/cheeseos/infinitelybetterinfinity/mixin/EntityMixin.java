package ml.cheeseos.infinitelybetterinfinity.mixin;

import ml.cheeseos.infinitelybetterinfinity.EntityInterface;
import ml.cheeseos.infinitelybetterinfinity.MinecraftDedicatedServerInterface;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityInterface {
    // yay shadows!
    @Shadow
    DimensionType dimension;
    @Shadow
    World world;
    @Shadow
    int getMaxNetherPortalTime() {
        return -1;
    }
    @Shadow
    boolean inNetherPortal;
    @Shadow
    boolean hasVehicle() {
        return false;
    }
    @Shadow
    int netherPortalTime;
    @Shadow
    int netherPortalCooldown;
    @Shadow
    int getDefaultNetherPortalCooldown() {
        return -1;
    }
    @Shadow
    int field_23406;
    @Shadow
    public Entity changeDimension(DimensionType newDimension) {
        return null;
    }
    @Shadow
    void tickNetherPortalCooldown() {

    }

    // dude imagine not being a @Shadow, kinda cring tbh
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
     * @author Penguin_Spy
     * @reason idk a better way to do this tbh. i have to remove the check for isNetherAllowed() in one if statement, then add it to a different one, and @Inject can't do that (that i know of).
     */
    @Overwrite
    public void tickNetherPortal() {
        if (this.world instanceof ServerWorld) {
            int i = this.getMaxNetherPortalTime();
            if (this.inNetherPortal) {
                if (!this.hasVehicle() && this.netherPortalTime++ >= i) {   // CHANGE removed "this.world.getServer().isNetherAllowed() && "
                    this.world.getProfiler().push("portal");
                    this.netherPortalTime = i;
                    this.netherPortalCooldown = this.getDefaultNetherPortalCooldown();
                    if (this.field_23406 != 0) {
                        DimensionType dimensionType = (DimensionType) Registry.DIMENSION_TYPE.get(this.field_23406);
                        this.changeDimension(dimensionType);
                        this.field_23406 = 0;
                    } else {
                        boolean bl = Registry.DIMENSION_TYPE.getId(this.world.dimension.getType()).getNamespace().equals("_generated");
                        if (bl) {
                            this.changeDimension(DimensionType.OVERWORLD);
                        } else if(((MinecraftDedicatedServerInterface)this.world.getServer()).isNetherActuallyAllowed()) { // CHANGE added "if(...) "
                            this.changeDimension(this.world.dimension.getType() == DimensionType.THE_NETHER ? DimensionType.OVERWORLD : DimensionType.THE_NETHER);
                        }
                    }

                    this.world.getProfiler().pop();
                }

                this.inNetherPortal = false;
            } else {
                if (this.netherPortalTime > 0) {
                    this.netherPortalTime -= 4;
                }

                if (this.netherPortalTime < 0) {
                    this.netherPortalTime = 0;
                }
            }

            this.tickNetherPortalCooldown();
        }
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
