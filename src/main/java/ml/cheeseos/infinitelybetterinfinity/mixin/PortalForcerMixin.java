package ml.cheeseos.infinitelybetterinfinity.mixin;

import ml.cheeseos.infinitelybetterinfinity.EntityInterface;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.entity.NetherPortalBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.PortalForcer;
import net.minecraft.world.poi.PointOfInterestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;
import java.util.Random;

@Mixin(PortalForcer.class)
public class PortalForcerMixin {
    @Shadow
    ServerWorld world;
    @Shadow
    Random random;

    /**
     * @author Penguin_Spy
     * @reason this is a terrible idea but also this code will never change because its only for 1 update, so yeet!
     *  See the code below for why this isn't an @Inject.
     * @param entity aww, thanks minecraft! that's real nice of you to conveniently include the entity creating the portal!
     *               I honestly don't know what I would have done otherwise! :)
     * @return great question.
     */
    @Overwrite
    public boolean createPortal(Entity entity) {
        double d = -1.0D;
        int j = MathHelper.floor(entity.getX());
        int k = MathHelper.floor(entity.getY());
        int l = MathHelper.floor(entity.getZ());
        int m = j;
        int n = k;
        int o = l;
        int p = 0;
        int q = this.random.nextInt(4);
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        int ad;
        double ae;
        int af;
        double ag;
        int ah;
        int ai;
        int aj;
        int ak;
        int al;
        int am;
        int an;
        int ao;
        int ap;
        double aq;
        double ar;
        for(ad = j - 16; ad <= j + 16; ++ad) {
            ae = (double)ad + 0.5D - entity.getX();

            for(af = l - 16; af <= l + 16; ++af) {
                ag = (double)af + 0.5D - entity.getZ();

                label276:
                for(ah = this.world.getDimensionHeight() - 1; ah >= 0; --ah) {
                    if (this.world.isAir(mutable.set(ad, ah, af))) {
                        while(ah > 0 && this.world.isAir(mutable.set(ad, ah - 1, af))) {
                            --ah;
                        }

                        for(ai = q; ai < q + 4; ++ai) {
                            aj = ai % 2;
                            ak = 1 - aj;
                            if (ai % 4 >= 2) {
                                aj = -aj;
                                ak = -ak;
                            }

                            for(al = 0; al < 3; ++al) {
                                for(am = 0; am < 4; ++am) {
                                    for(an = -1; an < 4; ++an) {
                                        ao = ad + (am - 1) * aj + al * ak;
                                        ap = ah + an;
                                        int ac = af + (am - 1) * ak - al * aj;
                                        mutable.set(ao, ap, ac);
                                        if (an < 0 && !this.world.getBlockState(mutable).getMaterial().isSolid() || an >= 0 && !this.world.isAir(mutable)) {
                                            continue label276;
                                        }
                                    }
                                }
                            }

                            aq = (double)ah + 0.5D - entity.getY();
                            ar = ae * ae + aq * aq + ag * ag;
                            if (d < 0.0D || ar < d) {
                                d = ar;
                                m = ad;
                                n = ah;
                                o = af;
                                p = ai % 4;
                            }
                        }
                    }
                }
            }
        }

        if (d < 0.0D) {
            for(ad = j - 16; ad <= j + 16; ++ad) {
                ae = (double)ad + 0.5D - entity.getX();

                for(af = l - 16; af <= l + 16; ++af) {
                    ag = (double)af + 0.5D - entity.getZ();

                    label214:
                    for(ah = this.world.getDimensionHeight() - 1; ah >= 0; --ah) {
                        if (this.world.isAir(mutable.set(ad, ah, af))) {
                            while(ah > 0 && this.world.isAir(mutable.set(ad, ah - 1, af))) {
                                --ah;
                            }

                            for(ai = q; ai < q + 2; ++ai) {
                                aj = ai % 2;
                                ak = 1 - aj;

                                for(al = 0; al < 4; ++al) {
                                    for(am = -1; am < 4; ++am) {
                                        an = ad + (al - 1) * aj;
                                        ao = ah + am;
                                        ap = af + (al - 1) * ak;
                                        mutable.set(an, ao, ap);
                                        if (am < 0 && !this.world.getBlockState(mutable).getMaterial().isSolid() || am >= 0 && !this.world.isAir(mutable)) {
                                            continue label214;
                                        }
                                    }
                                }

                                aq = (double)ah + 0.5D - entity.getY();
                                ar = ae * ae + aq * aq + ag * ag;
                                if (d < 0.0D || ar < d) {
                                    d = ar;
                                    m = ad;
                                    n = ah;
                                    o = af;
                                    p = ai % 2;
                                }
                            }
                        }
                    }
                }
            }
        }

        int at = m;
        int au = n;
        af = o;
        int aw = p % 2;
        int ax = 1 - aw;
        if (p % 4 >= 2) {
            aw = -aw;
            ax = -ax;
        }

        if (d < 0.0D) {
            n = MathHelper.clamp(n, 70, this.world.getDimensionHeight() - 10);
            au = n;

            for(ah = -1; ah <= 1; ++ah) {
                for(ai = 1; ai < 3; ++ai) {
                    for(aj = -1; aj < 3; ++aj) {
                        ak = at + (ai - 1) * aw + ah * ax;
                        al = au + aj;
                        am = af + (ai - 1) * ax - ah * aw;
                        boolean bl = aj < 0;
                        mutable.set(ak, al, am);
                        this.world.setBlockState(mutable, bl ? Blocks.OBSIDIAN.getDefaultState() : Blocks.AIR.getDefaultState());
                    }
                }
            }
        }

        for(ah = -1; ah < 3; ++ah) {
            for(ai = -1; ai < 4; ++ai) {
                if (ah == -1 || ah == 2 || ai == -1 || ai == 3) {
                    mutable.set(at + ah * aw, au + ai, af + ah * ax);
                    this.world.setBlockState(mutable, Blocks.OBSIDIAN.getDefaultState(), 3);
                }
            }
        }

        // Oh also this changes and i have no ғʀᴇᴀᴋɪɴɢ idea how i would change it from NETHER_PORTAL
        BlockState blockState = Blocks.NEITHER_PORTAL.getDefaultState().with(NetherPortalBlock.AXIS, aw == 0 ? Direction.Axis.Z : Direction.Axis.X);

        for(ai = 0; ai < 2; ++ai) {
            for(aj = 0; aj < 3; ++aj) {
                mutable.set(at + ai * aw, au + aj, af + ai * ax);
                this.world.setBlockState(mutable, blockState, 18);
                // START of addition
                // this is an @Overwrite because i have no clue how I would use the local variables (mutable & entity) otherwise
                NetherPortalBlockEntity neitherPortal = (NetherPortalBlockEntity) this.world.getBlockEntity(mutable);
                if(neitherPortal == null) {
                    this.world.setBlockEntity(mutable, new NetherPortalBlockEntity(((EntityInterface)entity).getPreviousDimensionID()));
                } else {
                    neitherPortal.setDimension(((EntityInterface) entity).getPreviousDimensionID());
                }
                // END of addition
                Optional<PointOfInterestType> optional = PointOfInterestType.from(this.world.getBlockState(mutable));
                optional.ifPresent((pointOfInterestType) -> {
                    this.world.getPointOfInterestStorage().add(mutable, pointOfInterestType);
                    DebugInfoSender.sendPoiAddition(this.world, mutable);
                });
            }
        }

        return true;
    }
}
