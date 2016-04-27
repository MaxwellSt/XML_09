
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.math.BigDecimal;

/**
 * Created by maxim.stetsenko on 26.04.2016.
 */
public class ParserXML {

    public static void main(String[] args) {

        DOMParser("09.xml", "09_result_DOM.xml");
        SAXParser("09.xml", "09_result_SAX.xml");

    }

    private static void DOMParser(String in, String out){
        //DOM parser
        try {
            File inputFile = new File(in);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
            Document doc = dbBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            Document docOut = dbBuilder.newDocument();
            //create root element
            Element rootElement = docOut.createElement("group");
            docOut.appendChild(rootElement);


            NodeList nList = doc.getElementsByTagName("student");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nStudent = nList.item(i);

                float aver = 0.0f;
                int sum = 0;

                if (nStudent.getNodeType() == Node.ELEMENT_NODE) {
                    Element eStudent = (Element) nStudent;

                    //create students element
                    Element student = docOut.createElement("student");
                    rootElement.appendChild(student);

                    Attr attr = docOut.createAttribute("firstname");
                    attr.setValue(eStudent.getAttribute("firstname"));
                    student.setAttributeNode(attr);

                    attr = docOut.createAttribute("lastname");
                    attr.setValue(eStudent.getAttribute("lastname"));
                    student.setAttributeNode(attr);

                    attr = docOut.createAttribute("groupnumber");
                    attr.setValue(eStudent.getAttribute("groupnumber"));
                    student.setAttributeNode(attr);

                    NodeList nList2 = eStudent.getElementsByTagName("subject");
                    for (int j = 0; j < nList2.getLength(); j++) {
                        Node nSubject = nList2.item(j);
                        if (nSubject.getNodeType() == Node.ELEMENT_NODE) {
                            Element eSubject = (Element) nSubject;

                            //create subjects element
                            Element subject = docOut.createElement("subject");
                            student.appendChild(subject);

                            attr = docOut.createAttribute("title");
                            attr.setValue(eSubject.getAttribute("title"));
                            subject.setAttributeNode(attr);

                            attr = docOut.createAttribute("mark");
                            attr.setValue(eSubject.getAttribute("mark"));
                            subject.setAttributeNode(attr);

                            sum += Integer.parseInt(eSubject.getAttribute("mark"));
                        }
                    }
                    String average = "0";
                    if(eStudent.getElementsByTagName("average").getLength() > 0) {
                        average = eStudent.getElementsByTagName("average").item(0).getTextContent();
                    }

                    float correctAverage = 0.0f;
                    if(nList2.getLength() != 0){
                        correctAverage = (float) sum/nList2.getLength();
                    }

                    float fAverage = Float.parseFloat(average);

                    BigDecimal x1 = new BigDecimal(correctAverage);
                    x1 = x1.setScale(1, BigDecimal.ROUND_HALF_UP);

                    BigDecimal x2 = new BigDecimal(fAverage);
                    x2 = x2.setScale(1, BigDecimal.ROUND_HALF_UP);

                    //create subjects element
                    Element newAverage = docOut.createElement("average");

                    if(!x1.equals(x2)){
                        newAverage.appendChild(docOut.createTextNode(x1.toString()));
                    }else{
                        newAverage.appendChild(docOut.createTextNode(average));
                    }
                    student.appendChild(newAverage);
                }
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(docOut);
            StreamResult result = new StreamResult(new File(out));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //for new lines
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void SAXParser(String in, String out){

    }

//    private static float round(float number, int scale) {
//        int pow = 10;
//        for (int i = 1; i < scale; i++)
//            pow *= 10;
//        double tmp = number * pow;
//        int temp = (int)((tmp - (int) tmp) >= 0.5 ? tmp + 1 : tmp);
//        return (float)  temp / pow;
//    }
}
