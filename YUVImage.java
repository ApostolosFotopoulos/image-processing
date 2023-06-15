package ce325.hw2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class YUVImage implements Image {    
    YUVPixel[][] pixelbuf;
    int width;
    int height;
    static final int MAX_COLORDEPTH = 255;
    
    public YUVImage(int width, int height) {
        this.width = width;
        this.height = height;
        
        pixelbuf = new YUVPixel[height][width];
        
        for(int i=0; i<height; i++) {
            for(int k=0; k<width; k++) {
                pixelbuf[i][k] = new YUVPixel((short)16,(short)128,(short)128);
            }
        }          
    }
    
    public YUVImage(YUVImage copyImg) {
        width = copyImg.width;
        height = copyImg.height;
        
        pixelbuf = new YUVPixel[copyImg.height][copyImg.width];
        
        for(int i=0; i<copyImg.height; i++) {
            for(int k=0; k<copyImg.width; k++) {
                pixelbuf[i][k] = new YUVPixel(copyImg.pixelbuf[i][k]);
            }
        }
    }
    
    /*Kataskeuasths mias YUVImage apo mia RGBImage.
    Ousiastika allazei h kwdikopoihsh twn pixel apo RGB se YUV.*/
    public YUVImage(RGBImage RGBImg) {
        width = RGBImg.width;
        height = RGBImg.height;
        
        pixelbuf = new YUVPixel[RGBImg.height][RGBImg.width];
        
        for(int i=0; i<RGBImg.height; i++) {
            for(int k=0; k<RGBImg.width; k++) {
                pixelbuf[i][k] = new YUVPixel(RGBImg.pixelbuf[i][k]);
            }
        }
    }
    
    /*Kataskeuasths enos antikeimenou PPMImage apo ena arxeio .yuv
    h .jpg.An to arxeio den einai typou YUV h JPG tote paragei ena
    UnsupportedFileFormatException.*/
    public YUVImage(File file) {
        this(0,0);
        
        if (file.exists()) {
            //Eikona typou JPG.
            if (file.getName().endsWith(".jpg")) {
                BufferedImage img = null;
                RGBImage RGBImg = new  RGBImage(0,0,MAX_COLORDEPTH);
                
                try {
                    img = ImageIO.read(file);
                } catch (IOException ex) {
                    System.out.println("Could not read " + file.getName());
                    ex.printStackTrace();
                }
                
                RGBImg.height =  img.getHeight();
                RGBImg.width =  img.getWidth(); 
                RGBImg.pixelbuf = new RGBPixel[RGBImg.height][RGBImg.width];

                int[] pixels = img.getRGB(0, 0, RGBImg.width, RGBImg.height, null, 0, RGBImg.width);

                for (int i = 0; i < pixels.length; i++) {
                    Color c = new Color(pixels[i]);

                    int row = i / RGBImg.width;
                    int col = i % RGBImg.width;
                    RGBImg.pixelbuf[row][col] = new RGBPixel((short)c.getRed(), (short)c.getGreen(), (short)c.getBlue());
                }
                
                width = RGBImg.width;
                height = RGBImg.height;  
                pixelbuf = new YUVPixel[RGBImg.height][RGBImg.width];
                
                for(int i=0; i<RGBImg.height; i++) {
                    for(int k=0; k<RGBImg.width; k++) {
                        pixelbuf[i][k] = new YUVPixel(RGBImg.pixelbuf[i][k]);
                    }
                }
            }
            //Eikona typou YUV.
            else if (file.getName().endsWith(".yuv")) {
                try (Scanner yuvImg = new Scanner(file)) {
                        yuvImg.next();
                        width = yuvImg.nextInt();
                        height = yuvImg.nextInt();
                        pixelbuf = new YUVPixel[height][width];

                        for (int i=0; i < height; i++) {
                            for (int k=0; k < width; k++) {
                                short Y = yuvImg.nextShort();
                                short U = yuvImg.nextShort();
                                short V = yuvImg.nextShort();

                                pixelbuf[i][k] = new YUVPixel(Y, U, V);
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
    
    //Methodos pou epistrefei ena string pou periexei ta periexomena tou arxeiou YUV.
    public String toString() {
        String str = "P3" + "\n" + String.valueOf(width) + " ";
        str += String.valueOf(height) + "\n";
        for (int i=0; i < height; i++) {
            for (int k=0; k < width; k++) {
                str += String.valueOf(pixelbuf[i][k].getY()) + " ";
                str += String.valueOf(pixelbuf[i][k].getU()) + " ";
                str += String.valueOf(pixelbuf[i][k].getV()) + " ";
            }
            str = str + "\n";
        }
        return str;
    }
    
    /*Methodos pou graphei thn eikona , se morfh YUV , mesa se ena arxeio.
    An h eikona exei width h height iso me to 0 tote to arxeio den dhmiourgeitai*/
    public void toFile (File file) {
        if (height!=0 && width!=0) {
            try (PrintWriter imgCreator = new PrintWriter(file)){
                imgCreator.println("YUV3");
                imgCreator.printf("%d %d\n", width ,height);

                for (int i=0; i < height; i++) {
                    for (int k=0; k < width; k++) {
                        imgCreator.print(pixelbuf[i][k].getY() + " " + pixelbuf[i][k].getU() + " " + pixelbuf[i][k].getV() + "  ");
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
    
    //Methodos gia thn metatroph mias YUV eikonas se aspromaurh.
    public void grayscale() {
        short gray, red, green, blue;
        RGBImage RGBImg = new  RGBImage(width,height,MAX_COLORDEPTH);
        
        //Metatroph ths YUV eikonas se mia isodynamh RGB.
        for(int i=0; i<RGBImg.height; i++) {
            for(int k=0; k<RGBImg.width; k++) {
                RGBImg.pixelbuf[i][k] = new RGBPixel(pixelbuf[i][k]);
            }
        }
        
        for (int i=0; i<RGBImg.height; i++) {
            for (int k=0; k<RGBImg.width; k++) {
                red = RGBImg.pixelbuf[i][k].getRed();
                green = RGBImg.pixelbuf[i][k].getGreen();
                blue = RGBImg.pixelbuf[i][k].getBlue();

                gray = (short)(red * 0.3 + green * 0.59 + blue * 0.11);
                
                if (gray > RGBImg.colordepth) { 
                    gray = (short) RGBImg.colordepth; 
                }

                RGBImg.pixelbuf[i][k].setRed(gray);
                RGBImg.pixelbuf[i][k].setGreen(gray);
                RGBImg.pixelbuf[i][k].setBlue(gray);
            }
        }
        
        //Metatroph ths aspromaurhs pleon RGB eikonas xana se YUV.        
        for(int i=0; i<RGBImg.height; i++) {
            for(int k=0; k<RGBImg.width; k++) {
                pixelbuf[i][k] = new YUVPixel(RGBImg.pixelbuf[i][k]);
            }
        }
    }
    
    //Methodos gia to diplasiasmo tou megethous mias YUV eikonas.
    public void doublesize() {
        short red,green,blue;
        
        YUVImage newImg = new YUVImage(2*width ,2*height);
        
        for (int i=0; i< height; i++) {
            for (int k=0; k<width; k++) {
                 red = pixelbuf[i][k].getY();
                 green = pixelbuf[i][k].getU();
                 blue = pixelbuf[i][k].getV();
                 
                 newImg.pixelbuf[2*i][2*k].setY(red);
                 newImg.pixelbuf[2*i][2*k].setU(green);
                 newImg.pixelbuf[2*i][2*k].setV(blue);
                 
                 newImg.pixelbuf[2*i+1][2*k].setY(red);
                 newImg.pixelbuf[2*i+1][2*k].setU(green);
                 newImg.pixelbuf[2*i+1][2*k].setV(blue);
                 
                 newImg.pixelbuf[2*i][2*k+1].setY(red);
                 newImg.pixelbuf[2*i][2*k+1].setU(green);
                 newImg.pixelbuf[2*i][2*k+1].setV(blue);
                 
                 newImg.pixelbuf[2*i+1][2*k+1].setY(red);
                 newImg.pixelbuf[2*i+1][2*k+1].setU(green);
                 newImg.pixelbuf[2*i+1][2*k+1].setV(blue);
            }
        }
        
        height = newImg.height;
        width = newImg.width;
        pixelbuf = newImg.pixelbuf;
    }
    
    //Methodos gia ton ypodiplasiasmo tou megethous mias YUV eikonas.
    public void halfsize() {
        short red,green,blue;
        
        YUVImage newImg = new YUVImage(width/2 ,height/2);
        
        for (int i=0; i<height/2; i++) {
            for (int k=0; k < width/2; k++) {
                
                red = (short) ((pixelbuf[2*i][2*k].getY() + pixelbuf[2*i+1][2*k].getY() + pixelbuf[2*i][2*k+1].getY() + pixelbuf[2*i+1][2*k+1].getY())/4);
                green = (short) ((pixelbuf[2*i][2*k].getU() + pixelbuf[2*i+1][2*k].getU() +pixelbuf[2*i][2*k+1].getU() + pixelbuf[2*i+1][2*k+1].getU())/4);
                blue = (short) ((pixelbuf[2*i][2*k].getV() + pixelbuf[2*i+1][2*k].getV() + pixelbuf[2*i][2*k+1].getV() +pixelbuf[2*i+1][2*k+1].getV())/4);
                 
                newImg.pixelbuf[i][k].setY(red);
                newImg.pixelbuf[i][k].setU(green);
                newImg.pixelbuf[i][k].setV(blue);
                 
            }
        }
        
        height = newImg.height;
        width = newImg.width;
        pixelbuf = newImg.pixelbuf;
    }
    
    
    //Methodos gia thn dexiostrofh peristrofh mias YUV eikonas kata 90 moires.
    public void rotateClockwise() {
        YUVPixel[][] newPixelbuf = new YUVPixel[width][height];
        
        for(int i=0; i<height; i++) {
            for(int k=0; k<width; k++) {
                newPixelbuf[k][height-i-1] = pixelbuf[i][k];
            }
        }
        
        int temp = width;
        width = height;
        height = temp;
        pixelbuf = newPixelbuf;
    }
    
    //Methodos gia thn aristerostrofh peristrofh mias YUV eikonas kata 90 moires.
    public void rotateCounterClockwise() {
        YUVPixel[][] newPixelbuf = new YUVPixel[width][height];
        
        for(int i=0; i<height; i++) {
            for(int k=0; k<width; k++) {
                newPixelbuf[width-k-1][i] = pixelbuf[i][k];
            }
        }
        
        int temp = width;
        width = height;
        height = temp;
        pixelbuf = newPixelbuf;
    }
}
