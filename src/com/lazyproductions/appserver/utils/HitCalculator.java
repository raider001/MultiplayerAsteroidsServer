package com.lazyproductions.appserver.utils;

import com.lazyproductions.appserver.Data.Vector2D;

public class HitCalculator {

	private static final Vector2D[] lowResDimensions = {new Vector2D(15, 10),
			new Vector2D(15, -10), new Vector2D(-10, -10),
			new Vector2D(-10, 10)};

	public static boolean didTargetHit(double bulletX, double bulletY,
			double targetRotation, Vector2D targetPosition) {
		// Gets a copy of the translation data.

		Vector2D[] translation = new Vector2D[lowResDimensions.length];

		// Create a deep copy as to not interfere with basic starting data.
		for (int i = 0; i < translation.length; i++) {
			translation[i] = new Vector2D(lowResDimensions[i].x,
					lowResDimensions[i].y);
		}

		double[] x = new double[translation.length];
		double[] y = new double[translation.length];

		// Moved bounding box to correct location.
		// rotates to it the angle of the object.
		for (int i = 0; i < translation.length; i++) {
			translation[i].rotateBy(targetRotation);
			translation[i].add(targetPosition);

			x[i] = translation[i].x;
			y[i] = translation[i].y;
		}

		double minX = getMin(x);
		double minY = getMin(y);
		double maxX = getMax(x);
		double maxY = getMax(y);

		return bulletX >= minX && bulletX <= maxX && bulletY >= minY
				&& bulletY <= maxY;
	}

	private static double getMin(double... items) {
		double min = 0;
		for (int i = 0; i < items.length; i++) {
			if (i == 0) {
				min = items[i];
			} else if (min > items[i]) {
				min = items[i];
			}
		}
		return min;
	}

	private static double getMax(double... items) {
		double max = 0;
		for (int i = 0; i < items.length; i++) {
			if (i == 0) {
				max = items[i];
			} else if (max < items[i]) {
				max = items[i];
			}
		}
		return max;
	}

}
