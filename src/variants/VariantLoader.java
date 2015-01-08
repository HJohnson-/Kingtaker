package variants;

import main.ChessVariant;
import variants.BasicChess.BasicChessvar;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Jake on 08/01/2015.
 */
public class VariantLoader {
    private static VariantLoader instance = null;
    private final String VARIANT_DIRECTORY = "variants";
    private final String PACKAGE_NAME = "variants";
    private List<ChessVariant> loadedVariants;

    private VariantLoader() {
        loadAllVariants();
    }

    //Getting the instance loads all of the variants.
    public static VariantLoader getInstance() {
        if (instance == null) {
            instance = new VariantLoader();
        }
        return instance;
    }

    private void loadAllVariants() {
        loadedVariants = new ArrayList<ChessVariant>();
        loadedVariants.add(new BasicChessvar());

        try {
            File f = Paths.get(VARIANT_DIRECTORY).toFile();
            System.out.println(System.getProperty("user.dir"));
            ArrayList<File> files = new ArrayList<File>(Arrays.asList(f.listFiles()));

            for (File file : files) {
                if (file.getName().endsWith(".jar")) {
                    //////////////
                    //Adapted from:
                    //http://stackoverflow.com/questions/11016092/how-to-load-classes-at-runtime-from-a-folder-or-jar
                    //////////////
                    String variantName = file.getName().substring(0, file.getName().lastIndexOf("."));
                    JarFile jarFile = new JarFile(file.getPath());
                    Enumeration e = jarFile.entries();

                    URL[] urls = { new URL("jar:file:" + file.getPath() + "!/") };
                    URLClassLoader cl = URLClassLoader.newInstance(urls);

                    while (e.hasMoreElements()) {
                        try {
                            JarEntry je = (JarEntry) e.nextElement();
                            if(je.isDirectory() || !je.getName().endsWith(".class")){
                                continue;
                            }
                            // -6 because of .class
                            String className = je.getName().substring(0, je.getName().length() - 6);
                            className = className.replace('/', '.');
                            Class c = cl.loadClass(PACKAGE_NAME + "." + variantName + "." + className);
                            if (c.getSuperclass().equals(ChessVariant.class) ||
                                    c.getSuperclass().equals(BasicChessvar.class)) {
                                loadedVariants.add((ChessVariant) c.newInstance());
                            }
                        } catch (ClassNotFoundException e1) {
                            e1.printStackTrace();
                        } catch (InstantiationException e1) {
                            e1.printStackTrace();
                        } catch (IllegalAccessException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ChessVariant> getAllVariants() {
        return loadedVariants;
    }

    public ChessVariant getVariantByID(Integer id) {
        for(ChessVariant variant : loadedVariants) {
            if (variant.getVariationID() == id) {
                return variant;
            }
        }
        return null;
    }
}
