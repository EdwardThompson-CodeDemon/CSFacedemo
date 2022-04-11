package sparta.realm.csfacedemo.Verification;

import sparta.realm.DataManagement.Models.Query;
import sparta.realm.Realm;
import sparta.realm.csfacedemo.models.Member;
import sparta.realm.csfacedemo.models.MemberImage;
import sparta.realm.spartautils.biometrics.face.VerificationMemberModel;
import sparta.realm.spartautils.biometrics.face.VerificationModel;



public class Verifier extends VerificationModel {

    @Override
    public boolean canClock(boolean in, String image_transaction_no) {
        String sid = null;
        try {
//            sid = Realm.databaseManager.loadObject(MemberImage.class, new Query().setTableFilters("transaction_no='" + image_transaction_no + "'")).member_id;
            sid = image_transaction_no;
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return in ? Realm.databaseManager.getRecordCount(Clock.class, "member_id='" + sid + "' AND (clock_out_time IS NULL OR clock_out_time ='')") < 1 : !in ? Realm.databaseManager.getRecordCount(Clock.class, "member_id='" + sid + "' AND (clock_out_time IS NULL OR clock_out_time ='')") > 0 : super.canClock(in, sid);
        return super.canClock(in,image_transaction_no);



    }

    @Override
    public VerificationMemberModel loadMember(String tracker_identifier) {
        try {
            String member_id = Realm.databaseManager.loadObject(MemberImage.class, new Query().setTableFilters("transaction_no='" + tracker_identifier + "'")).member_id;

            Member m = Realm.databaseManager.loadObject(Member.class, new Query().setTableFilters("transaction_no='" + member_id + "'"));
            VerificationMemberModel vm = new VerificationMemberModel();
            vm.sid = m.transaction_no;
            vm.displayName = m.name;
            return vm;

        } catch (Exception ex) {
        }
        return super.loadMember(tracker_identifier);
    }
}
