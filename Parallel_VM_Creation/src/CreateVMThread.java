import java.util.GregorianCalendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import com.extl.jade.user.Disk;
import com.extl.jade.user.ExtilityException;
import com.extl.jade.user.FetchParameters;
import com.extl.jade.user.Image;
import com.extl.jade.user.Job;
import com.extl.jade.user.NetworkType;
import com.extl.jade.user.Nic;
import com.extl.jade.user.ResourceType;
import com.extl.jade.user.Server;
import com.extl.jade.user.VirtualizationType;

public class CreateVMThread implements Runnable {
	static String clusterUUID = "";
	static String vdcUUID = "";
	static String serverNamePrefix = "";
	static String imageUUID ="";
	static String networkUUID ="";
	
	// Product offer - Server Size
	static String serverProductOfferUUID = "1359c779-5b45-353a-895d-4b2bd317969f";
	static int cpuSize ;
	static int ramSize ;
	// Product offer - Disk
	static String diskProductOfferUUID = "0b54fac2-ce18-3b93-8285-5c827c00cf35";
	static int diskSize = 100;
	
	String serverName;
	private CountDownLatch latch;
	
	public CreateVMThread(String serverNo, CountDownLatch latch) {
		serverName = serverNamePrefix + serverNo;
		this.latch = latch;
	}

	@Override
	public void run() {
		// Create a resource disk using Standard disk product offer and set basic setting
		Server server = new Server();
		Disk disk = new Disk();
		
		disk.setClusterUUID(clusterUUID);
	    disk.setProductOfferUUID(diskProductOfferUUID);
	    disk.setIso(true);
	    disk.setResourceName(serverName);
	    disk.setResourceType(ResourceType.DISK);
	    disk.setSize(diskSize);
	    disk.setVdcUUID(vdcUUID);
	    // Create a server resource using Standard server product offer 
	    // and set basic settings
	    server.setClusterUUID(clusterUUID);
	    server.setImageUUID(imageUUID);
	    server.setProductOfferUUID(serverProductOfferUUID);
	    server.setCpu(cpuSize);
	    server.setRam(ramSize);
	    server.getDisks().add(disk);
	    server.setResourceName(serverName);
	    server.setResourceType(ResourceType.SERVER);
		server.setVdcUUID(vdcUUID);
		server.setVirtualizationType(VirtualizationType.VIRTUAL_MACHINE);
		// Add NIC card to the server
		Nic nicCard = new Nic();
		nicCard.setClusterUUID(clusterUUID);
		nicCard.setNetworkUUID(networkUUID);
		nicCard.setNetworkType(NetworkType.IP);
		nicCard.setResourceName("Nic-Card-1");
		nicCard.setResourceType(ResourceType.NIC);
		nicCard.setVdcUUID(vdcUUID);
		server.getNics().add(nicCard);

		String serverUUID = null;
	    try {
	    		int waitPeriod = ThreadLocalRandom.current().nextInt(30,100) * 1000;
	    		System.out.println("Wait period to create server- " + waitPeriod + " ms");
	    		Thread.sleep(waitPeriod);
	        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
			Job job = ParallelVMs.getService().createServer(server, null, null, null);
			job.setStartTime(datatypeFactory.newXMLGregorianCalendar(new GregorianCalendar()));
			// Wait for the server to be created
			Job response = ParallelVMs.getService().waitForJob(job.getResourceUUID(), true);
			serverUUID = response.getItemUUID();
			if (response.getErrorCode() == null) {
				System.out.println("Server created successfully" + serverUUID);
			} else {
				System.out.println("Failed to create server. Error: " + 
						response.getErrorCode());
			}
		
	    } catch (ExtilityException | DatatypeConfigurationException | InterruptedException e) {
			System.out.println("Exception caught: " + e.getMessage());
			
		} finally {
			latch.countDown();
		}
	}
}