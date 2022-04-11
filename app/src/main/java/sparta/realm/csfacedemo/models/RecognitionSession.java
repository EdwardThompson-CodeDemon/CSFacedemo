package sparta.realm.csfacedemo.models;


import com.realm.annotations.DynamicClass;
import com.realm.annotations.DynamicProperty;
import com.realm.annotations.RealmModel;
import com.realm.annotations.SyncDescription;

import java.io.Serializable;

import sparta.realm.csfacedemo.Globals;


@DynamicClass(table_name = "recognition_session")
@SyncDescription(service_id = "5", service_name = "Member", service_type = SyncDescription.service_type.Download)
@SyncDescription(service_id = "6", service_name = "Member", service_type = SyncDescription.service_type.Upload)
public class RecognitionSession extends RealmModel implements Serializable {
    @DynamicProperty(json_key = "session_name")
    public String name;

    @DynamicProperty(json_key = "creator_device")
    public String creator_device;


    public RecognitionSessionTrackerFile recognitionSessionTrackerFile = null;


    public RecognitionSession() {
        this.transaction_no = Globals.getTransactionNo();

        this.creator_device = Globals.myDevice().transaction_no;

        this.sync_status = com.realm.annotations.sync_status.pending.ordinal() + "";


    }

    public RecognitionSession(String name) {
        this.name = name;
        this.transaction_no = Globals.getTransactionNo();
        this.creator_device = Globals.myDevice().transaction_no;


        this.sync_status = com.realm.annotations.sync_status.pending.ordinal() + "";


    }

}
