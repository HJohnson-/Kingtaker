package main;

import BasicChess.BasicChess;
import GrandChess.GrandChess;
import RandomChess.RandomChess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jc4512 on 05/11/14.
 */
public class ChessVariantManager {
    private static ChessVariantManager instance;
    private Map<Integer, ChessVariant> variants;

    public static ChessVariantManager getInstance() {
        if (instance == null) {
            instance = new ChessVariantManager();
        }
        return instance;
    }

    private ChessVariantManager() {
        //TODO read in .JAR files instead
        variants = new HashMap<Integer, ChessVariant>();

        ChessVariant[] cvs = {new BasicChess(), new RandomChess(), new GrandChess()};
        for (ChessVariant var : cvs) {
            variants.put(var.getVariationID(), var);
        }
    }

    public ArrayList<ChessVariant> getAllVariants() {
        return new ArrayList<ChessVariant>(variants.values());
    }

    public ChessVariant getVariantByID(Integer id) {
        return variants.get(id);
    }

}
