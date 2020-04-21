package com.example.trafscot.Dao;
import com.example.trafscot.Models.Event;
import com.example.trafscot.Models.EventBuilder;
import com.example.trafscot.Models.GeoPoint;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.xml.parsers.DocumentBuilderFactory;
import static com.example.trafscot.Service.Helpers.getDateDiff;

/**
 * Alexander Carruthers - S1828301
 */
public class EventDaoImpl  implements EventDao{

    private String currentIncidents = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String currentRoadworksUrl = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String futureRoadworksUrl = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";

    @Override
    public List<Event> getAllCurrentIncidents() {
        List<Event> events = getData(currentIncidents);
        List<Event> currentIncidents = advanceParseCurrentIncidents(events);
        return currentIncidents;
    }

    @Override
    public List<Event> getAllCurrentRoadworks() {
        List<Event> events = getData(currentRoadworksUrl);
        List<Event> currentRoadworks = advanceParseCurrentRoadworks(events);
        return currentRoadworks;
    }

    @Override
    public List<Event> getAllFutureRoadworks() {
        List<Event> events = getData(futureRoadworksUrl);
        List<Event> futureRoadworks = advanceParseFutureRoadworks(events);
        return futureRoadworks;
    }



    private Document loadTestDocument(String url) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        return factory.newDocumentBuilder().parse(new URL(url).openStream());
    }

    private List<Event> getData(String urlSource) {
        List<Event> events = new ArrayList<>();
        XmlPullParserFactory xmlFactoryObject = null;
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        try {
            XmlPullParser myparser = xmlFactoryObject.newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = loadTestDocument(urlSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("item");
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;
                Node node1 = elem.getElementsByTagName("title").item(0);
                String title;
                try {
                    title = node1.getTextContent();
                } catch (NullPointerException e) {
                    title = "";
                }
                int iend = title.indexOf(" ");
                String motorway = title;
                if (iend != -1) {
                    motorway= title.substring(0 , iend); //this will give abc
                }

                Node node2 = elem.getElementsByTagName("description").item(0);
                String description = node2.getTextContent();
                Node node3 = elem.getElementsByTagName("link").item(0);
                String link = node3.getTextContent();
                Node node4 = elem.getElementsByTagName("georss:point").item(0);
                String georss = node4.getTextContent();
                Node node5 = elem.getElementsByTagName("author").item(0);
                String author = node5.getTextContent();
                Node node6 = elem.getElementsByTagName("comments").item(0);
                String comments = node6.getTextContent();
                Node node7 = elem.getElementsByTagName("pubDate").item(0);
                String pubDate = node7.getTextContent();
                Date parsedDate = null;
                try {
                    parsedDate = new SimpleDateFormat("E, dd MMM yyyy h:m:s z").parse(pubDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String[] geoXY = georss.split("\\s+"); //Split on the whitespace
                GeoPoint geoPoint = new GeoPoint(Double.parseDouble(geoXY[0]), Double.parseDouble(geoXY[1]));
                Event event = new EventBuilder()
                        .setTitle(title)
                        .setDescription(description)
                        .setLink(link)
                        .setPoint(geoPoint)
                        .setAuthor(author)
                        .setComments(comments)
                        .setPubDate(parsedDate)
                        .setTrunkRoad(motorway)
                        .createEvent();
                events.add(event);
            }
        }
        return events;
    }

    private List<Event> advanceParseCurrentIncidents(List<Event> rawCurrentIncidents){
        List<Event> currentIncidents = new ArrayList<>();
        for (Event event : rawCurrentIncidents){
            Event roadworkEvent = new EventBuilder()
                    .setTitle(event.getTitle())
                    .setDescription(event.getDescription())
                    .setLink(event.getLink())
                    .setPoint(event.getPoint())
                    .setAuthor(event.getAuthor())
                    .setComments(event.getComments())
                    .setPubDate(event.getPubDate())
                    .setTrunkRoad(event.getTrunkRoad())
                    .setStartDate(new Date()) //not published for current incident
                    .setEndDate(new Date())  //not published for current incident
                    .setDelayInformation("")
                    .setDirection("")
                    .setDisruption("")
                    .setLengthDisruptionDays(new Long(0)) //no publication date
                    .createEvent();
            currentIncidents.add(roadworkEvent);
        }
        return currentIncidents;
    }

    private List<Event> advanceParseCurrentRoadworks(List<Event> rawRoadworks){
        List<Event> currentRoadworks = new ArrayList<>();
        for (Event event : rawRoadworks){
            String title = event.getTitle();
            String description = event.getDescription();
            Date startDate = getRoadworksStartDate(description);
            Date endDate = getRoadworksEndDate(description);
            String delayInformation = getDelayInformation(description);
            String direction = getDirection(title);
            String disruption = getTypeOfDisruption(title);
            Long lengthOfDisruptionDays = getDateDiff(startDate,endDate, TimeUnit.DAYS);
            Event roadworkEvent = new EventBuilder()
                    .setTitle(event.getTitle())
                    .setDescription(event.getDescription())
                    .setLink(event.getLink())
                    .setPoint(event.getPoint())
                    .setAuthor(event.getAuthor())
                    .setComments(event.getComments())
                    .setPubDate(event.getPubDate())
                    .setTrunkRoad(event.getTrunkRoad())
                    .setStartDate(startDate)
                    .setEndDate(endDate)
                    .setDelayInformation(delayInformation)
                    .setDirection(direction)
                    .setDisruption(disruption)
                    .setLengthDisruptionDays(lengthOfDisruptionDays)
                    .createEvent();
            currentRoadworks.add(roadworkEvent);
        }
        return currentRoadworks;
    }

    private List<Event> advanceParseFutureRoadworks(List<Event> rawFutureRoadworks){
        List<Event> futureRoadworks = new ArrayList<>();
        for (Event event : rawFutureRoadworks){
            String title = event.getTitle();
            String description = event.getDescription();
            Date startDate = getRoadworksStartDate(description);
            Date endDate = getRoadworksEndDate(description);
            String delayInformation = getDelayInformation(description);
            String direction = getDirection(title);
            String disruption = getTypeOfDisruption(title);
            Long lengthOfDisruptionDays = getDateDiff(startDate,endDate, TimeUnit.DAYS);
            Event roadworkEvent = new EventBuilder()
                    .setTitle(event.getTitle())
                    .setDescription(event.getDescription())
                    .setLink(event.getLink())
                    .setPoint(event.getPoint())
                    .setAuthor(event.getAuthor())
                    .setComments(event.getComments())
                    .setPubDate(event.getPubDate())
                    .setTrunkRoad(event.getTrunkRoad())
                    .setStartDate(startDate)
                    .setEndDate(endDate)
                    .setDelayInformation(delayInformation)
                    .setDirection(direction)
                    .setDisruption(disruption)
                    .setLengthDisruptionDays(lengthOfDisruptionDays)
                    .createEvent();
            futureRoadworks.add(roadworkEvent);
        }
        return futureRoadworks;
    }
    public List<Event> getMotorwayEvents(String searchForMotorway, List<Event> events) {
        List<Event> matchedEvents = new ArrayList<>();
        for (Event event : events){
            String title = event.getTitle();
            int iend = title.indexOf(" ");
            String motorway = "";
            if (iend != -1) {
                motorway = title.substring(0 , iend); //this will give abc
            }
            if (motorway.equals(searchForMotorway)){
                matchedEvents.add(event);
            }
        }
        return matchedEvents;
    }


    private Date getRoadworksStartDate(String description){
        Date startDate = null;
        SimpleDateFormat formatter = new SimpleDateFormat(" E, d MMM y - H:m");
        String[] arrOfStr = description.split("<br />");
        String startDateStr = arrOfStr[0];
        String [] startDateArr = startDateStr.split(":",2);
        String actualStartDate = startDateArr[1];
        try {
            startDate = formatter.parse(actualStartDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startDate;
    }
    private Date getRoadworksEndDate(String description){
        Date endDate = null;
        SimpleDateFormat formatter = new SimpleDateFormat(" E, d MMM y - H:m");
        String[] arrOfStr = description.split("<br />");
        String endDateStr = arrOfStr[1];
        String [] endDateArr = endDateStr.split(":",2);
        String actualEndDate = endDateArr[1];
        try {
            endDate = formatter.parse(actualEndDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return endDate;
    }
    private String getDelayInformation(String description){
        String[] arrOfStr = description.split("<br />");
        String delayInformation = "";
        try {
            delayInformation = arrOfStr[2];
            String [] delayInformationArr = delayInformation.split(":",2);
            delayInformation = delayInformationArr[1];
        } catch (IndexOutOfBoundsException e) {
            delayInformation = "";
        }
        return delayInformation;
    }
    private String getDirection(String title){
        List<String> directions = Arrays.asList("NB", "SB", "WB", "EB");
        String directionFound = "";
        for(String direction : directions){
            if(title.contains(direction)){
                directionFound = direction;
                break;
            }
        }
        return directionFound;
    }
    private String getTypeOfDisruption(String title){
        List<String> typesOfDisruption = new ArrayList<>();
        typesOfDisruption.add("Rolling TTL");
        typesOfDisruption.add("Vergeworks");
        typesOfDisruption.add("Lane Closure");
        typesOfDisruption.add("Diversion Signage");
        typesOfDisruption.add("Diversion Route");
        typesOfDisruption.add("TTL");
        typesOfDisruption.add("Vergeworks with Layby Closure");
        typesOfDisruption.add("MLC");
        typesOfDisruption.add("Convoy Works");
        typesOfDisruption.add("Layby Closure");
        typesOfDisruption.add("Stop/Go Works");
        String [] disruptionType = title.split("-");
        String disruption = "";
        try {
            disruption = disruptionType[1].trim();
            boolean ans = typesOfDisruption.contains(disruption);
            if (ans)
                disruption = disruption;
            else
                disruption = "";
        } catch (IndexOutOfBoundsException e) {
            disruption = "";
        }
        return disruption;
    }

    public List<Event> getRoadworksOnDate(Date searchForDate, List<Event> roadworks) {
        List<Event> matchedRoadworks = new ArrayList<>();
        for (Event roadwork : roadworks){
            Date min = roadwork.getStartDate();
            Date max = roadwork.getEndDate();
            if (searchForDate.after(min) && searchForDate.before(max)){
                matchedRoadworks.add(roadwork);
            }
        }
        return matchedRoadworks;
    }
}
