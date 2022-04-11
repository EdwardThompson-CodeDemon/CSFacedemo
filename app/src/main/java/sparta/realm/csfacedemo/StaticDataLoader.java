package sparta.realm.csfacedemo;

import android.content.Context;


import java.util.ArrayList;

import sparta.realm.Realm;
import sparta.realm.csfacedemo.activities.ImportPhotosActivity;
import sparta.realm.csfacedemo.activities.SessionManagementActivity;
import sparta.realm.spartautils.app_control.models.module;


public class StaticDataLoader {


    public static ArrayList<module> sideMenuModules() {
        Context context = Realm.context;
        ArrayList<module> modules = new ArrayList<>();
        {
            module m = new module(ImportPhotosActivity.class.getName(), "Create session", true);
            m.icon = context.getDrawable(R.drawable.face_rec_white_icon);
            modules.add(m);
        }


        {
            module m = new module(SessionManagementActivity.class.getName(), "Session Management", true);
            m.icon = context.getDrawable(R.drawable.face_rec_white_icon);
            modules.add(m);
        }





        modules.add(new module("", "-------------------------", true));
        module m8 = new module(ImportPhotosActivity.class.getName(), "Configuration", true);
        m8.icon = context.getDrawable(R.drawable.ic_4);
        modules.add(m8);
        module m6 = new module("a", "Logout", true);
        m6.icon = context.getDrawable(R.drawable.ic_logout);
        modules.add(m6);

        module m7 = new module("b", "Exit", true);
        m7.icon = context.getDrawable(R.drawable.ic_s_6);
        modules.add(m7);

        modules.add(new module("", "---------DEV--------", true));
        modules.add(new module("1", "Reset Everything", true));
        modules.add(new module("2", "Reset Sessions", true));
        modules.add(new module("3", "Reset Members", true));
        modules.add(new module("4", "Reset Images", true));
        modules.add(new module("5", "Reset Sessions", true));


        return modules;

    }

}
