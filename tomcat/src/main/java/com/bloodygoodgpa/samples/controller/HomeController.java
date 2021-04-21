package com.bloodygoodgpa.controller;
import com.bloodygoodgpa.samples.MongoCross;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.lang.Class;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/data")
public class HomeController {

	@GetMapping
	public ResponseEntity<byte[]> get(RequestEntity<String> request) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
		MongoCross db = new MongoCross();
		
		String query = request.getUrl().toString();
		query = query.substring(query.indexOf("?")+1);
		String[] input = query.split("&");
		String method = input[0].split("=")[1];
		Integer numArgs = Integer.parseInt(input[1].split("=")[1]);
		
		Class[] argTypes = new Class[numArgs];
		Arrays.fill(argTypes, String.class);
		
		Object[] argVals =  new Object[numArgs];
		HttpHeaders headers = request.getHeaders();
				
		for(int i = 1; i <= numArgs; i++) {
			argVals[i-1] = headers.get("args"+i).get(0);
		}				
		Object list = db.getClass()
							.getDeclaredMethod(method, argTypes)
							.invoke(db, argVals);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream objOut = new ObjectOutputStream(out);
		objOut.writeObject(list);
		byte[] retVal = out.toByteArray();
		
		return new ResponseEntity<byte[]>(retVal, HttpStatus.OK); 
	}
	
	@PostMapping
	public ResponseEntity<byte[]> post(RequestEntity<String> request) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
		MongoCross db = new MongoCross();
		
		String query = request.getUrl().toString();
		query = query.substring(query.indexOf("?")+1);
		String[] input = query.split("&");
		String method = input[0].split("=")[1];
		Integer numArgs = Integer.parseInt(input[1].split("=")[1]);
		
		Class[] argTypes = new Class[numArgs];
		Arrays.fill(argTypes, String.class);
		
		Object[] argVals =  new Object[numArgs];
		HttpHeaders headers = request.getHeaders();
				
		for(int i = 1; i <= numArgs; i++) {
			argVals[i-1] = headers.get("args"+i).get(0);
		}				
		Object list = db.getClass()
							.getDeclaredMethod(method, argTypes)
							.invoke(db, argVals);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream objOut = new ObjectOutputStream(out);
		objOut.writeObject(list);
		byte[] retVal = out.toByteArray();
		
		return new ResponseEntity<byte[]>(retVal, HttpStatus.OK); 
	}
	
	@PutMapping
	public ResponseEntity<byte[]> put(RequestEntity<String> request) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
		MongoCross db = new MongoCross();
		
		String query = request.getUrl().toString();
		query = query.substring(query.indexOf("?")+1);
		String[] input = query.split("&");
		String method = input[0].split("=")[1];
		Integer numArgs = Integer.parseInt(input[1].split("=")[1]);
		
		Class[] argTypes = new Class[numArgs];
		Arrays.fill(argTypes, String.class);
		
		Object[] argVals =  new Object[numArgs];
		HttpHeaders headers = request.getHeaders();
				
		for(int i = 1; i <= numArgs; i++) {
			argVals[i-1] = headers.get("args"+i).get(0);
		}				
		Object list = db.getClass()
							.getDeclaredMethod(method, argTypes)
							.invoke(db, argVals);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream objOut = new ObjectOutputStream(out);
		objOut.writeObject(list);
		byte[] retVal = out.toByteArray();
		
		return new ResponseEntity<byte[]>(retVal, HttpStatus.OK); 
	}
	
	@DeleteMapping
	public ResponseEntity<byte[]> delete(RequestEntity<String> request) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
		MongoCross db = new MongoCross();
		
		String query = request.getUrl().toString();
		query = query.substring(query.indexOf("?")+1);
		String[] input = query.split("&");
		String method = input[0].split("=")[1];
		Integer numArgs = Integer.parseInt(input[1].split("=")[1]);
		
		Class[] argTypes = new Class[numArgs];
		Arrays.fill(argTypes, String.class);
		
		Object[] argVals =  new Object[numArgs];
		HttpHeaders headers = request.getHeaders();
				
		for(int i = 1; i <= numArgs; i++) {
			argVals[i-1] = headers.get("args"+i).get(0);
		}				
		Object list = db.getClass()
							.getDeclaredMethod(method, argTypes)
							.invoke(db, argVals);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream objOut = new ObjectOutputStream(out);
		objOut.writeObject(list);
		byte[] retVal = out.toByteArray();
		
		return new ResponseEntity<byte[]>(retVal, HttpStatus.OK); 
	}
	
}
