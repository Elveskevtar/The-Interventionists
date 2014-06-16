package com.elveskevtar.divebomb.net.packets;

import com.elveskevtar.divebomb.net.GameClient;
import com.elveskevtar.divebomb.net.GameServer;

public class Packet10UpdateUserInfo extends Packet {

	private String name;
	private String race;
	private String color;
	private String weapon;

	public Packet10UpdateUserInfo(byte[] data) {
		super(10);
		String[] dataArray = readData(data).split(",");
		this.name = dataArray[0];
		this.race = dataArray[1];
		this.color = dataArray[2];
		this.weapon = dataArray[3];
	}

	public Packet10UpdateUserInfo(String name, String race, String color, String weapon) {
		super(10);
		this.name = name;
		this.race = race;
		this.color = color;
		this.weapon = weapon;
	}

	@Override
	public void writeData(GameClient client) {
		client.sendData(getData());
	}

	@Override
	public void writeData(GameServer server) {
		server.sendDataToAllClients(getData());
	}

	@Override
	public byte[] getData() {
		return ("10" + name + "," + race + "," + color + "," + weapon)
				.getBytes();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getWeapon() {
		return weapon;
	}

	public void setWeapon(String weapon) {
		this.weapon = weapon;
	}
}