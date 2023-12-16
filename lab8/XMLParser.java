import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class XMLParser {
    public static void main(String[] args) throws URISyntaxException {
        String urlString = "http://www.cbr.ru/scripts/XML_daily.asp";

        try {
            // Получение XML документа по ссылке
            URI uri = new URI(urlString);
            InputStream inputStream = uri.toURL().openStream();

            // Преобразование XML документа в объекты с использованием различных парсеров
            List<Currency> currencyListDOM = parseWithDOM(inputStream);
            System.out.println("DOM Parser:");
            for (Currency currency : currencyListDOM) {
                System.out.println(currency);
            }

            inputStream = uri.toURL().openStream();
            List<Currency> currencyListSAX = parseWithSAX(inputStream);
            System.out.println("SAX Parser:");
            for (Currency currency : currencyListSAX) {
                System.out.println(currency);
            }

            inputStream = uri.toURL().openStream();
            List<Currency> currencyListStAX = parseWithStAX(inputStream);
            System.out.println("StAX Parser:");
            for (Currency currency : currencyListStAX) {
                System.out.println(currency);
            }
        } catch (IOException | ParserConfigurationException | SAXException | XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private static List<Currency> parseWithDOM(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
        List<Currency> currencyList = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputStream);

        Element rootElement = document.getDocumentElement();
        NodeList valuteNodes = rootElement.getElementsByTagName("Valute");

        for (int i = 0; i < valuteNodes.getLength(); i++) {
            Node valuteNode = valuteNodes.item(i);
            if (valuteNode.getNodeType() == Node.ELEMENT_NODE) {
                Element valuteElement = (Element) valuteNode;

                String id = valuteElement.getAttribute("ID");
                String numCode = getElementTextByTagName(valuteElement, "NumCode");
                String charCode = getElementTextByTagName(valuteElement, "CharCode");
                int nominal = Integer.parseInt(getElementTextByTagName(valuteElement, "Nominal"));
                String name = getElementTextByTagName(valuteElement, "Name");
                double value = Double.parseDouble(getElementTextByTagName(valuteElement, "Value").replace(',', '.'));

                Currency currency = new Currency(id, numCode, charCode, nominal, name, value);
                currencyList.add(currency);
            }
        }

        return currencyList;
    }

    private static List<Currency> parseWithSAX(InputStream inputStream) throws IOException, SAXException, ParserConfigurationException {
        List<Currency> currencyList = new ArrayList<>();

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        CurrencyHandler handler = new CurrencyHandler();
        parser.parse(inputStream, handler);
        currencyList = handler.getCurrencyList();

        return currencyList;
    }

    private static List<Currency> parseWithStAX(InputStream inputStream) throws XMLStreamException {
        List<Currency> currencyList = new ArrayList<>();

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

        Currency currency = null;
        String elementName = null;

        while (reader.hasNext()) {
            int eventType = reader.next();

            switch (eventType) {
                case XMLStreamConstants.START_ELEMENT:
                    elementName = reader.getLocalName();
                    if (elementName.equals("Valute")) {
                        currency = new Currency();
                        currency.setId(reader.getAttributeValue(null, "ID"));
                    }
                    break;

                case XMLStreamConstants.CHARACTERS:
                    String text = reader.getText().trim();
                    if (elementName.equals("NumCode")) {
                        currency.setNumCode(text);
                    } else if (elementName.equals("CharCode")) {
                        currency.setCharCode(text);
                    } else if (elementName.equals("Nominal")) {
                        currency.setNominal(Integer.parseInt(text));
                    } else if (elementName.equals("Name")) {
                        currency.setName(text);
                    } else if (elementName.equals("Value")) {
                        currency.setValue(Double.parseDouble(text.replace(',', '.')));
                    }
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    elementName = reader.getLocalName();
                    if (elementName.equals("Valute")) {
                        currencyList.add(currency);
                        currency = null;
                    }
                    break;
            }
        }

        return currencyList;
    }

    private static String getElementTextByTagName(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Element targetElement = (Element) nodeList.item(0);
            return targetElement.getTextContent();
        }
        return "";
    }

    private static class CurrencyHandler extends DefaultHandler {
        private List<Currency> currencyList = new ArrayList<>();
        private Currency currency;
        private StringBuilder stringBuilder;

        public List<Currency> getCurrencyList() {
            return currencyList;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals("Valute")) {
                currency = new Currency();
                currency.setId(attributes.getValue("ID"));
            }
            stringBuilder = new StringBuilder();
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            stringBuilder.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equals("NumCode")) {
                currency.setNumCode(stringBuilder.toString());
            } else if (qName.equals("CharCode")) {
                currency.setCharCode(stringBuilder.toString());
            } else if (qName.equals("Nominal")) {
                currency.setNominal(Integer.parseInt(stringBuilder.toString()));
            } else if (qName.equals("Name")) {
                currency.setName(stringBuilder.toString());
            } else if (qName.equals("Value")) {
                currency.setValue(Double.parseDouble(stringBuilder.toString().replace(',', '.')));
            } else if (qName.equals("Valute")) {
                currencyList.add(currency);
                currency = null;
            }
        }
    }
}

/**
 * Represents a currency with its attributes.
 */
class Currency {
    private String id;
    private String numCode;
    private String charCode;
    private int nominal;
    private String name;
    private double value;

    /**
     * Constructs a new Currency object.
     */
    public Currency() {
    }

    /**
     * Constructs a new Currency object with the specified attributes.
     *
     * @param id       the ID of the currency
     * @param numCode  the numeric code of the currency
     * @param charCode the character code of the currency
     * @param nominal  the nominal value of the currency
     * @param name     the name of the currency
     * @param value    the value of the currency
     */
    public Currency(String id, String numCode, String charCode, int nominal, String name, double value) {
        this.id = id;
        this.numCode = numCode;
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
        this.value = value;
    }

    /**
     * Returns the ID of the currency.
     *
     * @return the ID of the currency
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the currency.
     *
     * @param id the ID of the currency
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the numeric code of the currency.
     *
     * @return the numeric code of the currency
     */
    public String getNumCode() {
        return numCode;
    }

    /**
     * Sets the numeric code of the currency.
     *
     * @param numCode the numeric code of the currency
     */
    public void setNumCode(String numCode) {
        this.numCode = numCode;
    }

    /**
     * Returns the character code of the currency.
     *
     * @return the character code of the currency
     */
    public String getCharCode() {
        return charCode;
    }

    /**
     * Sets the character code of the currency.
     *
     * @param charCode the character code of the currency
     */
    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    /**
     * Returns the nominal value of the currency.
     *
     * @return the nominal value of the currency
     */
    public int getNominal() {
        return nominal;
    }

    /**
     * Sets the nominal value of the currency.
     *
     * @param nominal the nominal value of the currency
     */
    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    /**
     * Returns the name of the currency.
     *
     * @return the name of the currency
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the currency.
     *
     * @param name the name of the currency
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the value of the currency.
     *
     * @return the value of the currency
     */
    public double getValue() {
        return value;
    }

    /**
     * Sets the value of the currency.
     *
     * @param value the value of the currency
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Returns a string representation of the Currency object.
     *
     * @return a string representation of the Currency object
     */
    @Override
    public String toString() {
        return "Currency{" +
                "id='" + id + '\'' +
                ", numCode='" + numCode + '\'' +
                ", charCode='" + charCode + '\'' +
                ", nominal=" + nominal +
                ", name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}