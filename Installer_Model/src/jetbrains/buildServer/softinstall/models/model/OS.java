package jetbrains.buildServer.softinstall.models.model;

import org.jetbrains.annotations.Nullable;



/**
 * Operating System.
 * @author Leonid Bushuev from JetBrains
 */
public enum OS
{
  WINDOWS,
  LINUX,
  UNIX,
  MAC;

  @Nullable
  public static OS byName(final String name) {
    String upperName = name.toUpperCase();
    for (OS os : OS.values()) if (os.name().equals(upperName)) return os;
    return null;
  }
}
