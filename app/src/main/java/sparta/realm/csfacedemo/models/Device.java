package sparta.realm.csfacedemo.models;


import android.os.Build;

import com.realm.annotations.DynamicClass;
import com.realm.annotations.DynamicProperty;
import com.realm.annotations.RealmModel;
import com.realm.annotations.SyncDescription;

import java.io.Serializable;

import sparta.realm.csfacedemo.Globals;


@DynamicClass(table_name = "device")
@SyncDescription(service_id = "11", download_link = "/Employees/Details/GetMobileRecords", is_ok_position = "JO:isOkay", download_array_position = "JO:result", service_name = "Member", service_type = SyncDescription.service_type.Download)
@SyncDescription(service_id = "12", service_name = "Member", upload_link = "/Employees/Details/AddEmployee", service_type = SyncDescription.service_type.Upload)
public class Device extends RealmModel implements Serializable {


    @DynamicProperty(json_key = "model")
    public String model;

    @DynamicProperty(json_key = "device_name")
    public String device_name;


  @DynamicProperty(json_key = "session")
    public String board;

 @DynamicProperty(json_key = "session")
    public String brand;


  @DynamicProperty(json_key = "session")
    public String manufacturer;

  @DynamicProperty(json_key = "session")
    public String serial;



    public MemberImage profile_photo = null;


    public Device() {
this.model=Build.MODEL;
this.device_name=Build.DEVICE;
this.manufacturer=Build.MODEL;
this.board=Build.BOARD;
this.brand=Build.BRAND;
        try {
            this.serial=Build.SERIAL;
        } catch (Exception e) {
            e.printStackTrace();
        }


        this.transaction_no = Globals.getTransactionNo();


        this.sync_status = com.realm.annotations.sync_status.pending.ordinal() + "";


    }

}
