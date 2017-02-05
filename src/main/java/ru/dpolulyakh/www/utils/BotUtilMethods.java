package ru.dpolulyakh.www.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.dpolulyakh.www.entity.Currency;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Denis Polulyakh
 */
public class BotUtilMethods {
    private static final String CLASS_NAME = BotUtilMethods.class.getName();
    private static final Logger log = Logger.getLogger(CLASS_NAME);
    private static final String SYMBOLS="!@#$%^&*(){}[]/~`+-=<>,";

    /**
     * Method get xml from www.cbr.ru and put valute in Map
     *
     * @param cbrURL link get xml from www.cbr.ru
     * @return map ExchangeCurrency
     */
    public static Map<String, Currency> getMapCurrency(String cbrURL) {
        final String METHOD_NAME = "getMapCurrency";
        log.info(CLASS_NAME + " " + METHOD_NAME + " entry" + "Parameters: " + "cbrURL=" + cbrURL);

        Map<String, Currency> currencyMap = new HashMap<String, Currency>();
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
            // root.getAttributes().get
            NodeList valutes = root.getChildNodes();
            for (int i = 0; i < valutes.getLength(); i++) {
                Node valute = valutes.item(i);
                // Если нода не текст
                if (valute.getNodeType() != Node.TEXT_NODE) {
                    NodeList valuteProps = valute.getChildNodes();
                    Currency cur = new Currency();
                    for (int j = 0; j < valuteProps.getLength(); j++) {
                        Node valuteProp = valuteProps.item(j);
                        // Если нода не текст, то это один из параметров книги - печатаем
                        if (valuteProp.getNodeType() != Node.TEXT_NODE) {

                            String paremeter = valuteProp.getNodeName();
                            switch (paremeter) {
                                case "NumCode":
                                    cur.setNumCode(valuteProp.getChildNodes().item(0).getTextContent());
                                    break;
                                case "CharCode":
                                    cur.setCharCode(valuteProp.getChildNodes().item(0).getTextContent());
                                    break;
                                case "Nominal":
                                    cur.setNominal(valuteProp.getChildNodes().item(0).getTextContent());
                                    break;
                                case "Value":
                                    cur.setValue(valuteProp.getChildNodes().item(0).getTextContent());
                                    break;
                                case "Name":
                                    cur.setName(valuteProp.getChildNodes().item(0).getTextContent());
                                    break;
                            }
                            log.info(valuteProp.getNodeName() + ":" + valuteProp.getChildNodes().item(0).getTextContent());
                        }
                    }
                    currencyMap.put(cur.getCharCode(), cur);

                    log.info("===========>>>>");
                }
            }
        } catch (ParserConfigurationException e) {
            log.error(CLASS_NAME + " " + METHOD_NAME + " " + "Error parse configuration exception " + e.getMessage());
        } catch (SAXException e) {
            log.error(CLASS_NAME + " " + METHOD_NAME + " " + "Error SAX parse exception " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info(CLASS_NAME + " " + METHOD_NAME + " exit");
        return currencyMap;
    }

    public static Object deserializeObject(String strInput) {
        final String METHOD_NAME = "deserializeObject";
        log.info(strInput);
        if (strInput == null) {
            return null;
        }
        byte bytes[] = Base64.decode(strInput);
        ObjectInputStream ois = null;
        ByteArrayInputStream bais = null;
        Object obj = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            obj = ois.readObject();

        } catch (IOException e) {
            log.error(CLASS_NAME + " " + METHOD_NAME + " " + e.getMessage());
        } catch (ClassNotFoundException e) {
            log.error(CLASS_NAME + " " + METHOD_NAME + " " + e.getMessage());
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                log.error(CLASS_NAME + " " + METHOD_NAME + " " + e.getMessage());
            }
        }

        return obj;
    }


    public static String serializeObject(Object object)  {
        final String METHOD_NAME = "serializeObject";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
        } catch (IOException e) {
            log.debug(CLASS_NAME + " " + METHOD_NAME + e.getMessage());

        }

        return new String(Base64.encode(baos.toByteArray()));

    }

    public static String getProperty(String aliasProperty) {
        final String METHOD_NAME = "getProperty";
        log.info(CLASS_NAME + " " + METHOD_NAME + " entry" + "Parameters: " + "aliasProperty=" + aliasProperty);
        Properties props = null;
        try {
            Resource resource = new ClassPathResource("/configbot.properties");
            props = PropertiesLoaderUtils.loadProperties(resource);
        } catch (Exception e) {
            log.debug(CLASS_NAME + " " + METHOD_NAME + e.getMessage());

        }
        log.info(CLASS_NAME + " " + METHOD_NAME + " exit");
        return props.getProperty(aliasProperty);
    }

    public static String getPropertyFromJSON(String json, String property) {
        final String METHOD_NAME = "getPropertyFromJSON";
        JSONParser parser = new JSONParser();
        String message = "";
        try {
            Object messageObject = parser.parse(json);
            JSONObject resultJson = (JSONObject) messageObject;
            message = "" + resultJson.get(property);
        } catch (ParseException e) {
            log.error("Erorr parse json " + e.getMessage());
            e.printStackTrace();
        }
        return message;
    }

    public static String replaseSymbols(String input){
        final String METHOD_NAME = "replaseSymbols";
        log.info("Input string "+input);
        String output=input;
        for(int i=0;i<SYMBOLS.length();i++){
            if(input.contains(""+SYMBOLS.charAt(i))){
                output=output.replaceAll(""+SYMBOLS.charAt(i),"");
            }
        }
        output=output.replaceAll("\\?","");
        output=output.replaceAll("\\\\","");
        log.info("Output string "+output);
        return output;
    }

}
