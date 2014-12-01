package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;

public class VariantFactory {

    private static VariantFactory instance;

	public static VariantFactory getInstance() {
        if (instance == null) {
            instance = new VariantFactory();
        }
        return instance;
    }

	private static final String varDirectory = "";
	private static final String varPattern = "**var.java";
	private static List<ChessVariant> loadedVars;

	private VariantFactory() {
		loadedVars = new LinkedList<ChessVariant>();
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
				loadedVars.add(varFromString(file.toString()));
			}
		}

		@Override
		public FileVisitResult visitFile(Path file,
										 BasicFileAttributes attrs) {
			find(file);
			return FileVisitResult.CONTINUE;
		}
	}

	private ChessVariant varFromString(String name) {
		ChessVariant result = null;
		try {
			Class implClass = Class.forName(reformatName(name));
			result = (ChessVariant) implClass.newInstance();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	private void findVariants() {
		loadedVars = new LinkedList<ChessVariant>();
		try {
			Files.walkFileTree(Paths.get(varDirectory), new Finder());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String reformatName(String original) {
        String dotted;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            dotted = original.replaceAll("\\\\", ".");
        } else {
            dotted = original.replaceAll(File.separator, ".");
        }
		return dotted.substring(dotted.indexOf(".vari")+1, dotted.indexOf(".java"));

	}

	public List<ChessVariant> getAllVariants() {
		findVariants();
		return loadedVars;
	}

	public ChessVariant getVariantByID(Integer id) {
		for(ChessVariant var : loadedVars) {
			if(var.getVariationID() == id) {
				return var;
			}
		}
		return null;
	}
}