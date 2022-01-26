package mamcoco.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class XMLParser
{
    protected final String xml;
    protected Document doc;
    protected Element current;
    protected Element save;

    static public String getElementValueByTag(Element elt, String tag){
        return elt.getElementsByTagName(tag).item(0).getTextContent();
    }

    static public String getElementValue(Element elt){
        return elt.getTextContent();
    }

    public XMLParser(String xml)
    {
        this.xml = xml.replaceAll("\\u0014", "");
        this.save = null;
        this.build();
    }

    public XMLParser into(String tag){
        this.current = (Element) this.current.getElementsByTagName(tag).item(0);
        return this;
    }

    public String get(String tag){
        return this.current.getElementsByTagName(tag).item(0).getTextContent();
    }

    public NodeList getList(){
        return this.current.getChildNodes();
    }

    public NodeList getListByTag(String tag) { return this.current.getElementsByTagName(tag); }

    private void build(){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            this.doc = builder.parse(is);
            this.doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            System.out.println(this.xml);
        }
        this.current = doc.getDocumentElement();
    }

    public void init(){
        this.current = this.doc.getDocumentElement();
    }
    public void saveCurrent() { this.save = this.current; }
    public void restoreCurrent() { if (this.save != null) this.current = this.save; }
}























