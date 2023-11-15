package id.pobab.spawnprotect.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import id.pobab.spawnprotect.SpawnProtect;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Predicate;

@Mod(SpawnProtect.MODID)
@Mod.EventBusSubscriber(modid = SpawnProtect.MODID)
public class SpawnCommand {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        onCommand(event.getDispatcher());
    }

    public static void onCommand(CommandDispatcher d) {
        Predicate<CommandSourceStack> isPlayer = source -> {
            try {
                return source.getPlayerOrException() != null;
            } catch(CommandSyntaxException e) {
                return false;
            }
        };

        // spawn
        d.register(Commands.literal("spawn").requires(isPlayer).executes(command -> {
            ServerPlayer player = command.getSource().getPlayerOrException();
            SpawnProtect.WorldStorage data = SpawnProtect.WorldStorage.getOrCreate(player.level());
            if(data != null && !data.spawnExists()) {
                player.sendSystemMessage(Component.literal("No spawn has been set!").withStyle(ChatFormatting.RED));
                return 0;
            } else {
                ServerLevel level = player.level().getServer().getLevel(data.getSpawnLevel());
                Vec3 pos = data.getSpawnPos();
                player.teleportTo(level, pos.x(), pos.y(), pos.z(), (float) data.getSpawnYaw(), (float) data.getSpawnPitch());
            }
            return 1;
        }));

        // setspawn
        d.register(Commands.literal("setspawn").requires(isPlayer).requires(s -> s.hasPermission(3)).executes(command -> {
            ServerPlayer player = command.getSource().getPlayerOrException();
            SpawnProtect.WorldStorage.getOrCreate(player.level()).setSpawn(player.position(), player.getYHeadRot(), player.getXRot(), player.level().dimension());
            player.sendSystemMessage(Component.literal("Spawn set.").withStyle(ChatFormatting.GREEN));
            return 1;
        }));
    }
}
