package com.bxzmod.someusefulthings.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.Arrays;

import static org.objectweb.asm.Opcodes.*;

public class ItemBucketClassTransformer implements IClassTransformer
{
	private static final String[] classesBeingTransformed = { "net.minecraft.item.ItemBucket" };

	@Override
	public byte[] transform(String name, String transformedName, byte[] classBeingTransformed)
	{
		boolean isObfuscated = !name.equals(transformedName);
		int index = Arrays.asList(classesBeingTransformed).indexOf(transformedName);
		return index != -1 ? transform(index, classBeingTransformed, isObfuscated) : classBeingTransformed;
	}

	private static byte[] transform(int index, byte[] classBeingTransformed, boolean isObfuscated)
	{
		System.out.println("Transforming: " + classesBeingTransformed[index]);
		try
		{
			ClassNode classNode = new ClassNode();
			ClassReader classReader = new ClassReader(classBeingTransformed);
			classReader.accept(classNode, 0);

			switch (index)
			{
			case 0:
				transformItembucket(classNode, isObfuscated);
				break;
			}

			ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
			classNode.accept(classWriter);
			return classWriter.toByteArray();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return classBeingTransformed;
	}

	private static void transformItembucket(ClassNode blockCactusClass, boolean isObfuscated)
	{
		final String ENTITY_COLLIDE = isObfuscated ? "a" : "onItemRightClick";
		final String ENTITY_COLLIDE_DESC = isObfuscated ? "(Ladz;Laid;Lzs;Lqr;)Lqu;"
				: "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;)Lnet/minecraft/util/ActionResult;";

		for (MethodNode method : blockCactusClass.methods)
		{
			if (method.name.equals(ENTITY_COLLIDE) && method.desc.equals(ENTITY_COLLIDE_DESC))
			{
				AbstractInsnNode tempNode = null;
				for (AbstractInsnNode instruction : method.instructions.toArray())
				{
					if (instruction.getOpcode() == ALOAD)
					{
						if (((VarInsnNode) instruction).var == 10 && instruction.getNext().getOpcode() == INVOKEVIRTUAL)
						{
							tempNode = instruction;
							break;
						}
					}
				}
				if (tempNode != null)
				{
					AbstractInsnNode targetNode = method.instructions.get(method.instructions.indexOf(tempNode) + 14);
					AbstractInsnNode popNode = targetNode.getNext();
					for (int i = 0; i < 8; i++)
					{
						popNode = popNode.getNext();
						method.instructions.remove(popNode.getPrevious());
					}
					InsnList toInsert = new InsnList();
					toInsert.add(new VarInsnNode(ALOAD, 1));
					toInsert.add(new VarInsnNode(ALOAD, 3));
					toInsert.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HookItemBucket.class),
							"bucketFix",
							isObfuscated ? "(Ladz;Lzs;)Lqu;"
									: "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/util/ActionResult;",
							false));
					method.instructions.insertBefore(popNode, toInsert);
					System.out.println("Success Transformed: " + classesBeingTransformed[0]);

				} else
				{
					System.out.println("Something went wrong transforming ItemBucket!");
				}
			}
		}
	}
}
