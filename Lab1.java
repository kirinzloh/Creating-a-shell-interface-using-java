import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Lab1 {
	public static class CommandHistory{
		private List<String> history = new ArrayList<String>();
	}
	public static void main(String[] args) throws java.io.IOException {
		String commandLine;
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		ProcessBuilder pb = new ProcessBuilder();
		File startDir = new File(System.getProperty("user.dir"));
		pb.directory(startDir);
		
		CommandHistory cmdHistory = new CommandHistory();
		
		Integer indexOfHistory = 1;
		
		while (true) {
			// read what the user entered
			System.out.print("jsh>");
			commandLine = console.readLine();
			String[] mycommands = commandLine.split(" ");
						
			// if the user entered a return, just loop again
			if (commandLine.equals("")){
				continue;
			}
			
			try {
				
				if (commandLine.matches("!!")){
					if (cmdHistory.history.size() == 1){
						System.out.println("Error: No previous command found");
						continue;
					}
					else {
						String newcmds = cmdHistory.history.get(cmdHistory.history.size()-1).substring(indexOfHistory.toString().length()+1);
						mycommands = newcmds.split(" ");
						commandLine = newcmds;
					}
				}
				
				if (commandLine.matches("\\d+")){
					if (Integer.parseInt(commandLine) > indexOfHistory){
						System.out.println("Error: Invalid number in command history");
						continue;
					}
					Integer index = Integer.parseInt(commandLine);
					String newcmds = cmdHistory.history.get(index-1).substring(indexOfHistory.toString().length()+1);
					mycommands = newcmds.split(" ");
					commandLine = newcmds;
				}
				
				if (commandLine.matches("history")){
					for (String s: cmdHistory.history){
						System.out.println(s);
					}
					continue;
				}
			
				
				if (commandLine.contains("cd")){
					if (commandLine.matches("cd") == true){
						File home = new File(System.getProperty("user.home"));
						System.out.println(home);
						pb.directory(home);
						continue;
					}
					else if (commandLine.matches("cd ..")){
						File parentDir = new File(pb.directory().getParent());
						System.out.println(parentDir);
						pb.directory(parentDir);
						continue;
					}
					else{
						String dir = mycommands[1];
						File newDir = new File(pb.directory() + File.separator + dir);
						if (newDir.isDirectory()){
							System.out.println(newDir);
							pb.directory(newDir);
							continue;
						}
						else {
							System.out.println(dir + ": No such file or directory");
							continue;
						}
					}
				}
				
				pb.command(mycommands);
				Process p = pb.start();
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				
				String s;
				while ((s = br.readLine()) != null){
					System.out.println(s);
				}
				br.close();
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
			cmdHistory.history.add(indexOfHistory.toString() + " " + commandLine);
			indexOfHistory++;
			
		}
	}
}

