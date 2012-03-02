package au.edu.anu.viewer.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.edu.anu.viewer.listener.UploadProgressListener;

/**
 * Allows the upload of files to a server
 * 
 *
 */
@WebServlet(name="UploadController",
urlPatterns="/upload")
public class UploadController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//TODO Put this location in properties file
	private static final String tempDir = "C:/WorkSpace/UploadLocation/Test/";

	final Logger log = LoggerFactory.getLogger(UploadController.class);

	/**
	 * Accepts files to upload, either the full file or in parts sent from jupload.
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException{
		log.debug("In doPost");
		PrintWriter out = response.getWriter();
		String jupart = request.getParameter("jupart");
		String jufinal = request.getParameter("jufinal");
		int chunkNum = 0;
		boolean isFinal = false;
		if(jupart != null && !jupart.equals("")){
			chunkNum = Integer.parseInt(jupart);
		}
		if(jufinal != null && !jufinal.equals("")){
			isFinal = jufinal.equals("1");
		}
		
		try{
			DiskFileItemFactory factory = new DiskFileItemFactory();
			//factory.setRepository("");
			//TODO Put this location in properties file
			File file = new File("C:/WorkSpace/UploadLocation");
			factory.setRepository(file);
			ServletFileUpload upload = new ServletFileUpload(factory);
			UploadProgressListener uploadProgress = new UploadProgressListener();
			upload.setProgressListener(uploadProgress);
			HttpSession session = request.getSession();
			session.setAttribute("uploadProgress", uploadProgress);
			
			List<FileItem> items = upload.parseRequest(request);
			//items.
			Iterator<FileItem> iter = items.iterator();
			while(iter.hasNext()){
				FileItem item = (FileItem) iter.next();
				if(item.isFormField()){
					processFormField(item);
				}else{
					processUploadedFile(item, chunkNum);
					if(isFinal){
						processFinalFile(item, chunkNum);
					}
				}
			}
			out.println("Processed Submission");
		}catch(FileUploadException e){
			log.error("Exception uploading file: " + e.toString());
			out.println("Error Processing Submission");
		}
		out.close();
	}
	
	/**
	 * Processes form files
	 * @param item The item to process
	 */
	private void processFormField(FileItem item){
		String name = item.getFieldName();
		String value = item.getString();
		log.debug("Field: " + name + ", Value: " + value);
	}
	
	/**
	 * Processes uploaded files chunks
	 * @param item The item to process
	 * @param chunkNum The sequence number of the file
	 */
	private void processUploadedFile(FileItem item, int chunkNum){
		String fieldName = item.getFieldName();
		String fileName = item.getName();
		long sizeInBytes = item.getSize();
		log.debug("Field: " + fieldName + ", File: " + fileName);
		fileName = new File(fileName).getName();
		if(chunkNum > 0){
			fileName = fileName + ".part" + chunkNum;
		}
		try{
			File file = new File(tempDir + fileName);
			if(file.exists()){
				if(file.length() == sizeInBytes){
					return;
				}
				return;
			}
			InputStream is = item.getInputStream();
			file.createNewFile();
			OutputStream os = new FileOutputStream(file);
			byte[] bytes = new byte[1024];
			int len = is.read(bytes);
			while(len != -1){
				os.write(bytes, 0, len);
				len = is.read(bytes);
			}
			os.close();
			is.close();
		}catch(IOException e){
			log.error("Exception writing to file: " + e.toString());
		}
	}
	
	/**
	 * Does final processing on files.
	 * @param item The item to process
	 * @param chunkNum The sequence number of the file
	 */
	private void processFinalFile(FileItem item, int chunkNum){
		log.debug("Final Chunk, Number of Chunks: " + chunkNum);
		FileInputStream fis;
		try{
			FileOutputStream fos = new FileOutputStream(tempDir + item.getName());
			byte[] bytes = new byte[1024];
			int len;
			String filename;
			// put the chunks together
			for(int i = 1; i <= chunkNum; i++){
				filename = item.getName() + ".part" + i;
				fis = new FileInputStream(tempDir + filename);
				len = fis.read(bytes);
				while(len != -1){
					fos.write(bytes, 0, len);
					len = fis.read(bytes);
				}
				fis.close();
			}
			fos.close();

			//Delete the chunks
			boolean deleted;
			
			for(int i = 1; i <= chunkNum; i++){
				filename = item.getName() + ".part" + i;
				File file = new File(tempDir + filename);
				deleted = file.delete();
				if(!deleted){
					log.error("Unable to delete file: " + filename);
				}
			}
		}catch(FileNotFoundException e){
			log.error("Erring with file: " + e.toString());
		}catch(IOException e){
			log.error("Error with file: " + e.toString());
		}
	}
}
