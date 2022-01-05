package com.example.servingwebcontent.users;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    public static List<OLGUser> OLGUsers = new ArrayList<>();
    public static String SUPERADMIN = "SUPERADMIN";
    public static String ADMIN = "ADMIN";
    public static String USER = "USER";

    public static OLGUser getUserByEmail(String email) {
        for (OLGUser user : OLGUsers) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    public static OLGUser getUserByUid(String uid) {
        for (OLGUser user : OLGUsers) {
            if (user.getUid().equals(uid)) {
                return user;
            }
        }
        return null;
    }

    public static void setUserByUid(String uid, OLGUser user) {
        for (int i = 0; i < OLGUsers.size(); i++) {
            if (OLGUsers.get(i).getUid().equals(uid)) {
                OLGUsers.set(i, user);
                return;
            }
        }
        System.out.println("DIDN'T FIND USER BY UID " + uid);
    }

    public static void removeUserByUid(String uid) {
        OLGUsers.removeIf(user -> user.getUid().equals(uid));
    }
}
