package net.celestialgaze.IkuBot.command.input;

import net.dv8tion.jda.internal.utils.tuple.Pair;

public class StringProcessor extends TypeProcessor {

	@Override
	public Pair<Integer, Integer> findFirst(String[] args) {
		return Pair.of(0, args.length);
	}

	@Override
	public Object parse(String str) {
		if (countQuotes(str) > 1) {
			return str.substring(1, str.length() - 1);
		}
		return str;
	}

	@Override
	public boolean matches(String str) {
		return countQuotes(str) != 1;
	}

	private long countQuotes(String str) {
		return str.chars().filter(ch -> ch == '"').count();
	}
}
