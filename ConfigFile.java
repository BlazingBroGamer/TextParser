import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;


public class ConfigFile {
	
	String pn;
	String fn;
	String type;
	String name;
	File f;
	Yaml y;
	
	public static void main(String[] args){
		new ConfigFile("Hi", "lol");
	}
	
	public ConfigFile getByName(String pn, String name){
		File f = new File("plugins/" + pn);
		for(File f2 : f.listFiles()){
			if(f2.getName().split(".")[0].equalsIgnoreCase(name))
				return new ConfigFile(pn, f2.getName().split(".")[0]);
		}
		return new ConfigFile(pn, name + ".pr");
	}
	
	public ConfigFile(String pn, String fn){
		System.out.println("Loading document...");
		this.pn = pn;
		this.fn = fn;
		f = new File("plugins/" + pn + "/" + fn + ".pr");
		String docinfo = getAllLines().get(0);
		List<String> lines = getAllLines();
		lines.remove(0);
		if(lines.get(0).startsWith("@")){
			String[] arr = docinfo.split("@doc ")[1].split(",");
			if(arr.length == 2){
				type = arr[0].split(":")[1];
				name = arr[1].split(":")[1];
			}else{
				type = "Yaml";
				name = "Unknown";
			}
		}else{
			type = "Yaml";
			name = "Unknown";
		}
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			for(String s : lines){
				bw.append(s);
				bw.newLine();
				bw.flush();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Document loaded with type: " + type + " and name: " + name);
		if(type.equalsIgnoreCase("yaml")){
			loadYaml();
			System.out.println(getYamlStringList("Warning"));
			finishEdit();
		}
	}
	
	public String getYamlString(String s){
		return (String) getYaml(s);
	}
	
	@SuppressWarnings("unchecked")
	public Object getYaml(String s){
		Map<String, Object> object;
		try {
			object = (Map<String, Object>) y.load(new FileInputStream(f));
			return object.get(s);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getYamlStringList(String s){
		return (List<String>)getYaml(s);
	}
	
	public int getYamlInt(String s){
		return (int) getYaml(s);
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> getYamlIntList(String s){
		return (List<Integer>)getYaml(s);
	}
	
	public void finishEdit(){
		List<String> lines = getAllLines();
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.append("@doc type:" + type + ", name:" + name);
			bw.newLine();
			bw.flush();
			for(String s : lines){
				bw.append(s);
				bw.newLine();
				bw.flush();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadJson(){
		
	}
	
	public void loadYaml(){
		y = new Yaml();
		
	}
	
	public List<String> getAllLines(){
		List<String> lines = null;
    	try {
			lines = Files.readAllLines(Paths.get("plugins/" + pn + "/" + fn + ".pr"),
			        Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return lines;
	}
	
}
