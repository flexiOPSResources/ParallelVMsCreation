# ParallelVMsCreation

Java program used to create a number of VM's using multiple threads within a Flexiant Cloud Orchestration platform.

A number of variables are required to be set within both classes to create VMs.
Within ParallelVMs.java:

**int THREAD_POOL** (Number of threads/VMs to be created)

**String userEmailAddress** (User email address for FCO account)

**String customerUUID** (Customer UUID address for FCO account)

**String password** (User password address for FCO account)

**String soapURL** (SOAP endpoint for FCO platform)

Within CreateVMThread.java:

**int THREAD_POOL** (Number of threads/VMs to be created)

**String vdcUUID** (User email address for FCO account)

**String serverNamePrefix** (Customer UUID address for FCO account)

**String imageUUID** (User password address for FCO account)

**String networkUUID** (SOAP endpoint for FCO platform)static String clusterUUID = "";

**serverProductOfferUUID**(Number of threads/VMs to be created)

**int cpuSize** (Number of threads/VMs to be created)

**int ramSize**(Number of threads/VMs to be created)

**String diskProductOfferUUID**(Number of threads/VMs to be created)

**int diskSize**(Number of threads/VMs to be created)

Once these parameters are correctly set, the script will create and start a new server on the FCO platform for the specified customer.
