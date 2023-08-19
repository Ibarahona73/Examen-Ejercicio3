package hn.uth.examen201930010221.entity;

public class GPSLocation {
    private double latitude;
    private double longitude;

    public GPSLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getLatitudeStr() {
        return latitude+"";
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getLongitudeStr() {
        return longitude+"";
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String toText(){
        return this.latitude+","+this.longitude;
    }
}
