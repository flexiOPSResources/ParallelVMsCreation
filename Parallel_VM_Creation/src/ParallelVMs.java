import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import com.extl.jade.user.UserAPI;
import com.extl.jade.user.UserService;


public class ParallelVMs {
	private static UserService service;
    //Set number of threads to create number of VM's
	private static int THREAD_POOL = 1;
	
	public static void main(String[] args) throws MalformedURLException {
		//Account username
		String userEmailAddress ="";
		//Customer account UUID
		String customerUUID ="";
		String password = "";
		String soapURL="";
	    	
		URL url = null;
	    url = new URL(com.extl.jade.user.UserAPI.class.getResource("."), 
	    		soapURL);
		
	    // Get the UserAPI
	    UserAPI api = new UserAPI(url,
	            new QName("http://extility.flexiant.net", "UserAPI"));
	    // and set the service port on the service
	    service = api.getUserServicePort();
	     
	    // Get the binding provider
	    BindingProvider portBP = (BindingProvider) service;
	     
	    // and set the service endpoint
	    portBP.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
	    		soapURL);
	     
	    // and the caller's authentication details and password
	    portBP.getRequestContext().put(BindingProvider.USERNAME_PROPERTY,
	            userEmailAddress + "/" + customerUUID);
	    portBP.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY,
	            password);
	    
	    CountDownLatch latch = new CountDownLatch(THREAD_POOL);
	    
	    ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL);
        for (int i = 1; i <= THREAD_POOL; i++) {
            Runnable worker = new CreateVMThread("" + i, latch);
            executor.execute(worker);
        }
        try {
        		latch.await();
        } catch(InterruptedException e) {
        		System.out.println("Threads - Interrupted");
        } finally {
	        executor.shutdown();
	        while (!executor.isTerminated()) {
	        }
	        System.out.println("Threads - finished");
        }
	}
	
	protected static UserService getService() {
		return service;
	}

}