package com.lazyproductions.appserver.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerMapping {
	public static Map<String, PlayerVectorData> registeredPlayers = new ConcurrentHashMap<>();
	public static List<BulletData> liveBullets = new ArrayList<>();
}
