/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.mysqlbrowser.wizard;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Josep
 */
class ExtensionFileFilter extends FileFilter {
    private final String description;
    private final String[] extensions;

    public ExtensionFileFilter(String description, String[] extensions) {
        this.description = description;
        this.extensions = extensions;
    }

    @Override
    public boolean accept(File f) {
     if (f.isDirectory()) {
       return false;
    } else {
      String path = f.getAbsolutePath().toLowerCase();
      for (int i = 0, n = extensions.length; i < n; i++) {
        String extension = extensions[i];
        if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')) {
          return true;
        }
      }
    }
    return false;
  }

    @Override
    public String getDescription() {
        return description;
    }
    
}
