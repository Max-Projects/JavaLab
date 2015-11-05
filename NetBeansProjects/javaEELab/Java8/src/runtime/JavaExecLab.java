package runtime;

import java.io.BufferedReader;
import java.io.File;
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
//            Process proc = rt.exec("javac Test.java", null, new File("/Users/Max/tmp/"));
            Process proc = rt.exec("javac Test.java", null, new File("/Users/Max/maxlab/NetBeansProjects/javaEELab/Java8/src/runtime/"));
            StreamConsumer errorConsumer = new
                StreamConsumer (proc.getErrorStream(), "error");            
                 
                // any output?
            StreamConsumer outputConsumer = new
                StreamConsumer (proc.getInputStream(), "output");
                     
                // kick them off
            errorConsumer.start();
            outputConsumer.start();
            
            int exitcode = proc.waitFor();
            if (exitcode == 0)
                System.out.println("compile ok!");

            if (exitcode == 0) {
//                proc = rt.exec("java Test", null, new File("/Users/Max/tmp/"));
                proc = rt.exec("java Test", null, new File("/Users/Max/maxlab/NetBeansProjects/javaEELab/Java8/src/runtime/"));

                // 2nd for running java

                errorConsumer = new StreamConsumer (proc.getErrorStream(), "error");            
                 
                // any output?
                outputConsumer = new StreamConsumer (proc.getInputStream(), "output");
                     
                // kick them off
                errorConsumer.start();
                outputConsumer.start();
                                         
                // any error???
                exitcode = proc.waitFor ();
     
                exitcode = proc.waitFor();
                System.out.println("Process exit value: "+exitcode);
            } else {
                System.out.println("You got compile errors!");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }
}
