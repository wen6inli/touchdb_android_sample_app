package com.example.touchdbtest;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.couchbase.touchdb.router.TDURLStreamHandlerFactory;

public class MainActivity extends Activity {

    {
        TDURLStreamHandlerFactory.registerSelfIgnoreError();
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		CouchbaseUtils.startTouchDB(getBaseContext());
		
		
//	    TDServer server = null;
//	    String filesDir = getFilesDir().getAbsolutePath();
//	    try {
//	        server = new TDServer(filesDir);
//	    } catch (IOException e) {
//	        Log.e("MAIN", "Error starting TDServer", e);
//	    }
//	    
//	    HttpClient httpClient = new TouchDBHttpClient(server);
//        CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
//        
//        // create a local database
//        CouchDbConnector dbConnector = dbInstance.createConnector("testdb", true);
//
//        // pull this database to the test replication server
//        ReplicationCommand pullCommand = new ReplicationCommand.Builder()
//            .source("http://staging1.bi.sv3.us:5984/vehicle")
//            .target("vehicle")
//            .continuous(true)
//            .build();
//
//        ReplicationStatus status = dbInstance.replicate(pullCommand);   
//                       
//        String dDocName = "vehicle";
//        String viewName = "vehicle";
//        
//		TDDatabase db = server.getDatabaseNamed("vehicle");
//        TDView view = db.getViewNamed(String.format("%s/%s", dDocName, viewName));
//        
//        
//        view.setMapReduceBlocks(new TDViewMapBlock() {
//
//            @Override
//            public void map(Map<String, Object> document, TDViewMapEmitBlock emitter) {
//				emitter.emit(document.get("license_plate_number").toString(), document);
//            }
//        }, new TDViewReduceBlock() {
//                public Object reduce(List<Object> keys, List<Object> values, boolean rereduce) {
//                        return null;
//                }
//        }, "1.0");
//        
        
        
	
     
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	public void onClick(View view) {
		TextView tv = (TextView) findViewById(R.id.editText1);
		String keyword = tv.getText().toString();

	    long startTime = System.currentTimeMillis();


		
		
		List<Vehicle> list = VehicleRepository.getInstance().findByPL(keyword);
		if (list.size()> 0)
			Toast.makeText(this, "Vehicle Found", Toast.LENGTH_LONG).show();
		else 
			Toast.makeText(this, "Vehicle Not Found", Toast.LENGTH_LONG).show();

	    long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    Toast.makeText(this, "Cost" + elapsedTime, Toast.LENGTH_LONG).show();

	    
	}
}
