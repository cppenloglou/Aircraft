package minicraft.level.tile;

import java.util.ArrayList;
import java.util.HashMap;

import minicraft.core.Game;
import minicraft.level.tile.WoolTile.WoolType;
import minicraft.level.tile.farming.CarrotTile;
import minicraft.level.tile.farming.FarmTile;
import minicraft.level.tile.farming.PotatoTile;
import minicraft.level.tile.farming.SkyFarmTile;
import minicraft.level.tile.farming.SkyWartTile;
import minicraft.level.tile.farming.WheatTile;

public final class Tiles {
	/// idea: to save tile names while saving space, I could encode the names in
	/// base 64 in the save file...^M
	/// then, maybe, I would just replace the id numbers with id names, make them
	/// all private, and then make a get(String) method, parameter is tile name.

	public static ArrayList<String> oldids = new ArrayList<>();

	private static final HashMap<Short, Tile> tiles = new HashMap<>();

	public static void initTileList() {

		if (Game.debug) {
			System.out.println("Initializing tile list...");
		}

		tiles.put((short) 0, new GrassTile("Grass"));
		tiles.put((short) 1, new DirtTile("Dirt"));
		tiles.put((short) 2, new FlowerTile("Flower"));
		tiles.put((short) 3, new HoleTile("Hole"));
		tiles.put((short) 4, new StairsTile("Stairs Up", true));
		tiles.put((short) 5, new StairsTile("Stairs Down", false));
		tiles.put((short) 6, new WaterTile("Water"));
		// this is out of order because of lava buckets
		tiles.put((short) 17, new LavaTile("Lava"));

		tiles.put((short) 7, new RockTile("Rock"));
		tiles.put((short) 8, new TreeTile(TreeTile.TreeType.Oak));
		tiles.put((short) 9, new SaplingTile("Oak Sapling", Tiles.get("Grass"), Tiles.get("Oak Tree")));
		tiles.put((short) 10, new SandTile("Sand"));
		tiles.put((short) 11, new CactusTile("Cactus"));
		tiles.put((short) 12, new SaplingTile("Cactus Sapling", Tiles.get("Sand"), Tiles.get("Cactus")));
		tiles.put((short) 13, new OreTile(OreTile.OreType.Iron));
		tiles.put((short) 14, new OreTile(OreTile.OreType.Gold));
		tiles.put((short) 15, new OreTile(OreTile.OreType.Gem));
		tiles.put((short) 16, new OreTile(OreTile.OreType.Lapis));
		tiles.put((short) 18, new LavaBrickTile("Lava Brick"));
		tiles.put((short) 19, new ExplodedTile("Explode"));
		tiles.put((short) 20, new FarmTile("Farmland"));
		tiles.put((short) 21, new WheatTile("Wheat"));
		tiles.put((short) 22, new HardRockTile("Hard Rock"));
		tiles.put((short) 23, new InfiniteFallTile("Infinite Fall"));
		tiles.put((short) 24, new CloudTile("Cloud"));
		tiles.put((short) 25, new CloudCactusTile("Cloud Cactus"));

		// Building tiles
		tiles.put((short) 26, new DoorTile(Tile.Material.Oak));
		tiles.put((short) 27, new DoorTile(Tile.Material.Spruce));
		tiles.put((short) 28, new DoorTile(Tile.Material.Birch));
		tiles.put((short) 29, new DoorTile(Tile.Material.Stone));
		tiles.put((short) 30, new DoorTile(Tile.Material.Obsidian));
		tiles.put((short) 31, new DoorTile(Tile.Material.Holy));
		tiles.put((short) 32, new FloorTile(Tile.Material.Oak));
		tiles.put((short) 33, new FloorTile(Tile.Material.Spruce));
		tiles.put((short) 34, new FloorTile(Tile.Material.Birch));
		tiles.put((short) 35, new FloorTile(Tile.Material.Stone));
		tiles.put((short) 36, new FloorTile(Tile.Material.Obsidian));
		tiles.put((short) 37, new FloorTile(Tile.Material.Holy));
		tiles.put((short) 38, new WallTile(Tile.Material.Oak));
		tiles.put((short) 39, new WallTile(Tile.Material.Spruce));
		tiles.put((short) 40, new WallTile(Tile.Material.Birch));
		tiles.put((short) 41, new WallTile(Tile.Material.Stone));
		tiles.put((short) 42, new WallTile(Tile.Material.Obsidian));
		tiles.put((short) 43, new WallTile(Tile.Material.Holy));

		tiles.put((short)44, new PathTile("Path"));

		// Wool tiles
		tiles.put((short) 45, new WoolTile("Wool", WoolType.NORMAL));
		tiles.put((short) 46, new WoolTile("Red Wool", WoolType.RED));
		tiles.put((short) 47, new WoolTile("Blue Wool", WoolType.BLUE));
		tiles.put((short) 48, new WoolTile("Lime Wool", WoolType.LIME));
		tiles.put((short) 49, new WoolTile("Yellow Wool", WoolType.YELLOW));
		tiles.put((short) 50, new WoolTile("Purple Wool", WoolType.PURPLE));
		tiles.put((short) 51, new WoolTile("Black Wool", WoolType.BLACK));
		tiles.put((short) 52, new WoolTile("Pink Wool", WoolType.PINK));
		tiles.put((short) 53, new WoolTile("Green Wool", WoolType.GREEN));
		tiles.put((short) 54, new WoolTile("Light Gray Wool", WoolType.LIGHT_GRAY));
		tiles.put((short) 55, new WoolTile("Brown Wool", WoolType.BROWN));
		tiles.put((short) 56, new WoolTile("Magenta Wool", WoolType.MAGENTA));
		tiles.put((short) 57, new WoolTile("Light Blue Wool", WoolType.LIGHT_BLUE));
		tiles.put((short) 58, new WoolTile("Cyan Wool", WoolType.CYAN));
		tiles.put((short) 59, new WoolTile("Orange Wool", WoolType.ORANGE));
		tiles.put((short) 60, new WoolTile("Gray Wool", WoolType.GRAY));

		tiles.put((short) 61, new CarrotTile("Carrot"));
		tiles.put((short) 62, new PotatoTile("Potato"));
		tiles.put((short) 63, new SkyWartTile("Sky Wart"));
		tiles.put((short) 64, new LawnTile("Lawn"));
		tiles.put((short) 65, new OrangeTulipTile("Orange Tulip"));

		tiles.put((short) 66, new SnowTile("Snow"));

		tiles.put((short) 67, new TreeTile(TreeTile.TreeType.Birch));
		tiles.put((short) 68, new SaplingTile("Birch Sapling", Tiles.get("Grass"), Tiles.get("Birch tree")));

		tiles.put((short) 69, new TreeTile(TreeTile.TreeType.Fir));
		tiles.put((short) 70, new SaplingTile("Fir Sapling", Tiles.get("Snow"), Tiles.get("Fir tree")));

		tiles.put((short) 71, new TreeTile(TreeTile.TreeType.Pine));
		tiles.put((short) 72, new SaplingTile("Pine Sapling", Tiles.get("Snow"), Tiles.get("Pine tree")));

		tiles.put((short) 73, new CloudTreeTile("Cloud Tree"));

		tiles.put((short) 74, new IceSpikeTile("Ice Spike"));
		tiles.put((short) 75, new ObsidianTile("Raw Obsidian"));

		// heaven tiles
		tiles.put((short) 76, new SkyGrassTile("Sky Grass"));
		tiles.put((short) 77, new SkyHighGrassTile("Sky High Grass"));
		tiles.put((short) 78, new SkyDirtTile("Sky Dirt"));
		tiles.put((short) 79, new SkyLawnTile("Sky Lawn"));
		tiles.put((short) 80, new FerrositeTile("Ferrosite"));
		tiles.put((short) 81, new SkyFarmTile("Sky Farmland"));
		tiles.put((short) 82, new HolyRockTile("Holy Rock"));
		tiles.put((short) 83, new GoldenCloudTreeTile("Golden Cloud Tree"));
		tiles.put((short) 84, new BlueCloudTreeTile("Blue Cloud Tree"));
		tiles.put((short) 85, new SkyFernTile("Sky Fern"));
		tiles.put((short) 86, new UpRockTile("Up rock"));

		tiles.put((short) 87, new JungleGrassTile("Jungle Grass"));
		
		tiles.put((short) 88, new CloudHoleTile("Cloud Hole"));
		
		tiles.put((short) 89, new IceTile("Ice"));

		// tiles.put((short)?, new SandRockTile("Sand rock"));

		// WARNING: don't use this tile for anything!
		tiles.put((short)255, new ConnectTile());
		
		for(short i = 0; i < 256; i++) {
			if(tiles.get(i) == null) continue;
			tiles.get(i).id = (short)i;
		}
	}

