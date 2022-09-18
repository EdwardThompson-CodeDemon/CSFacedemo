package sparta.realm.csfacedemo.activities;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.luxand.FSDK;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import sparta.realm.Activities.SpartaAppCompactActivity;
import sparta.realm.DataManagement.Models.Query;
import sparta.realm.Realm;
import sparta.realm.csfacedemo.MyApplication;
import sparta.realm.csfacedemo.adapters.MemberAccountAdapter;
import sparta.realm.csfacedemo.databinding.ActivityImportPhotosBinding;
import sparta.realm.csfacedemo.models.Member;
import sparta.realm.csfacedemo.models.MemberImage;
import sparta.realm.csfacedemo.models.RecognitionSession;
import sparta.realm.csfacedemo.models.RecognitionSessionTrackerFile;
import sparta.realm.csfacedemo.utils.Storage.StorageManager;
import sparta.realm.spartautils.svars;

public class ImportPhotosActivity extends SpartaAppCompactActivity {

    ActivityImportPhotosBinding binding;
    RecognitionSession recognitionSession = new RecognitionSession();
    ArrayList<Member> members = new ArrayList<>();//https://we.tl/t-g5qIfJnrUu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImportPhotosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
    }


    void initUI() {
        binding.include.title.setText("Create recognition session");
        binding.include.back.setOnClickListener(v -> onBackPressed());
        binding.memberList.setLayoutManager(new LinearLayoutManager(this));
        binding.memberList.setAdapter(new MemberAccountAdapter(members, new MemberAccountAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Member mem, View view) {

            }
        }));

        binding.createSession.setOnClickListener(view -> saveSession());

        binding.folderPath.setOnClickListener(view -> StorageManager.selectFile(Environment.getExternalStorageDirectory().getAbsolutePath(), new StorageManager.FileBrowserInterface() {
            @Override
            public void onFleSelected(String filePath) {
                Log.e(logTag, "Loaded filepath:" + filePath);
                binding.folderPath.setText(filePath);
                members.clear();
                members = loadMembersFromFolder(filePath);
                binding.memberList.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCanceled() {

            }
        }, true));
    }


    ArrayList<Member> loadMembersFromFolder(String folder_path) {//Recursive loading of files in folders inside folders

        File[] files = new File(folder_path).listFiles();
        int fsize = files.length;
        binding.progressBar2.setMax(fsize);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < fsize; i++) {
                    File file = files[i];
                    if (file.isFile()) {
                        try {
                            if (BitmapFactory.decodeFile(file.getAbsolutePath()) != null) {
                                copyFile(file.getAbsolutePath(), svars.current_app_config(act).file_path_employee_data + file.getName());
                                if (new File(svars.current_app_config(act).file_path_employee_data + file.getName()).exists()) {
                                    Member m = new Member(recognitionSession.transaction_no, file.getName());
                                    m.profile_photo = new MemberImage(m.transaction_no, file.getName());
                                    m.profile_photo.member_id = m.transaction_no;
                                    members.add(m);
                                }

                            }


                        } catch (Exception ex) {

                            Log.e(logTag, "Error in import: " + ex.getMessage());
                        }

                    } else {
                        members.addAll(loadMembersFromFolder(files[i].getAbsolutePath()));
                    }
                    int finalI = i + 1;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (binding.progressBar2) {//I should stop overthinking
                                binding.progressBar2.setProgress(finalI);
                                binding.createSession.setText(finalI == fsize ? "Uploading ..." : "Loading " + finalI + " of " + fsize);
                                if (finalI == finalI) {
                                    binding.memberList.getAdapter().notifyDataSetChanged();
                                    binding.progressBar3.setMax(fsize);

                                }
                            }

                        }
                    });

                }


                saveAllFacesToTracker(members);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.memberList.getAdapter().notifyDataSetChanged();
                        binding.createSession.setText("Create session");

                    }
                });
            }
        }).start();


        return members;
    }

    public void saveAllFacesToTracker(ArrayList<Member> members) {
//        ArrayList<Member> members1=new ArrayList<>();
        recognitionSession.recognitionSessionTrackerFile = new RecognitionSessionTrackerFile("Memory" + System.currentTimeMillis() + getAlphaNumericString(10) + ".dat", recognitionSession.transaction_no);
        FSDK.HTracker randomTracker = Tracker(recognitionSession.recognitionSessionTrackerFile.name);
        int loaded_images = 0;
        int total_images = members.size();
        for (int i = 0; i < total_images; i++) {
            Member member = members.get(i);
            for (int j = 0; j < 20; j++) {
                int finalLoaded_images1 = loaded_images;
                int finalJ = j;
                int finalI = i;
                runOnUiThread(() -> {
                    binding.createSession.setText(finalI + " Uploaded " + finalLoaded_images1 + " of " + total_images + "Try:" + finalJ);
                    binding.progressBar3.setSecondaryProgress(finalLoaded_images1);
                });
                if (save_image_face(randomTracker, member.profile_photo.transaction_no, svars.current_app_config(act).file_path_employee_data + member.profile_photo.image)) {
                    loaded_images++;
                    int finalLoaded_images = loaded_images;
                    runOnUiThread(() -> binding.info.setText("Out of the " + total_images + " member accounts you are trying to import ,only " + finalLoaded_images + " accounts with images can be loaded and created"));

                    member.profile_photo.loaded_to_tracker = "1";
//                    members1.add(member);
                    Log.e(logTag, loaded_images + " of " + total_images);

                    break;
                }
            }


        }

    }
    FSDK.HTracker Tracker(String data_file_name) {
        FSDK.HTracker mTracker = new FSDK.HTracker();
        randomTrackerPath = Uri.parse(svars.current_app_config(act).file_path_employee_data) + data_file_name;
        if (FSDK.FSDKE_OK != FSDK.LoadTrackerMemoryFromFile(mTracker, randomTrackerPath)) {
            int res = FSDK.CreateTracker(mTracker);
            if (FSDK.FSDKE_OK != res) {
                return null;
            }
        }
        int errpos[] = new int[1];
        FSDK.SetTrackerMultipleParameters(mTracker, "ContinuousVideoFeed=true;FacialFeatureJitterSuppression=0;RecognitionPrecision=1;Threshold=0.996;Threshold2=0.9995;ThresholdFeed=0.97;MemoryLimit=2000;HandleArbitraryRotations=false;DetermineFaceRotationAngle=false;InternalResizeWidth=70;FaceDetectionThreshold=3;", errpos);
        if (errpos[0] != 0) {
            return null;
        }
        return mTracker;
    }


    /**
     * @param n length of the output
     * @return lower case String
     */
    static String getAlphaNumericString(int n)///edited to return only lower case
    {

        // chose a Character random from this String
//        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
//                + "0123456789"
//                + "abcdefghijklmnopqrstuvxyz";
        String AlphaNumericString = "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    public static boolean save_image_face(FSDK.HTracker mTracker, String transaction_no, String image_fullpath) {
        FSDK.HImage Image = new FSDK.HImage();
        int i = FSDK.LoadImageFromFile(Image, image_fullpath);
        Log.e(logTag, "Loading result " + i);
        long IDs[] = new long[5];
        long face_count[] = new long[1];
        i = FSDK.FeedFrame(mTracker, 0, Image, face_count, IDs);
        Log.e(logTag, "Feed frame result " + i);
        Log.e(logTag, "Feed frame face count " + face_count[0]);
        for (int j = 0; j < face_count[0]; ++j) {
            boolean named = false;
            if (IDs[i] != -1) {
                String names[] = new String[1];
                FSDK.GetAllNames(mTracker, IDs[i], names, 1024);
                if (names[0] != null && names[0].length() > 0) {
                    Log.e(logTag, "Name found " + names[0] + " For id:" + IDs[i]+".Purging");
                    named = true;
                    FSDK.PurgeID(mTracker, IDs[i]);

                    FSDK.FreeImage(Image);
                    return false;
                }
                if (!named) {
                    Log.e(logTag, "Saving to tracker");
//                    save_face_to_tracker(IDs[i],transaction_no);
                    FSDK.LockID(mTracker, IDs[i]);
                    FSDK.SetName(mTracker, IDs[i], transaction_no);
                    if (transaction_no.length() <= 0) FSDK.PurgeID(mTracker, IDs[i]);
                    FSDK.SaveTrackerMemoryToFile(mTracker, randomTrackerPath);
                    FSDK.UnlockID(mTracker, IDs[i]);
                    FSDK.FreeImage(Image);
                    return true;
                } else {
                    Log.e(logTag, "Image data exists in the tracker ");
                    FSDK.FreeImage(Image);
                    return false;

                }

            }
        }
        try {
            FSDK.FreeImage(Image);
        } catch (Exception EX) {
        }
        return false;
    }

    static String randomTrackerPath;

    FSDK.HTracker Randomtracker() {
        FSDK.HTracker mTracker = new FSDK.HTracker();
        randomTrackerPath = Uri.parse(svars.current_app_config(act).file_path_employee_data) + "Memory" + System.currentTimeMillis() + getAlphaNumericString(10) + ".dat";
        if (FSDK.FSDKE_OK != FSDK.LoadTrackerMemoryFromFile(mTracker, randomTrackerPath)) {
            int res = FSDK.CreateTracker(mTracker);
            if (FSDK.FSDKE_OK != res) {
                return null;
            }
        }
        int errpos[] = new int[1];
        FSDK.SetTrackerMultipleParameters(mTracker, "ContinuousVideoFeed=true;FacialFeatureJitterSuppression=0;RecognitionPrecision=1;Threshold=0.996;Threshold2=0.9995;ThresholdFeed=0.97;MemoryLimit=2000;HandleArbitraryRotations=false;DetermineFaceRotationAngle=false;InternalResizeWidth=70;FaceDetectionThreshold=3;", errpos);
        if (errpos[0] != 0) {
            return null;
        }
        return mTracker;
    }




    private void copyFile(String inputPath, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File(new File(outputPath).getParent());
            if (!dir.exists()) {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
//            new File(inputPath ).delete();//moving not copying


        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

    void saveSession() {
        recognitionSession.name = binding.name.getText().toString();
        if (Realm.databaseManager.loadObject(RecognitionSession.class, new Query().setTableFilters("name='" + recognitionSession.name + "'")) == null && Realm.databaseManager.insertObject(recognitionSession) && Realm.databaseManager.insertObject(recognitionSession.recognitionSessionTrackerFile)) {
            for (Member m : members) {
                if (m.profile_photo.loaded_to_tracker!=null&&m.profile_photo.loaded_to_tracker.equalsIgnoreCase("1")) {
//                    members1.add(m);
                    Realm.databaseManager.insertObject(m);
                    Realm.databaseManager.insertObject(m.profile_photo);

                }else{
                    File f=new File(svars.current_app_config(act).file_path_employee_data+m.profile_photo.image);
                    f.delete();


                }

            }
            MyApplication.rcso.upload("2");
            MyApplication.rcso.upload("4");
            MyApplication.rcso.upload("6");
            MyApplication.rcso.upload("8");

            finish();
        }

    }


}