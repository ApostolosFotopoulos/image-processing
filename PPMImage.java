package ce325.hw2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class PPMImage extends RGBImage{ 
    /*Kataskeuasths pou dhmiourgei mia PPMImage kai arxikopoiei ola ta pixels
    me red=0,green=0,blue=0.Dhmiourgei ousiastika mia maurh eikona.*/
    public PPMImage(int width, int height, int colordepth) {
        super(width,height,colordepth);
    }
    
    //Kataskeuasths mias PPMImage apo mia YUVImage.
    public PPMImage(YUVImage YUVImg) {
        super(YUVImg);
    }
    
    //Kataskeuasths mias PPMImage apo mia RGBImage.
    public PPMImage(RGBImage img) {
        super(img);
    }
    
    /*Kataskeuasths enos antikeimenou PPMImage apo ena arxeio .ppm
    h .jpg.An to arxeio den einai typou PPM h JPG tote paragei ena
    UnsupportedFileFormatException.*/
    public PPMImage(File file) {       
        super(0,0,MAX_COLORDEPTH);
        
        if (file.exists()) {
            //Eikona typou JPG.
            if (file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg")) {
                BufferedImage img = null;
                try {
                    img = ImageIO.read(file);
                } catch (IOException ex) {
                    System.out.println("Could not read " + file.getName());
                    ex.printStackTrace();
                }
                
                height =  img.getHeight();
                width =  img.getWidth(); 
                pixelbuf = new RGBPixel[height][width];

                int[] pixels = img.getRGB(0, 0, width, height, null, 0, width);

                for (int i = 0; i < pixels.length; i++) {
                    Color c = new Color(pixels[i]);

                    int row = i / width;
                    int col = i % width;
                    pixelbuf[row][col] = new RGBPixel((short)c.getRed(), (short)c.getGreen(), (short)c.getBlue());
                }
            }
            //Eikona typou PPM.
            else if (file.getName().endsWith(".ppm")) {
                try (Scanner ppmImg = new Scanner(file)) {
                        ppmImg.next();
                        width = ppmImg.nextInt();
                        height = ppmImg.nextInt();
                        colordepth = ppmImg.nextInt();
                        pixelbuf = new RGBPixel[height][width];

                        for (int i=0; i < height; i++) {
                            for (int k=0; k < width; k++) {
                                short red = ppmImg.nextShort();
                                short green = ppmImg.nextShort();
                                short blue = ppmImg.nextShort();

                                pixelbuf[i][k] = new RGBPixel(red, green, blue);
                            }
                        }  
                }
                catch (IOException ex) {
                    System.out.println("Error while reading " + file.getName());
                    ex.printStackTrace();
                }

            }
            //Eikona h kapoio arxeio tou opoiou o typos den ypostirizetai.
            else {
                UnsupportedFileFormatException ex = new UnsupportedFileFormatException();
                ex.printStackTrace();
            }
        }
        //An den yparxei to arxeio.
        else {
            FileNotFoundException ex = new FileNotFoundException();
            ex.printStackTrace();
        }
    }
    
    //Methodos pou epistrefei ena string pou periexei ta periexomena tou arxeiou PPM.
    public String toString() {
        String str = "P3" + "\n" + String.valueOf(width) + " ";
        str += String.valueOf(height) + "\n";
        str += String.valueOf(colordepth) + "\n";
        for (int i=0; i < height; i++) {
            for (int k=0; k < width; k++) {
                str += String.valueOf(pixelbuf[i][k].getRed()) + " ";
                str += String.valueOf(pixelbuf[i][k].getGreen()) + " ";
                str += String.valueOf(pixelbuf[i][k].getBlue()) + " ";
            }
            str = str + "\n";
        }
        return str;
    }
        
    /*Methodos pou graphei thn eikona , se morfh PPM , mesa se ena arxeio.
    An h eikona exei width h height iso me to 0 tote to arxeio den dhmiourgeitai*/
    public void toFile (File file) {
        if (height!=0 && width!=0) {
            try (PrintWriter imgCreator = new PrintWriter(file)){
                imgCreator.println("P3");
                imgCreator.printf("%d %d\n", width ,height);
                imgCreator.printf("%d\n" , colordepth);

                for (int i=0; i < height; i++) {
                    for (int k=0; k < width; k++) {
                        imgCreator.print(pixelbuf[i][k].getRed() + " " + pixelbuf[i][k].getGreen() + " " + pixelbuf[i][k].getBlue() + "  ");
                    }
                    imgCreator.print("\n");
                }
            }
            catch (IOException ex){
                System.out.println("Error while writing " + file.getName());
                ex.printStackTrace();
            }
        }
    }   
}
