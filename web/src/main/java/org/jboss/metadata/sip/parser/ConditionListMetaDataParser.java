package org.jboss.metadata.sip.parser;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.jboss.metadata.parser.util.MetaDataElementParser;
import org.jboss.metadata.sip.spec.Attribute;
import org.jboss.metadata.sip.spec.ConditionMetaData;
import org.jboss.metadata.sip.spec.Element;

/**
 * @author josemrecio@gmail.com
 *
 */
public class ConditionListMetaDataParser extends MetaDataElementParser {

    public static List<ConditionMetaData> parse(XMLStreamReader reader) throws XMLStreamException {

        List<ConditionMetaData> listConditionMetaData = new ArrayList<ConditionMetaData>();

        // Handle attributes
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            final String value = reader.getAttributeValue(i);
            if (attributeHasNamespace(reader, i)) {
                continue;
            }
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            switch (attribute) {
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }

        // Handle elements
        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            switch (element) {
                case AND:
                    listConditionMetaData.add(AndMetaDataParser.parse(reader));
                    break;
                case OR:
                    listConditionMetaData.add(OrMetaDataParser.parse(reader));
                    break;
                case NOT:
                    listConditionMetaData.add(NotMetaDataParser.parse(reader));
                    break;
                case EQUAL:
                    listConditionMetaData.add(EqualMetaDataParser.parse(reader));
                    break;
                case CONTAINS:
                    listConditionMetaData.add(ContainsMetaDataParser.parse(reader));
                    break;
                case EXISTS:
                    listConditionMetaData.add(ExistsMetaDataParser.parse(reader));
                    break;
                default:
                    throw unexpectedElement(reader);
            }
        }

        return listConditionMetaData;
    }

}
