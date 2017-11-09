package de.canitzp.carz.asm;

import de.canitzp.carz.api.EntityRenderedBase;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.apache.commons.io.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import scala.reflect.NameTransformer;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author canitzp
 */
public class CarzEntityLightTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if("net.minecraft.world.chunk.Chunk".equals(transformedName)){
            ClassReader cr = new ClassReader(basicClass);
            ClassNode cn = new ClassNode();
            cr.accept(cn, 0);
            boolean stopInserting = false;
            for(MethodNode method : cn.methods){
                if(method.desc.equals("(Lnet/minecraft/world/EnumSkyBlock;Lnet/minecraft/util/math/BlockPos;)I")){
                    InsnList insnList = new InsnList();
                    for(AbstractInsnNode node : method.instructions.toArray()){
                        if(node.getOpcode() == Opcodes.GETSTATIC){
                            FieldInsnNode fn = (FieldInsnNode) node;
                            if("net/minecraft/world/EnumSkyBlock".equals(fn.owner) && "BLOCK".equals(fn.name)){
                                stopInserting = true;
                            }
                        }
                        if(!stopInserting){
                            insnList.add(node);
                        } else if(node.getOpcode() == Opcodes.IRETURN){
                            insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                            insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                            insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/chunk/Chunk", "world", "Lnet/minecraft/world/World;"));
                            insnList.add(new VarInsnNode(Opcodes.ALOAD, 2));
                            insnList.add(new VarInsnNode(Opcodes.ALOAD, 6));
                            insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, this.getClass().getName().replace(".", "/"), "call", "(Lnet/minecraft/world/EnumSkyBlock;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;)I", false));
                            insnList.add(node); // add the old IReturn
                            stopInserting = false;
                        }
                    }
                }
            }
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            cn.accept(writer);
            return writer.toByteArray();
        }
        return basicClass;
    }

    public static int call(EnumSkyBlock type, World world, BlockPos pos, ExtendedBlockStorage storage){
        if(type == EnumSkyBlock.BLOCK){
            int blockLight = storage.getBlockLight(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15);
            List<EntityRenderedBase> cars = world.getEntitiesWithinAABB(EntityRenderedBase.class, new AxisAlignedBB(pos).expand(64, 64, 64));
            if(!cars.isEmpty()){
                for(EntityRenderedBase car : cars){
                    blockLight = Math.max(car.getLightAt(world, pos), blockLight);
                }
            }
            return blockLight;
        } else {
            return type.defaultLightValue;
        }
    }

}
