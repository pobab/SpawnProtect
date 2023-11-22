package id.pobab.spawnprotect.events;

import com.mojang.logging.LogUtils;
import id.pobab.spawnprotect.SpawnProtect;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = SpawnProtect.MODID)
public class EventPlayerAttack {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onPlayerBreaksBlock(AttackEntityEvent event) {
        String[] target = event.getTarget().getType().toString().split("\\.");
        if (target[2].equals("pixelmon")) return;
        if (!EventProtection.inSpawn(event.getEntity())) return;
        event.setCanceled(true);
    }
}
