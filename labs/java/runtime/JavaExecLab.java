import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 *
 * @author Max
 */

class StreamConsumer extends Thread {
    InputStream is;
    String type;
     
    StreamConsumer (InputStream is, String type) {
        this.is = is;
        this.type = type;
    }
     
    public void run () {
        try {
            InputStreamReader isr = new InputStreamReader (is);
            BufferedReader br = new BufferedReader (isr);
            String line = null;
            while ((line = br.readLine()) != null)
                System.out.println (type + ">" + line);    
        } catch (IOException ioe) {
            ioe.printStackTrace();  
        }
    }
}

public class JavaExecLab {
    public static void main(String[] args) {
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("javac Test.java");
            String line = null;
            
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            System.out.println("<error>");
            while((line=br.readLine()) != null)
                System.out.println(line);
            System.out.println("</error>");
            
            InputStream stdout = proc.getInputStream();
            InputStreamReader osr = new InputStreamReader(stdout);
            BufferedReader obr = new BufferedReader(osr);
            
            System.out.println("<stdout>");
            while((line=obr.readLine())!= null) {
                System.out.println(line);
            }
            System.out.println("</stdout>");
            
            int exitcode = proc.waitFor();
            System.out.println("Process exit value: "+exitcode);

            // proc = rt.exec(new String[]{"java", "Test"});
            if (exitcode == 0) {
                proc = rt.exec("java Test");

                // 2nd for running java

                StreamConsumer errorConsumer = new
                    StreamConsumer (proc.getErrorStream(), "error");            
                 
                // any output?
                StreamConsumer outputConsumer = new
                    StreamConsumer (proc.getInputStream(), "output");
                     
                // kick them off
                errorConsumer.start();
                outputConsumer.start();
                                         
                // any error???
                int exitVal = proc.waitFor ();
     
                exitcode = proc.waitFor();
                System.out.println("Process exit value: "+exitVal);
            } else {
                System.out.println("You got compile errors!");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }
}
