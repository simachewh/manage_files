package org.sime.seven.managefiles.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.naming.spi.DirStateFactory;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.sime.seven.managefiles.model.FileModel;
import org.sime.seven.managefiles.model.FilesListModel;
import org.sime.seven.managefiles.model.Link;
import org.sime.seven.managefiles.resource.FilesResource;

public class FilesService {

	// @Context
	// private ServletContext context;
	final private String separator = System.getProperty("file.separator");
	final private String PROJECT_DIR = (new File("")).getAbsolutePath() + separator;//"webapps/managefiles1/";
	final private String DIR_NAME = "remotefiles";
	private String DIR_PATH = "";
	private File file = null;

	public FilesService() {
		/*DIR_PATH = (new File("")).getAbsolutePath() + PROJECT_DIR + DIR_NAME + separator;*/
		
		DIR_PATH =  PROJECT_DIR + DIR_NAME;	
		
		file = new File(DIR_PATH);
		if(!file.exists()){
			file.mkdir();
		}	
		
	}
	
	public String getDirPath(){
		return file.getAbsolutePath() + " file exists " + file.exists();
	}
	
	

	/**
	 * @return {@link FileModel}
	 */
	public FilesListModel getFilesList(UriInfo uriInfo) {
		FilesListModel fileList = new FilesListModel();
		File dir = new File(DIR_PATH);
		try {
			if (!dir.exists()) {
				fileList.getFilesList().clear();
				return fileList;
			}
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File[] files = dir.listFiles();
		for (File f : files) {
			FileModel fm = new FileModel();

			fm.setFileName(f.getName());
			String link = uriInfo.getBaseUriBuilder().path(FilesResource.class)
					.path("download").path(f.getName()).build().toString();
			fm.addLink(link, "downloadself");

			fileList.getFilesList().add(fm);
		}
		return fileList;
	}

	/**
	 * A service method to save the file submitted by the client to a location
	 * on the server.
	 * 
	 * @param is
	 *            {@link InputStream} the input stream containing the byte array
	 *            of the file.
	 * @param fileName
	 *            {@link String} the name of the file
	 * @return
	 */
	public boolean uploadFile(InputStream is, String fileName) {
		boolean ok = false;
		// uploadDir = context.getRealPath(separator) + "upload" + separator;
		String filePath = DIR_PATH + separator + fileName;
		File uploadDir = new File(DIR_PATH);
		File uploadedFile = new File(filePath);
		try {
			if (!uploadDir.exists()) {
				uploadDir.mkdir();
			}
			if (!uploadedFile.exists()) {
				uploadedFile.createNewFile();
			}else{
				uploadedFile = new File(DIR_PATH + separator + Calendar.SECOND + fileName);
				uploadedFile.createNewFile();
			}
			OutputStream os = new FileOutputStream(uploadedFile);
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = is.read(bytes)) != -1) {
				os.write(bytes);
				bytes = new byte[1024];
			}
			os.flush();
			os.close();
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (uploadedFile.exists() && uploadedFile.length() > 0) {
			ok = true;
		}
		return ok;
	}

	public File downloadFile(String fileName) {
		String path = DIR_PATH + separator + fileName;
		File file = new File(path);
		return file;
	}

	public String getDIR_PATH() {
		return DIR_PATH;
	}

	public void setDIR_PATH(String dIR_PATH) {
		DIR_PATH = dIR_PATH;
	}

	public String getSeparator() {
		return separator;
	}

}
