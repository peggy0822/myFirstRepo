package mid_stu;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.stream.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 *
 * @author Jason
 */
public class CarsXmlRW extends DefaultHandler {

	private SAXParser saxParser;
    private StringWriter stringWriter;
    private XMLOutputFactory xmlOutputFactory;
    private XMLStreamWriter xmlStreamWriter;
    private String str;
    private List<Car> carList;
    private String name;
    private int price;
    private boolean imported;
	protected boolean bname;
	protected boolean bprice;

    public CarsXmlRW() throws ParserConfigurationException, SAXException, IOException {
        carList = new ArrayList<>();	
    }
    
    public List<Car> read() throws SAXException, IOException, ParserConfigurationException{
    	 SAXParserFactory factory=SAXParserFactory.newInstance();
    	 saxParser=factory.newSAXParser();
    	 DefaultHandler handler = new DefaultHandler() {
    	   public void startElement(String uri,String lname,String name,Attributes attributes) throws SAXException{
    	    if(name.equals("name")) {
    	    	bname=true;
    	    	String con=attributes.getValue("imported");
    	     	if(con.equals("true")) {
    	     		imported=true;
    	     	}
    	     	else
    	     		imported=false;
    	    	}
    	    	if(name.equals("price")) {
    	    		bprice=true;
    	    	}
    	   	}
    	   
    	   public void characters(char[] ch,int start,int length) throws SAXException{
    		   if(bname) {
    			   name = new String(ch,start,length);
    		   }
    		   if(bprice) {
    			   price=Integer.valueOf(new String(ch,start,length));
    			   carList.add(new Car(name,price,imported));
    		   }
    	   }
    	   
    	   public void endElement(String uri,String lname,String name) throws SAXException{
    		   if(name.equals("name")) {
    	    	      bname=false;
    	    	      bprice=false;
	    	     }
    	   }
    	     
    	  };
    	  saxParser.parse("Cars.xml",handler);
    	
        return carList;
    }
    
    public void write(List<Car> list) throws XMLStreamException, IOException{
    	stringWriter = new StringWriter();
        xmlOutputFactory = XMLOutputFactory.newInstance();
		xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(stringWriter);
		xmlStreamWriter.writeStartDocument();
		xmlStreamWriter.writeStartElement("Cars");
		for(int i=0;i<list.size();i++) {
			xmlStreamWriter.writeStartElement("Car");
			
			xmlStreamWriter.writeStartElement("name");
			xmlStreamWriter.writeAttribute("imported",""+list.get(i).isImported());
			xmlStreamWriter.writeCharacters(list.get(i).getName());
			xmlStreamWriter.writeEndElement();
			
			xmlStreamWriter.writeStartElement("price");
			xmlStreamWriter.writeCharacters(""+list.get(i).getPrice());
			xmlStreamWriter.writeEndElement();
			
			xmlStreamWriter.writeEndElement();
			
		}
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeEndDocument();
		
		String xmlString = stringWriter.getBuffer().toString();
		FileWriter out = new FileWriter("Cars.xml");
		out.write(xmlString);
		out.close();
		
		xmlStreamWriter.close();
    }


}
