package main;

import java.util.List;

/**
 * Class to grab variants from .jar files
 */
public class VariantFactory {
	private List<String> unloadedVars;

	public VariantFactory() {
		unloadedVars = findVariants();
	}

	public ChessVariant getNextVariant() {
		if(hasNextVariant()) {
			String nextVarName = unloadedVars.remove(0);
			ChessVariant result = null;
			try {
				Class implClass = Class.forName(nextVarName);
				result = (ChessVariant)implClass.newInstance();
			}
			catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
			catch (InstantiationException ex) {
				ex.printStackTrace();
			}
			catch (IllegalAccessException ex) {
				ex.printStackTrace();
			}
			return result;
		} else {
			return null;
		}
	}

	public boolean hasNextVariant() {
		return !unloadedVars.isEmpty();
	}

	private List<String> findVariants() {
		//TODO search appropriate directory for variant-files
		return null;
	}

}
