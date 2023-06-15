package ce325.hw2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PPMImageStacker {
    private List<PPMImage> images;
    private PPMImage stackedImage;
    
    static final int MAX_COLORDEPTH = 255;
    
    //Kataskeuasths ths listas twn eikonwn.
    public PPMImageStacker(File dir) {
        if (dir.exists()) {
            if (dir.isDirectory()) {
                
                File files[] =  dir.listFiles();
                images = new ArrayList<>();
                
                for(File f : files) {
                    System.out.println(f.getName());
                    /*Ola ta arxeia pou briskontai sto fakelo mpainoun sth lista
                    twn eikonwn pou theloume na kanoume stack*/
                    if (f.getName().endsWith(".ppm")) {
                        PPMImage newImg = new PPMImage(f);
                        images.add(newImg);
                    }
                    /*An kapoio arxeio pou brisketai mesa sto fakelo den einai 
                     typou PPM tote den mpainei sthn lista */
                    else {
                        System.out.println(f.getName()+" is not a ppm file.");
                    }
                }
            }
            //An to arxeio den einai fakelos.
            else {
               System.out.println("[ERROR] " + dir.getName() + " is not a directory!"); 
            }
        }
        //An den yparxei o fakelos.
        else {
            FileNotFoundException ex = new FileNotFoundException("Directory " + dir.getName() + " does not exist!");
            ex.printStackTrace();
        }
    }
    
    public PPMImageStacker(File[] files) {
                
        images = new ArrayList<>();

        for(File f : files) {
            System.out.println(f.getName());
            /*Ola ta arxeia pou dialexe o xrhsths mpainoun sth lista
            twn eikonwn pou theloume na kanoume stack*/
            if (f.getName().endsWith(".ppm")) {
                PPMImage newImg = new PPMImage(f);
                images.add(newImg);
            }
            /*An kapoio arxeio pou brisketai mesa sto fakelo den einai 
             typou PPM tote den mpainei sthn lista */
            else {
                System.out.println(f.getName()+" is not a ppm file.");
            }
        }
    }
    
    //Methodos pou dhmiourgei thn stackedImage , dhladh thn eikona sthn opoia elaxistopoieitai o thorybos.
    public void stack() {
        if (!images.isEmpty()) {
            //Pinakes pou periexoun to athroisma twn timwn rgb olwn twn eikonwn gia kathe pixel.
            short[][] redSum = new short[images.get(0).height][images.get(0).width];
            short[][] greenSum = new short[images.get(0).height][images.get(0).width];
            short[][] blueSum = new short[images.get(0).height][images.get(0).width];
            
            stackedImage = new PPMImage(images.get(0).width,images.get(0).height,MAX_COLORDEPTH);
            
            //Arxikopoihsh twn pinakwn.
            for (int i=0; i<stackedImage.height; i++) {
                for (int k=0; k<stackedImage.width; k++) {
                    redSum[i][k] = 0;
                    greenSum[i][k] = 0;
                    blueSum[i][k] = 0;
                }
            }
            
            //Ypologismos twn athroismatwn.
            for (PPMImage image : images) {
                for (int i=0; i<stackedImage.height; i++) {
                    for (int k=0; k<stackedImage.width; k++) {
                        redSum[i][k] += image.pixelbuf[i][k].getRed();
                        greenSum[i][k] += image.pixelbuf[i][k].getGreen();
                        blueSum[i][k] += image.pixelbuf[i][k].getBlue();
                    }
                }   
            }
            
            /*Dhmiourgia ths stackedImage.Se kathe pixel ths mpainei h mesh timh
            twn rgb timwn olwn twn eikonwn pou emperiexontai sth lista.*/
            for (int i=0; i<stackedImage.height; i++) {
                for (int k=0; k<stackedImage.width; k++) {
                    stackedImage.pixelbuf[i][k].setRed((short)(redSum[i][k]/images.size()));
                    stackedImage.pixelbuf[i][k].setGreen((short)(greenSum[i][k]/images.size()));
                    stackedImage.pixelbuf[i][k].setBlue((short)(blueSum[i][k]/images.size()));
                }
            }
        }
    }
    
    //Methodos pou epistrefei thn stackedImage.An den yparxei apla epistrefei mia kenh eikona.
    public PPMImage getStackedImage() {
        if (stackedImage==null) {
            stackedImage = new PPMImage(0,0,MAX_COLORDEPTH);
        }
        return stackedImage;
    }
}
