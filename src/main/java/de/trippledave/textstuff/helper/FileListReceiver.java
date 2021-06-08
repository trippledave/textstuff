/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.trippledave.textstuff.helper;

import java.io.File;
import java.util.List;

/**
 * Connect the dragAndDrop to the service
 * @author trippledave
 */
public interface FileListReceiver {

    public void handleFiles(List<File> files);
}
