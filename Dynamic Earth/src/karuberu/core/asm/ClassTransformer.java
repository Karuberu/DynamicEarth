package karuberu.core.asm;

import java.io.DataInputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import karuberu.core.KaruberuLogger;
import karuberu.core.event.EventFactory;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import cpw.mods.fml.relauncher.IClassTransformer;

public class ClassTransformer implements IClassTransformer, Opcodes {
	
	@Override
	public byte[] transform(String name, byte[] bytes) {
		System.out.println(name);
		if (ObfHelper.ObfName.WorldServer.equalsIgnoreCase(name.replace('.', '/'))) {
			KaruberuLogger.fine("Starting injection process for block update hooks.");
			return insertBlockUpdateHooks(bytes);
		}
		return bytes;
	}

	private byte[] insertBlockUpdateHooks(byte[] bytes) {
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);
		
		final String
			tickUpdatesDescriptor = Type.getMethodDescriptor(Type.BOOLEAN_TYPE, Type.BOOLEAN_TYPE),
			tickBlocksAndAmbianceDescriptor = "()V",
			updateTickDescriptor = Type.getMethodDescriptor(Type.VOID_TYPE, Type.getObjectType(ObfHelper.ObfName.World.toString()), Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE, Type.getObjectType("java/util/Random")),
			blocksListDescriptor = "[" + Type.getObjectType(ObfHelper.ObfName.Block.toString()).getDescriptor(),
			getYLocationDescriptor = "()I",
			hookClassName = Type.getInternalName(EventFactory.class),
			hookMethodName = "onBlockUpdate",
			hookDescriptor = Type.getMethodDescriptor(Type.BOOLEAN_TYPE, Type.getObjectType(ObfHelper.ObfName.World.toString()), Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE, Type.BOOLEAN_TYPE);
		
