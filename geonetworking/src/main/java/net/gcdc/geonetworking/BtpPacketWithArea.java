package net.gcdc.geonetworking;

/**
 * @author Xue
 * @create 2024-04-23 14:14
 */
public class BtpPacketWithArea {

    private BtpPacket btpPacket;

    private Area area;

    private Boolean inside;

    public BtpPacketWithArea(BtpPacket btpPacket, Area area, Boolean inside) {
        this.btpPacket = btpPacket;
        this.area = area;
        this.inside = inside;
    }

    public BtpPacketWithArea(BtpPacket btpPacket, Area area) {
        this.btpPacket = btpPacket;
        this.area = area;
    }

    public BtpPacket getBtpPacket() {
        return btpPacket;
    }

    public void setBtpPacket(BtpPacket btpPacket) {
        this.btpPacket = btpPacket;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Boolean getInside() {
        return inside;
    }

    public void setInside(Boolean inside) {
        this.inside = inside;
    }
}
