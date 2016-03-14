package com.entity.utils;

import java.util.Random;

public class Utils {
	public static int getRandomBetween(Random rnd, int min, int max){
		return rnd.nextInt((max - min) + 1) + min;
	}
	public static float getRandomBetween(Random rnd, float min, float max){
		return rnd.nextFloat() * (max - min) + min;
	}

}
