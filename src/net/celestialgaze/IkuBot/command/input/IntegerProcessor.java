package net.celestialgaze.IkuBot.command.input;

import net.celestialgaze.IkuBot.util.IkuUtil;
import net.dv8tion.jda.internal.utils.tuple.Pair;

public class IntegerProcessor extends TypeProcessor {

	@Override
	public Pair<Integer, Integer> findFirst(String[] args) {
		for (int i = 0; i < args.length; i++) {
			String s = args[i];
			if (matches(s)) return Pair.of(i, i);
		}
		return null;
	}

	@Override
	public Object parse(String str) {
		if (matches(str)) return IkuUtil.getInteger(str);
		return null;
	}

	@Override
	public boolean matches(String str) {
		return IkuUtil.isInteger(str);
	}

}
