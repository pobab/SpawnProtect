package id.pobab.spawnprotect.events;

import com.mojang.logging.LogUtils;
import id.pobab.spawnprotect.SpawnProtect;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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
        int x = event.getPos().getX();
        int z = event.getPos().getZ();
        if (!(x <= range && z <= range && x >= -range && z >= -range)) return;
        if (event.getPlayer().hasPermissions(2)) return;
        event.getPlayer().sendSystemMessage(Component.nullToEmpty(noAccess));
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerPlacesBlock(BlockEvent.EntityPlaceEvent event) {
        int x = event.getPos().getX();
        int z = event.getPos().getZ();
        if (!(x <= range && z <= range && x >= -range && z >= -range)) return;
        if (event.getEntity() instanceof Player player) {
            if (player.hasPermissions(2)) return;
            player.sendSystemMessage(Component.nullToEmpty(noAccess));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onExplosion(ExplosionEvent.Detonate event) {
        Iterator<BlockPos> iterator = event.getAffectedBlocks().iterator();
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
        double x = event.getEntity().getX();
        double z = event.getEntity().getZ();
        if (!(x <= range && z <= range && x >= -range && z >= -range)) return;
        if (event.getEntity().hasPermissions(2)) return;
        event.getEntity().sendSystemMessage(Component.nullToEmpty(noAccess));
        event.setCanceled(true);
    }

}
