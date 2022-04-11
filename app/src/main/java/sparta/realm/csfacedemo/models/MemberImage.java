package sparta.realm.csfacedemo.models;


import com.realm.annotations.DynamicClass;
import com.realm.annotations.DynamicProperty;
import com.realm.annotations.RealmModel;
import com.realm.annotations.SyncDescription;

import java.io.Serializable;

import sparta.realm.csfacedemo.Globals;


@DynamicClass(table_name = "member_image_table")
@SyncDescription(service_id = "3",service_name = "Member image",service_type = SyncDescription.service_type.Download,chunk_size = 10,storage_mode_check = true)
@SyncDescription(service_id = "4",service_name = "Member image",service_type = SyncDescription.service_type.Upload,chunk_size = 10,storage_mode_check = true)
public class MemberImage extends RealmModel implements Serializable {



@DynamicProperty(json_key = "biometric_type_details_id")
public String data_index;

@DynamicProperty(json_key = "biometric_type_id")
public String data_type;

@DynamicProperty(json_key = "employee_details_id")
 public String member_id;

@DynamicProperty(json_key = "image",storage_mode = DynamicProperty.storage_mode.FilePath)
 public String image;


@DynamicProperty(json_key = "msid")
public String msid="";



@DynamicProperty(json_key = "loaded_to_tracker")
public String loaded_to_tracker;




    public MemberImage()
    {
        this.transaction_no= Globals.getTransactionNo();

        this.sync_status= com.realm.annotations.sync_status.pending.ordinal()+"";


    }
 public MemberImage(String member_id,String image)
    {

        this.image = image;
        this.data_type="2";
        this.data_index="14";
        this.transaction_no= Globals.getTransactionNo();

        this.sync_status= com.realm.annotations.sync_status.pending.ordinal()+"";


    }

}
