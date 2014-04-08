package jetbrains.buildServer.softinstall.util;

import jetbrains.buildServer.util.FileUtil;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class Xml
{

  //// LOADING AND PARSING \\\\

  public static void readXmlFile(@NotNull final File file, @NotNull final FileUtil.Processor p) {
    if (!file.exists()) throw new IllegalArgumentException("The file \""+file.getAbsolutePath()+"\" doesn't exist");

    try {
      final FileInputStream fis = new FileInputStream(file);
      try {
        readXmlFile(fis, p);
      }
      finally {
        fis.close();
      }
    }
    catch (IOException ioe) {
      throw new RuntimeException("Failed to read XML file \"" + file.getAbsolutePath() +"\": " + ioe.getMessage(), ioe);
    }
  }

  public static void readXmlFile(@NotNull final InputStream stream, @NotNull final FileUtil.Processor p) {
    try {
      parseXml(stream, p);
    }
    catch (IOException ioe) {
      throw new RuntimeException("Failed to read XML document: " + ioe.getMessage(), ioe);
    }
    catch (JDOMException je) {
      throw new RuntimeException("Failed to parse XML document: " + je.getMessage(), je);
    }
  }

  @NotNull
  private static Document parseXml(@NotNull final InputStream stream, @NotNull final FileUtil.Processor p) throws JDOMException, IOException
  {
    final Document document = prepareXmlBuilder(false).build(stream).getRootElement().getDocument();
    p.process(document.getRootElement());
    return document;
  }


  @NotNull
  private static SAXBuilder prepareXmlBuilder(final boolean validate) {
    SAXBuilder builder = new SAXBuilder(validate);
    builder.setFeature("http://xml.org/sax/features/namespaces", true);
    builder.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
    builder.setEntityResolver(new DefaultHandler() {
      @Override
      public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException
      {
        String dtdFileName = new File(systemId).getName();
        InputStream dtdStream = getClass().getClassLoader().getResourceAsStream(dtdFileName);
        if (dtdStream != null) {
          return new InputSource(dtdStream);
        }

        return super.resolveEntity(publicId, systemId);
      }
    });

    return builder;
  }


  //// USEFUL UTILS \\\\

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
