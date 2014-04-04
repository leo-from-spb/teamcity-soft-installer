package jetbrains.buildServer.softinstall.server;

import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;



/**
 * Эта штука валидирует форму (C) Eugene
 * @author Leonid Bushuev from JetBrains
 */
public class SoftInstallPropertiesProcessor implements PropertiesProcessor
{

  @Override
  public Collection<InvalidProperty> process(Map<String, String> stringStringMap)
  {
    return Collections.emptySet();
  }

}
