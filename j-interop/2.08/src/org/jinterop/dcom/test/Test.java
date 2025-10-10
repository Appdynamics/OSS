package org.jinterop.dcom.test;

import java.io.IOException;
import java.net.UnknownHostException;

import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbException;
import jcifs.smb.SmbSession;

import org.jinterop.dcom.core.JIComServer;

public class Test {

 public static void main(String[] args) {
	 try {

		 boolean b = null instanceof JIComServer ? false : true;
		 //PLEASE NOTE THAT THE WINDOWS "SERVER" SERVICE SOULD BE RUNNING !!! OTHERWISE THE
		 //GETCHALLENGE WILL FAIL.
//		 UniAddress mydomaincontoller = UniAddress.getByName("itl-hw-lt15522.ad.infosys.com");
//		 byte[] b =  SmbSession.getChallenge(mydomaincontoller,139);
		 int i = 0;
		// NtlmChallenge challenge = SmbSession.getChallengeForDomain();

	     // SUCCESS

	 } catch( SmbAuthException sae ) {
	     // AUTHENTICATION FAILURE
	     sae.printStackTrace();
	 } catch( SmbException se ) {
	     // NETWORK PROBLEMS?
	     se.printStackTrace();
	 } catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

 public void test()
 	{
 		System.out.println("Called back !!!");
 	}

}
