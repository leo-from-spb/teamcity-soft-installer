package jetbrains.buildServer.softinstall.util;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class Xml
{

  @NotNull
  public static List<Element> getChildren(@Nullable Element element, @Nullable String tagName) {
    if (element == null) return Collections.emptyList();
    final List objects = tagName == null
                       ? element.getChildren()
                       : element.getChildren(tagName);
    ArrayList<Element> children = new ArrayList<Element>(objects.size());
    for (Object object : objects) if (object instanceof Element) children.add((Element) object);
    return children;
  }

  @Nullable
  public static String getInnerElementContent(@Nullable Element element, @Nullable String tagName) {
    if (element == null) return null;
    Element innerElement = element.getChild(tagName);
    if (innerElement == null) return null;
    return innerElement.getText();
  }


  @Nullable
  public static String getTextContent(@Nullable final Element element) {
    if (element == null) return null;
    String text = element.getText();
    if (text != null) text = text.trim();
    return text;
  }


  @Nullable
  public static String getAttr(@Nullable final Element element, @NotNull final String attrName)
  {
    if (element == null) return null;

    final Attribute attribute = element.getAttribute(attrName);
    if (attribute == null) return null;

    return attribute.getValue();
  }


  public static boolean getAttrBool(@Nullable final Element element, @NotNull final String attrName) {
    String str = getAttr(element, attrName);
    if (str == null) return false;

    str = str.toLowerCase();

    return str.equals("true") ||
           str.equals("yes") ||
           str.equals("y") ||
           str.equals("1") ||
           str.equals("+");
  }



}
