package main;

import BasicChess.BasicBoard;
import BasicChess.BasicDecoder;
import GrandChess.GrandBoard;
import GrandChess.GrandDecoder;
import RandomChess.RandomBoard;

/**
 * Created by hj1012 on 07/11/14.
 */
public class GameControllerMaker {
	public static GameController get(String variant, String code) {
		if(variant.equals("Basic")) {
			return new GameController(new BasicBoard(), new BasicDecoder(), code);
		} else if(variant.equals("Random960")) {
			return new GameController(new RandomBoard(), new BasicDecoder(), code);
		} else if(variant.equals("Grand")) {
			return new GameController(new GrandBoard(), new GrandDecoder(), code);
		} else {
			return new GameController(new BasicBoard(), new BasicDecoder(), code);
		}
	}
}
