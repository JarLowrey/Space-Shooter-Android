package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class GameStatsAdapter extends DbAdapter{
    
    public GameStatsAdapter(Context mctx) {
		super(mctx);
	}

	/**
     * Return a Cursor over the list of all rows in the database
     * 
     * @return Cursor over all FGlists
     */
    public Cursor fetchAllEntries() {
        return mDb.query(GAME_STATS_DB_TABLE, gameStatsColumns, null,null, null, null, null, null);
    }

    public long addRow(int score, long responseTime,long time, int totalCases,int totalCasesPassed,int numBonusCases,
    		int numBonusPoints,int numCombos, int numCombosPassed,int highestStreak,int gamemode){
    	ContentValues values = new ContentValues();
    	values.put(KEY_SCORE, score);
    	
    	values.put(KEY_RESPONSE_TIME_THIS_GAME, responseTime);
    	values.put(KEY_TIME, time);
    	
    	values.put(KEY_NUM_CASES, totalCases);
    	values.put(KEY_NUM_CASES_PASSED, totalCasesPassed);
    	values.put(KEY_NUM_BONUS_CASES, numBonusCases);
    	values.put(KEY_NUM_BONUS_POINTS, numBonusPoints);
    	values.put(KEY_NUM_COMBOS, numCombos);
    	values.put(KEY_NUM_COMBOS_PASSED, numCombosPassed);
    	
    	values.put(KEY_HIGHEST_STREAK, highestStreak);

    	return mDb.insert(GAME_STATS_DB_TABLE, null, values);
    }

    public int getNumGames(){
    	Cursor c = fetchAllEntries();
    	
    	int total=c.getCount();
    	c.close();
    	return total;
    }
    
    //SCORE
    public int getTotalScore(){
    	return getAllRowsAndReturnInt(KEY_SCORE);
    }
    
    public int getHighestScore(){
    	return getLargestInt(KEY_SCORE);
    }
    
    public int[] getRecentScores(int howManyScores){
    	return this.getMostRecentItemsInt(howManyScores, KEY_SCORE);
    }

    //TIME
    public long[] getRecentTimes(int howManyTimes){
    	return this.getMostRecentItemsLong(howManyTimes, KEY_TIME);
    }
    
    public int getLongestGame(){
    	return (int)(getLargestLong(KEY_TIME)/1000);
    }
    
    public long getTotalResponseTime(){
    	return getAllRowsAndReturnLong(KEY_RESPONSE_TIME_THIS_GAME);
    }
    
    public double getAverageResponseTime(){
    	return ((double)getTotalResponseTime()/(getNumCases()-getNumBonusCases()))/1000.0;
    }
    
    public int getTotalTime(){
    	return (int)getAllRowsAndReturnLong(KEY_TIME);
    }
        
    //NUM CASES
    public int getNumCases(){
    	return getAllRowsAndReturnInt(KEY_NUM_CASES);
    }
    
    public int getNumCasesPassed(){
    	return getAllRowsAndReturnInt(KEY_NUM_CASES_PASSED);
    }
    
    public int getNumCasesFailed(){
    	return getNumCases()-getNumCasesPassed();
    }
        
    //NUM BONUS
    public int getNumBonusCases(){
    	return getAllRowsAndReturnInt(KEY_NUM_BONUS_CASES);
    }
    
    public int getTotalBonusPoints(){
    	return getAllRowsAndReturnInt(KEY_NUM_BONUS_POINTS);
    }
    
    //NUM COMBOS
    public int getNumCombos(){
    	return getAllRowsAndReturnInt(KEY_NUM_COMBOS);
    }
    
    public int getNumCombosPassed(){
    	return getAllRowsAndReturnInt(KEY_NUM_COMBOS_PASSED);
    }
    
    public int getNumCombosFailed(){ 
    	return getNumCombos()-getNumCombosPassed();
    }

    //STREAKS    
    public int[] getRecentStreaks(int howManyStreak){
    	return this.getMostRecentItemsInt(howManyStreak, KEY_HIGHEST_STREAK);
    }
    
    public int getHighestStreak(){
    	return getLargestInt(KEY_HIGHEST_STREAK);
    }

    //top ten
    public int[] getTopTenScores(){    	
    	int[] topTen = new int[10];
    	Cursor c = fetchAllEntries();
    	int scoreColumnIndex = c.getColumnIndex(KEY_SCORE);

    	while(c.moveToNext()){
			int score = c.getInt(scoreColumnIndex);
			for(int i =0;i<topTen.length;i++){
				if(score>topTen[i]){
					int temp=score;
					score=topTen[i];
					topTen[i]=temp;
				}
			}
    	}
    	
    	c.close();
    	return topTen;
    }
    
//    public int[] getNumMedals(){
//    	int[] cutoffs = ctx.getResources().getIntArray(R.array.medal_cutoff_scores);
//    	int[] numMedals = new int[4];
//    	Cursor c = fetchAllEntries();
//    	int scoreColumnIndex = c.getColumnIndex(KEY_SCORE);
//
//    	while(c.moveToNext()){
//			int score = c.getInt(scoreColumnIndex);
//			if(score<cutoffs[0]){
//				numMedals[0]++;
//			}else if(score<cutoffs[1]){
//				numMedals[1]++;
//			}else if(score<cutoffs[2]){
//				numMedals[2]++;
//			}else{
//				numMedals[3]++;
//			}
//    	}
//    	
//    	c.close();
//    	return numMedals;
//    }
    
//    public int[] getNumTimesPlayedEveryGameMode(){
//    	int[] modes = new int[3];
//    	Cursor c = fetchAllEntries();
//    	int gameModeColumnIndex = c.getColumnIndex(KEY_GAME_MODE_THIS_GAME);
//
//    	while(c.moveToNext()){
//			int currMode = c.getInt(gameModeColumnIndex);
//			if(currMode>2){
//				currMode=2;//set all time trials of different lengths to just 2, time trial
//			}
//			modes[currMode]++;
//    	}
//    	
//    	c.close();
//    	return modes;
//    }
    
//    //regulate DB methods
//    public void dumpRows(){
//    	mDb.delete(GAME_STATS_DB_TABLE, null, null);
//    }
    
    //Helper Methods
    
    private long getAllRowsAndReturnLong(String column){
    	Cursor c = fetchAllEntries();
    	long total=0;
    	int columnIndex = c.getColumnIndex(column);
    	while(c.moveToNext()){
    		total+=c.getLong(columnIndex);
    	}
    	c.close();
    	return total;
    }
    
    private int getAllRowsAndReturnInt(String column){
    	Cursor c = fetchAllEntries();
    	int total=0;
    	int columnIndex = c.getColumnIndex(column);
    	while(c.moveToNext()){
    		total+=c.getLong(columnIndex);
    	}
    	c.close();
    	return total;
    }

    private int[] getMostRecentItemsInt(int howManyItems,String column){
    	Cursor c = fetchAllEntries();
    	int[] pastItems = new int[howManyItems];
    	int columnIndex = c.getColumnIndex(column);
    	c.moveToLast();
    	int i=0;
    	boolean continueLoop=true;
    	while(continueLoop && i<howManyItems){
    		pastItems[i] = c.getInt(columnIndex);
    		continueLoop=c.moveToPrevious();
    		i++;
    	}
    	c.close();
    	return pastItems;
    }
    
    private long[] getMostRecentItemsLong(int howManyItems,String column){////////Time must be divided by 1000
    	Cursor c = fetchAllEntries();
    	long[] pastItems = new long[howManyItems];
    	int columnIndex = c.getColumnIndex(column);
    	c.moveToLast();
    	boolean continueLoop=true;
    	int i=0;
    	while(continueLoop && i<howManyItems){
    		pastItems[i] = c.getLong(columnIndex)/1000;
    		continueLoop=c.moveToPrevious();
    		i++;
    	}
    	c.close();
    	return pastItems;
    }
    
    private int getLargestInt(String column){
    	Cursor c = fetchAllEntries();
    	int highest=0;
    	int columnIndex = c.getColumnIndex(column);
    	while(c.moveToNext()){
    		int tmp=c.getInt(columnIndex);
    		highest = (tmp>highest) ? tmp : highest;
    	}
    	c.close();
    	return highest;
    }
    
    private long getLargestLong(String column){
    	Cursor c = fetchAllEntries();
    	long highest=0;
    	int columnIndex = c.getColumnIndex(column);
    	while(c.moveToNext()){
    		long tmp=c.getLong(columnIndex);
    		highest = (tmp>highest) ? tmp : highest;
    	}
    	c.close();
    	return highest;
    }
}