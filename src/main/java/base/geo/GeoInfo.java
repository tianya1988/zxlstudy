package base.geo;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by jason on 19-1-10.
 */
public class GeoInfo {

    private static File database = new File("/home/jason/Desktop/APPbushu/jiaotong/interactive-alert-demand/GeoLite2-City.mmdb");
    private static DatabaseReader reader = null;

    private static void getGeoInfo(String ip) throws IOException, GeoIp2Exception {
        if (reader == null) {
            System.out.println("**********init database************");
            reader = new DatabaseReader.Builder(database).withCache(new CHMCache()).build();
        }

        InetAddress ipAddress = InetAddress.getByName(ip);

        CityResponse response = reader.city(ipAddress);

        Country country = response.getCountry();
        System.out.println(country.getIsoCode());
        System.out.println(country.getName());
        System.out.println(country.getNames().get("zh-CN"));

        City city = response.getCity();
        System.out.println(city.getName());
        System.out.println(city.getNames().get("zh-CN"));

        Location location = response.getLocation();
        System.out.println(location.getLatitude());  // 44.9733
        System.out.println(location.getLongitude()); // -93.2323
    }

    public static void main(String[] args) throws IOException, GeoIp2Exception {
        getGeoInfo("128.101.101.101");
        System.out.println("==============我是华丽的分割线==============");
        getGeoInfo("220.181.112.244");
        System.out.println("==============我是华丽的分割线==============");
        getGeoInfo("85.25.43.84");

        System.out.println("==============我是华丽的分割线==============");
        getGeoInfo("230.100.100.20");


    }
}
