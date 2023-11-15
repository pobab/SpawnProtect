package id.pobab.spawnprotect.events;

import com.mojang.logging.LogUtils;
import id.pobab.spawnprotect.SpawnProtect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = SpawnProtect.MODID)
public class EventPlayerLogin {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void onPlayerFirstLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        ServerPlayer serverPlayer = new ServerPlayer(player.getServer(), player.getServer().getLevel(Level.OVERWORLD), player.getGameProfile());
        LOGGER.info(player.level().toString());
    }
}
