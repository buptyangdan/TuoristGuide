package org.me.tuoristguide.entities;

import org.me.tuoristguide.model.Comment;
import org.me.tuoristguide.model.User;

import java.util.ArrayList;

/**
 * Created by zy on 4/14/16.
 */
public class UserManager {

    private User current_user = null;

    public void createUser(String name, String email){

    }

    public User getCurrentUser() {

        return current_user;
    }


    public ArrayList<Comment> getUserLocalComments() {
        return null;
    }
}
