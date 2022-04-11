package sparta.realm.csfacedemo.models;


import com.realm.annotations.DynamicClass;
import com.realm.annotations.DynamicProperty;
import com.realm.annotations.RealmModel;
import com.realm.annotations.SyncDescription;

import java.io.Serializable;

import sparta.realm.csfacedemo.Globals;


@DynamicClass(table_name = "member_info_table")
@SyncDescription(service_id = "1", download_link = "/Employees/Details/GetMobileRecords", is_ok_position = "JO:isOkay", download_array_position = "JO:result", service_name = "Member", service_type = SyncDescription.service_type.Download)
@SyncDescription(service_id = "2", service_name = "Member", upload_link = "/Employees/Details/AddEmployee", service_type = SyncDescription.service_type.Upload)
public class Member extends RealmModel implements Serializable {
    @DynamicProperty(json_key = "worker_name")
    public String name;

    @DynamicProperty(json_key = "session")
    public String session;



    public MemberImage profile_photo = null;
    @DynamicProperty(json_key = "member_no")
    public String member_no;


    public Member() {
        this.transaction_no = Globals.getTransactionNo();


        this.sync_status = com.realm.annotations.sync_status.pending.ordinal() + "";


    }
 public Member(String session,String name) {
        this.transaction_no = Globals.getTransactionNo();
        this.session = session;
        this.name = name;


        this.sync_status = com.realm.annotations.sync_status.pending.ordinal() + "";


    }

}
