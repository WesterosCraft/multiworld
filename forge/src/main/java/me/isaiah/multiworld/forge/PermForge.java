package me.isaiah.multiworld.forge;

import net.minecraft.server.network.ServerPlayerEntity;
import me.isaiah.multiworld.MultiworldMod;
import me.isaiah.multiworld.perm.Perm;
import net.minecraftforge.fml.OptionalMod;

public class PermForge extends Perm {

    public static void init() {
        Perm.setPerm(new PermForge());
    }

    private boolean cyber;
    private boolean luck;
    
    public PermForge() {
        luck = OptionalMod.of("luckperms").isPresent();
        if (!luck) {
        	cyber = OptionalMod.of("cyberpermissions").isPresent();
        	if (cyber) {
        		MultiworldMod.log.info("CyberPermissions handling multiworld perms");
        	}
        }
        else {
        	MultiworldMod.log.info("LuckPerms handling multiworld perms");
        }
    	
    }
    @Override
    public boolean has_impl(ServerPlayerEntity plr, String perm) {
        boolean res = plr.hasPermissionLevel(2);

        if (luck) {
        	if (LuckPermsHandler.hasPermission(plr, perm)) res = true;
        }
        else if (cyber) {
            if (CyberHandler.hasPermission(plr, perm)) res = true;        	
        }

        return res;
    }

}