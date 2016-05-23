package cn.ifingers.mytown.entities;

/**
 * Created by syfing on 2016/5/17.
 */
public class ShopInfo {

    private String id;// 卖家ID
    private String name;// 卖家名称
    private String address;// 卖家地址
    private String area;// 卖家城市
    private String opentime;// 营业时间
    private String lon;// 经度
    private String lat;// 纬度
    private String tel;// 卖家电话

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

}
