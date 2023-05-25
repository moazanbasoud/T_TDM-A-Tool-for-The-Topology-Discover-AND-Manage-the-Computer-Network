package telnetclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import org.apache.commons.net.telnet.TelnetClient;

public class Telnet {

    private static final String USERNAME_PROMPT = "User Access Verification";
    private static final String PASSWORD_PROMPT = "Please Enter Password: ";
    private static final String CONFIG_PROMPT = "moazan_device1(config)#"; // Modify based on your device's behavior

    public static void executeRemoteCommands(String host, int port) {
        try {
            TelnetClient telnet = new TelnetClient();
            telnet.connect(host, port);

            InputStream in = telnet.getInputStream();
            PrintStream out = new PrintStream(telnet.getOutputStream());

            // Read until username prompt
            System.out.print(readUntil(in, USERNAME_PROMPT));

            // Prompt for username and send
            String username = promptUser("Enter username: ");
            write(out, username);

            // Read until password prompt
            System.out.print(readUntil(in, PASSWORD_PROMPT));

            // Prompt for password and send
            String password = promptUser("Enter password: ");
            write(out, password);

            // Read until initial command prompt
            String initialPrompt = readCommandPrompt(readUntil(in, ""));

            // Execute commands
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String command;
            String currentPrompt = initialPrompt;
            StringBuilder accumulatedOutput = new StringBuilder();

            while (true) {
                // Prompt for command
                System.out.print("[" + currentPrompt + "] Enter a command (or 'quit' to quit): ");
                command = reader.readLine();
                if (command.equalsIgnoreCase("quit")) {
                    break;
                }

                // Send command
                write(out, command);

                // Read command output
                String output;
                if (command.equals("configure terminal")) {
                    output = readUntil(in, CONFIG_PROMPT);
                    accumulatedOutput.append(output);
                    currentPrompt = CONFIG_PROMPT;
                } else {
                    output = readUntil(in, currentPrompt);
                    accumulatedOutput.append(output);
                    currentPrompt = readCommandPrompt(accumulatedOutput.toString());
                }

                // Print the output
                System.out.println(output);
            }

            // Disconnect
            telnet.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readUntil(InputStream in, String pattern) throws IOException {
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) > -1) {
            String chunk = new String(buffer, 0, bytesRead);
            sb.append(chunk);
            if (chunk.contains(pattern)) {
                return sb.toString();
            }
        }
        return sb.toString();
    }

    private static String readCommandPrompt(String output) {
        String[] lines = output.split("\n");
        String lastLine = lines[lines.length - 1].trim();
        if (lastLine.endsWith("#")) {
            return lastLine;
        } else if (lastLine.equals(CONFIG_PROMPT)) {
            return lastLine;
        } else {
            return "";
        }
    }

    private static void write(PrintStream out, String value) {
        out.println(value);
        out.flush();
    }

    private static String promptUser(String prompt) throws IOException {
        System.out.print(prompt);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    public static void main(String[] args) {
        String host = "192.168.3.3";
        int port = 23;

        executeRemoteCommands(host, port);
    }
}
