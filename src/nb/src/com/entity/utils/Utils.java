package com.entity.utils;

import java.math.BigInteger;
import java.util.Random;

import com.jme3.math.ColorRGBA;

public class Utils {
	public static ColorRGBA getColorFromFloats(float[] v){
		return new ColorRGBA(v[0], v[1], v[2], v[3]);
	}
	public static int getRandomBetween(Random rnd, int min, int max){
		return rnd.nextInt((max - min) + 1) + min;
	}
	public static float getRandomBetween(Random rnd, float min, float max){
		return rnd.nextFloat() * (max - min) + min;
	}
        public static String toHex(String arg) {
            return String.format("%x", new BigInteger(1, arg.getBytes()));
        }
}
