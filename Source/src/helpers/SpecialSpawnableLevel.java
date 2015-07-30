package helpers;

import java.util.ArrayList;

import levels.Factory_ScriptedWaves;

public class SpecialSpawnableLevel {

	static ArrayList<SpecialSpawnableLevel> allSpecialSpawnableLevels = new ArrayList<SpecialSpawnableLevel>();
	SpawnableWave wave;
	int level;
	

	public static void addSpecialSpawnableLevel(SpawnableWave specialWaveToSpawnAtIndicatedLevel, int whichLevel){
		SpecialSpawnableLevel ssl = new SpecialSpawnableLevel(specialWaveToSpawnAtIndicatedLevel, whichLevel);
		allSpecialSpawnableLevels.add(ssl);
	}
	
	public static SpawnableWave specialSpawnableForThisLevel(int level){
		for(SpecialSpawnableLevel ssl : allSpecialSpawnableLevels){
			if(ssl.level == level){
				return ssl.wave();
			}
		}
		
		return Factory_ScriptedWaves.doNothing();
	}

	private SpecialSpawnableLevel(SpawnableWave specialWaveToSpawnAtIndicatedLevel, int whichLevel){
		level = whichLevel;
		wave = specialWaveToSpawnAtIndicatedLevel;
	}
	
	public int level(){
		return level;
	}
	
	public SpawnableWave wave(){
		return wave;
	}
}
