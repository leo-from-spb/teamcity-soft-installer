package jetbrains.buildServer.softinstall.models.loader;

import jetbrains.buildServer.softinstall.models.model.*;
import jetbrains.buildServer.util.FileUtil;
import jetbrains.buildServer.util.XmlUtil;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class ModelLoader
{


  @NotNull
  public SoftDescriptor loadDescriptor(@NotNull final File file) {
    String name = file.getName();
    name = removeSuffix(name, ".xml");
    name = removeSuffix(name, ".soft");
    final SoftDescriptor sd = new SoftDescriptor(name);

    FileUtil.readXmlFile(file, new FileUtil.Processor()
    {
      @Override
      public void process(Element element)
      {

        loadDescriptorContent(sd, element);

      }
    });

    return sd;
  }


  private void loadDescriptorContent(@NotNull SoftDescriptor sd, @NotNull Element rootElement)
  {
    final List children = rootElement.getChildren();
    for (Object child : children)
    {
      if (child instanceof Element) {
        Element element = (Element) child;
        SoftSection section = new SoftSection();
        loadSectionContent(section, element);
        sd.addsection(section);
      }
      else {
        // TODO log it
      }
    }


  }


  private void loadSectionContent(@NotNull SoftSection section, @NotNull Element sectionElement)
  {
    final String osName = sectionElement.getName();
    OS os = OS.byName(osName);
    if (os == null) throw new WrongModelException("Unknown OS: " + osName);

    section.setOS(os);

    Element installElement = sectionElement.getChild("install");
    if (installElement != null) loadInstallPath(section, installElement);

    Element provideElement = sectionElement.getChild("provide");
    if (provideElement != null) loadProvideVars(section, provideElement);
  }


  private void loadInstallPath(@NotNull SoftSection section, @NotNull Element installElement)
  {
    // TODO loadInstallPath

  }


  private void loadProvideVars(@NotNull SoftSection section, @NotNull Element provideElement)
  {
    final List children = provideElement.getChildren("var");
    for (Object child : children)
    {
      if (child instanceof Element) {
        Element varElement = (Element) child;
        String name = getAttr(varElement, "name");
        if (name == null) throw new WrongModelException("Unnamed variable definition");
        String expression = getTextContent(varElement);
        if (expression == null) throw new WrongModelException("Variable "+name+" has no expression");
        boolean override = getAttrBool(varElement, "override");
        SoftVariable var = new SoftVariable(name, expression, override);
        section.addVar(var);
      }
    }
  }


  @Nullable
  private static String getTextContent(@Nullable final Element element) {
    if (element == null) return null;
    String text = element.getText();
    if (text != null) text = text.trim();
    return text;
  }


  @Nullable
  private static String getAttr(@Nullable final Element element, @NotNull final String attrName)
  {
    if (element == null) return null;

    final Attribute attribute = element.getAttribute(attrName);
    if (attribute == null) return null;

    return attribute.getValue();
  }


  private static boolean getAttrBool(@Nullable final Element element, @NotNull final String attrName) {
    String str = getAttr(element, attrName);
    if (str == null) return false;

    str = str.toLowerCase();

    return str.equals("true") ||
           str.equals("yes") ||
           str.equals("y") ||
           str.equals("1") ||
           str.equals("+");
  }


  @NotNull
  private static String removeSuffix(@NotNull final String str, @NotNull final String suffix)
  {
    return str.endsWith(suffix) ? str.substring(0, str.length() - suffix.length()) : str;
  }


}
