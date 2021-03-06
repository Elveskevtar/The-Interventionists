package com.elveskevtar.divebomb.weapons;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import com.elveskevtar.divebomb.gfx.GameDeathmatchMP;
import com.elveskevtar.divebomb.net.packets.Packet04Attack;
import com.elveskevtar.divebomb.net.packets.Packet06Kill;
import com.elveskevtar.divebomb.race.Player;

public abstract class Melee extends Weapon {

	private static String[] swordFiles = { "res/icon/sword.png" };
	private static String[] example1Files = { "res/icon/meleeExample1.png" };
	private static String[] example2Files = { "res/icon/meleeExample2.png" };
	private static String[] example3Files = { "res/icon/meleeExample3.png" };

	public static enum MeleeWeaponTypes {
		SWORD(swordFiles, "sword"), EXAMPLE1(example1Files, "sword"), EXAMPLE2(example2Files,
				"sword"), EXAMPLE3(example3Files, "sword");

		private String[] files;
		private String name;

		MeleeWeaponTypes(String[] files, String name) {
			this.setFiles(files);
			this.setName(name);
		}

		public String[] getFiles() {
			return files;
		}

		public void setFiles(String[] files) {
			this.files = files;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	private int distance;

	public Melee(Player p, String name) {
		super(p, name);
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	@Override
	public void attack(CopyOnWriteArrayList<Player> players, boolean server) {
		if (getPlayer().getGame().getSocketClient() == null || server == true) {
			ArrayList<Player> attackedPlayers = new ArrayList<Player>();
			for (Player p : players)
				if (p != getPlayer() && !p.isDead()) {
					if (getPlayer().isFacingRight() && p.getxPosition() - getPlayer().getxPosition() <= 0
							&& Math.sqrt(Math.pow((p.getxPosition() - getPlayer().getxPosition()), 2)
									+ Math.pow((p.getyPosition() - getPlayer().getyPosition()), 2)) <= getDistance())
						attackedPlayers.add(p);
					if (!getPlayer().isFacingRight() && p.getxPosition() - getPlayer().getxPosition() >= 0
							&& Math.sqrt(Math.pow((p.getxPosition() - getPlayer().getxPosition()), 2)
									+ Math.pow((p.getyPosition() - getPlayer().getyPosition()), 2)) <= getDistance())
						attackedPlayers.add(p);
				}
			for (Player p : attackedPlayers) {
				p.setHealth(p.getHealth() + (Math.random() * -10) + p.getInHand().getDefense() - getDamage());
				if (p.getHealth() <= 0) {
					getPlayer().setKills(getPlayer().getKills() + 1);
					p.setDeaths(p.getDeaths() + 1);
					if (server) {
						Packet06Kill packet = new Packet06Kill(getPlayer().getName(), p.getName());
						packet.writeData(getPlayer().getGame().getSocketServer());
						if (getPlayer().getGame() instanceof GameDeathmatchMP
								&& getPlayer().getGame().getSocketClient() == null
								&& ((GameDeathmatchMP) getPlayer().getGame()).getFirstPlaceName() == null
								|| getPlayer().getKills() > ((GameDeathmatchMP) getPlayer().getGame())
										.getFirstPlaceKills()) {
							((GameDeathmatchMP) getPlayer().getGame()).setFirstPlaceKills(getPlayer().getKills());
							((GameDeathmatchMP) getPlayer().getGame()).setFirstPlaceName(getPlayer().getName());
						}
					}
				}
			}
			for (int i = 0; i < attackedPlayers.size(); i++)
				attackedPlayers.remove(i);
		} else if (getPlayer().getName().equalsIgnoreCase(getPlayer().getGame().getUserName()) && !server) {
			Packet04Attack packet = new Packet04Attack(getPlayer().getName());
			packet.writeData(getPlayer().getGame().getSocketClient());
		}
	}
}