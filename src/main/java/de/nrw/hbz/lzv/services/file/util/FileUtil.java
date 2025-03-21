/**
 * FileUtil.java - This file is part of the LZV-Services Project by hbz
 * Library Service Center North Rhine Westphalia, Cologne 
 *
 * -----------------------------------------------------------------------------
 *
 * <p><b>License and Copyright: </b>The contents of this file are subject to the
 * D-FSL License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License
 * at <a href="http://www.dipp.nrw.de/dfsl/">http://www.dipp.nrw.de/dfsl/.</a></p>
 *
 * <p>Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.</p>
 *
 * <p>Portions created for the Fedora Repository System are Copyright &copy; 2002-2005
 * by The Rector and Visitors of the University of Virginia and Cornell
 * University. All rights reserved."</p>
 *
 * -----------------------------------------------------------------------------
 *
 */
package de.nrw.hbz.lzv.services.file.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;


/**
 * Class filteUtil
 * 
 * <p><em>Title: </em></p>
 * <p>Description: </p>
 * 
 * @author aquast, email
 * creation date: 24.09.2009
 *
 */
public class FileUtil {

	// Initiate Logger for filteUtil
	private static Logger log = LogManager.getLogger(FileUtil.class);
	
	private static File inputFile = null;

	/**
	 * Method saves a String to File
	 * @param fileName
	 * @param contentString
	 * @return
	 */
	public static String saveStringToResultFile(String fileName, String contentString){
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try{
			inputFile = new File(fileName);
			fos = new FileOutputStream(inputFile);
			bos = new BufferedOutputStream(fos);			
			bos.write(contentString.getBytes("UTF-8"));
			
		}catch(IOException ioExc){
			log.error(ioExc);
		}finally{
			if(bos != null){
				try{
					bos.close();
				}catch(IOException ioExc){
					log.error(ioExc);
				}
			}
			if(fos != null){
				try{
					fos.close();
				}catch(IOException ioExc){
					log.error(ioExc);
				}
			}
		}
		log.debug("File-Size: " + inputFile.length());
		return inputFile.getName();
	}
	
	
	 /**
   * <p><em>Title: Save InputSream to an temporary File</em></p>
   * <p>Description: </p>
   * 
   * @return 
   */
  public static File saveTempFile(InputStream is, String fileName){

    File tmpFile = null;
    String splitString = "[.]";
    String[] fileNameParts = fileName.split(splitString);
    String fileNameSuffix = fileNameParts[fileNameParts.length -1];
    String fileNamePrefix = fileName.replace("." + fileNameSuffix, "");
    
    try {
      tmpFile = File.createTempFile(fileNamePrefix + "_", "." + fileNameSuffix);

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    BufferedInputStream bis = new BufferedInputStream(is);
    BufferedOutputStream bos = null;
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(tmpFile);
      bos = new BufferedOutputStream(fos);
      int i = -1;
      while((i = bis.read()) != -1){
        bos.write(i);
      }
      bos.flush();

    }catch(Exception e){
      log.error(e);
    }finally{
      if(bos != null){
        try{
          bos.close();
        }catch(IOException ioExc){
          log.error(ioExc);
        }
      }
      if(fos != null){
        try{
          fos.close();
        }catch(IOException ioExc){
          log.error(ioExc);
        }
      }
      if(bis != null){
        try{
          bis.close();
        }catch(IOException ioExc){
          log.error(ioExc);
        }
      }
      if(is != null){
        try{
          is.close();
        }catch(IOException ioExc){
          log.error(ioExc);
        }
      }
      

    }
    return tmpFile; 
  }

	/**
	 * Method appends a String to File
	 * @param fileName
	 * @param contentString
	 * @return
	 */
	public static String appendStringToResultFile(String fileName, String contentString){
		FileWriter fw = null;
		try{
			inputFile = new File(fileName);
			fw = new FileWriter(inputFile, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.append(contentString);
			bw.flush();
		}catch(IOException ioExc){
			log.error(ioExc);
		}finally{
			if(fw != null){
				try{
					fw.close();
				}catch(IOException ioExc){
					log.error(ioExc);
				}
			}
		}
		log.info("File-Size, Ergebnis: " + inputFile.length());
		return inputFile.getName();
	}
	 
  /**
   * @param file
   * @return FileInputStream with content of the file
   */
  public static InputStream loadFile(File file) {
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(file);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return fis;
  }
  
  public static void copyFile(File sourceFile, String targetFileName) {
    
    InputStream fis = FileUtil.loadFile(sourceFile);
    FileOutputStream fos;
    try {
      fos = new FileOutputStream(targetFileName);
      fis.transferTo(fos);
      fos.flush();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  /**
   * @param basePath
   * @param workDirName
   * @return
   */
  public static String makeWorkDir(String basePath, String workDirName) {
    
    String wdPath = basePath + System.getProperty("file.separator") + workDirName;
    removeWorkDir(basePath, workDirName);
    
    if(!new File(basePath + System.getProperty("file.separator") + workDirName).isDirectory()) {
      File workDir = new File(basePath + System.getProperty("file.separator") + workDirName);
      workDir.mkdir();
    }
    
    return wdPath;
    
  }

  public static void removeWorkDir(String basePath, String workDirName) {
    
    //if(new File(basePath + System.getProperty("file.separator") + workDirName).isDirectory()) {
      File workDir = new File(basePath + System.getProperty("file.separator") + workDirName);
      
      deleteDir(workDir);
      workDir.delete();
      
    //}
    
  }
    
  /**
   * method provides a recursive way to delete directories with content
   * see: https://www.baeldung.com/java-delete-directory
   * 
   * @param file
   */
  private static void deleteDir(File file) {
    File[] delFiles = file.listFiles();
    if(delFiles != null) {
      for(File dFile : delFiles) {
        deleteDir(dFile);
      }
    }
    file.delete();
    
  }

}
	

