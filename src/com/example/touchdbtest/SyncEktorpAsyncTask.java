package com.example.touchdbtest;


import org.ektorp.DbAccessException;
import org.ektorp.android.util.EktorpAsyncTask;

import android.util.Log;

public abstract class SyncEktorpAsyncTask extends EktorpAsyncTask {

	@Override
	protected void onDbAccessException(DbAccessException dbAccessException) {
		Log.e("CouchbaseAsyncTask", "DbAccessException in background", dbAccessException);
	}

}
