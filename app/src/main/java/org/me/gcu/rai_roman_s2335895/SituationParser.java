package org.me.gcu.rai_roman_s2335895;

import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class SituationParser {

    public ArrayList<Situation> parse(String dataToParse) {
        ArrayList<Situation> situations = new ArrayList<>();
        Situation aSituation = null;
        boolean insideASituation = false;
        boolean insideComment = false;
        String lastValue = "";
        String section = "";   // "to" or "from" while inside that point
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(dataToParse));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tag = xpp.getName();
                    if (tag.equalsIgnoreCase("situation")) {
                        insideASituation = true;
                        aSituation = new Situation();
                        section = "";
                    } else if (tag.equalsIgnoreCase("situationRecord")) {
                        if (insideASituation && xpp.getAttributeCount() > 0) {
                            aSituation.setType(xpp.getAttributeValue(0));
                        }
                    } else if (tag.equalsIgnoreCase("generalPublicComment")) {
                        insideComment = true;
                    } else if (tag.equalsIgnoreCase("value")) {
                        String temp = xpp.nextText();
                        if (insideComment && insideASituation) {
                            aSituation.setDescription(temp);
                        } else {
                            lastValue = temp;
                        }
                    } else if (tag.equalsIgnoreCase("tpegOtherPointDescriptorType")) {
                        String marker = xpp.nextText();
                        if (insideASituation && marker.equals("linkName") && aSituation.getRoad() == null) {
                            aSituation.setRoad(lastValue);
                        }
                    } else if (tag.equalsIgnoreCase("overallStartTime")) {
                        String temp = xpp.nextText();
                        if (insideASituation) aSituation.setStartTime(temp);
                    } else if (tag.equalsIgnoreCase("overallEndTime")) {
                        String temp = xpp.nextText();
                        if (insideASituation) aSituation.setEndTime(temp);
                    } else if (tag.equalsIgnoreCase("delaysType")) {
                        String temp = xpp.nextText();
                        if (insideASituation) aSituation.setDelayType(temp);
                    } else if (tag.equalsIgnoreCase("to")) {
                        section = "to";
                    } else if (tag.equalsIgnoreCase("from")) {
                        section = "from";
                    } else if (tag.equalsIgnoreCase("tpegDirection")) {
                        String temp = xpp.nextText();
                        if (insideASituation) aSituation.setDirection(temp);
                    } else if (tag.equalsIgnoreCase("latitude")) {
                        double v = toDouble(xpp.nextText());
                        if (insideASituation && section.equals("from")) aSituation.setFromLat(v);
                        else if (insideASituation && section.equals("to")) aSituation.setToLat(v);
                    } else if (tag.equalsIgnoreCase("longitude")) {
                        double v = toDouble(xpp.nextText());
                        if (insideASituation && section.equals("from")) aSituation.setFromLon(v);
                        else if (insideASituation && section.equals("to")) aSituation.setToLon(v);
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    String tag = xpp.getName();
                    if (tag.equalsIgnoreCase("generalPublicComment")) {
                        insideComment = false;
                    } else if (tag.equalsIgnoreCase("to") || tag.equalsIgnoreCase("from")) {
                        section = "";
                    } else if (tag.equalsIgnoreCase("situation")) {
                        situations.add(aSituation);
                        insideASituation = false;
                    }
                }
                eventType = xpp.next();
            }
        } catch (XmlPullParserException e) {
            Log.e("MyTag", "Parsing error " + e);
        } catch (IOException e) {
            Log.e("MyTag", "IO error during parsing");
        }
        return situations;
    }

    private double toDouble(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return Double.NaN;
        }
    }
}
