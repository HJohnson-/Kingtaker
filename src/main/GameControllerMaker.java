package main;

import BasicChess.BasicBoard;
import BasicChess.BasicDecoder;
import variants.GrandChess.GrandBoard;
import variants.GrandChess.GrandDecoder;
import variants.RandomChess.RandomBoard;

/**
 * Created by hj1012 on 07/11/14.
 */
public class GameControllerMaker {
	public static GameController get(String variant, String code) {
		if(variant.equals("Basic")) {
			return new GameController(new BasicBoard(), new BasicDecoder(), code, GameMode.MULTIPLAYER_LOCAL);
		} else if(variant.equals("Random960")) {
			return new GameController(new RandomBoard(), new BasicDecoder(), code, GameMode.MULTIPLAYER_LOCAL);
		} else if(variant.equals("Grand")) {
			return new GameController(new GrandBoard(), new GrandDecoder(), code, GameMode.MULTIPLAYER_LOCAL);
		} else {
			return new GameController(new BasicBoard(), new BasicDecoder(), code, GameMode.MULTIPLAYER_LOCAL);
		}
	}
}
