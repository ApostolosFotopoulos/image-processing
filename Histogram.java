package ce325.hw2;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Histogram {
    private int[] histogram;
    private YUVImage image;

    static final int MAX_Y_VALUE = 235;
    static final int MIN_Y_VALUE = 16;
    static final int MAX_LINE_LENGTH = 80;
    
    double[] cumulativeProbDisribution;
    
    //Kataskeuasths pou dhmiourgei ena istogramma mias YUV eikonas.
    public Histogram(YUVImage img) {
        image = img;
        histogram = new int[MAX_Y_VALUE+1];
        
        for(int i=0; i<image.height; i++) {
            for(int k=0; k<image.width; k++) {
                histogram[image.pixelbuf[i][k].getY()]++;
            }
        }
    }
    
    //Methodos gia thn exisorrophsh tou istogrammatos.
    public void equalize() {
        int totalPixels = image.height*image.width;
        
        if (histogram == null || totalPixels == 0) {
            return;
        }
        
        //Ypologismos katanomhs pithanothtas.
        double[] probDisribution = new double[MAX_Y_VALUE+1];
        for(int i=MIN_Y_VALUE; i<MAX_Y_VALUE+1; i++) {
            probDisribution[i] =  (double)histogram[i]/totalPixels;
        }
        
        //Ypologismos athroistikhs katanomhs pithanothtas.
        cumulativeProbDisribution = new double[MAX_Y_VALUE+1];
        for(int i=MIN_Y_VALUE; i<MAX_Y_VALUE+1; i++) {
            for(int k=MIN_Y_VALUE; k<=i; k++) {
                cumulativeProbDisribution[i] += probDisribution[k];
            }  
        }
        
        /*for(int i=MIN_Y_VALUE; i<MAX_Y_VALUE+1; i++) {
            System.out.println(i+" "+probDisribution[i]+" "+cumulativeProbDisribution[i]+" "+newYvalue[i]);
        }*/
        
        //Antikatastash ths palias Y timhs gia kathe pixel ths eikonas me thn antistoixh kainourgia.
        for(int i=0; i<image.height; i++) {
            for(int k=0; k<image.width; k++) {
                short oldY = image.pixelbuf[i][k].getY();
                image.pixelbuf[i][k].setY(getEqualizedLuminocity(oldY));
            }
        }
    }
    
    //Dinontas thn palia timh fwteinothtas epistrefei thn eksisorrophmenh nea timh.
    public short getEqualizedLuminocity (int luminocity) {
        //Pollaplasiasmos me thn max fwteinothta kai metatroph twn double timwn se short.
        return (short) (cumulativeProbDisribution[luminocity]*MAX_Y_VALUE);
    }
    
    //Methodos pou ektypwnei to String ths to String se ena arxeio.
    public void toFile(File file) {
        String str = toString();
        if (!str.equals("")) {
            try (PrintWriter writer = new PrintWriter(file)){
                writer.print(str);
            }
            catch (IOException ex){
                System.out.println("Error while writing " + file.getName());
                ex.printStackTrace();
            }
        }
    }
    
    /*Methodos pou epistrefei ena string pou exei ton arithmo ths fwteinothtas
    akolouthomeno apo mia seira apo "*" xarakthres.O arithmos twn "*' xarakthrwn
    krinetai apo ton arithmo twn pixel pou exoun thn sygkekrymenh fwteinothta.
    Oso pio megalos o arithmos toso megalyteros kai o arithmos twn "*" pou 
    antistoixoun sthn antistoixh fwteinothta.Oi arithmoi kanonikopoiountai me
    max to 80*/
    public String toString() {
        String str = "";
        
        //Ypologismos tou max stoixeiou tou pinaka.
        int max = -1;
        for(int i=MIN_Y_VALUE; i<MAX_Y_VALUE+1; i++) {
            if (histogram[i]>max) {
                max = histogram[i];
            }      
        }
        
        //Kanonikopoihsh sthn klimaka [0-80] ean max>80.
        if (max>0) {
            for(int i=MIN_Y_VALUE; i<MAX_Y_VALUE+1; i++) {
                int normValue = histogram[i];
                if (max>80) {
                    normValue = (histogram[i]*MAX_LINE_LENGTH)/max; 
                }
                
                //str += i + " " + histogram[i] + " ";
                str += i + " ";
                for(int k=0; k<normValue; k++) {
                    str += "*";
                }
                str += "\n";
            }
        }
        
        return str;
    }
}
