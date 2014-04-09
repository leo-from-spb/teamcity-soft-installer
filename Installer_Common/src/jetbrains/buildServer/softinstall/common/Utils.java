package jetbrains.buildServer.softinstall.common;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Set;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class Utils
{

  @NotNull
  public static Set<String> splitSoftNames(@NotNull final String namesAsText) {
    String[] namesAsStrings = namesAsText.split("\\r?\\n");
    Set<String> names = new LinkedHashSet<String>(namesAsStrings.length);
    for (String str : namesAsStrings) {
      String s = str.trim();
      if (s.length() > 0) names.add(s);
    }
    return names;
  }

}