	protected static void add(int id, Tile tile) {
		tiles.put((short)id, tile);
		System.out.println("Adding " + tile.name + " to tile list with id " + id);
		tile.id = (short) id;
	}

	static {
		for (int i = 0; i < 32768; i++) {
			oldids.add(null);
		}
		
		oldids.set(0, "grass");
		oldids.set(1, "rock");
		oldids.set(2, "water");
		oldids.set(3, "flower");
		oldids.set(4, "oak tree");
		oldids.set(5, "dirt");
		oldids.set(41, "wool");
		oldids.set(42, "red wool");
		oldids.set(43, "blue wool");
		oldids.set(45, "green wool");
		oldids.set(127, "yellow wool");
		oldids.set(56, "black wool");
		oldids.set(6, "sand");
		oldids.set(7, "cactus");
		oldids.set(8, "hole");
		oldids.set(9, "oak Sapling");
		oldids.set(10, "cactus Sapling");
		oldids.set(11, "farmland");
		oldids.set(12, "wheat");
		oldids.set(13, "lava");
		oldids.set(14, "stairs Down");
		oldids.set(15, "stairs Up");
		oldids.set(17, "cloud");
		oldids.set(30, "explode");
		oldids.set(31, "Wood Planks");
		oldids.set(33, "plank wall");
		oldids.set(34, "stone wall");
		oldids.set(35, "wood door");
		oldids.set(36, "wood door");
		oldids.set(37, "stone door");
		oldids.set(38, "stone door");
		oldids.set(39, "lava brick");
		oldids.set(32, "Stone Bricks");
		oldids.set(120, "Obsidian");
		oldids.set(121, "Obsidian wall");
		oldids.set(122, "Obsidian door");
		oldids.set(123, "Obsidian door");
		oldids.set(18, "hard Rock");
		oldids.set(19, "iron Ore");
		oldids.set(24, "Lapis");
		oldids.set(20, "gold Ore");
		oldids.set(21, "gem Ore");
		oldids.set(22, "cloud Cactus");
		oldids.set(16, "infinite Fall");
		
		// Light/torch versions, for compatibility with before 1.9.4-dev3. (were removed in making dev3)
		oldids.set(100, "grass");
		oldids.set(101, "sand");
		oldids.set(102, "oak tree");
		oldids.set(103, "cactus");
		oldids.set(104, "water");
		oldids.set(105, "dirt");
		oldids.set(107, "flower");
		oldids.set(108, "stairs Up");
		oldids.set(109, "stairs Down");
		oldids.set(110, "Wood Planks");
		oldids.set(111, "Stone Bricks");
		oldids.set(112, "wood door");
		oldids.set(113, "wood door");
		oldids.set(114, "stone door");
		oldids.set(115, "stone door");
		oldids.set(116, "Obsidian door");
		oldids.set(117, "Obsidian door");
		oldids.set(119, "hole");
		oldids.set(57, "wool");
		oldids.set(58, "red wool");
		oldids.set(59, "blue wool");
		oldids.set(60, "green wool");
		oldids.set(61, "yellow wool");
		oldids.set(62, "black wool");
		oldids.set(63, "Obsidian");
		oldids.set(64, "oak Sapling");
		oldids.set(65, "cactus Sapling");
		
		oldids.set(44, "torch grass");
		oldids.set(40, "torch sand");
		oldids.set(46, "torch dirt");
		oldids.set(47, "torch wood planks");
		oldids.set(48, "torch stone bricks");
		oldids.set(49, "torch Obsidian");
		oldids.set(50, "torch wool");
		oldids.set(51, "torch red wool");
		oldids.set(52, "torch blue wool");
		oldids.set(53, "torch green wool");
		oldids.set(54, "torch yellow wool");
		oldids.set(55, "torch black wool");
	}

