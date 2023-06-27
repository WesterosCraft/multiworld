package me.isaiah.multiworld.command;

import java.util.Random;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import me.isaiah.multiworld.MultiworldMod;

public class CreateCommand {

    public static int run(MinecraftServer mc, ServerCommandSource source, String[] args) {
        if (args.length < 3) {
            source.sendFeedback(new LiteralText("Usage: /mv create <id> <env>"), false);
            return 0;
        }

        
        RegistryKey<DimensionType> dim = null;
        Random r = new Random();
        long seed = r.nextInt();

        ChunkGenerator gen = null;
        if (args[2].contains("NORMAL")) {
            gen = mc.getWorld(World.OVERWORLD).getChunkManager().getChunkGenerator().withSeed(seed);
            dim = DimensionType.OVERWORLD_REGISTRY_KEY;
        }
        else if (args[2].contains("NETHER")) {
            gen = mc.getWorld(World.NETHER).getChunkManager().getChunkGenerator();
            dim = DimensionType.THE_NETHER_REGISTRY_KEY;
        }
        else if (args[2].contains("END")) {
            gen = mc.getWorld(World.END).getChunkManager().getChunkGenerator().withSeed(seed);
            dim = DimensionType.THE_END_REGISTRY_KEY;
        }
        else {
            source.sendFeedback(new LiteralText("Invalid world type " + args[2]), false);
            return 0;
        }
        
        String arg1 = args[1];
        if (arg1.indexOf(':') == -1) arg1 = "multiworld:" + arg1;
        
        MultiworldMod.create_world(arg1, dim, gen, Difficulty.NORMAL);

        source.sendFeedback(new LiteralText("Created world with id: " + args[1]).formatted(Formatting.GREEN), false);
        
        return 1;
    }

}