package jetbrains.buildServer.softinstall.models.loader;

import jetbrains.buildServer.softinstall.models.model.*;
import jetbrains.buildServer.util.FileUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.List;

import static jetbrains.buildServer.softinstall.util.Xml.*;


/**
 * @author Leonid Bushuev from JetBrains
 */
public class ModelLoader
{

  @NotNull
  public SoftModel loadModel(@NotNull final File directory) {
    SoftModel model = new SoftModel();
    final File[] files = directory.listFiles(new FileFilter()
                          {
                            @Override
                            public boolean accept(File f) {
                              return f.isFile()
                                  && f.length() > 0
                                  && f.getName().endsWith(".soft.xml");
                            }
                          });
    if (files != null)
    {
      for (File file : files)
      {
        SoftDescriptor descriptor =
                loadDescriptor(file);
        model.addDescriptor(descriptor);
      }
    }

    return model;
  }



  @NotNull
  public SoftDescriptor loadDescriptor(@NotNull final File file) {
    String name = file.getName();
    name = removeSuffix(name, ".xml");
    name = removeSuffix(name, ".soft");

    try {
      InputStream inputStream = new FileInputStream(file);
      try {
        return loadDescriptor(inputStream, name);
      }
      finally {
        inputStream.close();
      }
    }
    catch (IOException ioe) {
      throw new WrongModelException("Failed to read descriptor from file \"" + file.getAbsolutePath() + "\": " + ioe.getMessage(), ioe);
    }
  }


  @NotNull
  public SoftDescriptor loadDescriptor(@NotNull final InputStream inputStream, String name) {
    final SoftDescriptor sd = new SoftDescriptor(name);

    readXmlFile(inputStream, new FileUtil.Processor()
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
        sd.addSection(section);
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

    List<Element> existentElements = getChildren(sectionElement, "existent");
    for (Element existentElement : existentElements) loadExistent(section, existentElement);

    Element installElement = sectionElement.getChild("install");
    if (installElement != null) loadInstallPath(section, installElement);

    Element provideElement = sectionElement.getChild("provide");
    if (provideElement != null) loadProvideVars(section, provideElement);
  }


  private void loadExistent(@NotNull SoftSection section, @NotNull Element existentElement)
  {
    Element checkElement = existentElement.getChild("check");
    if (checkElement == null) return;

    SoftExistent existent = new SoftExistent();

    List<Element> fileExistsChecks = getChildren(checkElement, "file-exists");
    for (Element fileExistsCheck : fileExistsChecks) {
      String fileSpec = getTextContent(fileExistsCheck);
      if (fileSpec == null) continue;
      existent.addCheck(new SoftCheckFile(fileSpec));
    }

    Element provideElement = existentElement.getChild("provide");
    if (provideElement != null) loadProvideVars(existent, provideElement);

    section.addExistent(existent);
  }


  private void loadInstallPath(@NotNull SoftSection section, @NotNull Element installElement)
  {
    final List<Element> execElements = getChildren(installElement, "exec");
    for (Element execElement : execElements)
    {
      String cmdline = getInnerElementContent(execElement, "cmd-line");
      if (cmdline == null) throw new WrongModelException("The exec step contains no cmd-line");
      SoftInstallExecStep step = new SoftInstallExecStep(cmdline);
      section.addInstallStep(step);
    }
  }


  private void loadProvideVars(@NotNull SoftVarsProvider varsProvider, @NotNull Element provideElement)
  {
    final List<Element> varElements = getChildren(provideElement, "var");
    for (Element varElement : varElements)
    {
      String name = getAttr(varElement, "name");
      if (name == null) throw new WrongModelException("Unnamed variable definition");
      String expression = getTextContent(varElement);
      if (expression == null) throw new WrongModelException("Variable "+name+" has no expression");
      boolean override = getAttrBool(varElement, "override");
      SoftVariable var = new SoftVariable(name, expression, override);
      varsProvider.addVar(var);
    }
  }


  @NotNull
  private static String removeSuffix(@NotNull final String str, @NotNull final String suffix)
  {
    return str.endsWith(suffix) ? str.substring(0, str.length() - suffix.length()) : str;
  }


}
