package de.canitzp.carz.api;

import de.canitzp.carz.CarzStats;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;

/**
 * DamageSource for getting run over by a car
 * @author MisterErwin
 */
public class EntityDamageSourceCared extends EntityDamageSourceIndirect {
    public EntityDamageSourceCared( String damageTypeIn, Entity source, @Nullable Entity indirectEntityIn) {
        super(damageTypeIn, source, indirectEntityIn);
        //death.attack.car.hitby=%1 was ....
    }

    @Override
    @MethodsReturnNonnullByDefault
    public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
        ITextComponent itextcomponent = this.getTrueSource() == null ? this.damageSourceEntity.getDisplayName() : this.getTrueSource().getDisplayName();
        return  new TextComponentTranslation("death.attack." + this.damageType,
                entityLivingBaseIn.getDisplayName(), itextcomponent);
    }

    public static EntityDamageSourceCared causeDamageAndStat(Entity source, Entity transmitter, int dmg) {
        if (source instanceof EntityPlayer){
            ((EntityPlayer) source).addStat(CarzStats.ENTITY_HIT_COUNT);
            ((EntityPlayer) source).addStat(CarzStats.ENTITY_HIT_DAMAGE, dmg);
        }
        return causeDamage(source, transmitter);
    }

    public static EntityDamageSourceCared causeDamage(Entity source, Entity transmitter) {
        return new EntityDamageSourceCared("car.hitby", transmitter, source);
    }
}
