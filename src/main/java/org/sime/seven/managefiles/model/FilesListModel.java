package org.sime.seven.managefiles.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FilesListModel {
	
	@XmlElement(required=true)
	private List<FileModel> filesList = new ArrayList<FileModel>();
	
	public FilesListModel(){
		
	}

	public List<FileModel> getFilesList() {
		return filesList;
	}

	public void setFilesList(List<FileModel> filesList) {
		this.filesList = filesList;
	}
	
}
