package com.lara.bru.rastreio.DbContext;

import android.provider.BaseColumns;

public final class PostContract {
    private PostContract() {}

    public static class PostEntry implements BaseColumns {
        public static final String TABLE_NAME = "syncData";
        public static final String _ID = "id";
        public static final String COLUMN_NAME_TOKEN = "token";
        public static final String COLUMN_NAME_LNG = "lng";
        public static final String COLUMN_NAME_LAN = "lan";
        public static final String COLUMN_NAME_DATA = "data";
        public static final String COLUMN_NAME_STATUS = "status";
    }
}
