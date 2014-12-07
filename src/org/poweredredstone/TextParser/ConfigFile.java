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
	
	/**
	 * GetByName
	 * 
	 * Gets a file according to their doc name
	 * 
	 * @param pn
	 * @param name
	 */
	
	public ConfigFile getByName(String pn, String name){
		File f = new File("plugins/" + pn);
		for(File f2 : f.listFiles()){
			if(f2.getName().split(".")[0].equalsIgnoreCase(name))
				return new ConfigFile(pn, f2.getName().split(".")[0]);
		}
		return new ConfigFile(pn, name + ".pr");
	}
	
	/**
	 * ConfigFile
	 * 
	 * Creates a config file according to their file name
	 * 
	 * @param pn
	 * @param fn
	 */
	
	public ConfigFile(String pn, String fn){
		System.out.println("Loading document...");
		this.pn = pn;
		this.fn = fn;
		f = new File("plugins/" + pn + "/" + fn + ".pr");
		String docinfo = getAllLines().get(0);
		List<String> lines = getAllLines();
		lines.remove(0);
		if(lines.get(0).startsWith("@")){
			//Checks for @doc
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
		//Checking Type
		System.out.println("Document loaded with type: " + type + " and name: " + name);
		if(type.equalsIgnoreCase("yaml")){
			loadYaml();
			System.out.println(getYamlStringList("Warning"));
			finishEdit();
		}
	}
	
	/**
	 * GetYamlString
	 * 
	 * Gets a string from the path, in the yaml document loaded above.
	 * 
	 * @param path
	 * @return
	 */
	
	public String getYamlString(String path){
		return (String) getYaml(path);
	}
	
	/**
	 * GetYaml
	 * 
	 * This is used to get objects from the Text File
	 * 
	 * @param s
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public Object getYaml(String path){
		Map<String, Object> object;
		try {
			object = (Map<String, Object>) y.load(new FileInputStream(f));
			return object.get(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * GetYamlStringList
	 * 
	 * Gets a string list from the path, in the yaml document loaded above.
	 * 
	 * @param path
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public List<String> getYamlStringList(String path){
		return (List<String>)getYaml(path);
	}
	
	/**
	 * GetYamlInt
	 * 
	 * Gets an integer from the path, in the yaml document loaded above.
	 * 
	 * @param path
	 * @return
	 */
	
	public int getYamlInt(String path){
		return (int) getYaml(path);
	}
	
	/**
	 * GetYamlIntList
	 * 
	 * Gets an integer list from the path, in the yaml document above.
	 * 
	 * @param path
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public List<Integer> getYamlIntList(String path){
		return (List<Integer>)getYaml(path);
	}
	
	/**
	 * FinishEdit
	 * 
	 * This command saves the progress to the file, and retypes the doc data in the top line.
	 * 
	 */
	
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
	
	/**
	 * LoadYaml
	 * 
	 * Planning to add more later, but this loads the Yaml file up.
	 * 
	 */
	
	public void loadYaml(){
		y = new Yaml();
	}

	/**
	 * GetAllLines
	 * 
	 * Gets all the lines from the File above.
	 * 
	 * @return
	 */
	
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
