package sepp.arduino;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;
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


@Component
public class SocketTextHandler extends TextWebSocketHandler {
	//Atributos de arduinos
	private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
	
	//
    public void afterConnectionEstablished(WebSocketSession session) throws Exception 
    {
    	//Codigo de prueba
        sessions.add(session);
        super.afterConnectionEstablished(session);
        //fin de codigo de prueba
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
			// TODO Auto-generated catch block
			Logger.getLogger(ArduinoApplication.class.getName()).log(Level.SEVERE, null,e);
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			Logger.getLogger(ArduinoApplication.class.getName()).log(Level.SEVERE, null,e);
		}
    	
    	
    	
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        super.afterConnectionClosed(session, status);
    }
    
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {

		String payload = message.getPayload();
		JSONObject jsonObject = new JSONObject(payload);
		session.sendMessage(new TextMessage("Hi " + jsonObject.get("user") + " how may we help you?"));
	}
}