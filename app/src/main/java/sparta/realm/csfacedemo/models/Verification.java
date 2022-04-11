package sparta.realm.csfacedemo.models;

import com.realm.annotations.DynamicClass;
import com.realm.annotations.DynamicProperty;
import com.realm.annotations.RealmModel;
import com.realm.annotations.SyncDescription;

@DynamicClass(table_name = "verification")
@SyncDescription(service_id = "9",service_name = "Verifications", service_type = SyncDescription.service_type.Download)
@SyncDescription(service_id = "10",service_name = "Verifications", service_type = SyncDescription.service_type.Upload)
public class Verification extends RealmModel {



    @DynamicProperty(json_key = "employee_details_id")
    public String member_id;
    @DynamicProperty(json_key = "date_time")
    public String verification_date;
    @DynamicProperty(json_key = "verification_time")
    public String verification_time;
    @DynamicProperty(json_key = "lat")
    public String lat;
    @DynamicProperty(json_key = "lon")
    public String lon;
    @DynamicProperty(json_key = "verification_type")
    public String verification_type;
    @DynamicProperty(json_key = "verification_mode")
    public String verification_mode;





}
