package ap.autonpc.apdevteamuni.configuration;

public class NPC {
    private RegionType regionType;
    private String name;
    private int id;
    private int price;
    private String permNode;

    public NPC(String name, int id, int price, String permNode, RegionType regionType) {
        this.name = name;
        this.id = id;
        this.price = price;
        this.permNode = permNode;
        this.regionType = regionType;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getID() {
        return id;
    }

    public String getPermNode() {
        return permNode;
    }

    public RegionType getRegionType() {
        return regionType;
    }
}
