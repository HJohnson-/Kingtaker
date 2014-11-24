package main;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to grab variants from .jar files
 */
public class VariantFactory {

	private static final String varDirectory = "";
	private static final String varPattern = "**var.java";
	private static List<String> unloadedVars;

	public VariantFactory() {
		unloadedVars = new LinkedList<String>();
		findVariants();
	}

	class Finder extends SimpleFileVisitor<Path> {
		private final PathMatcher matcher;

		public Finder() {
			matcher = FileSystems.getDefault()
					.getPathMatcher("glob:"+varPattern);
		}

		private void find(Path file) {
			Path name = file.getFileName();
			if (name != null && matcher.matches(name)) {
				unloadedVars.add(file.toString());
			}
		}

		@Override
		public FileVisitResult visitFile(Path file,
										 BasicFileAttributes attrs) {
			find(file);
			return FileVisitResult.CONTINUE;
		}
	}


	public ChessVariant getNextVariant() {
		if(hasNextVariant()) {
			String nextVarName = reformatName(unloadedVars.remove(0));
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

	public String getNextVariantName() {
		if(hasNextVariant()) {
			return unloadedVars.get(0);
		} else {
			return null;
		}
	}

	public boolean hasNextVariant() {
		return !unloadedVars.isEmpty();
	}

	private void findVariants() {
		try {
			Files.walkFileTree(Paths.get(varDirectory), new Finder());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		VariantFactory var = new VariantFactory();
		while(var.hasNextVariant()) {
			System.out.println("I found a variant called:" + reformatName(var.getNextVariantName()));
			ChessVariant extremelyDangerous = var.getNextVariant();
			extremelyDangerous.drawBoard();
		}
	}

	private static String reformatName(String original) {
		String plant = original.replaceAll("/", ".");
		return plant.substring(plant.indexOf(".vari")+1, plant.indexOf(".java"));

	}
}


