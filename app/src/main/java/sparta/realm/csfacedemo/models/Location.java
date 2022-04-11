package sparta.realm.csfacedemo.models;


import com.realm.annotations.DynamicClass;
import com.realm.annotations.DynamicProperty;
import com.realm.annotations.RealmModel;
import com.realm.annotations.SyncDescription;

import sparta.realm.csfacedemo.Globals;


@DynamicClass(table_name = "location")
@SyncDescription(service_id = "13",service_name = "Tracking",service_type = SyncDescription.service_type.Download)
@SyncDescription(service_id = "14",service_name = "Tracking",service_type = SyncDescription.service_type.Upload)

public class Location extends RealmModel {

    public Location(){

    }
    public Location(String member_id, String lat, String lon, String alt, String speed, String accuracy){
        this.member_id=member_id;
        this.lat=lat;
        this.lon=lon;
        this.alt=alt;
        this.speed=speed;
        this.accuracy=accuracy;
        this.sync_status=com.realm.annotations.sync_status.pending.ordinal()+"";
        this.transaction_no= Globals.getTransactionNo();
    }
    @DynamicProperty(json_key = "employee_detais_id")
    public String member_id;
   @DynamicProperty(json_key = "lat")
    public String lat;
 @DynamicProperty(json_key = "longi")
    public String lon;
 @DynamicProperty(json_key = "alt")
    public String alt;
 @DynamicProperty(json_key = "speed")
    public String speed;
 @DynamicProperty(json_key = "accuracy")
    public String accuracy;
}
