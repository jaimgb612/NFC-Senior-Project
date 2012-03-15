package com.nfc.demo;

import com.data.connection.Connection;
import com.data.connection.ConnectionException;
import com.message.ndef.NdefParser;
import com.message.ndef.ParserException;
import com.nfc.reader.ACR122Reader;
import com.nfc.reader.Reader;
import com.nfc.reader.ReaderException;
import com.nfc.reader.UnsupportedDeviceException;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

public class NFCDemo extends JFrame implements ActionListener,InternalFrameListener {
		
		private static final long serialVersionUID = 805619783377478380L;
		public JMenuBar menuBar;
		public JMenu fileMenu;
		public JMenuItem examination ;
		public JMenuItem examItem;
		public JInternalFrame iFrame;
		public JDesktopPane desktop;
		
		
		// Fields 
		
		JLabel lbId = new JLabel("Identification Number");
		   
		   JLabel lbName = new JLabel("Name");
		   JLabel lbSurname = new JLabel("Surname");
		   JLabel IbBloodType = new JLabel("Blood Type");
		   JLabel lbBloodValue = new JLabel("Blood Value");
		   JLabel lbDoctorSurname= new JLabel("Diagnois");
		   JLabel lbDate = new JLabel("Date");
		   JTextField txtName = new JTextField();
		   JTextField txtSurname = new JTextField();
		   JTextField txtBloodType = new JTextField();
		   JTextField txtDate = new JTextField();
		   JTextField txtDiagnosis = new JTextField();

		   JTextField txtId = new JTextField();
		   
		   NFCListener listener = new NFCListener();
		
		
		public NFCDemo()
		{
			this.setTitle("Welcome to Epriv");
			this.setSize(this.getMaximumSize());
			this.setContentPane(new JDesktopPane());
			//Create the MenuBar
			menuBar = new JMenuBar();
			//Create Menu
			fileMenu = new JMenu("File");
			//Create menu Item
			examItem = new JMenuItem("New Examination");
			examItem.addActionListener((ActionListener) this);
			fileMenu.add(examItem);
			menuBar.add(fileMenu);
			this.setJMenuBar(menuBar);
			this.setVisible(true);
		}
		
		public NFCListener getListener()
		{
			return this.listener;
		}
		public void openExamination(Patient p)
		{
			  iFrame = new JInternalFrame("Examination Info");
		      createInternalFrame();
		      iFrame.setResizable(false);
		      iFrame.setClosable(true);
		      iFrame.setMaximizable(true);
		      iFrame.setIconifiable(true);
		      iFrame.setSize(this.getSize());
		      iFrame.setLocation(0,0);
		      iFrame.addInternalFrameListener((InternalFrameListener) this);
		      iFrame.setVisible(true);
		      this.getContentPane().add(iFrame);
		      if(p != null)
		      {
		    	  this.txtId.setText(p.getID());
		    	  this.txtName.setText(p.getName());
		    	  this.txtSurname.setText(p.getSurname());
		    	  this.txtBloodType.setText(p.getBlood());
		      }
		}
		
		
		public void actionPerformed(ActionEvent e) {
			      openExamination(null);
			   }
			   public void internalFrameActivated(InternalFrameEvent e) {
			      System.out.println("Internal frame activated");
			   }
			   public void internalFrameClosed(InternalFrameEvent e) {
			      System.out.println("Internal frame closed");
			   }
			   public void internalFrameClosing(InternalFrameEvent e) {
			      System.out.println("Internal frame closing");
			   }
			   public void internalFrameDeactivated(InternalFrameEvent e) {
			      System.out.println("Internal frame deactivated");
			   }
			   public void internalFrameDeiconified(InternalFrameEvent e) {
			      System.out.println("Internal frame deiconified");
			   }
			   public void internalFrameIconified(InternalFrameEvent e) {
			      System.out.println("Internal frame iconified");
			   }
			   public void internalFrameOpened(InternalFrameEvent e) {
			      System.out.println("Internal frame opened");
			   }
			   
			   public void createInternalFrame()
			   {
				   
				   txtId.setSize(10, 10);
				   txtId.setSize(10, 10);

				   txtId.setSize(10, 10);
				   txtId.setSize(10, 10);
				   txtId.setSize(10, 10);
				   txtDate.setSize(10, 10);
				   txtDiagnosis.setSize(10, 10);
				   
				   JPanel panel0 = new JPanel(new GridLayout(0,2));
				   
				   panel0.setSize(300, 400);
				   panel0.add(lbId);
				   panel0.add(txtId);
				 			  
				   panel0.add(lbName);
				   panel0.add(txtName);
				   
				   panel0.add(lbSurname);
				   panel0.add(txtSurname);
				   
				   panel0.add(IbBloodType);
				   panel0.add(txtBloodType);
				   
				   iFrame.add(panel0);
				   
	}
	
	public static Patient getPatientInfo(String message)
	{
		String[] details = message.split(";");
		Patient patient = new Patient();
		patient.setID(details[0]);
		patient.setName(details[1]);
		patient.setSurname(details[2]);
		patient.setBlood(details[3]);
		return patient;	
	}
	
	
	
	public  void Handler(String message)
	{
		System.out.println("Handler : " + message);
		Patient patient = getPatientInfo(message);
		openExamination(patient);
		
	}
	
	public  class NFCListener extends Thread
	{
		public void run()
		{
			NdefParser parser = new NdefParser(true);
			
			Reader reader = new ACR122Reader(true);
			byte[] result  = null;
			byte[] message = null;
					
			while (!Thread.interrupted() && message == null) {
				try {
					if (reader.waitForDeviceReady(500)) {					
						Connection con = reader.getConnection();
						
						
						if(con != null)
						{
							result  = con.receiveData();
							parser.parse(result);
							message = parser.getPayload();
							
//							System.out.println("Byte Message : " + NfcUtils.byteArrayToHexString(message));
//							System.out.println("Localization : " + new String(parser.getLocale()));
//							System.out.println("Only Message : " + new String(message));
							Handler(new String(message));
						}
					}
				}
				catch(ParserException pe)
				{
					pe.printStackTrace();
				}
				catch(ReaderException e) {
					e.printStackTrace();
				}
				catch(ConnectionException ce) {
					ce.printStackTrace();
				}
				catch(UnsupportedDeviceException usde)
				{
					usde.printStackTrace();
				}

				try {
					reader.disconnect();
				}
				catch(ReaderException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
}
