package com.lazyproductions.appserver.Data;

public class PlayerVectorData {
	private static final double TURN_RATE = 0.07;
	private static final double ACCELERATION = 0.0105;
	private PlayerData playerData;

	private PlayerStatisticData playerStatistics;

	private Vector2D orientation = new Vector2D(0, 0);
	private Vector2D velocity = new Vector2D(0, 0);

	private Vector2D vector = new Vector2D(0, 0);

	private int playerSize = 8;

	private long lastFired = 0;
	private boolean isAccelerating = false, isTurningLeft = false,
			isTurningRight = false;

	public PlayerVectorData(PlayerData playerData,
			PlayerStatisticData playerStatistics) {
		this.playerData = playerData;
		this.playerStatistics = playerStatistics;
		this.playerStatistics.name = this.playerData.name;
	}

	public PlayerData getPlayerData() {
		return playerData;
	}

	public PlayerStatisticData getPlayerStats() {
		return playerStatistics;
	}

	public int getPlayerSize() {
		return playerSize;
	}

	public void setControls(boolean isFiring, boolean isTurningLeft,
			boolean isTurningRight, boolean isAccelerating) {
		long currentTime = System.currentTimeMillis();
		if (isFiring == true && lastFired < currentTime - 200) {
			shoot();
			lastFired = currentTime;
		}

		this.isTurningLeft = isTurningLeft;
		this.isTurningRight = isTurningRight;
		this.isAccelerating = isAccelerating;
	}

	public void shoot() {
		BulletData data = new BulletData();
		data.name = playerData.name;
		data.instantiationTime = System.currentTimeMillis() + 4000;
		data.rotation = playerData.rotation;
		data.x = playerData.x;
		data.y = playerData.y;
		data.velocity = new Vector2D(orientation);
		data.velocity.multiply(2);
		data.x += data.velocity.x * 11;
		data.y += data.velocity.y * 11;
		PlayerMapping.liveBullets.add(data);
	}

	public void update() {

		if (playerData.shield < PlayerData.maxShield) {
			playerData.shield += PlayerData.shieldRegenRate;
		}

		if (playerData.shield > PlayerData.maxShield) {
			playerData.shield = PlayerData.maxShield;
		}

		if (isTurningLeft && !isTurningRight) {
			rotateLeft(TURN_RATE);
		} else if (isTurningRight && !isTurningLeft) {
			rotateRight(TURN_RATE);
		}

		if (isAccelerating) {
			increaseVelocity();
		}

		playerData.x += velocity.x;
		playerData.y += velocity.y;

		if (playerData.x < -playerSize * 2) {
			playerData.x = 500 + playerSize * 2;
		} else if (playerData.x > 500 + playerSize * 2) {
			playerData.x = -2 * playerSize;
		}

		if (playerData.y < -playerSize * 2) {
			playerData.y = 500 + playerSize * 2;
		} else if (playerData.y > 500 + playerSize * 2) {
			playerData.y = -2 * playerSize;
		}

	}

	private void increaseVelocity() {

		if (this.velocity.x == 0 && this.velocity.y == 0) {
			this.velocity.set(this.orientation);
			this.velocity.multiply(ACCELERATION);
		}

		this.vector.set(this.orientation);
		this.vector.multiply(ACCELERATION);
		this.velocity.add(this.vector);

	}

	private void rotateLeft(double turnRate) {

		this.playerData.rotation -= turnRate;
		if (this.playerData.rotation < 0) {
			this.playerData.rotation += Math.PI * 2;
		}
		this.orientation.x = 1;
		this.orientation.y = 0;
		this.orientation.rotateTo(this.playerData.rotation);

	}

	private void rotateRight(double turnRate) {

		this.playerData.rotation += turnRate;
		this.playerData.rotation %= Math.PI * 2;
		this.orientation.x = 1;
		this.orientation.y = 0;
		this.orientation.rotateTo(this.playerData.rotation);
	}
}
