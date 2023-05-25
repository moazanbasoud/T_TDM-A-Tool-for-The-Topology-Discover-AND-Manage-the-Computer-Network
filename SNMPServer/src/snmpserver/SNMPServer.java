/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package snmpserver;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;




/**
 *
 * @author Administrator
 */
public class SNMPServer {

    /**
     * @param args the command line arguments
     * @throws java.net.SocketException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws SocketException, IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH_mm_ss");
        Date date = new Date();
        File file = new File("D:\\DATA\\SNMP Server\\output-"+ dateFormat.format(date)+".txt");
        file.getParentFile().mkdirs();
       
        PrintStream printStream = new PrintStream(new FileOutputStream(file, true)); // Set append mode to true
        
        System.setOut(printStream);
        System.out.println("----------" + System.lineSeparator()); // add separator between each log entry
        
        System.out.println("Output at " + dateFormat.format(date) + ":");
    
        // TODO ccode application logic here
        System.out.println("\n\nTeam Nadir Shah");
        DatagramSocket s = new DatagramSocket(161);
        byte[] msg = new byte[1900];
        DatagramPacket pkt = new DatagramPacket(msg, msg.length);
        
        for (int a = 1; a < 10; a++) {
            
         System.out.println("\n---------------------------------------------------This is "+a+" loop--------------------------------------------------------------");
        s.receive(pkt);
        
        System.out.println("\nMessage Length can be upto "+pkt.getData().length);
        
        int by=0;
        System.out.print("Message is received from IP "+pkt.getAddress()+"\nMessage is received from port "+pkt.getPort()+"\nMessage is received from Socket Address "+pkt.getSocketAddress());

        int type = pkt.getData()[by]& 0xff;
        System.out.println("\n\nMessage Type is "+Integer.toHexString(type));
        System.out.println("This is in "+by+" byte.");
        by++;
        
        
        int len = pkt.getData()[by]&0xff;
        if (len<128){
            System.out.println("Packet Length is "+len);
            System.out.println("This is in "+by+" byte.");
            by++;
        }
        else{
            //len was above 127 so consider the second
            len = pkt.getData()[by]&0x7f;
            by=by+len;
            len = pkt.getData()[by]&0xff;
            System.out.println("Packet Length "+len);
            System.out.println("This is in "+by+" byte.");
        by++;
        }       
        
        int version_type = pkt.getData()[by]&0xff;
        System.out.println("\nVersion type is "+version_type);
        System.out.println("This is in "+by+" byte.");
        by++;
        
        int version_len = pkt.getData()[by]& 0xff;
        if (version_len<128){
            System.out.println("Version Length is "+version_len);
            System.out.println("This is in "+by+" byte.");
            by++;
        }
        else{
            //len was above 127 so consider the second
            version_len = pkt.getData()[by]&0x7f;
            by=by+version_len;
            version_len = pkt.getData()[by]&0xff;
            System.out.println("Version Length "+version_len);
            System.out.println("This is in "+by+" byte.");
        by++;
        }
        
        int version = pkt.getData()[by] & 0xff;
        System.out.println("Version Value is "+version);
        System.out.println("This is in "+by+" byte.");
        by++;
        
        int comm_type = pkt.getData()[by] & 0xff;
        System.out.println("\nCommunity Type "+comm_type);
        System.out.println("This is in "+by+" byte.");
        by++;
        
        int comm_len = pkt.getData()[by]& 0xff;
        if (comm_len<128){
            System.out.println("Snmp Community Length is "+comm_len);
            System.out.println("This is in "+by+" byte.");
            by++;
        }
        else{
            //len was above 127 so consider the second
            comm_len = pkt.getData()[by]&0x7f;
            by=by+comm_len;
            comm_len = pkt.getData()[by]&0xff;
            System.out.println("Snmp Community Length "+comm_len);
            System.out.println("This is in "+by+" byte.");
            by++;
        }
        
        //print community string
        int ctr;
        byte[] comm_string_b = new byte[comm_len];
        
        for (ctr = 0; ctr< comm_len; ctr++)
        {
            
            comm_string_b[ctr]=pkt.getData()[by+ctr];
        }
        String comm_string = new String(comm_string_b);
        System.out.println("Community String is : "+comm_string);
        System.out.println("This is from "+by+" to "+(by+(comm_len-1))+" byte.");
        by=by+comm_len;
        
        
        //we are processing snmpv2 so its format is is giver on link http://www.tcpipguide.com/free/t_SNMPVersion2SNMPv2MessageFormats-5.htm
        //pdu type
        int pdu_type = pkt.getData()[by] & 0xff;
        System.out.println("\nSnmp PDU type is"+ Integer.toHexString(pdu_type).toUpperCase());
        System.out.println("This is in "+by+" byte.");
        by++;
        
        
        int pdu_len = pkt.getData()[by] & 0xff;
        if (pdu_len <128){
            System.out.println("Snmp PDU Length is "+pdu_len);
            System.out.println("This is in "+by+" byte.");
            by++;
        }
        else{
            //len was above 127 so consider the second
            pdu_len = pkt.getData()[by]&0x7f;
            by=by+pdu_len;
            pdu_len = pkt.getData()[by]&0xff;
            System.out.println("Snmp PDU Length "+pdu_len);
            System.out.println("This is in "+by+" byte.");
            by++;
        }
        
        int next_type = pkt.getData()[by] & 0xff;
        System.out.println("\nRequest ID Type "+ next_type);
        System.out.println("This is in "+by+" byte.");
        by++;
        
        int next_len = pkt.getData()[by] & 0xff;
        if (next_len <128){
            System.out.println("Request ID Length is "+next_len);
            System.out.println("This is in "+by+" byte.");
            by++;
        }
        else{
            //len was above 127 so consider the second
            next_len = pkt.getData()[by]&0x7f;
            by=by+next_len;
            next_len = pkt.getData()[by]&0xff;
            System.out.println("Request ID Length "+next_len);
            System.out.println("This is in "+by+" byte.");
            by++;
        }
        
        int next_value = pkt.getData()[by] & 0xff;
        System.out.println("RequestID Value "+next_value);
        System.out.println("This is in "+by+" byte.");
        by++;
        
        int error_type = pkt.getData()[22] & 0xff;
        System.out.println("\nError Type is "+ error_type);
        System.out.println("This is in "+by+" byte.");
        by++;
        
        int error_len = pkt.getData()[23] & 0xff;
        if (error_len <128){
            System.out.println("Error Length is "+error_len);
            System.out.println("This is in "+by+" byte.");
            by++;
        }
        else{
            //len was above 127 so consider the second
            error_len = pkt.getData()[by]&0x7f;
            by=by+error_len;
            error_len = pkt.getData()[by]&0xff;
            System.out.println("Error Length is "+error_len);
            System.out.println("This is in "+by+" byte.");
            by++;
        }
        
        int error_val = pkt.getData()[by] & 0xff;
        System.out.println("Error Value is "+error_val);
        System.out.println("This is in "+by+" byte.");
        by++;
        
        
        int errorind_type = pkt.getData()[25] & 0xff;
        System.out.println("\nError Index Type is "+ errorind_type );
        System.out.println("This is in "+by+" byte.");
        by++;
        
        int errorind_len = pkt.getData()[26] & 0xff;
        if (errorind_len <128){
            System.out.println("Error Index Length is "+errorind_len);
            System.out.println("This is in "+by+" byte.");
            by++;
        }
        else{
            //len was above 127 so consider the second
            errorind_len = pkt.getData()[by]&0x7f;
            by=by+errorind_len;
            errorind_len = pkt.getData()[by]&0xff;
            System.out.println("Error Index Length is "+errorind_len);
            System.out.println("This is in "+by+" byte.");
            by++;
        }
        
        int errorind_val = pkt.getData()[by] & 0xff;
        System.out.println("Error Index Value is "+errorind_val);
        System.out.println("This is in "+by+" byte.");
        by++;
        
        int varbindlist_type = pkt.getData()[by] & 0xff;
        System.out.println("\nVarbind List Type is "+  Integer.toHexString(varbindlist_type).toUpperCase());
        System.out.println("This is in "+by+" byte.");
        by++;
        
        int varbindlist_len = pkt.getData()[by] & 0xff;
        if (varbindlist_len <128){
            System.out.println("Varbind List Length is "+varbindlist_len);
            System.out.println("This is in "+by+" byte.");
            by++;
        }
        else{
            //len was above 127 so consider the second
            varbindlist_len = pkt.getData()[by]&0x7f;
            by=by+varbindlist_len;
            varbindlist_len = pkt.getData()[by]&0xff;
            System.out.println("Varbind List Length is "+varbindlist_len);
            System.out.println("This is in "+by+" byte.");
            by++;
        }

        while(by<len){
            
        System.out.println("\n---------------------------Varbind----------------------------------");
        int varbind_type = pkt.getData()[by] & 0xff;
        System.out.println("\nVarbind Type is "+  Integer.toHexString(varbind_type).toUpperCase());
        System.out.println("This is in "+by+" byte.");
        by++;
        
        int varbind_len = pkt.getData()[by] & 0xff;
        if (varbind_len <128){
            System.out.println("Varbind Length is "+varbind_len);
            System.out.println("This is in "+by+" byte.");
            by++;
        }
        else{
            //len was above 127 so consider the second
            varbind_len = pkt.getData()[by]&0x7f;
            by=by+varbind_len;
            varbind_len = pkt.getData()[by]&0xff;
            System.out.println("Varbind Length is "+varbind_len);
            System.out.println("This is in "+by+" byte.");
            by++;
        }
        
        int oid_type = pkt.getData()[by] & 0xff;
        System.out.println("\nOID Type is "+  Integer.toHexString(oid_type).toUpperCase() );
        System.out.println("This is in "+by+" byte.");
        by++;
        
        int oid_len = pkt.getData()[by] & 0xff;
        if (oid_len <128){
            System.out.println("OID Length is "+oid_len);
            System.out.println("This is in "+by+" byte.");
            by++;
        }
        else{
            //len was above 127 so consider the second
            oid_len = pkt.getData()[by]&0x7f;
            by=by+oid_len;
            oid_len = pkt.getData()[by]&0xff;
            System.out.println("OID Length is "+oid_len);
            System.out.println("This is in "+by+" byte.");
            by++;
        }
        
        int oid_val = pkt.getData()[by] & 0xff;
        System.out.println("OID Value is " + oid_val);        
//My OID Code
System.out.println("OID Value in Proper Format is ");
// here will extract oid length and values from packet


int[] oid_values = new int[oid_len];
for (int i = 0; i < oid_len; i++) {
    oid_values[i] = pkt.getData()[by+i] & 0xff;
}

// here will print oid values in dotted form
for (int i = 0; i < oid_len; i++) {
    System.out.print(oid_values[i]);
    if (i < oid_len - 1) {
        System.out.print(".");
    }
}
System.out.println("\nThis from "+by+" to "+(by+(oid_len-1))+(" byte"));
by=by+oid_len;

        int value_type = pkt.getData()[by] & 0xff;
        System.out.println("\n\nValue Type is "+  value_type );
        System.out.println("This is in "+by+" byte.");
        by++;
        
        int value_len = pkt.getData()[by] & 0xff;
        if (value_len <128){
            System.out.println("Value Length is "+value_len);
            System.out.println("This is in "+by+" byte.");
            by++;
        }
        else{
            //len was above 127 so consider the second
            value_len = pkt.getData()[by]&0x7f;
            by=by+value_len;
            value_len = pkt.getData()[by]&0xff;
            System.out.println("Value Length is "+value_len);
            System.out.println("This is in "+by+" byte.");
            by++;
        }
        
        int value_v = ((pkt.getData()[by] & 0xff) << 16) + 
            ((pkt.getData()[by + 1] & 0xff) << 8) + 
            (pkt.getData()[by + 2] & 0xff);
        System.out.println("Value is "+value_v);
        System.out.println("This is in "+by+" byte.");
        by=by+value_len;
        }
        System.out.println("\n--------------------------------------------------------------------------------------------------------------------------------------------------------------------------" ); 
        
        }
        
        
}
}
