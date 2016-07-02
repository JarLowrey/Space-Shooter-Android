package levels;


import java.util.ArrayList;
import java.util.Arrays;


public class SpecialSpawnableLevel {

	static ArrayList<SpecialSpawnableLevel> allSpecialSpawnableLevels = new ArrayList<SpecialSpawnableLevel>();
	SpawnableWave wave;
	int level;
	

	public static void initializeSpecialSpawnableLevels(SpecialSpawnableLevel[] allSpawnableWaves){
		allSpecialSpawnableLevels = new ArrayList<SpecialSpawnableLevel>(Arrays.asList(allSpawnableWaves));
	}
	
	public static ArrayList<SpawnableWave> specialSpawnableWavesForThisLevel(int level){
		ArrayList<SpawnableWave> specialWavesForThisLevel = new ArrayList<SpawnableWave>();
		
		for(SpecialSpawnableLevel ssl : allSpecialSpawnableLevels){
			if(ssl.level == level){
				specialWavesForThisLevel.add( ssl.wave() );
			}
		}
		
		return specialWavesForThisLevel;
	}

	public SpecialSpawnableLevel(SpawnableWave specialWaveToSpawnAtIndicatedLevel, int whichLevel){
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
