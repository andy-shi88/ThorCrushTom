package com.ND.thorcrushtom;

public class Constant {
	//ThorHammer
	public static String STATUS_STANDBY = "standby";
	public static String STATUS_HIT = "hit";
	//Tom
	public static String STATUS_HIDE = "hide";
	public static String STATUS_APPEAR = "appear";
	public static String STATUS_DAMAGED = "damaged";
	//hole
	public static String STATUS_EMPTY = "empty";
	public static String STATUS_OCCUPPIED = "occupied";
	//Game stATUS
	public static String STATUS_GAMEOVER = "game over";
	public static String STATUS_LIVE = "live";
	public static String STATUS_TITLE = "title";
	//gamethread setting
	public static int FPS_VALUE = 30;
	//game level
	public static int[] SCORE_LEVEL = {500, 1000, 2000, 3000, 4000, 5000, 6000};
	public static int[] TIME_LEVEL = {3000, 2900, 2800, 2600, 2400, 2000, 1000}; //stored in millisecond
	public static int[] APPEARANCE_LEVEL = {2, 3, 3, 3, 3, 3, 3};
	public static int[] RANDOM_LEVEL = {50, 40, 20, 10, 5, 3, 1};
	//default level value
	public static int DEFAULT_APPEARANCE_DURATION = 3000;
	public static int DEFAULT_RANDOM_GAP = 60;
	//GAME SCORE
	public static int GAME_SCORE_INCREMENT = 100;
}