		Iterator methods = classNode.methods.iterator();
		boolean otherComplete = false;
		while (methods.hasNext()) {
			int start = -1;
			MethodNode method = (MethodNode)methods.next();
			if (ObfHelper.ObfName.tickUpdates.equalsIgnoreCase(method.name)
			&& tickUpdatesDescriptor.equalsIgnoreCase(method.desc)) {
				KaruberuLogger.finer("Found method " + method.name + method.desc + " in class " + ObfHelper.ObfName.WorldServer);
				for (int i = 0; i < method.instructions.size(); i++) {
					AbstractInsnNode instruction = method.instructions.get(i);
					if (instruction.getType() == AbstractInsnNode.FIELD_INSN
					&& instruction.getOpcode() == Opcodes.GETSTATIC
					&& ObfHelper.ObfName.blocksList.equalsIgnoreCase(((FieldInsnNode)instruction).name)
					&& blocksListDescriptor.equalsIgnoreCase(((FieldInsnNode)instruction).desc)) {
						KaruberuLogger.finer("Landmark found. Searching for second landmark.");
						start = i;
						break;
					}
				}
				if (start > -1) {
					for (int i = start + 1; i < method.instructions.size(); i++) {
						AbstractInsnNode instruction = method.instructions.get(i);
						if (instruction.getType() == AbstractInsnNode.METHOD_INSN
						&& instruction.getOpcode() == Opcodes.INVOKEVIRTUAL
						&& ObfHelper.ObfName.updateTick.equalsIgnoreCase(((MethodInsnNode)instruction).name)
						&& updateTickDescriptor.equalsIgnoreCase(((MethodInsnNode)instruction).desc)) {
							KaruberuLogger.finer("Second landmark found. Injecting block update hook.");
	
							InsnList hookStart = new InsnList();
							final int nextTickListEntryVarRegister = 4;
							hookStart.add(new VarInsnNode(Opcodes.ALOAD, 0));
							hookStart.add(new VarInsnNode(Opcodes.ALOAD, nextTickListEntryVarRegister));
							hookStart.add(new FieldInsnNode(Opcodes.GETFIELD, ObfHelper.ObfName.NextTickListEntry.toString(), ObfHelper.ObfName.xCoord.toString(), Type.INT_TYPE.getDescriptor()));
							hookStart.add(new VarInsnNode(Opcodes.ALOAD, nextTickListEntryVarRegister));
							hookStart.add(new FieldInsnNode(Opcodes.GETFIELD, ObfHelper.ObfName.NextTickListEntry.toString(), ObfHelper.ObfName.yCoord.toString(), Type.INT_TYPE.getDescriptor()));
							hookStart.add(new VarInsnNode(Opcodes.ALOAD, nextTickListEntryVarRegister));
							hookStart.add(new FieldInsnNode(Opcodes.GETFIELD, ObfHelper.ObfName.NextTickListEntry.toString(), ObfHelper.ObfName.zCoord.toString(), Type.INT_TYPE.getDescriptor()));
							hookStart.add(new InsnNode(Opcodes.ICONST_0)); // false
							hookStart.add(new MethodInsnNode(Opcodes.INVOKESTATIC, hookClassName, hookMethodName, hookDescriptor));
							LabelNode endif = new LabelNode(new Label());
							hookStart.add(new JumpInsnNode(Opcodes.IFNE, endif));
							
							InsnList hookEnd = new InsnList();
							hookEnd.add(endif);
							
							AbstractInsnNode startingNode = method.instructions.get(start);
							AbstractInsnNode endingNode = method.instructions.get(i);
							
							method.instructions.insert(endingNode, hookEnd);
							method.instructions.insertBefore(startingNode, hookStart);
							break;
						}
					}
				}
				if (!otherComplete) {
					otherComplete = true;
				} else {
					break;
				}
			}
			if (ObfHelper.ObfName.tickBlocksAndAmbiance.equalsIgnoreCase(method.name)
			&& tickBlocksAndAmbianceDescriptor.equalsIgnoreCase(method.desc)) {
				KaruberuLogger.finer("Found method " + method.name + method.desc + " in class " + ObfHelper.ObfName.WorldServer);
				for (int i = 0; i < method.instructions.size(); i++) {
					AbstractInsnNode instruction = method.instructions.get(i);
					if (instruction.getType() == AbstractInsnNode.VAR_INSN
					&& instruction.getOpcode() == Opcodes.ALOAD
					&& ((VarInsnNode)instruction).var == 22) {
						KaruberuLogger.finer("Landmark found. Searching for second landmark.");
						start = i;
						break;
					}
				}
				if (start > -1) {
					for (int i = start + 1; i < method.instructions.size(); i++) {
						AbstractInsnNode instruction = method.instructions.get(i);
						if (instruction.getType() == AbstractInsnNode.METHOD_INSN
						&& instruction.getOpcode() == Opcodes.INVOKEVIRTUAL
						&& ObfHelper.ObfName.updateTick.equalsIgnoreCase(((MethodInsnNode)instruction).name)
						&& updateTickDescriptor.equalsIgnoreCase(((MethodInsnNode)instruction).desc)) {
							KaruberuLogger.finer("Second landmark found. Injecting block update hook.");
	
							InsnList hookStart = new InsnList();
							hookStart.add(new VarInsnNode(Opcodes.ALOAD, 0));
							hookStart.add(new VarInsnNode(Opcodes.ILOAD, 18));
							hookStart.add(new VarInsnNode(Opcodes.ILOAD, 7));
							hookStart.add(new InsnNode(Opcodes.IADD));
							hookStart.add(new VarInsnNode(Opcodes.ILOAD, 20));
							hookStart.add(new VarInsnNode(Opcodes.ALOAD, 16));
							hookStart.add(new MethodInsnNode(INVOKEVIRTUAL, ObfHelper.ObfName.ExtendedBlockStorage.toString(), ObfHelper.ObfName.getYLocation.toString(), getYLocationDescriptor));
							hookStart.add(new InsnNode(Opcodes.IADD));
							hookStart.add(new VarInsnNode(Opcodes.ILOAD, 19));
							hookStart.add(new VarInsnNode(Opcodes.ILOAD, 8));
							hookStart.add(new InsnNode(Opcodes.IADD));
							hookStart.add(new InsnNode(Opcodes.ICONST_1)); // true
							hookStart.add(new MethodInsnNode(Opcodes.INVOKESTATIC, hookClassName, hookMethodName, hookDescriptor));
							LabelNode endif = new LabelNode(new Label());
							hookStart.add(new JumpInsnNode(Opcodes.IFNE, endif));

							InsnList hookEnd = new InsnList();
							hookEnd.add(endif);
							
							AbstractInsnNode startingNode = method.instructions.get(start);
							AbstractInsnNode endingNode = method.instructions.get(i);
							
							method.instructions.insert(endingNode, hookEnd);
							method.instructions.insertBefore(startingNode, hookStart);
							break;
						}
					}
				}
				if (!otherComplete) {
					otherComplete = true;
				} else {
					break;
				}
			}
		}
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		return writer.toByteArray();
	}
}
