package sparta.realm.csfacedemo.models;


import com.realm.annotations.DynamicClass;
import com.realm.annotations.DynamicProperty;
import com.realm.annotations.RealmModel;
import com.realm.annotations.SyncDescription;

import java.io.Serializable;

import sparta.realm.csfacedemo.Globals;


@DynamicClass(table_name = "recognition_session_tracking_file")
@SyncDescription(service_id = "7", service_name = "Member", service_type = SyncDescription.service_type.Download,storage_mode_check = true)
@SyncDescription(service_id = "8", service_name = "Member",service_type = SyncDescription.service_type.Upload,storage_mode_check = true)
public class RecognitionSessionTrackerFile extends RealmModel implements Serializable {

    @DynamicProperty(json_key = "file",storage_mode = DynamicProperty.storage_mode.FilePath)
    public String name;



  @DynamicProperty(json_key = "session")
    public String session;






    public RecognitionSessionTrackerFile() {
        this.transaction_no = Globals.getTransactionNo();


        this.sync_status = com.realm.annotations.sync_status.pending.ordinal() + "";


    }
  public RecognitionSessionTrackerFile(String name,String session) {
        this.name=name;
        this.session=session;
        this.transaction_no = Globals.getTransactionNo();
        this.sync_status = com.realm.annotations.sync_status.pending.ordinal() + "";


    }

}
