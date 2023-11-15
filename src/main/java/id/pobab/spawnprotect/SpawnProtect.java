package id.pobab.spawnprotect;

import com.mojang.logging.LogUtils;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

@Mod(SpawnProtect.MODID)
public class SpawnProtect {
    public static final String MODID = "spawnprotect";
    private static final Logger LOGGER = LogUtils.getLogger();

    public SpawnProtect() {
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static class WorldStorage extends SavedData {
        private static final String DATA_NAME = SpawnProtect.MODID + "_SpawnData";
        private boolean exists;
        private Vec3 pos;
        private ResourceKey<Level> dim;
        private double yaw;
        private double pitch;

        public WorldStorage() {
            exists = false;
        }

        public WorldStorage(Vec3 pos, double yaw, double pitch, ResourceKey<Level> dim) {
            this.pos = pos;
            this.yaw = yaw;
            this.pitch = pitch;
            this.dim = dim;
            exists = true;
        }

        public static WorldStorage getOrCreate(Level level) {
            return level.getServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(compoundTag -> {
                if(compoundTag != null && compoundTag.contains("x") && compoundTag.contains("y") && compoundTag.contains("z") && compoundTag.contains("yaw") && compoundTag.contains("pitch") && compoundTag.contains("dimension")) {
                    Vec3 pos = new Vec3(compoundTag.getDouble("x"), compoundTag.getDouble("y"), compoundTag.getDouble("z"));
                    double yaw = compoundTag.getDouble("yaw");
                    double pitch = compoundTag.getDouble("pitch");
                    ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(compoundTag.getString("dimension")));
                    return new WorldStorage(pos, yaw, pitch, dim);
                }
                return null;
            }, WorldStorage::new, DATA_NAME);
        }

        @Override
        public CompoundTag save(CompoundTag compoundTag) {
            if(compoundTag == null) {
                compoundTag = new CompoundTag();
            }
            compoundTag.putDouble("x", pos.x());
            compoundTag.putDouble("y", pos.y());
            compoundTag.putDouble("z", pos.z());
            compoundTag.putDouble("yaw", yaw);
            compoundTag.putDouble("pitch", pitch);
            compoundTag.putString("dimension", dim.location().toString());
            return compoundTag;
        }

        public WorldStorage setSpawn(Vec3 pos, double yaw, double pitch, ResourceKey<Level> dim) {
            this.pos = pos;
            this.yaw = yaw;
            this.pitch = pitch;
            this.dim = dim;
            exists = true;
            this.setDirty();
            return this;
        }

        public boolean spawnExists() {
            return exists;
        }

        public Vec3 getSpawnPos() {
            return pos;
        }

        public double getSpawnYaw() {
            return yaw;
        }

        public double getSpawnPitch() {
            return pitch;
        }

        public ResourceKey<Level> getSpawnLevel() {
            return dim;
        }
    }
}
