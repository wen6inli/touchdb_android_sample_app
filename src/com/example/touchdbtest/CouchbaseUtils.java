package com.example.touchdbtest;


import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.ReplicationCommand;
import org.ektorp.http.HttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import android.content.Context;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.util.Log;

import com.couchbase.touchdb.TDDatabase;
import com.couchbase.touchdb.TDServer;
import com.couchbase.touchdb.TDView;
import com.couchbase.touchdb.TDViewMapBlock;
import com.couchbase.touchdb.TDViewMapEmitBlock;
import com.couchbase.touchdb.ektorp.TouchDBHttpClient;
import com.couchbase.touchdb.support.HttpClientFactory;

public class CouchbaseUtils {

	public static String TAG = "Sync";

	// constants
	public static final String DATABASE_AGENDA = "agenda";
	public static final String DATABASE_LOG = "log";
	public static final String DATABASE_DRIVER = "driver";
	public static final String DATABASE_VEHICLE = "vehicle";
	public static final String DATABASE_GROUP = "group";
	private static final String VENDOR = "vendor";
	private static final String TENANT = "tenant";

	public static final String dDocId = "_design/";
	public static final String AllViewName = "all";
	public static final String CustomViewName = "hash";

	public static ArrayList<SyncEktorpAsyncTask> asyncTasks = new ArrayList<SyncEktorpAsyncTask>();

	protected static TDServer server;

	protected static ServiceConnection couchServiceConnection;
	protected static HttpClient httpClient;

	// ektorp impl
	protected static CouchDbInstance dbInstance;
	protected static CouchDbConnector connectorVehicle;
	
	public static CouchDbConnector couchDbConnector;

	
	public static CouchDbConnector getConnectorVehicle() {
		return connectorVehicle;
	}

	
	public static Context context;

	public static void Destroy() {
		Log.v(TAG, "onDestroy");
		if (context != null)
			context.unbindService(couchServiceConnection);

		// clean up our http client connection manager
		if (httpClient != null) {
			httpClient.shutdown();
		}
	}

	public static void startTouchDB(Context c) {
		context = c;
		String filesDir = context.getFilesDir().getAbsolutePath();
		try {
			server = new TDServer(filesDir);
		} catch (IOException e) {
			Log.e(TAG, "Error starting TDServer", e);
		}

//		// register our own custom HttpClientFactory
//		server.setDefaultHttpClientFactory(new HttpClientFactory() {
//
//			@Override
//			public org.apache.http.client.HttpClient getHttpClient() {
//
//			    // start with a default http client
//			    DefaultHttpClient httpClient = new DefaultHttpClient();
//
//			    // create a credentials provider to store our credentials
//			    BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
//
//			    // store our credentials (make sure the URL is not prefixed by https:// if using SSL)
//			    AuthScope myScope = new AuthScope("staging1.bi.sv3.us", 5984);
//			    Credentials myCredentials = new UsernamePasswordCredentials("admin","admin");
//			    credsProvider.setCredentials(myScope, myCredentials);
//
//			    // set the credentials providerad
//			    httpClient.setCredentialsProvider(credsProvider);
//
//			    // add an interceptor to sent the credentials preemptively
//			    HttpRequestInterceptor preemptiveAuth = new HttpRequestInterceptor() {
//
//			        @Override
//			        public void process(HttpRequest request,
//			                HttpContext context) throws HttpException,
//			                IOException {
//			            AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
//			            CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(
//			                    ClientContext.CREDS_PROVIDER);
//			            HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
//
//			            if (authState.getAuthScheme() == null) {
//			                AuthScope authScope = new AuthScope(targetHost.getHostName(), targetHost.getPort());
//			                Credentials creds = credsProvider.getCredentials(authScope);
//			                authState.setCredentials(creds);
//			                authState.setAuthScheme(new BasicScheme());
//			            }
//			        }
//			    };
//
//			    httpClient.addRequestInterceptor(preemptiveAuth, 0);
//
//			    return httpClient;
//			}
//			});

		creatView();

//		Actions.sendCouchbaseStartBroadcast(context);
		startEktorp();
	}

	private static void creatView() {
 
 
		
		TDViewMapBlock vehicle_mapFunction = new TDViewMapBlock() {
			@Override
			public void map(Map<String, Object> document, TDViewMapEmitBlock emitter) {
				emitter.emit(document.get("license_plate_number").toString(), document);
			}
		};

		createView(DATABASE_VEHICLE, vehicle_mapFunction);

 
	}

	private static void createView(String db_name, TDViewMapBlock tdViewMapBlock) {
		createView(db_name, null, tdViewMapBlock);
	}

	private static void createView(String db_name, String view_name, TDViewMapBlock tdViewMapBlock) {
		TDDatabase db = server.getDatabaseNamed(db_name);
		TDView view = db.getViewNamed(String.format("%s/%s", capitalize(db_name), view_name == null ? "view"
				: view_name));
 

		view.setMapReduceBlocks(tdViewMapBlock, null, "1.0");

	}

	protected static void startEktorp() {
		Log.v(TAG, "starting ektorp");

		if (httpClient != null) {
			httpClient.shutdown();
		}

		httpClient = new TouchDBHttpClient(server);
		dbInstance = new StdCouchDbInstance(httpClient);

		SyncEktorpAsyncTask startupTask = new SyncEktorpAsyncTask() {
			@Override
			protected void doInBackground() {
				connectorVehicle = dbInstance.createConnector(DATABASE_VEHICLE, true);
			}

			@Override
			protected void onSuccess() {
				// init the vendor and tenant info

				// try to reconnect to socket if not connected every 3 seconds
				// this delay also allows socket to establish itself

				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						String url = "http://10.100.131.157:5984";			 
						startPullReplications(url, DATABASE_VEHICLE);	 

						return null;
					}
				}.execute();

			}
		};
		startupTask.execute();
	}
 

	private static void startPullReplications(String url, final String databaseName) {

		final ReplicationCommand pullReplicationCommand = new ReplicationCommand.Builder()
				.source(url + "/" + databaseName).target(databaseName).continuous(true).build();

		SyncEktorpAsyncTask pullReplication = new SyncEktorpAsyncTask() {

			@Override
			protected void onSuccess() {
				super.onSuccess();
				// Actions.sendCouchbasePullReplicationsFinishBroadcast(context,
				// databaseName);
			}

			@Override
			protected void doInBackground() {
				dbInstance.replicate(pullReplicationCommand);
				System.out.println("replication begin");
			}
		};
		pullReplication.execute();
		asyncTasks.add(pullReplication);
	}

	public void stopEktorp() {
	} 

	private static JsonNode getJsonNode(Object o) {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = null;
		try {
			String json = mapper.writeValueAsString(o);
			jsonNode = mapper.readTree(json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return jsonNode;
	}

	 
	
	
	public static String capitalize(final String str) {
		final char[] buffer = str.toCharArray();
		boolean capitalizeNext = true;
		for (int i = 0; i < buffer.length; i++) {
			final char ch = buffer[i];
			if (capitalizeNext) {
				buffer[i] = Character.toTitleCase(ch);
				capitalizeNext = false;
			}
		}
		return new String(buffer);
	}	
	
}
