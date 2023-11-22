package id.pobab.spawnprotect.events;

import com.mojang.logging.LogUtils;
import id.pobab.spawnprotect.SpawnProtect;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.util.Iterator;

@Mod.EventBusSubscriber(modid = SpawnProtect.MODID)
public class EventProtection {
    private static final Logger LOGGER = LogUtils.getLogger();
    static String noAccess = ChatFormatting.RED + "Tidak memiliki izin untuk melakukannya";
    static int range = 300;

    @SubscribeEvent
    public static void onPlayerBreaksBlock(BlockEvent.BreakEvent event) {
        if (!inSpawn(event.getPlayer())) return;
//        event.getPlayer().sendSystemMessage(Component.nullToEmpty(noAccess));
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerPlacesBlock(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!inSpawn(player)) return;
//            player.sendSystemMessage(Component.nullToEmpty(noAccess));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onExplosion(ExplosionEvent.Detonate event) {
        Iterator<BlockPos> iterator = event.getAffectedBlocks().iterator();
        if (!event.getLevel().dimension().toString().contains("minecraft:overworld")) return;
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();
            int x = pos.getX();
            int z = pos.getZ();
            if (!(x <= range && z <= range && x >= -range && z >= -range)) return;
            iterator.remove();
        }
    }

    @SubscribeEvent
    public static void onBucketFill(FillBucketEvent event) {
        if (!inSpawn(event.getEntity())) return;
//        event.getEntity().sendSystemMessage(Component.nullToEmpty(noAccess));
        event.setCanceled(true);
    }

    public static Boolean inSpawn(Entity entity) {
        double x = entity.getX();
        double z = entity.getZ();
        if (!(x <= range && z <= range && x >= -range && z >= -range)) return false;
        if (!entity.level().dimension().toString().contains("minecraft:overworld")) return false;
        if (entity.hasPermissions(2)) return false;
        return true;
    }
}
