package net.celestialgaze.IkuBot.command.input;

import net.dv8tion.jda.internal.utils.tuple.Pair;

public abstract class TypeProcessor {
	/**
	 * @return The range from which a match was found for this type.
	 */
	public abstract Pair<Integer, Integer> findFirst(String[] args);
	
	/**
	 * @param str String to parse
	 * @return This parsed type
	 */
	public abstract Object parse(String str);
	
	/**
	 * @param str String to check
	 * @return Whether or not the string parses to this type
	 */
	public abstract boolean matches(String str);
}
