package main;

import BasicChess.BasicChess;
import RandomChess.RandomChess;
import GrandChess.GrandChess;

/**
 * Created by hj1012 on 07/11/14.
 */
public class ChessVariantMaker {
	public static ChessVariant get(String variant, GameController gameState) {
		if(variant.equals("Basic")) {
			return new BasicChess(gameState);
		} else if(variant.equals("Random960")) {
			return new RandomChess(gameState);
		} else if(variant.equals("Grand")) {
			return new GrandChess(gameState);
		} else {
			return new BasicChess(gameState);
		}
	}
}
