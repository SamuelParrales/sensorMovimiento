package sepp.arduino;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;


@Component //i
public class SocketTextHandler extends TextWebSocketHandler {
	//Atributos de arduinos
	private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
	private boolean listenArduino = false;
	
	//
    public void afterConnectionEstablished(WebSocketSession session) throws Exception 
    {
        sessions.add(session);
        super.afterConnectionEstablished(session);
        
        if(listenArduino==true)
        	return;
        
        
    	PanamaHitek_Arduino ino = new PanamaHitek_Arduino();
    	SerialPortEventListener listener = new SerialPortEventListener() 
    	{
    		
    		public void serialEvent(SerialPortEvent serialPortEvent) 
    		{
    			
    			try {
    				if(ino.isMessageAvailable())
    				{
    					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    					
    					for(WebSocketSession s:sessions)
    					{
    						s.sendMessage(new TextMessage(dtf.format(LocalDateTime.now())));
    						listenArduino = true;
    					}
    					
    					
    				}
    			} catch (ArduinoException e) {
    				// TODO Auto-generated catch block
    				Logger.getLogger(ArduinoApplication.class.getName()).log(Level.SEVERE, null,e);
    			} catch (SerialPortException e) {
    				// TODO Auto-generated catch block
    				Logger.getLogger(ArduinoApplication.class.getName()).log(Level.SEVERE, null,e);
    			}
    			// TODO Auto-generated method stub
 catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    		}
    	};//Fin de atributos de  arduinos
    	
    	try {
			ino.arduinoRX("COM3",9600, listener);
		} catch (ArduinoException e) {
			Logger.getLogger(ArduinoApplication.class.getName()).log(Level.SEVERE, null,e);
		} catch (SerialPortException e) {
			Logger.getLogger(ArduinoApplication.class.getName()).log(Level.SEVERE, null,e);
		}
    	
    	
    	
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        super.afterConnectionClosed(session, status);
    }
    

}