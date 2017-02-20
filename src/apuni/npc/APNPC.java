package apuni.npc;

public enum APNPC{
	// @formatter:off
	MERCHANT_BUILDING_BLOCKS	("BBMerch",			"Building Blocks Merchant",		1000000,	2056),
	MERCHANT_MINERALS			("MineralMerch",	"Minerals Merchant",			1000000,	1297),
	MERCHANT_SHIP_SUPPLY		("ShipMerch",		"Ship Supply Merchant",			1000000,	1759),
	MERCHANT_BLACKSMITH			("SmithMerch",		"Blacksmith Merchant",			1000000,	2057),
	MERCHANT_FOODSTUFFS			("FoodMerch",		"Foodstuffs Merchant",			1000000,	2058),
	MERCHANT_BARTENDER			("BTMerch",			"Bartender Mercahnt",	Double.MAX_VALUE,	1435);
	// @formatter:on
	
	public final String alias, name;
	public final double price;
	public final int npcID;
	
	APNPC(String newAlias, String newName, double newPrice, int newNPCID){
		alias = newAlias;
		name = newName;
		price = newPrice;
		npcID = newNPCID;
	}
}
