package com.lazyproducitons.appserver.utils;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import com.lazyproductions.appserver.Data.Vector2D;
import com.lazyproductions.appserver.utils.HitCalculator;

public class HitCalculatorTests {

	@Test
	public void testStuff() {
		assertTrue(HitCalculator.didTargetHit(250, 250, 0,
				new Vector2D(250, 250)));
	}
}
