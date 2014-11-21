package main;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

/**
 * Class to grab variants from .jar files
 */
public class VariantFactory {


	private static List<String> unloadedVars;

	public VariantFactory() {

		findVariants();
	}

	class Finder extends SimpleFileVisitor<Path> {
		private final PathMatcher matcher;

		public Finder() {
			matcher = FileSystems.getDefault()
					.getPathMatcher("glob:./variants/*/*variant");
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

	private void findVariants() {
		try {
			Files.walkFileTree(Paths.get("."), new Finder());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}


