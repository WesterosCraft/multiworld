package me.isaiah.multiworld.command;

import java.io.File;
import java.io.IOException;

import me.isaiah.multiworld.config.FileConfiguration;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SetspawnCommand {

    public static int run(MinecraftServer mc, ServerPlayerEntity plr, String[] args) {
        World w = plr.getWorld();
        Vec3d pos = plr.getPos();
        double py = plr.getYaw();
        double pp = plr.getPitch();
        try {
            setSpawn(w, pos, py, pp);
            plr.sendMessage(new LiteralText("Spawn for world \"" + w.getRegistryKey().getValue() + "\" changed to " 
                    + pos.toString()).formatted(Formatting.GOLD), false);
        } catch (IOException e) {
            plr.sendMessage(new LiteralText("Error: " + e.getMessage()), false);
            e.printStackTrace();
        }
        return 1;
    }

    public static void setSpawn(World w, Vec3d spawn, double yaw, double pitch) throws IOException {
        File config_dir = new File("config");
        config_dir.mkdirs();
        
        File cf = new File(config_dir, "multiworld"); 
        cf.mkdirs();

        File worlds = new File(cf, "worlds");
        worlds.mkdirs();

        Identifier id = w.getRegistryKey().getValue();
        File namespace = new File(worlds, id.getNamespace());
        namespace.mkdirs();

        File wc = new File(namespace, id.getPath() + ".yml");
        wc.createNewFile();
        FileConfiguration config = new FileConfiguration(wc);

        config.set("spawnX", spawn.x);
        config.set("spawnY", spawn.y);
        config.set("spawnZ", spawn.z);
        config.set("spawnYaw", yaw);
        config.set("spawnPitch", pitch);
        config.save();
    }


}