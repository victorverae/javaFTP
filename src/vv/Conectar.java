package vv;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class Conectar {

	public String IP_FTP = "192.168.1.1";
	public String USUARIO_FTP = "user";
	public String PASSWORD_FTP = "1234.";
	public String RUTA_DESTINO = "c:\\tmp";
	public String RUTA_FUENTE;
	
	public static void main(String[] args) {
		Conectar con = new Conectar();
		
		
		Date date = new Date();
		DateFormat hourFormat = new SimpleDateFormat("HHmm");
		System.out.println("Hora: "+hourFormat.format(date));
		//Caso 2: obtener la fecha y salida por pantalla con formato:
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		System.out.println("Fecha: "+dateFormat.format(date));
		//Caso 3: obtenerhora y fecha y salida por pantalla con formato:
		DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		System.out.println("Hora y fecha: "+hourdateFormat.format(date));
		
		String nombreCarpeta = "FTF_"+dateFormat.format(date)+"_"+hourFormat.format(date);
		
		con.createDir("c:\\tmp\\"+nombreCarpeta);
		//con.createDir("c:\\tmp\\FTF_26-11-2014_1303\\Sistema");
		FTPClient client = new FTPClient();
		
		String sFTP = con.IP_FTP;
		String sUser = con.USUARIO_FTP;
		String sPassword = con.PASSWORD_FTP;
		
		try {
		  client.connect(sFTP);
		  boolean login = client.login(sUser,sPassword);
		  con.copiarDir(client, nombreCarpeta);
		  client.logout();
		  client.disconnect();
		}catch(Exception e){System.out.println("Error:"+e);}
		//con.getArchivoFTP("newfile.php", "c:\\tmp\\"+nombreCarpeta, "Sistema");
		/*
		FTPClient client = new FTPClient();
		
		String sFTP = con.IP_FTP;
		String sUser = con.USUARIO_FTP;
		String sPassword = con.PASSWORD_FTP;
		 
		try {
		  client.connect(sFTP);
		  boolean login = client.login(sUser,sPassword);
//		  System.out.println(client.printWorkingDirectory());
//		  client.changeWorkingDirectory("\\Sistema");
//		  System.out.println(client.printWorkingDirectory());
//		  System.out.println("Login"+login);
//		  con.listarDirectorio(client);
		  
		  con.copiarDir(client);
		  
		  //FTPFile[] ftpDirectories = client.listDirectories();
		  //con.listar(ftpDirectories);
		  
		  client.changeWorkingDirectory("Sistema");
		  
//		  con.listarDirectorio(client);
		  
		  System.out.println("replicode:"+client.getReplyCode());
//		  ArrayList<objFTP> obj = con.getContent(client);
		  
		  client.logout();
		  client.disconnect();
		} catch (IOException ioe) {}
		*/
	}
	
	private String getTime(String strFormat){
		Date date = new Date();
		DateFormat fFormat = new SimpleDateFormat(strFormat);
		return fFormat.format(date);
	}
	
	private void createDir(String strPath){
		File directorio = new File(strPath);
		if (directorio.mkdir())
		     System.out.println("Se ha creado directorio");
		   else
		     System.out.println("No se ha podido crear el directorio");
	}
	public void copiarDir(FTPClient client,String carpeta){
		try{
			System.out.println("INICIO: "+this.getTime("dd-MM-yyyy HH:mm:ss"));
			//Copiar archivos
			FTPFile[] ftpArchivos = client.listFiles();
			try{
				  if (ftpArchivos.length > 0){
					  for(FTPFile arch : ftpArchivos){
					   if(arch.getType()==1){
						   String dir = this.RUTA_DESTINO+"\\"+carpeta+"\\"+arch.getName();
						   //System.out.println("Crear directorio:"+dir);
						   this.createDir(dir);
						   client.changeWorkingDirectory(arch.getName());
						   this.copiarDir(client,carpeta+"\\"+arch.getName());
						   client.changeWorkingDirectory("..");
					   }else{
						   //copia archivo
						   String ls_nombre_archivo = arch.getName();
						   //System.out.println(ls_nombre_archivo+", "+arch.getType());
						   this.getArchivoFTPClient(client,arch.getName(), this.RUTA_DESTINO+"\\"+carpeta, carpeta);
					   }
					  }
				  }
				}catch(Exception e){System.out.println("ERROR:"+e);}
			//this.listar(ftpArchivos);
			//FTPFile[] ftpDirectories = client.listDirectories();
			//this.listar(ftpDirectories);
			System.out.println("FIN: "+this.getTime("dd-MM-yyyy HH:mm:ss"));	
			
		}catch(Exception e){System.out.println("ERROR:"+e);}
	}
	public void listar(FTPFile[] list){
		//Lista archivos y directorios
		try{
		  if (list.length > 0){
		   for(FTPFile arch : list){
		    String ls_nombre_archivo = arch.getName();
		    System.out.println(ls_nombre_archivo+", "+arch.getType());
		   }
		  }
		}catch(Exception e){}
	}
	
	public void listarDirectorio(FTPClient client){
		//Lista archivos y directorios
		try{
		FTPFile[] archivosFTP = client.listFiles();
		  if (archivosFTP.length > 0){
		   for(FTPFile arch : archivosFTP){
		    String ls_nombre_archivo = arch.getName();
		    //System.out.println(ls_nombre_archivo+", "+arch.getType());
		   }
		  }
		}catch(Exception e){}
	}
	
	
	
	public ArrayList<objFTP> getContent(FTPClient client){
		//Lista archivos y directorios
		ArrayList<objFTP> obj = null;
		try{
			FTPFile[] archivosFTP = client.listFiles();
			if (archivosFTP.length > 0){
				for(FTPFile arch : archivosFTP){
					objFTP obj1 = new objFTP();
					obj1.setName(arch.getName());
					obj1.setType(arch.getType()); 
					String ls_nombre_archivo = arch.getName();
					System.out.println(ls_nombre_archivo+", "+arch.getType());
					obj.add(obj1);
				}
			}
		}catch(Exception e){}
		return obj;
	}
	
	public File getArchivoFTPClient(FTPClient client, String nombreArchivo, String rutaDestino, String rutaFuente) {
		 File f_retorno = null;
		 int respuesta;
		 String ps_archivo = nombreArchivo;
		 try {
		  // establecer conexion
		  String ruta_pdf_local = rutaDestino;

		  if (true) {
		   respuesta = client.getReplyCode();
		   if (true) {
		    client.setFileType(FTP.BINARY_FILE_TYPE);
		    //client.changeWorkingDirectory(rutaFuente);
		    //respuesta = client.getReplyCode();
		    //if (FTPReply.isPositiveCompletion(respuesta)) {
		    if(true){ 
		    FTPFile archivosFTP[] = client.listFiles();
		     respuesta = client.getReplyCode();
		     if (FTPReply.isPositiveCompletion(respuesta)) {
		      if (archivosFTP.length > 0) {
		        for(int i=0; i< archivosFTP.length; i++){
		        String nombre = archivosFTP[i].getName();
		        if (nombre.equals(ps_archivo)) {
		         String archivo_salida = ruta_pdf_local
		           + File.separator
		           + archivosFTP[i].getName();
		         boolean retorno_download = client
		           .retrieveFile(archivosFTP[i]
		             .getName(),
		             new FileOutputStream(
		               archivo_salida));
		         if (retorno_download) {
		          f_retorno = new File(archivo_salida);
		          if (!(f_retorno.length() > 0)) {
		           System.out
		             .println("Advertencia: Archivo con longitud 0");
		           f_retorno = null;
		          }
		         } else {
		          System.out
		            .println("No se pudo descargar en: "
		              + archivo_salida);
		         }
		         }
		         if (f_retorno != null) {
		          break;
		         }
		        }
		       if (f_retorno == null) {
		        System.out
		          .println("No se pudo descargar el archivo: "
		            + ps_archivo);
		       }
		      } else {
		       System.out
		         .println("Listado de archivos de longitud invalida -"
		           + archivosFTP.length);
		      }
		     } else {
		      System.out
		        .println("No se pudo listar el directorio -");
		     }
		    } else {
		     System.out
		       .println("No se pudo cambiar de directorio -");
		    }
		   } else {
		    System.out.println("No se pudo autenticar -");
		   }
		  } else {
		   System.out.println("Login y password invalidos -");
		  }
		 } catch (SocketException ex) {
		  ex.printStackTrace();
		 } catch (IOException ex) {
		  ex.printStackTrace();
		 } finally {
//		  try {
//		   //ftp.disconnect();
//		  } catch (IOException ex) {
//		   ex.printStackTrace();
//		  }
		 }
		 return f_retorno;
	}
	
	public File getArchivoFTP(String nombreArchivo, String rutaDestino, String rutaFuente) {
		 File f_retorno = null;
		 FTPClient ftp = new FTPClient();
		 int respuesta;
		 
		 String ps_ip = this.IP_FTP;
		 String ps_archivo = nombreArchivo;
		 String ps_usuario = this.USUARIO_FTP;
		 String ps_password = this.PASSWORD_FTP;
		 try {
		  // establecer conexion
		  String ruta_pdf_local = rutaDestino;

		  ftp.connect(ps_ip);
		  if (!ps_usuario.equals("") && !ps_password.equals("")) {
		   ftp.login(ps_usuario, ps_password);
		   respuesta = ftp.getReplyCode();
		   if (respuesta == 230) {
		    ftp.setFileType(FTP.BINARY_FILE_TYPE);
		    ftp.changeWorkingDirectory(rutaFuente);
		    respuesta = ftp.getReplyCode();
		    if (FTPReply.isPositiveCompletion(respuesta)) {
		     FTPFile archivosFTP[] = ftp.listFiles();
		     respuesta = ftp.getReplyCode();
		     if (FTPReply.isPositiveCompletion(respuesta)) {
		      if (archivosFTP.length > 0) {
		        for(int i=0; i< archivosFTP.length; i++){
		        String nombre = archivosFTP[i].getName();
		        if (nombre.equals(ps_archivo)) {
		         String archivo_salida = ruta_pdf_local
		           + File.separator
		           + archivosFTP[i].getName();
		         boolean retorno_download = ftp
		           .retrieveFile(archivosFTP[i]
		             .getName(),
		             new FileOutputStream(
		               archivo_salida));
		         if (retorno_download) {
		          f_retorno = new File(archivo_salida);
		          if (!(f_retorno.length() > 0)) {
		           System.out
		             .println("Advertencia: Archivo con longitud 0");
		           f_retorno = null;
		          }
		         } else {
		          System.out
		            .println("No se pudo descargar en: "
		              + archivo_salida);
		         }
		         }
		         if (f_retorno != null) {
		          break;
		         }
		        }
		       if (f_retorno == null) {
		        System.out
		          .println("No se pudo descargar el archivo: "
		            + ps_archivo);
		       }
		      } else {
		       System.out
		         .println("Listado de archivos de longitud invalida -"
		           + archivosFTP.length);
		      }
		     } else {
		      System.out
		        .println("No se pudo listar el directorio -");
		     }
		    } else {
		     System.out
		       .println("No se pudo cambiar de directorio -");
		    }
		   } else {
		    System.out.println("No se pudo autenticar -");
		   }
		  } else {
		   System.out.println("Login y password invalidos -");
		  }
		 } catch (SocketException ex) {
		  ex.printStackTrace();
		 } catch (IOException ex) {
		  ex.printStackTrace();
		 } finally {
		  try {
		   ftp.disconnect();
		  } catch (IOException ex) {
		   ex.printStackTrace();
		  }
		 }
		 return f_retorno;
	}
}
