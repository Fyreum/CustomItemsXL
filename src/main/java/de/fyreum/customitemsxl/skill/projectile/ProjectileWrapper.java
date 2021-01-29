package de.fyreum.customitemsxl.skill.projectile;

import org.bukkit.entity.Projectile;

import java.util.UUID;

public class ProjectileWrapper<T extends Projectile> implements TaggedProjectile {

    private final String name;
    private final UUID uuid;
    private final Class<T> projectile;

    protected ProjectileWrapper(String name, UUID uuid, Class<T> projectile) {
        this.name = name;
        this.uuid = uuid;
        this.projectile = projectile;
    }

    public String getName() {
        return name;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public Class<T> getProjectile() {
        return projectile;
    }
}