	private static int overflowCheck = 0;
	public static Tile get(String name) {
		//System.out.println("Getting from tile list: " + name);
		
		name = name.toUpperCase();
		
		overflowCheck++;
		
		if(overflowCheck > 50) {
			System.out.println("STACKOVERFLOW prevented in Tiles.get(), on: " + name);
			System.exit(1);
		}
		
		//System.out.println("Fetching tile " + name);
		
		Tile getting = null;
		
		boolean isTorch = false;
		if(name.startsWith("TORCH")) {
			isTorch = true;
			name = name.substring(6); // Cuts off torch prefix.
		}

		if(name.contains("_")) {
			name = name.substring(0, name.indexOf("_"));
		}

		for(Tile t: tiles.values()) {
			if(t == null) continue;
			if(t.name.equals(name)) {
				getting = t;
				break;
			}
		}
		
		if (getting == null) {
			System.out.println("TILES.GET: Invalid tile requested: " + name);
			getting = tiles.get((short)0);
		}
		
		if(isTorch) {
			getting = TorchTile.getTorchTile(getting);
		}
		
		overflowCheck = 0;
		return getting;
	}

	public static Tile get(int id) {
		//System.out.println("Requesting tile by id: " + id);
		if(id < 0) id += 32768;

		if(tiles.get((short)id) != null) {
			return tiles.get((short)id);
		}
		else if(id >= 32767) {
			return TorchTile.getTorchTile(get(id - 32767));
		}
		else {
			System.out.println("TILES.GET: Unknown tile id requested: " + id);
			return tiles.get((short)0);
		}
	}
	
	public static boolean containsTile(int id) {
		return tiles.get((short)id) != null;
	}
	
	public static String getName(String descriptName) {
		if(!descriptName.contains("_")) return descriptName;
		int data;
		String[] parts = descriptName.split("_");
		descriptName = parts[0];
		data = Integer.parseInt(parts[1]);
		return get(descriptName).getName(data);
	}
}