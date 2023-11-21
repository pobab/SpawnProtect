package id.pobab.spawnprotect.events;

import com.mojang.logging.LogUtils;
import id.pobab.spawnprotect.SpawnProtect;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = SpawnProtect.MODID)
public class EventPlayerAttack {
    private static final Logger LOGGER = LogUtils.getLogger();
    static int range = 300;

    @SubscribeEvent
    public static void onPlayerBreaksBlock(AttackEntityEvent event) {
        String[] target = event.getTarget().getType().toString().split("\\.");
        if (target[2].equals("pixelmon")) return;
        Player player = event.getEntity();
        int x = player.getBlockX();
        int z = player.getBlockZ();
        if (!(x <= range && z <= range && x >= -range && z >= -range)) return;
        event.setCanceled(true);
    }
}
