package findtests;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by hj1012 on 24/11/14.
 */
public class Finder extends SimpleFileVisitor<Path> {
	private final PathMatcher matcher;
	private static final String pathStart = ".";
	private static final String matchString = "**var.class";

	public Finder() {
		matcher = FileSystems.getDefault()
				.getPathMatcher("glob:"+matchString);
	}

	private void find(Path file) {
		Path name = file.getFileName();
		if (name != null && matcher.matches(name)) {
			System.out.println(name);
		}
	}

	@Override
	public FileVisitResult visitFile(Path file,
									 BasicFileAttributes attrs) {
		find(file);
		return FileVisitResult.CONTINUE;
	}

	public static void main(String[] args) {
		try {
			Files.walkFileTree(Paths.get(pathStart), new Finder());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}