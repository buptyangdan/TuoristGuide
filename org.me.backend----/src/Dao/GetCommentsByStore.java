package Dao;

import DB.JDBCAdapter;


/**
 * Created by caoyi on 16/4/19.
 */
public class GetCommentsByStore extends JDBCAdapter {
    public GetCommentsByStore(String fileName) {
        super(fileName);
    }
}
