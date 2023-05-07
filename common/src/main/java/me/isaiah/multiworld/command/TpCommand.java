package me.isaiah.multiworld.command;

import java.util.HashMap;

import dimapi.FabricDimensionInternals;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

public class TpCommand {

    public static int run(MinecraftServer mc, ServerPlayerEntity plr, String[] args) {
        HashMap<String,ServerWorld> worlds = new HashMap<>();
        mc.getWorldRegistryKeys().forEach(r -> {
            ServerWorld world = mc.getWorld(r);
            worlds.put(r.getValue().toString(), world);
        });
        
        String arg1 = args[1];
        if (arg1.indexOf(':') == -1) arg1 = "multiworld:" + arg1;

        if (worlds.containsKey(arg1)) {
            ServerWorld w = worlds.get(arg1);

            TeleportTarget target = SpawnCommand.getSpawn(w);
            plr.sendMessage(new LiteralText("Telelporting...").formatted(Formatting.GOLD), false);
            
            FabricDimensionInternals.changeDimension(plr, w, target);
            return 1;
        }
        return 1;
    }
}
