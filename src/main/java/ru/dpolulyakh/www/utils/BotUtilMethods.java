package ru.dpolulyakh.www.utils;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.dpolulyakh.www.entity.Valute;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Денис on 27.12.2016.
 *
 */
public class BotUtilMethods {
    private static final String CLASS_NAME = BotUtilMethods.class.getName();
    private static final Logger log = Logger.getLogger(CLASS_NAME);
    /**
     * Method get xml from www.cbr.ru and put valute in Map
     * @param cbrURL link get xml from www.cbr.ru
     * @return map Valute
     */
    public static Map<String, Valute> getMapValute(String cbrURL) {

        final String METHOD_NAME = "getMapValute";
        log.info(CLASS_NAME + " " + METHOD_NAME + " entry" + "Parameters: " + "cbrURL=" + cbrURL);

        Map<String, Valute> valuteMap = new HashMap<String, Valute>();
        try {
            URL connection = new URL(cbrURL);
            HttpURLConnection urlconn;
            urlconn = (HttpURLConnection) connection.openConnection();
            urlconn.setRequestMethod("GET");
            urlconn.connect();
            InputStream in = null;
            in = urlconn.getInputStream();

            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(in);
            // Получаем корневой элемент
            Node root = document.getDocumentElement();
            NodeList valutes = root.getChildNodes();
            for (int i = 0; i < valutes.getLength(); i++) {
                Node valute = valutes.item(i);
                // Если нода не текст
                if (valute.getNodeType() != Node.TEXT_NODE) {
                    NodeList valuteProps = valute.getChildNodes();
                    Valute val = new Valute();
                    for (int j = 0; j < valuteProps.getLength(); j++) {
                        Node valuteProp = valuteProps.item(j);
                        // Если нода не текст, то это один из параметров книги - печатаем
                        if (valuteProp.getNodeType() != Node.TEXT_NODE) {

                            String paremeter = valuteProp.getNodeName();
                            switch (paremeter) {
                                case "NumCode":
                                    val.setNumCode(valuteProp.getChildNodes().item(0).getTextContent());
                                    break;
                                case "CharCode":
                                    val.setCharCode(valuteProp.getChildNodes().item(0).getTextContent());
                                    break;
                                case "Nominal":
                                    val.setNominal(valuteProp.getChildNodes().item(0).getTextContent());
                                    break;
                                case "Value":
                                    val.setValue(valuteProp.getChildNodes().item(0).getTextContent());
                                    break;
                                case "Name":
                                    val.setName(valuteProp.getChildNodes().item(0).getTextContent());
                                    break;
                            }
                            log.info(valuteProp.getNodeName() + ":" + valuteProp.getChildNodes().item(0).getTextContent());
                        }
                    }
                    valuteMap.put(val.getCharCode(), val);

                    log.info("===========>>>>");
                }
            }
        } catch (ParserConfigurationException e) {
            log.error(CLASS_NAME+" "+METHOD_NAME+" "+"Error parse configuration exception "+e.getMessage());
        } catch (SAXException e) {
            log.error(CLASS_NAME+" "+METHOD_NAME+" "+"Error SAX parse exception "+e.getMessage());
        } catch (IOException e) {
             e.printStackTrace();
        }

        log.info(CLASS_NAME + " " + METHOD_NAME + " exit");
        return valuteMap;
    }
}
