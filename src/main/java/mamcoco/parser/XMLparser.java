package mamcoco.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class XMLparser {
    private final String xml;
    private Document doc;
    private Element current;

    public XMLparser(String s)
    {
        this.xml = s;
    }

    public void build(){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputStream is = new ByteArrayInputStream(xml.getBytes());
            this.doc = builder.parse(is);
            this.doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        this.current = doc.getDocumentElement();
    }

    public XMLparser into(String tag){
        this.current = (Element) this.current.getElementsByTagName(tag).item(0);
        return this;
    }

    public String get(String tag){
        return this.current.getElementsByTagName(tag).item(0).getTextContent();
    }

    public void goback(){
        this.current = this.doc.getDocumentElement();
    }
}























